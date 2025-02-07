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
        
        public variable(complexType c, int v) {
            type = c;
            varNum = v;
        }
    }
    
    public static class constant extends desc {
        public int value; // (v)
        public basicType type;
        
        public constant(basicType b, int v) {
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
        public complexType.funcsig signature;
        public boolean defined;
        
        public function(int num, complexType.funcsig s) {
            funcNum = num; // funcTable.newfunc(...);
            signature = s;
            defined = false;
        }
    }
    
    @Override
    public String toString() {
        String s = "[ ";
        
        return s;
    }
}