package arbol;
import arbol.type.complexType;
import arbol.type.type_node;
import arbol.val.expr_node;
import arbol.val.literal_expr_node;
import datos.*;
import arbol.type.type_node;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class decl_node extends node {
    type_node type;
    String identifier;
    expr_node expr;
    boolean isConst;
    
    public decl_node(boolean c, type_node t, String s) {
        super("variable/constant declaration");
        type = t;
        identifier = s;
        isConst = c;
    }
    
    public decl_node(boolean c, type_node t, String s, expr_node e) {
        super("variable/constant declaration");
        type = t;
        identifier = s;
        isConst = c;
        expr = e;
    }
    
    @Override
    public void gest() {
        type.gest();
        complexType typeDesc = type.typeDesc; // (dt)
        
        desc d;
        if (isConst) { // TODO: Allow assign of constants to other constants?
            if (!(typeDesc instanceof complexType.primitive)) {
                Main.report_error("Cannot define constant of non-primitive type \""+typeDesc.toString()+"\"", value);
                return;
            }
            
            long val = ((literal_expr_node) expr).value;
            basicType btype = ((complexType.primitive) typeDesc).btype;
            d = new desc.constant(btype, val);
        }
        else {
            d = new desc.variable(typeDesc);
            if (expr != null) {
                expr.gest();
                int varNumDst = ((desc.variable) d).varNum;
                cod.genera(cod.op.COPY, expr.varNum, 0, varNumDst);
            }
        }
        
        symbolTable.add(identifier, d);
    }
}
