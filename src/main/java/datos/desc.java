package datos;

import arbol.type.complexType;

/**
 *
 * @author kjorda
 */

// Stores information associated with an identifier
public abstract class desc { // (d)
    // (td): Identifier type: JUST USE INSTANCEOF
    // (idt): DataType name: Not used since we keep a pointer to the actual type.
    
    public static class variable extends desc {
        public int varNum; // (nv) Position in the variable table
        public complexType type;
        
        public variable(complexType c, int varnum) {
            type = c;
        }
    }
    
    public static class constant extends desc {
        public long value; // (v)
        public basicType type;
        
        public constant(basicType b, long v) {
            type = b;
            value = v;
        }
    }
    
    public static class type extends desc {
        public complexType type; // (dt) Type descriptor
        
        public type(complexType c) {
            type = c;
        }
    }
    
    public static class function extends desc {
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