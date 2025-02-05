package arbol.fun;

import arbol.node;
import arbol.terminal_node;
import arbol.type.complexType;

/**
 *
 * @author kjorda
 */
public class arglist_node extends node{
    public arglist_node list;
    public complexType type;
    public String name;
    public boolean out;
    
    public arglist_node(complexType t, terminal_node<String> id, arglist_node l, boolean o) {
        type = t;
        out = o;
        name = id.value;
    }
    
    public arglist_node() {
        
    }
    
    @Override
    public void gest() {
        
    }
}
