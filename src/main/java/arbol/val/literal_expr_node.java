package arbol.val;

import arbol.terminal_node;
import arbol.type.complexType;
import datos.*;

/**
 *
 * @author kjorda
 */
public class literal_expr_node extends expr_node {
    // Integer literal
    public literal_expr_node(terminal_node<?> node) {
        super(node.left, node.right);
        
        error = false;
        if (node.value instanceof Long) { // NUMERIC LITERAL
            type = new complexType.primitive(null, basicType.INT);
            value = (Long) node.value;
        }
        else if (node.value instanceof Boolean) { // BOOLEAN LITERAL
            type = new complexType.primitive(null, basicType.BOOL);
            if ((Boolean) node.value)
                value = -1l;
            else
                value = 0l;
        }
        else 
            error = true;
    }
}
