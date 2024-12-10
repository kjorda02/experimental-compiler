package arbol.val;

import arbol.type.complexType;
import arbol.assign_node;
import arbol.node;
import arbol.ref.identifier_ref_node;

/**
 *
 * @author kjorda
 */
public class expr_node extends node { // Array indices and literals are only allowed here!
    private node n; // expr_node, identifier_ref_node or assign_node
    public complexType type;
    public Long value; // Value for constants
    
    public expr_node(node node) {
       super("atomic expression");
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
    
    public expr_node(String nodeName) { // For inheritance
        super(nodeName);
    }
    
    @Override
    public void gest() {
        if (n.value != null) // DO NOT GENERATE CODE FOR COMPILE TIME EXPRESSIONS
            return;
        
        n.gest(); // Either process the expression or process the assignment (which will process its expression)
        varNum = n.varNum;
        type = ((expr_node) n).type;
    }
}
