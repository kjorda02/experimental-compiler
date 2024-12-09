package arbol.ref;
import datos.*;
import datos.desc.*;
import datos.cod.*;
import arbol.node;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class identifier_ref_node extends ref_node {
    String identifier;
    
    public identifier_ref_node(String id) {
        super("variable");
        identifier = id;
    }
    
    public void gest() {
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
