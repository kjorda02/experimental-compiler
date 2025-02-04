package arbol.fun;

import arbol.flow.stmts_node;
import arbol.node;
import arbol.type.complexType;
import datos.funcTable;

/**
 *
 * @author kjorda
 */
public class fundecl_node extends node {
    complexType.funcsig signature;
    stmts_node stmts;
    
    public fundecl_node(complexType.funcsig sig, stmts_node s) {
        signature = sig;
        stmts = s;
    }
    
    @Override
    public void gest() {
        funcTable.currentFunc = signature.name.value;
    }
}
