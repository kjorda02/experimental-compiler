package arbol.ref;

import datos.complexType;
import arbol.node;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class ref_node extends node {
    public complexType type;
    private ref_node n;
            
    public ref_node(ref_node node) {
        n = node;
    }
    
    public ref_node(String nodeName) { // For inheritance
        super(nodeName);
    }
    
    public void gest() {
       n.gest();
       if (n.type instanceof complexType.primitive) {
           varNum = n.varNum;
       }
       else {
           Main.report_error("Cannot use non-primitive value in expression", this); // TODO: Print out the type
       }
    }
}
