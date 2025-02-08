package arbol;
import arbol.ref.displ_node;
import arbol.ref.ref_node;
import arbol.val.expr_node;
import experimental_compiler.Main;
import datos.*;
/**
 *
 * @author kjorda
 */
public class assign_node extends node {
    public ref_node ref;
    public expr_node expr;
    
    public assign_node(ref_node v, expr_node e) {
        super(v.left, e.right);
        ref = v;
        expr = e;
        
        if (node.error(v, e))
            return;
        
        if (!ref.type.equals(expr.type)) {
            Main.report_error("Cannot convert type <"+expr.type.toString()+"> to <"+ref.type.toString()+"> in assignment.", this);
        }
        error = false;
    }
    
    @Override
    public void gest() {
        if (error)
            return;
        
        expr.gest();
        ref.gest();
        
        if (expr.value == null) {
            if (ref instanceof displ_node) {
                cod.genera(cod.op.IDX_ASS, 0, expr.varNum, ref.varNum);
                cod.setImmediate(true, false);
            }
            else
                cod.genera(cod.op.COPY, expr.varNum, 0, ref.varNum);
        }
        else {
            if (ref instanceof displ_node) {
                cod.genera(cod.op.IDX_ASS, 0, expr.value, ref.varNum);
                cod.setImmediate(true, true);
            }
            else {
                cod.genera(cod.op.COPY, expr.value, 0, ref.varNum);
                cod.setImmediate(true, false);
            }
        }
    }
}
