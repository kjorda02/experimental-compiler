package sintactico;
import datos.val;
import datos.Type;
import datos.symbolTable;

/**
 *
 * @author kjorda
 */
public class var_access_node extends node {
    String identifier;
    public var_access_node(String id) {
        super("variable", null);
        identifier = id;
    }
    
    public void gest() {
        value = symbolTable.get(identifier);
    }
}
