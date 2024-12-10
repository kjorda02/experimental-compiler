package arbol;

/**
 *
 * @author kjorda
 */
public class stmts_node extends node{
    node stmt, stmts;
    
    public stmts_node(node n1, node n2) {
        stmt = n1;
        stmts = n2;
    }
    
    @Override
    public void gest() {
        stmt.gest();
        stmts.gest();
    }
}
