package arbol.fun;

import arbol.val.expr_node;

/**
 *
 * @author kjorda
 */
public class arglist_node {
    public expr_node expr;
    public arglist_node next;
    
    public arglist_node(expr_node e, arglist_node n) {
        expr = e;
        next = n;
    }
    
    public arglist_node() {
        
    }
}
