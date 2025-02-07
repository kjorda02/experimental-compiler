package arbol.val;
import arbol.node;
import arbol.type.complexType;
import datos.*;
import static datos.OP.AND;
import static datos.OP.OR;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class binaryOp_node extends expr_node {
    expr_node leftChild, rightChild; // unaryOp_node or binaryOp_node
    OP op;
    
    public binaryOp_node(OP operator, binaryOp_node n1, binaryOp_node n2) {
        super(n1.left, n2.right);
        leftChild = n1;
        rightChild = n2;
        op = operator;
        
        if (node.error(n1, n2) || checkTypes()) {
            return;
        }   
        
        if (leftChild.value != null && rightChild.value != null)  // CONSTANT EXPRESSION
            value = evalConst(); // Will update type if it's not the same as child's
        if (leftChild.value != null && op == OP.NONE)
            value = leftChild.value;
        error = false;
    }
    
    public binaryOp_node(unaryOp_node n) {
        super(n.left, n.right);
        leftChild = n;
        op = OP.NONE;
        type = leftChild.type;
        value = leftChild.value; // Null unless compile-time expression
        error = n.isEmpty();
    }
    
    private int evalConst() {
        switch(op){
            case PLUS:
                return leftChild.value + rightChild.value;
            case NEG:
                return leftChild.value - rightChild.value;
            case TIMES:
                return leftChild.value * rightChild.value;
            case DIV:
                return leftChild.value / rightChild.value;
            case AND:
                return leftChild.value & rightChild.value;
            case OR:
                return leftChild.value | rightChild.value;
            case LT:
                return leftChild.value<rightChild.value ? -1 : 0;
            case GT:
                return leftChild.value>rightChild.value ? -1 : 0;
            case LEQ:
                return leftChild.value<=rightChild.value ? -1 : 0;
            case GEQ:
                return leftChild.value>=rightChild.value ? -1 : 0;
            case EQ:
                return leftChild.value==rightChild.value ? -1 : 0;
            case NEQ:
                return leftChild.value!=rightChild.value ? -1 : 0;
        }
        return 0;
    }
    
    private boolean checkTypes() {
        // Not allowing operator overloading for now
        if (!(leftChild.type instanceof complexType.primitive)) {
            Main.report_error("Cannot perform binary operation \""+op.toString()+"\" with left operand of non-primitive type \""+leftChild.type.toString()+"\"", this);
            return true;
        }
        if (!(rightChild.type instanceof complexType.primitive)) {
            Main.report_error("Cannot perform binary operation \""+op.toString()+"\" with right operand of non-primitive type \""+rightChild.type.toString()+"\"", this);
            return true;
        }
        
        basicType leftType = ((complexType.primitive) leftChild.type).btype;
        basicType rightType = ((complexType.primitive) rightChild.type).btype;

        type = new complexType.primitive(null, basicType.BOOL);
        switch (op) {
            case PLUS:
            case NEG:
            case TIMES:
            case DIV:
                type = new complexType.primitive(null, basicType.INT);
            case LT:
            case GT:
            case LEQ:
            case GEQ:
                if (leftType != basicType.INT) {
                    Main.report_error("Invalid type \""+leftChild.type.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    return true;
                }
                if (rightType != basicType.INT) {
                    Main.report_error("Invalid type \""+rightChild.type.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    return true;
                }
                break;
            case AND:
            case OR:
                if (leftType != basicType.BOOL) {
                    Main.report_error("Invalid type \""+leftChild.type.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    return true;
                }
                if (rightType != basicType.BOOL) {
                    Main.report_error("Invalid type \""+rightChild.type.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    return true;
                }
                break;
            case EQ:
            case NEQ:
                if (leftType != rightType) {
                    Main.report_error("Cannot compare expressions of differing types \""+leftChild.type.toString()+"\" and \""+rightChild.type.toString()+"\"", value);
                    return true;
                }
        }
        return false;
    }
    
    @Override
    public void gest() {
        if (value != null || error) // DO NOT GENERATE CODE FOR COMPILE TIME EXPRESSIONS
            return;
        
        if (leftChild.value == null) {
            leftChild.gest();
            if (op == OP.NONE) { // binary_expr -> unary_expr
                varNum = leftChild.varNum;
                return;
            }
        }
        if (rightChild.value == null)
            rightChild.gest();
        
        // EXPRESSION EVALUATION -----------------------------------------------
        int op1 = (leftChild.value == null) ? leftChild.varNum : leftChild.value;
        int op2 = (rightChild.value == null) ? rightChild.varNum : rightChild.value;
        
        int t = varTable.newvar(funcTable.currentFunc, type.bytes);
        switch(op){
            case LT:
                gest_rel(cod.op.IFLT, op1, op2, t);
                break;
            case GT:
                gest_rel(cod.op.IFGT, op1, op2, t);
                break;
            case LEQ:
                gest_rel(cod.op.IFLE, op1, op2, t);
                break;
            case GEQ:
                gest_rel(cod.op.IFGE, op1, op2, t);
                break;
            case EQ:
                gest_rel(cod.op.IFEQ, op1, op2, t);
                break;
            case NEQ:
                gest_rel(cod.op.IFNE, op1, op2, t);
                break;
            default:
                switch(op) {
                    case PLUS:
                        cod.genera(cod.op.ADD, op1, op2, t);
                        break;
                    case NEG:
                        cod.genera(cod.op.SUB, op1, op2, t);
                        break;
                    case TIMES:
                        cod.genera(cod.op.PROD, op1, op2, t);
                        break;
                    case DIV:
                        cod.genera(cod.op.DIV, op1, op2, t);
                        break;
                    case AND:
                        cod.genera(cod.op.AND, op1, op2, t);
                        break;
                    case OR:
                        cod.genera(cod.op.OR, op1, op2, t);
                        break;
                }
                cod.setImmediate(leftChild.value != null, rightChild.value != null);
        }
        varNum = t;
    }
    
    private void gest_rel(cod.op op, int op1, int op2, int t) {
        int tag1 = cod.newTag();
        int tag2 = cod.newTag();
        
        cod.genera(op, op1, op2, 0); // if leftchild ⊕ rightchild goto tag1
        cod.setImmediate(leftChild.value != null, rightChild.value != null);
        cod.jmpTag(tag1);
        
        cod.genera(cod.op.COPY, 0, 0, t); // t = 0
        cod.setImmediate(true, false);
        
        cod.genera(cod.op.GOTO, 0, 0, 0); // GOTO tag2
        cod.jmpTag(tag2);
        
        cod.setTag(tag1); // tag1 : skip
        cod.genera(cod.op.COPY, -1, 0, t); // t = -1
        cod.setImmediate(true, false);
        cod.setTag(tag2); // tag2 : skip
    }
}
