package sintactico;
import experimental_compiler.Main;
import datos.*;
/**
 *
 * @author kjorda
 */
public class assign_node extends node {
    var_access_node var;
    expr_node expr;
    
    public assign_node(var_access_node v, expr_node e) {
        super("assign node", null);
        var = v;
        expr = e;
    }
    
    public void gest() {
        var.gest();
        expr.gest();
        if (symbolTable.get(var.identifier) != null && var.value.type != expr.value.type) {
            Main.report_error("Cannot convert type \""+expr.value.type.toString()+"\" to \""+var.value.type.toString()+"\" in assignation.", this);
            return;
        }
        value = expr.value;
        symbolTable.add(var.identifier, new desc(expr.value.val, expr.value.type));
    }
}
