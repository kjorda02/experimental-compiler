package arbol.ref;

import arbol.type.complexType;
import arbol.node;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public abstract class ref_node extends node {
    public complexType type;
    private ref_node n;
            
    public ref_node(ref_node node) {
        n = node;
    }
    
    public ref_node(String nodeName) { // For inheritance
        super(nodeName);
    }
    
    @Override
    public void gest() {
        
    }
}