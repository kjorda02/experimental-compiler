package arbol;

import arbol.val.expr_node;
import datos.cod;
import datos.funcTable;
import datos.funcTable.funcInfo;

/**
 *
 * @author kjorda
 */
public class print_node extends node {
    expr_node ex;
    
    public print_node(expr_node e) {
        ex = e;
    }
    
    @Override
    public void gest() {
        funcInfo f = funcTable.getCurrent();
        f.unsafe_a_regs[0] = true; // We use a0 and a7 for the print syscall
        f.unsafe_a_regs[7] = true;
        
        ex.gest();
        int v = ex.getVarNum();
        
        cod.genera(cod.op.PRINT_CHAR, v, 0, 0);
    }
}
