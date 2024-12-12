package arbol.ref;

import arbol.val.expr_node;
import arbol.type.complexType;
import datos.basicType;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
// ARRAY DISPLACEMENT NODE
public class displ_node extends ref_node {
    ref_node base;
    expr_node displ;
    
    public displ_node(ref_node b, expr_node d) {
        super(b.left, d.right);
        base = b;
        displ = d;
    }
    
    @Override
    public void gest() {
        base.gest();
        displ.gest();
        
        // TODO: Add toString to references so they can be printed out like id.field.otherField[32][32] ?
        if (!(base.type instanceof complexType.array)) {
            Main.report_error("Cannot index array: Not an array..", this);
            return;
        }
        
        if (!(displ.type instanceof complexType.primitive) || ((complexType.primitive) displ.type).btype != basicType.INT) {
            Main.report_error("Cannot index array with non-integer value: "+displ.type.toString(), this);
            return;
        }
        
//        int t = varTable.newvar(0, false);
//        cod.genera(cod.op.IDX_VAL, n.var, n.displ, t);
//        var = t;
    }
}
