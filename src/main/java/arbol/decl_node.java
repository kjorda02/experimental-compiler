package arbol;
import arbol.type.complexType;
import arbol.val.expr_node;
import datos.*;
import experimental_compiler.Main;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class decl_node extends node {
    complexType type; // (dt)
    terminal_node<String> id;
    expr_node expr;
    desc.variable vard;
    
    public decl_node(complexType t, terminal_node<String> ident) {
        super(((node)ident).left, ident.right);
        type = t;
        id = ident;
        if (node.error(t, ident))
            return;
        
        addVar();
    }
    
    public decl_node(complexType t, terminal_node<String> ident, expr_node e) {
        super(((node)ident).left, e.right);
        type = t;
        id = ident;
        expr = e;
        if (node.error(t, ident, e)) {
            return;
        }
        
        addVar();
    }
    
    public decl_node(Location l, complexType t, terminal_node<String> ident, expr_node e) { // constant
        super(l, e.right);
        type = t;
        id = ident;
        expr = e;
        
        if (node.error(t, ident, e))
            return;
        
        if (!(type instanceof complexType.primitive)) {
                Main.report_error("Cannot define constant of non-primitive type \""+type.toString()+"\"", id);
                return;
            }
            if (!type.equals(expr.type)) {
                Main.report_error("Cannot assign expression of type <"+expr.type.toString()+"> to constant of type <"+type.toString()+">", expr);
                return;
            }
            if (expr.value == null) {
                Main.report_error("Cannot assign runtime expression to constant", expr);
                return;
            }
            
            int val = ((expr_node) expr).value;
            basicType btype = ((complexType.primitive) type).btype;
            desc dc = new desc.constant(btype, val);
            symbolTable.add(ident.value, dc);
            error = false;
    }
    
    private void addVar() {
        if (expr != null && !type.equals(expr.type)) {
            Main.report_error("Cannot assign expression of type <"+expr.type.toString()+"> to variable of type <"+type.toString()+">", expr);
            return;
        }
        
        int num = varTable.newvar(funcTable.currentFunc, type.bytes, false, id.value);
        vard = new desc.variable(type, num); // Placeholder variable number
        if (symbolTable.add(id.value, vard)) {
            Main.report_error("Identifier \""+id.value+"\" is already in use in current scope.", id);
            return;
        }
        error = false;
    }

    @Override
    public void gest() {
        if (error)
            return;
        
        if (vard == null) // If it's a constant
            return;
        
        if (expr == null) // if it's a declaration without initial value
            return;
        
        if (expr.value == null) { // Runtime expression
            expr.gest();
            cod.genera(cod.op.COPY, expr.varNum, 0, vard.varNum);
        }
        else { // Compile-time expression
            cod.genera(cod.op.COPY, expr.value, 0, vard.varNum);
            cod.setImmediate(true, false);
        }  
    }
}