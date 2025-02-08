package arbol.val;

import arbol.type.complexType;
import datos.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public class input_node extends expr_node {
    public input_node(Location l, Location r) {
        super(l,r);
        
        type = new complexType.primitive(null, basicType.INT);
        varNum = varTable.newvar(funcTable.currentFunc, 4);
        error = false;
    }
    
    @Override
    public void gest() {
        funcTable.funcInfo f = funcTable.getCurrent();
        f.unsafe_a_regs[0] = true; // We use a0 and a7 for the print syscall
        f.unsafe_a_regs[7] = true;
        
        cod.genera(cod.op.INPUT_CHAR, 0, 0, varNum);
    }
}
