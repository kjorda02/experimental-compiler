package arbol.fun;

import arbol.flow.stmts_node;
import arbol.node;
import arbol.type.complexType;
import datos.cod;
import datos.desc;
import datos.funcTable;
import datos.symbolTable;

/**
 *
 * @author kjorda
 */
public class fundecl_node extends node {
    complexType.funcsig signature;
    stmts_node stmts;
    int funcID;
    
    public fundecl_node(complexType.funcsig sig, stmts_node s) {
        signature = sig;
        stmts = s;
        
        funcID = funcTable.add(sig);
        
        desc.function d = new desc.function(funcID, sig);
        symbolTable.add(sig.name.value, d);
    }
    
    @Override
    public void gest() {
        funcTable.currentFunc = funcID;
        cod.setTag(signature.name.value);
        cod.genera(cod.op.PMB, 0, 0, 0);
        
        stmts.gest();
        
        cod.genera(cod.op.RTN, 0, 0, 0);
        funcTable.currentFunc = -1;
    }
}
