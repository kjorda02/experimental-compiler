package sintactico;

import datos.*;
import datos.cod.*;
import datos.desc.*;

/**
 *
 * @author kjorda
 */
public class literal_expr_node extends node {
    long literalval;
    
    // Integer literal
    public literal_expr_node(long l) {
        super("numeric literal");
        literalval = l;
        type = new typeDesc(basicType.INT, 8);
    }
    
    // Boolean literal
    public literal_expr_node(boolean b) {
       super("boolean literal");
       type = new typeDesc(basicType.BOOL, 1);
       if (b)
           literalval = -1;
       else
           literalval = 0;
    }
    
    public void gest() {
        int t = varTable.newvar(0, false);
        cod.genera(cod.op.COPYLIT, literalval, 0, t);
        var = t;
    }
}
