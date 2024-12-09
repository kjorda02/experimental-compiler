package arbol.val;

import arbol.assign_node;
import datos.*;
import datos.cod.*;
import datos.desc.*;
import arbol.node;
import arbol.ref.identifier_ref_node;
import arbol.ref.ref_node;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class expr_node extends node { // Array indices and literals are only allowed here!
    private node n; // expr_node, identifier_ref_node or assign_node
    public complexType type;
    Long value; // Value for constants
    
    public expr_node(node node) {
       super("atomic expression");
       n = node;
    }
    
    public expr_node(String nodeName) { // For inheritance
        super(nodeName);
    }
    
    public void gest() {
        n.gest(); // Either process the expression or process the assignment (which will process its expression)
        if (n instanceof assign_node) 
            n = ((assign_node) n).expr; // Get the expression if it was an assignment
        
        if (n instanceof identifier_ref_node) 
            value = ((identifier_ref_node) n).value; // Null if not a constant
        
        varNum = n.varNum;
        type = ( (expr_node) n).type;
        
        if (!(type instanceof complexType.primitive)) 
           Main.report_error("Cannot use non-primitive value in expression", this); // TODO: Print out the type
    }
}
