package arbol;

/**
 *
 * @author kjorda
 */
public class stmt_node extends node {
    node n;
    
    public stmt_node(node n) {
        this.n = n;
    }
    
    public void gest() {
        n.gest();
    }
}
