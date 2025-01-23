package arbol;
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
        
        cod.genera(cod.op.COPY, expr.varNum, 0, ref.varNum);
    }
}
