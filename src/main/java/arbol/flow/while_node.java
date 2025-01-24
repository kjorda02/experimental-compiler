package arbol.flow;

import arbol.node;
import arbol.stmts_node;
import arbol.type.complexType;
import arbol.val.expr_node;
import datos.basicType;
import datos.cod;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class while_node extends node {
    private expr_node expr;
    private stmts_node stmts;
    
    public while_node(expr_node e, stmts_node n) {
        expr = e;
        stmts = n;
        
        if (!(expr.type instanceof complexType.primitive) || ((complexType.primitive) expr.type).btype != basicType.BOOL) 
            Main.report_error("While condition must be boolean expression", expr);
        else 
            error = false;
    }
    
    public void gest() {
        if (error) return;
        
        int condTag = cod.newTag();
        int endTag = cod.newTag();
        cod.setTag(condTag); // condTag : skip
        expr.gest(); // <expression evaluation code>
        
        if (expr.value != null) { // Compile-time expression
            if (expr.value != 0) { // Infinite loop
                stmts.gest();
                cod.genera(cod.op.GOTO, 0, 0, 0);
                cod.replaceWithTag(2, condTag);
            }
            return;
        }
        
        cod.genera(cod.op.IFEQ, expr.varNum, 0, 0); // if t_expr = 0 goto endTag
        cod.setImmediate(false, true);
        cod.replaceWithTag(2, endTag);
        
        stmts.gest();
        cod.genera(cod.op.GOTO, 0, 0, 0);
        cod.replaceWithTag(2, condTag);
        
        cod.setTag(endTag);
    }
}
