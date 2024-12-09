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
    ref_node ref;
    expr_node expr;
    
    public assign_node(ref_node v, expr_node e) {
        super("assign node");
        ref = v;
        expr = e;
    }
    
    public void gest() {
        ref.gest();
        expr.gest();
        
        empty = true;
        if (ref.type.basicType != expr.type.basicType) {
            Main.report_error("Cannot convert type \""+expr.type.basicType.toString()+"\" to \""+ref.type.basicType.toString()+"\" in assignation.", this);
            return;
        }
        
        if (expr.dataType != null) {
            if (!ref.dataType.equals(expr.dataType)) {
                Main.report_error("Cannot convert type \""+expr.dataType+"\" to \""+ref.dataType+"\" in assignation.", this);
                return;
            }
        }
        empty = false;
        
        cod.genera(cod.op.COPY, expr.var, 0, ref.var);
    }
}
