package arbol;
import arbol.ref.ref_node;
import arbol.val.expr_node;
import experimental_compiler.Main;
import datos.complexType;
import datos.complexType.*;
import datos.*;
/**
 *
 * @author kjorda
 */
public class assign_node extends node {
    public ref_node ref;
    public expr_node expr;
    
    public assign_node(ref_node v, expr_node e) {
        super("assign node");
        ref = v;
        expr = e;
    }
    
    public void gest() {
        ref.gest();
        expr.gest();
        
        if (ref.type.equals(expr.type)) {
            Main.report_error("Cannot convert type \""+expr.type.toString()+"\" to \""+ref.type.toString()+"\" in assignation.", this);
            empty = true;
            return;
        }
        
        cod.genera(cod.op.COPY, expr.varNum, 0, ref.varNum);
    }
}
