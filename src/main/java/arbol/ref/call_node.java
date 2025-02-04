package arbol.ref;

import arbol.fun.arglist_node;
import arbol.terminal_node;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class call_node extends ref_node {
    public call_node(terminal_node<String> id, arglist_node l, Location r) {
        super(id.left, r);
        error = false;
    }
    
    @Override
    public void gest() {
        
    }
}
