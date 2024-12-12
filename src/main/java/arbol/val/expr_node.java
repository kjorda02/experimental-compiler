package arbol.val;

import arbol.type.complexType;
import arbol.assign_node;
import arbol.node;
import arbol.ref.identifier_ref_node;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class expr_node extends node { // Array indices and literals are only allowed here!
    public Integer varNum = null;    // (nv) Points to a variable in the variable table
    private node n; // expr_node, identifier_ref_node or assign_node
    public complexType type;
    public Long value; // Value for constants
    
    public expr_node(node node) {
       super(node.left, node.right);
       if (left == null)
            System.out.println("BBBBBBBBBBBBBBBBBBB");
       n = node;
       if (n instanceof assign_node) 
            n = ((assign_node) n).expr; // Get the expression if it was an assignment

       if (n instanceof identifier_ref_node && ((identifier_ref_node) n).value != null) {
           value = ((identifier_ref_node) n).value;
           type =  ((identifier_ref_node) n).type;
       }
       if (n instanceof expr_node && ((expr_node) n).value != null) {
           value = ((expr_node) n).value;
           type =  ((expr_node) n).type;
       }
    }
    
    public expr_node(Location left, Location  right) { // For inheritance
        super(left, right);
        if (left == null)
            System.out.println("BBBBBBBBBBBBBBBBBBB");
    }
    
    @Override
    public void gest() {
        if (value != null) // DO NOT GENERATE CODE FOR COMPILE TIME EXPRESSIONS
            return;
        
        n.gest(); // Either process the expression or process the assignment (which will process its expression)
        if (n instanceof expr_node)
            varNum = ((expr_node) n).varNum;
        else if (n instanceof identifier_ref_node)
            varNum = ((identifier_ref_node) n).varNum;
        
        type = ((expr_node) n).type;
    }
}
