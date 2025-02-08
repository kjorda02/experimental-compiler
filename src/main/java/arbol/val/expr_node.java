package arbol.val;

import arbol.type.complexType;
import arbol.assign_node;
import arbol.node;
import arbol.ref.call_node;
import arbol.ref.displ_node;
import arbol.ref.identifier_ref_node;
import arbol.ref.ref_node;
import datos.cod;
import datos.funcTable;
import datos.varTable;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class expr_node extends node { // Array indices and literals are only allowed here!
    public Integer varNum = null;    // (nv) Points to a variable in the variable table
    private node n; // expr_node, identifier_ref_node or assign_node
    public complexType type;
    public Integer value; // Value for constants
    
    public expr_node(node node) {
        super(node.left, node.right);
        n = node;
        
        if (n.isEmpty()) {
            return;
        }
            
        
        if (node instanceof ref_node) {
            type = ((ref_node) node).type;

            if (node instanceof identifier_ref_node)
                value = (((identifier_ref_node) node)).value; // Compile-time value, only for basic references, not arrays or structs
        }
        else if (node instanceof assign_node) {
             node = ((assign_node) node).expr; // Get the expression if it was an assignment
        }
        if (node instanceof expr_node) {
            expr_node exn = (expr_node) node;
            type =  exn.type;
            value = exn.value;
        }
       error = false;
    }
    
    public expr_node(Location left, Location  right) { // For inheritance
        super(left, right);
    }
    
    public int getVarNum() {
        if (varNum == null) {
            int v = varTable.newvar(funcTable.currentFunc, type.bytes);
            cod.genera(cod.op.COPY, this.value, 0, v);
            cod.setImmediate(true, false);
            varNum = v;
        }
        
        return varNum;
    }
    
    @Override
    public void gest() {
        if (value != null && !(n instanceof assign_node)) // DO NOT GENERATE CODE FOR COMPILE TIME EXPRESSIONS
            return;
        
        n.gest(); // Either process the expression or process the assignment (which will process its expression)
        
        if (n instanceof displ_node) {
            varNum = ((displ_node)n).varNum;
            cod.genera(cod.op.IDX_VAL, varNum, 0, varNum);
            cod.setImmediate(false, true);
        }
        if (n instanceof assign_node)
            n = ((assign_node)n).expr;
        if (n instanceof expr_node)
            varNum = ((expr_node) n).varNum;
        else if (n instanceof identifier_ref_node)
            varNum = ((identifier_ref_node) n).varNum;
        else if (n instanceof call_node) {
            varNum = ((call_node) n).varNum;
        }
        else if (n instanceof input_node) {
            varNum = ((input_node) n).varNum;
        }
        else {
            varNum = ((ref_node)n).varNum;
        }
    }
}
