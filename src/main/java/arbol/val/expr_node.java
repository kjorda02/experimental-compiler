package arbol.val;

import datos.*;
import datos.cod.*;
import datos.desc.*;
import arbol.node;

/**
 *
 * @author kjorda
 */
public class expr_node extends node { // Array indices and literals are only allowed here!
    private node n;
    public typeDesc type; // Also used for expressions
    public String dataType = null;
    
    public expr_node(node node) {
       super("atomic expression");
       n = node;
    }
    
    // Function call?
    
    public void gest() {
        n.gest(); // E -> R
        if (n.displ == null) {
            var = n.var;
        } else {
            int t = varTable.newvar(0, false);
            cod.genera(cod.op.IDX_VAL, n.var, n.displ, t);
            var = t;
        }
        type = n.type;
        dataType = n.dataType;
    }
}
