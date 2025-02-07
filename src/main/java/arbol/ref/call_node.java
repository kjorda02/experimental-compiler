package arbol.ref;

import arbol.fun.arglist_node;
import arbol.terminal_node;
import arbol.type.complexType;
import datos.cod;
import datos.desc;
import datos.funcTable;
import datos.symbolTable;
import datos.varTable;
import experimental_compiler.Main;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class call_node extends ref_node {
    int funcID;
    arglist_node args;
    
    public call_node(terminal_node<String> id, arglist_node l, Location r) {
        super(id.left, r);
        args = l;
        
        desc d = symbolTable.get(id.value);
        
        if (!(d instanceof desc.function)) {
            Main.report_error("Symbol <"+id.value+"> is not a function", id.left, id.right);
            return;
        }
        
        funcID = ((desc.function) d).funcNum;
        complexType.funcsig sig = ((desc.function) d).signature;
        type = sig.returnType;
        
        int i = 0;
        for (arglist_node n = args; n != null; n = n.next) {
            if (i >= sig.paramTypes.size()){  // More arguments than required
                i++;
                continue;
            }
            
            if (!sig.paramTypes.get(i).equals(n.expr.type)) {
                Main.report_error("Type mismatch: Argument "+i+" of <"+sig.name.value+"> is of type <"+sig.paramTypes.get(i).toString()+">, but passed in <"+n.expr.type.toString()+">", n.expr.left, n.expr.right);
                return;
            }
            i++;
        }
        
        if (i > sig.paramTypes.size()) { // More arguments than required
            Main.report_error("Passed in"+i+"arguments. Function <"+sig.name.value+"> only requires "+sig.paramTypes.size(), id.left, r);
            return;
        }
        
        if (i < sig.paramTypes.size()) { // Less arguments than required
            Main.report_error("Passed in"+i+"arguments. Function <"+sig.name.value+"> requires "+sig.paramTypes.size(), id.left, r);
            return;
        }
        
        error = false;
    }
    
    @Override
    public void gest() {
        funcTable.addCaller(funcID); // Adds currentFunc as a caller
        
        varNum = varTable.newvar(funcTable.currentFunc, type.bytes); // Holds the return value once we've returned
        
        cod.genera(cod.op.INIT_PARAMS, 0, 0, funcID);
        
        for (arglist_node n = args; n != null; n = n.next) { // For each argument
            n.expr.gest(); // Generate its code
            cod.genera(cod.op.PARAM, 0, 0, n.expr.getVarNum()); // Push the result to the stack
        }
        
        // Call instruction. Jumps to tag of the function. Copies return value from
        // a0 to the location of varNum
        cod.genera(cod.op.CALL, 0, 0, funcID);
        
        if (type.bytes <= 4) {
            cod.genera(cod.op.COPY, funcTable.getCurrent().returnVar, 0, varNum); // Copies from a0 to our local variable
        }
        else {
            // TODO: Handle complex types
        }
    }
}
