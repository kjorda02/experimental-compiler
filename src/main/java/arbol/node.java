package arbol;

import java_cup.runtime.ComplexSymbolFactory.Location;


/**
 *
 * @author kjorda
 */
public class node {
    public Location left, right;
    protected boolean error;
    
    public node(Location l, Location r) {
        left = l;
        right = r;
        this.error = true;
    }
    
    public node() {
        error = true;
    }

    public boolean isEmpty() {
        return error;
    }
    
    public void gest() {
        
    }
    
    public static boolean error(node n1) {
        return n1.isEmpty();
    }
    
    public static boolean error(node n1, node n2) {
        return n1.isEmpty() || n2.isEmpty();
    }
    
    public static boolean error(node n1, node n2, node n3) {
        return n1.isEmpty() || n2.isEmpty() || n3.isEmpty();
    }
 }