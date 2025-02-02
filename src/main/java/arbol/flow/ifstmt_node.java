package arbol.flow;

import arbol.node;
import arbol.type.complexType;
import arbol.val.expr_node;
import datos.basicType;
import datos.cod;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class ifstmt_node extends node {
    private expr_node expr;
    private stmts_node stmts;
    
    public ifstmt_node(expr_node e, stmts_node s) {
        expr = e;
        stmts = s;
        
        if (!(expr.type instanceof complexType.primitive) || ((complexType.primitive) expr.type).btype != basicType.BOOL) {
            System.out.println(expr.type.toString());
            Main.report_error("If condition must be boolean expression", expr);
        }
        else 
            error = false;
    }
    
    @Override
    public void gest() {
        if (error) return;
        
        int endTag = cod.newTag();
        expr.gest();
        
        if (expr.value != null) { // Compile-time expression
            if (expr.value != 0)
                stmts.gest();
            
            return;
        }
        
        cod.genera(cod.op.IFEQ, expr.varNum, 0, 0); // if expr = 0 goto endTag
        cod.setImmediate(false, true);
        cod.replaceWithTag(2, endTag);
        
        stmts.gest();
        
        cod.setTag(endTag); // endTag : skip
    }
}
