package arbol.flow;

import arbol.node;

/**
 *
 * @author kjorda
 */
public class stmts_node extends node {
    stmt_node stmt;
    stmts_node stmts;
    
    public stmts_node(stmt_node n1, stmts_node n2) {
        stmt = n1;
        stmts = n2;
    }
    
    public stmts_node(stmt_node n1) {
        stmt = n1;
    }
    
    public stmts_node() {
        stmt = null;
    }
    
    @Override
    public void gest() {
        stmt.gest();
        if (stmts !=  null) // Null if it's the last statement
            stmts.gest();
    }
}
