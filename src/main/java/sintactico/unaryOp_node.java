package sintactico;
import datos.OP;
import datos.Type;
import datos.desc;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class unaryOp_node extends node {
    private OP op;
    expr_node child;
    
    public unaryOp_node(expr_node expr) {
        super("unary operator", null);
        op = OP.NONE;
        child = expr;
    }
    
    public unaryOp_node(OP operator, expr_node expr) {
        super("unary operator", null);
        child = expr;
        op = operator;
    }
    
    public void gest() {
        child.gest();
        desc val = child.value;
        
        switch(op) {
            case NOT:
                if (val.type != Type.BOOL) 
                    Main.report_error("Operator '!' cannot be aplied to non-boolean type", this);
                else
                    value = new desc(!((boolean) val.val), val.type);
                break;
            case NEG:
                if (val.type != Type.INT)
                    Main.report_error("Operator '-' cannot be aplied to non-numeric type", this);
                else
                    value = new desc(-((int) val.val), val.type);
                break;
            default:
                value = new desc(val.val, val.type);
        }
    }
}
