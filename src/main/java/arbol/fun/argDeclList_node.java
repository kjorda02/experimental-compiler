package arbol.fun;

import arbol.node;
import arbol.terminal_node;
import arbol.type.complexType;

/**
 *
 * @author kjorda
 */
public class argDeclList_node extends node{
    public argDeclList_node next;
    public complexType type;
    public String name;
    public boolean out;
    
    public argDeclList_node(complexType t, terminal_node<String> id, argDeclList_node n, boolean o) {
        type = t;
        out = o;
        name = id.value;
        next = n;
    }
    
    public argDeclList_node() {
        
    }
}
