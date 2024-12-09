package datos;

/**
 *
 * @author kjorda
 */
public enum basicType { // Primitive types
        INT(4),
        BOOL(1),
        VOID(0);
        
        int bytes; // Size in memory in bytes
        private basicType(int b) {
            bytes = b;
        }
}