package arbol.val;

import arbol.type.complexType;
import datos.*;

/**
 *
 * @author kjorda
 */
public class literal_expr_node extends expr_node {
    // Integer literal
    public literal_expr_node(long l) {
        super("numeric literal");
        value = l;
        type = new complexType.primitive(null, basicType.INT);
    }
    
    // Boolean literal
    public literal_expr_node(boolean b) {
       super("boolean literal");
       type = new complexType.primitive(null, basicType.BOOL);
       if (b)
           value = -1l;
       else
           value = 0l;
    }
}
