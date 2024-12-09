package arbol.ref;

import arbol.ref.identifier_ref_node;
import arbol.val.expr_node;
import datos.*;
import datos.cod.*;
import datos.desc.*;
import arbol.node;

/**
 *
 * @author kjorda
 */
// ARRAY DISPLACEMENT NODE
public class displ_node extends ref_node {
    ref_node base;
    expr_node displ;
    
    public displ_node(ref_node b, expr_node d) {
        super("Array index");
        base = b;
        displ = d;
    }
    
    public void gest() {
        base.gest();
        displ.gest();
        
        
//        int t = varTable.newvar(0, false);
//        cod.genera(cod.op.IDX_VAL, n.var, n.displ, t);
//        var = t;
    }
}
