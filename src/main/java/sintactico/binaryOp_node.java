package sintactico;
import datos.OP;
import datos.Type;
import datos.val;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class binaryOp_node extends node {
    unaryOp_node leftChild;
    binaryOp_node rightChild;
    OP op;
    
    public binaryOp_node(OP operator, unaryOp_node n1, binaryOp_node n2) {
        super("BinaryOp", null);
        leftChild = n1;
        rightChild = n2;
        op = operator;
    }
    
    public binaryOp_node(unaryOp_node n) {
        super("BinaryOp", null);
        leftChild = n;
        op = OP.NONE;
    }
    
    public void gest() {
        leftChild.gest();
        if (op == OP.NONE) {
            value = leftChild.value;
            return;
        }
        rightChild.gest();
        val val1 = leftChild.value;
        val val2 = rightChild.value;
        
        switch (op) {
            case PLUS:
            case NEG:
            case TIMES:
            case DIV:
            case LT:
            case GT:
            case LEQ:
            case GEQ:
                if (val1.type != Type.INT) {
                    Main.report_error("Invalid type \""+val1.type.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                if (val2.type != Type.INT) {
                    Main.report_error("Invalid type \""+val2.type.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                break;
            case AND:
            case OR:
                value = new val(logicOp(op, (boolean) val1.val, (boolean) val2.val), Type.BOOL);
                if (val1.type != Type.BOOL) {
                    Main.report_error("Invalid type \""+val1.type.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    break;
                }
                if (val2.type != Type.BOOL) {
                    Main.report_error("Invalid type \""+val2.type.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    break;
                }
            case EQ:
            case NEQ:
                if (val1.type != val2.type) {
                    Main.report_error("Cannot compare expressions of differing types \""+val1.type.toString()+"\" and \""+val2.type.toString()+"\"", value);
                }
        }
        
        switch(op){
            case PLUS:
            case NEG:
            case TIMES:
            case DIV:
                value = new val(arithmeticOp(op, (int) val1.val, (int) val2.val), Type.INT);
                break;
            case LT:
            case GT:
            case LEQ:
            case GEQ:
                value = new val(relationalOp(op, (int) val1.val, (int) val2.val), Type.BOOL);
                break;
            case EQ:
                value = new val(val1.val == val2.val, Type.BOOL);
                break;
            case NEQ:
                value = new val(val1.val != val2.val, Type.BOOL);
        }
        
    }
    
    private int arithmeticOp(OP op, int a, int b) {
        switch(op) {
            case PLUS:
                return a+b;
            case NEG:
                return a-b;
            case TIMES:
                return a*b;
            case DIV:
                return a/b;
        }
        (new Exception("UNEXPECTED ARITHMETIC OPERATOR")).printStackTrace();
        return 0;
    }
    
    private boolean relationalOp(OP op, int a, int b) {
        switch(op) {
            case LT:
                return a<b;
            case GT:
                return a>b;
            case LEQ:
                return a<=b;
            case GEQ:
                return a>=b;
        }
        (new Exception("UNEXPECTED RELATIONAL OPERATOR")).printStackTrace();
        return false;
    }
    
    private boolean logicOp(OP op, boolean a, boolean b) {
        switch(op) {
            case AND:
                return a && b;
            case OR:
                return a || b;
        }
        (new Exception("UNEXPECTED LOGICAL OPERATOR")).printStackTrace();
        return false;
    }
    
}
