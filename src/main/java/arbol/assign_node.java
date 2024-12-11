package arbol;
import arbol.ref.ref_node;
import arbol.val.expr_node;
import experimental_compiler.Main;
import arbol.type.complexType;
import arbol.type.complexType.*;
import datos.*;
/**
 *
 * @author kjorda
 */
public class assign_node extends node {
    public ref_node ref;
    public expr_node expr;
    
    public assign_node(ref_node v, expr_node e) {
        super("assign");
        ref = v;
        expr = e;
    }
    
    @Override
    public void gest() {
        expr.gest();
        ref.gest();
        
        if (!ref.type.equals(expr.type)) {
            Main.report_error("Cannot convert type <"+expr.type.toString()+"> to <"+ref.type.toString()+"> in assignment.", this);
            empty = true;
            return;
        }
        
        cod.genera(cod.op.COPY, expr.varNum, 0, ref.varNum);
    }
}
