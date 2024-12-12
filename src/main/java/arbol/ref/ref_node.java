package arbol.ref;

import arbol.type.complexType;
import arbol.node;
import experimental_compiler.Main;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author kjorda
 */
public abstract class ref_node extends node {
    public Integer varNum = null;    // (nv) Points to a variable in the variable table
    public complexType type;
//    private ref_node n;
//            
//    public ref_node(ref_node node) {
//        n = node;
//    }
    
    public ref_node(Location left, Location right) { // For inheritance
        super(left, right);
    }
    
    @Override
    public void gest() {
        
    }
}