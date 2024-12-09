package datos;

/**
 *
 * @author kjorda
 */

// Stores information associated with an identifier
public abstract class desc { // (d)
    // (td): Identifier type: JUST USE INSTANCEOF
    // (idt): DataType name: Not used since we keep a pointer to the actual type.
    
    public class variable extends desc {
        public int varNum; // (nv) Position in the variable table
        public complexType type;
        
        public variable(complexType c) {
            type = c;
            varNum = varTable.newvar(0, false);
        }
    }
    
    public class constant extends desc {
        long value; // (v)
        basicType type;
        
        public constant(basicType b, long v) {
            type = b;
            value = v;
        }
    }
    
    public class type extends desc {
        public complexType type; // (dt) Type descriptor
        
        public type(complexType c) {
            type = c;
        }
    }
    
    public class function extends desc {
        public int funcNum; // (np) Position in the function table
        public int stackSize; // Stack size in bytes
        public complexType.funcsig signature;
        
        public function(complexType.funcsig s) {
            funcNum = 0; // funcTable.newfunc(...);
            signature = s;
            // stackSize = ...
        }
    }
    
    @Override
    public String toString() {
        String s = "[ ";
        
        return s;
    }
}