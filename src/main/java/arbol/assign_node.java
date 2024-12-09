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
        
        empty = true;
        
        
        if (( (complexType.primitive) ref.type).btype != expr.type) {
            Main.report_error("Cannot convert type \""+expr.type.toString()+"\" to \""+ref.type.toString()+"\" in assignation.", this);
            return;
        }
        
        // What happens with assignation of complex types (no operators)?
        // Will take care of later
//        if (expr.dataType != null) {
//            if (!ref.dataType.equals(expr.dataType)) {
//                Main.report_error("Cannot convert type \""+expr.dataType+"\" to \""+ref.dataType+"\" in assignation.", this);
//                return;
//            }
//        }
//        empty = false;
        
        cod.genera(cod.op.COPY, expr.varNum, 0, ref.varNum);
    }
}
