package arbol;

import java_cup.runtime.ComplexSymbolFactory.Location;


/**
 *
 * @author kjorda
 */
public class node {
    public Location left, right;
    protected boolean empty;
    
    public node(Location l, Location r) {
        left = l;
        right = r;
        this.empty = false;
    }
    
    public node() {
        empty = true;
    }

    public boolean isEmpty() {
        return empty;
    }
    
    public void gest() {
        
    }
    
 }