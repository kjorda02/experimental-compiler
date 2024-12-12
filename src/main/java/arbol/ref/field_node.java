package arbol.ref;

import arbol.terminal_node;
import arbol.type.complexType;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
// STRUCT FIELD ACCESS NODE
public class field_node extends ref_node {
    ref_node base;
    String fieldName;
    
    public field_node(ref_node s, terminal_node<String> name) {
        super(s.left, name.right);
        base = s;
        fieldName = name.value;
    }
    
    @Override
    public void gest() {
        if (!(base.type instanceof complexType.struct)) 
            Main.report_error("Cannot access field \""+fieldName+"\": not a struct.", this);
        
        // Figure out var number based on the base's varNum and the displacement of the field in the type
    }
}
