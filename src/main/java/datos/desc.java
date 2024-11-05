package datos;

/**
 *
 * @author kjorda
 */

// Stores information associated with an identifier
public class desc { // (d)
    public enum idType { // Identifier type
        VAR,
        CONST,
        FUNC,
        TYPE
    }

    
    static int varIdCount = 0;
    static int funcIdCount = 0;
    
    public idType type; // (td) Identifier type (e.g. variable, constant, function name, struct field, etc)
    public String dataType; // (idt) Only used for variables, constants, struct fields, function arguments, etc
    public int tableIdx; // (nv/np) Position in the variable or function table (depending on type)
    public long val; // (v) Value for constants (if idType == CONST)
    public typeDesc typeDesc; // (dt) Used if idType == TYPE.

    // Variable or function parameter descriptor
    public desc(idType idt, String datat) {
        tableIdx = varTable.newvar(0, false);
        type = idt;
        dataType = datat;
    }
    
    // Function descriptor
    public desc() {
        tableIdx = 0; // funcTable.newfunc(...);
        type = type.FUNC;
    }
    
    // Type descriptor
    public desc(typeDesc t) {
        type = type.TYPE;
        typeDesc = t;
    }
    
    // Constant descriptor
    public desc(long v, String datat) {
        type = type.CONST;
        val = v;
        dataType = datat;
    }
    
    // (dc) field descriptor
    // (idr) record name
    
    // (dta) 
    
    @Override
    public String toString() {
        String s = "[ ";
        
        return s;
    }
}