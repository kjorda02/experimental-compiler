package arbol;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class terminal_node<T> {
    public T value;
    public Location left;
    public Location right;
    
    public terminal_node(T val, Location l, Location r) {
        value = val;
        left = l;
        right = r;
    }
}
