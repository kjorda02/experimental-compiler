package sintactico;
import datos.desc;
import datos.Type;

/**
 *
 * @author kjorda
 */
public class expr_node extends node {
    private node n;

    // Integer literal
    public expr_node(int i) {
        super("atomic expression", new desc(i, Type.INT));
    }
    
    // Boolean literal
    public expr_node(boolean b) {
       super("atomic expression", new desc(b, Type.BOOL));
    }
    
    // Var access node, function call, array access, ( expr )
    public expr_node(node node) {
       super("atomic expression", null);
       n = node;
    }
    
    public void gest() {
        if (n != null) {
            n.gest();
            value = n.value;
        }
    }
    
    @Override
    public String toString() {
        if (value == null)
            return "NULL";
        else
            return value.toString();
    }
}
