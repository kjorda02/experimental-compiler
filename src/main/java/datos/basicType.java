package datos;

/**
 *
 * @author kjorda
 */
public enum basicType { // Primitive types
        INT(4,"int"),
        BOOL(1, "bool"),
        VOID(0, "void");
        
        public int bytes; // Size in memory in bytes
        String name;
        private basicType(int b, String s) {
            bytes = b;
            name = s;
        }
        
        @Override
        public String toString() {
            return name;
        }
}