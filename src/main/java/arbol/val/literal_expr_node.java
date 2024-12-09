package arbol.val;

import datos.*;
import datos.cod.*;
import datos.desc.*;
import arbol.node;

/**
 *
 * @author kjorda
 */
public class literal_expr_node extends expr_node {
    long literalval;
    
    // Integer literal
    public literal_expr_node(long l) {
        super("numeric literal");
        literalval = l;
        type = new complexType.primitive(null, basicType.INT);
    }
    
    // Boolean literal
    public literal_expr_node(boolean b) {
       super("boolean literal");
       type = new complexType.primitive(null, basicType.BOOL);
       if (b)
           literalval = -1;
       else
           literalval = 0;
    }
    
    public void gest() {
        int t = varTable.newvar(0, false);
        cod.genera(cod.op.COPYLIT, literalval, 0, t);
        varNum = t;
    }
}
