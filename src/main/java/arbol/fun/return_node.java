package arbol.fun;

import arbol.node;
import arbol.terminal_node;
import arbol.val.expr_node;
import datos.cod;
import datos.desc;
import datos.funcTable;
import datos.symbolTable;
import datos.varTable;
import datos.varTable.varInfo;
import experimental_compiler.Main;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class return_node extends node {
    private expr_node expr;
    Location l, r;
    desc.function f;
    String name;
    
    public return_node(terminal_node t) {
        l = t.left;
        r = t.right;
        error = false;
        
        String name = funcTable.name(funcTable.currentFunc);
        f = (desc.function) symbolTable.get(name);
    }
    
    public return_node(terminal_node t, expr_node e) {
        l = t.left;
        r = e.right;
        expr = e;
        error = false;
        
        name = funcTable.name(funcTable.currentFunc);
        f = (desc.function) symbolTable.get(name);
    }
    
    public void gest() {
        
        if (f.signature.returnType == null) {
            if (expr != null) {
                Main.report_error("Function <"+name+"> has void return type", l, r);
            }
            return;
        }
        
        expr.gest();
        
        if (!f.signature.returnType.equals(expr.type)) {
            Main.report_error("Type mismatch: Return value is of type <"+expr.type.toString()+"> but function <"+name+"> returns <"+f.signature.returnType.toString()+">", l, r);
            return;
        }
        
        varInfo exprVar = varTable.get(expr.getVarNum());
        if (exprVar.inRegister && exprVar.offset == 0) // Return expression is already in a0
            return;
        
        int returnNum = funcTable.getCurrent().returnVar;
        
        cod.genera(cod.op.COPY, expr.getVarNum(), 0, returnNum);
        cod.genera(cod.op.RTN, 0, 0, funcTable.currentFunc);
    }
}
