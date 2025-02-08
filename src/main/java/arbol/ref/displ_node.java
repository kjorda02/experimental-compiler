package arbol.ref;

import arbol.val.expr_node;
import arbol.type.complexType;
import datos.basicType;
import datos.cod;
import datos.funcTable;
import datos.varTable;
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
        
        // TODO: Add toString to references so they can be printed out like id.field.otherField[32][32] ?
        if (!(base.type instanceof complexType.array)) {
            Main.report_error("Cannot index array: Not an array.", b.left, d.right);
            return;
        }
        
        if (!(displ.type instanceof complexType.primitive) || ((complexType.primitive) displ.type).btype != basicType.INT) {
            Main.report_error("Cannot index array with non-integer value: "+displ.type.toString(), b.left, d.right);
            return;
        }
        
        type = ((complexType.array)base.type).baseType; // We peel back the type of the original base reference like an onion
        
        error = false;
    }
    
    @Override
    public void gest() {
        if (error) return;
        base.gest();
        displ.gest();
        
        if (base instanceof displ_node) {
            varNum = base.varNum;
        }
        else {
            varNum = varTable.newvar(funcTable.currentFunc, 4); // 4 byte address
            System.out.println("num: "+varTable.getOffset(base.varNum));
            cod.genera(cod.op.LOAD_OFFSET, base.varNum, 0, varNum); // Copy address of base reference
        }
        
        if (displ.value == null) {
            cod.genera(cod.op.ADD_DISPL, type.bytes, displ.varNum, varNum); // varNum += displ*type.bytes;
            cod.setImmediate(true, false);
        }
        else {
            cod.genera(cod.op.ADD_DISPL, type.bytes, displ.value, varNum); // varNum += displ*type.bytes;
            cod.setImmediate(true, true);
        }
        
        
        
//        if (type.bytes <= 4) {
//            if (displ.value == null)
//                cod.genera(cod.op.IDX_VAL, base.varNum, displ.varNum, varNum);
//            else {
//                
//                cod.genera(cod.op.IDX_VAL, base.varNum, displ.value, varNum);
//                cod.setImmediate(false, true);
//            }
//        }
//        else { // We store the address
//            if (displ.value == null)
//                cod.genera(cod.op.COPY, varTable.getOffset(base.varNum) + varTable.getOffset(displ.varNum), 0, varNum); 
//            else {
//                cod.genera(cod.op.COPY, varTable.getOffset(base.varNum), displ.value, varNum);
//                cod.setImmediate(false, true);
//            }
//        }
    }
}
