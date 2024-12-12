package arbol.ref;
import arbol.type.complexType;
import datos.*;
import arbol.terminal_node;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class identifier_ref_node extends ref_node {
    public Long value; // For constants, we'll just pretend it doesn't exist until we reduce to EXPR
    String identifier;
    
    public identifier_ref_node(terminal_node<String> id) {
        super(id.left, id.right);
        identifier = id.value;
        
        desc d = symbolTable.get(identifier);
        if (d == null) {
            Main.report_error("Unknown identifier: <"+identifier+">", this);
            return;
        }
        if (d instanceof desc.constant) {
            desc.constant constd = ((desc.constant) d);
            value = constd.value;
            type = new complexType.primitive(null, constd.type);
        }
    }
    
    @Override
    public void gest() {
        if (value != null)
            return;
        
        desc d = symbolTable.get(identifier);
        if (d instanceof desc.variable) {
            desc.variable vard = ((desc.variable) d);
            varNum = vard.varNum;
            type = vard.type;
        }
        else {
            Main.report_error("\""+identifier+"\" is not a variable or constant.", this);
        }
    }
}
