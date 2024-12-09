package arbol.val;

import arbol.assign_node;
import datos.*;
import datos.cod.*;
import datos.desc.*;
import arbol.node;

/**
 *
 * @author kjorda
 */
public class expr_node extends node { // Array indices and literals are only allowed here!
    private expr_node n;
    public basicType type;
    
    public expr_node(expr_node node) {
       super("atomic expression");
       n = node;
    }
    
    public expr_node(int num) { // EXPR -> REF
       super("atomic expression");
       varNum = num;
    }
    
    public expr_node(assign_node as) { // EXPR -> ASSIGN
        n = as.expr;
    }
    
    public expr_node(String nodeName) { // For inheritance
        super(nodeName);
    }
    
    public void gest() {
        n.gest(); // E -> R
        varNum = n.varNum;
        type = n.type;
    }
}
