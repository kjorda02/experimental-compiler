package arbol.fun;

import arbol.flow.stmts_node;
import arbol.node;
import arbol.type.complexType;
import datos.cod;
import datos.desc;
import datos.funcTable;
import datos.funcTable.funcInfo;
import datos.symbolTable;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class fundecl_node extends node {
    complexType.funcsig signature;
    stmts_node stmts;
    int funcID;
    
    public fundecl_node(complexType.funcsig sig, boolean definition) {
        signature = sig;
        
        desc de = symbolTable.get(sig.name.value); 
        if (de != null) { // Check if it is already declared
            if (!definition) {
                Main.report_error("<"+sig.name.value+"> has already been declared.", sig.name.value);
                return;
            }

            if (!(de instanceof desc.function)) {
                Main.report_error("<"+sig.name.value+"> is already defined as different kind of identifier", sig.name.value);
                return;
            }

            if (((desc.function) de).defined) {
                Main.report_error("Function body for <"+sig.name.value+"> is already defined.", left);
                return;
            }
        }
        
        funcID = funcTable.add(sig);
        desc.function d = new desc.function(funcID, signature);
        symbolTable.add(signature.name.value, d); // Need to add it before '{' rather than after '}', so that we can have recursion
        
        // TODO: If returns something, last stmt must be return_node
    }
    
    public void addArgs() {
        funcTable.currentFunc = funcID;
        funcInfo f = funcTable.get(funcID);

        for (int i = 0; i < signature.paramNames.size(); i++) {
            symbolTable.add(signature.paramNames.get(i), new desc.variable(signature.paramTypes.get(i), f.params.get(i)));
        }
    }
    
    public void setStmts(stmts_node n) { // Called when we reduce "stmts }"
        stmts = n;
        funcTable.currentFunc = -1;
    }
    
    @Override
    public void gest() {
        funcTable.currentFunc = funcID;
        cod.setTag(signature.name.value);
        cod.genera(cod.op.PMB, 0, 0, funcTable.currentFunc);
        
        if (stmts != null)
            stmts.gest();
        
        cod.genera(cod.op.RTN, 0, 0, funcTable.currentFunc);
        funcTable.currentFunc = -1;
    }
}
