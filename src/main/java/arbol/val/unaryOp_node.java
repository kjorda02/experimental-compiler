package arbol.val;
import arbol.val.expr_node;
import experimental_compiler.Main;
import datos.*;
import datos.desc.*;
import datos.cod.*;
import arbol.node;

/**
 *
 * @author kjorda
 */
public class unaryOp_node extends node {
    private OP oper;
    expr_node child;
    
    public unaryOp_node(expr_node expr) {
        super("unary operator");
        oper = OP.NONE;
        child = expr;
    }
    
    public unaryOp_node(OP operator, expr_node expr) {
        super("unary operator");
        child = expr;
        oper = operator;
    }
    
    public void gest() {
        child.gest();
        type = child.type;
        dataType = child.dataType;
        
        if (oper == oper.NONE) {
            var = child.var;
            return;
        }
        
        // We cannot store the result of the operation in the same variable, since that variable
        int t = varTable.newvar(0, false); // Could correspond to an actual variable
        empty = true;
        switch(oper) {
            case NOT:
                if (child.type.basicType != basicType.BOOL) {
                    Main.report_error("Operator '!' cannot be aplied to non-boolean type", this);
                    return;
                }
                    
                cod.genera(op.NOT, child.var, 0, t);
                break;
            case NEG:
                if (child.type.basicType != basicType.INT) {
                    Main.report_error("Operator '-' cannot be aplied to non-numeric type", this);
                    return;
                }
                
                cod.genera(op.NEG, child.var, 0, t);
                break;
        }
        empty = false;
        var = t;
    }
}
