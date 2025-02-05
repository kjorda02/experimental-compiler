package arbol.ref;

import arbol.fun.arglist_node;
import arbol.terminal_node;
import arbol.type.complexType;
import datos.cod;
import datos.desc;
import datos.funcTable;
import datos.symbolTable;
import datos.varTable;
import experimental_compiler.Main;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class call_node extends ref_node {
    int funcID;
    complexType returnType;
    
    public call_node(terminal_node<String> id, arglist_node l, Location r) {
        super(id.left, r);
        
        desc d = symbolTable.get(id.value);
        
        if (!(d instanceof desc.function)) {
            Main.report_error("Symbol is not a function", id.left, id.right);
            return;
        }
        
        // Check for argument mismatches...
        
        funcID = ((desc.function) d).funcNum;
        returnType = ((desc.function) d).signature.returnType;
        
        error = false;
    }
    
    @Override
    public void gest() {
        funcTable.addCaller(funcID); // Adds currentFunc as a caller
        
        // TODO: Handle complex types
        
        varNum = varTable.newvar(funcTable.currentFunc, 4);
        
        // TODO: param_s, param_c, param_out
        
        // Call instruction. Jumps to tag of the function. Copies return value from
        // a0 to the location of varNum
        cod.genera(cod.op.CALL, 0, 0, funcID);
    }
}
