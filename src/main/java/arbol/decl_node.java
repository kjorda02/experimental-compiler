package arbol;
import arbol.type.complexType;
import arbol.val.expr_node;
import datos.*;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class decl_node extends node {
    complexType type; // (dt)
    String identifier;
    expr_node expr;
    boolean isConst;
    
    public decl_node(boolean c, complexType t, String s) {
        super("variable/constant declaration");
        type = t;
        identifier = s;
        isConst = c;
    }
    
    public decl_node(boolean c, complexType t, String s, expr_node e) {
        super("variable/constant declaration");
        type = t;
        identifier = s;
        isConst = c;
        expr = e;
    }
    
    @Override
    public void gest() {
        type.gest();
        System.out.println(type.toString()); // Debug
        
        desc d;
        if (isConst) {
            if (!(type instanceof complexType.primitive)) {
                Main.report_error("Cannot define constant of non-primitive type \""+type.toString()+"\"", this);
                return;
            }
            if (expr == null) {
                Main.report_error("Constant must be assigned a value at declaration.", this);
                return;
            }
            if (!type.equals(expr.type)) {
                Main.report_error("Cannot assign expression of type <"+expr.type.toString()+"> to constant of type <"+type.toString()+">", this);
                return;
            }
            if (expr.value == null) {
                Main.report_error("Constants cannot be assigned runtime expressions", this);
                return;
            }
            
            long val = ((expr_node) expr).value;
            basicType btype = ((complexType.primitive) type).btype;
            d = new desc.constant(btype, val);
        }
        else {
            d = new desc.variable(type);
            int varNumDst = ((desc.variable) d).varNum;
            
            if (expr == null)
                return;
            
            if (!type.equals(expr.type)) {
                Main.report_error("Cannot assign expression of type <"+expr.type.toString()+"> to variable of type <"+type.toString()+">", this);
                return;
            }
            
            if (expr.value == null) { // Runtime expression
                expr.gest();
                cod.genera(cod.op.COPY, expr.varNum, 0, varNumDst);
            }
            else  // Compile-time expression
                cod.genera(cod.op.COPYLIT, expr.value, 0, varNumDst);
        }
        
        symbolTable.add(identifier, d);
    }
}