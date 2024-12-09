package arbol.ref;
import datos.*;
import datos.desc.*;
import datos.cod.*;
import arbol.node;

/**
 *
 * @author kjorda
 */
public class ref_node extends node {
    String identifier;
    public String dataType = null;
    
    public ref_node(String id) {
        super("variable");
        identifier = id;
    }
    
    public void gest() {
        desc d = symbolTable.get(identifier);
        if (d.type == idType.VAR) {
            var = d.tableIdx;
        }
        else { // constant
            int t = varTable.newvar(0, false);
            cod.genera(op.COPYLIT, d.val, 0, t);
            var = t;
        }
        type = d.typeDesc;
        dataType = d.dataType;
    }
}
