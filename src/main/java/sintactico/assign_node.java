package sintactico;
import experimental_compiler.Main;
import datos.*;
/**
 *
 * @author kjorda
 */
public class assign_node extends node {
    var_access_node var_acc;
    expr_node expr;
    
    public assign_node(var_access_node v, expr_node e) {
        super("assign node");
        var_acc = v;
        expr = e;
    }
    
    public void gest() {
        var_acc.gest();
        expr.gest();
        
        empty = true;
        if (var_acc.type.basicType != expr.type.basicType) {
            Main.report_error("Cannot convert type \""+expr.type.basicType.toString()+"\" to \""+var_acc.type.basicType.toString()+"\" in assignation.", this);
            return;
        }
        
        if (expr.dataType != null) {
            if (!var_acc.dataType.equals(expr.dataType)) {
                Main.report_error("Cannot convert type \""+expr.dataType+"\" to \""+var_acc.dataType+"\" in assignation.", this);
                return;
            }
        }
        empty = false;
        
        cod.genera(cod.op.COPY, expr.var, 0, var_acc.var);
    }
}
