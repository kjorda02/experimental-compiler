package arbol.val;
import arbol.type.complexType;
import experimental_compiler.Main;
import datos.*;
import datos.cod.*;

/**
 *
 * @author kjorda
 */
public class unaryOp_node extends expr_node {
    private OP oper;
    expr_node child;
    
    public unaryOp_node(expr_node expr) {
        super(expr.left, expr.right);
        oper = OP.NONE;
        child = expr;
        type = child.type;
        value = child.value; // Null unless its a compile-time expression
        error = expr.isEmpty();
    }
    
    public unaryOp_node(OP operator, expr_node expr) {
        super(expr.left, expr.right);
        child = expr;
        oper = operator;
        type = child.type;
        if (expr.isEmpty())
            return;
        
        if (child.value != null)
            value = evalConst();
        
        basicType btype = ((complexType.primitive) type).btype;
        switch(oper) {
            case NOT:
                if (btype != basicType.BOOL) {
                    Main.report_error("Operator '!' cannot be aplied to non-boolean type", this);
                    return;
                }
                    
                break;
            case NEG:
                if (btype != basicType.INT) {
                    Main.report_error("Operator '-' cannot be aplied to non-numeric type", this);
                    return;
                }
        }
        error = false;
    }
    
    private long evalConst() {
        switch(oper) {
            case NOT:
                return ~child.value;
            case NEG:
                return -child.value;
        }
        return 0;
    }
    
    @Override
    public void gest() { // TODO: CHECK IF ASSIGNING TYPES AT CODE GENERATION TIME IS NECESSARY
        if (value != null || error) // DO NOT GENERATE CODE FOR COMPILE TIME EXPRESSIONS
            return;
        
        child.gest();
        
        if (oper == oper.NONE) {
            varNum = child.varNum;
            return;
        }
        
        // We cannot store the result of the operation in the same variable, since that variable
        int t = varTable.newvar(0, false); // Could correspond to an actual variable
        varNum = t;
        switch(oper) {
            case NOT:
                cod.genera(op.NOT, child.varNum, 0, t);
                break;
            case NEG:
                cod.genera(op.NEG, child.varNum, 0, t);
        }
    }
}
