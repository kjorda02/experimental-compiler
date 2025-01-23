package arbol;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class terminal_node<T> extends node {
    public T value;
    
    public terminal_node(T val, Location l, Location r) {
        super(l, r);
        value = val;
        error = false;
    }
}
