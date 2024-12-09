package arbol.val;
import datos.*;
import static datos.OP.AND;
import static datos.OP.OR;
import experimental_compiler.Main;
import arbol.node;

/**
 *
 * @author kjorda
 */
public class binaryOp_node extends node {
    unaryOp_node leftChild;
    binaryOp_node rightChild;
    OP op;
    
    public binaryOp_node(OP operator, unaryOp_node n1, binaryOp_node n2) {
        super("BinaryOp");
        leftChild = n1;
        rightChild = n2;
        op = operator;
    }
    
    public binaryOp_node(unaryOp_node n) {
        super("BinaryOp");
        leftChild = n;
        op = OP.NONE;
    }
    
    public void gest() {
        leftChild.gest();
        type = leftChild.type;
        if (op == OP.NONE) { // binary_expr -> unary_expr
            var = leftChild.var;
            value = leftChild.value;
            return;
        }
        rightChild.gest();
        
        if (leftChild.dataType != null && rightChild.dataType != null) {
            if (!leftChild.dataType.equals(rightChild.dataType)) {
                Main.report_error("Differing types for binary operation: \""+leftChild.dataType+"\" and \""+rightChild.dataType+"\"", value);
            }
        }
        
        // Type checking
        empty = true;
        switch (op) {
            case PLUS:
            case NEG:
            case TIMES:
            case DIV:
            case LT:
            case GT:
            case LEQ:
            case GEQ:
                if (leftChild.type.basicType != basicType.INT) {
                    Main.report_error("Invalid type \""+leftChild.type.basicType.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                if (rightChild.type.basicType != basicType.INT) {
                    Main.report_error("Invalid type \""+rightChild.type.basicType.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                break;
            case AND:
            case OR:
                if (leftChild.type.basicType != basicType.BOOL) {
                    Main.report_error("Invalid type \""+leftChild.type.basicType.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                if (rightChild.type.basicType != basicType.BOOL) {
                    Main.report_error("Invalid type \""+rightChild.type.basicType.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                break;
            case EQ:
            case NEQ:
                if (leftChild.type.basicType != rightChild.type.basicType) {
                    Main.report_error("Cannot compare expressions of differing types \""+leftChild.type.basicType.toString()+"\" and \""+rightChild.type.basicType.toString()+"\"", value);
                    return;
                }
        }
        empty = false;
        
        int t = varTable.newvar(0, false);
        switch(op){
            case PLUS:
                cod.genera(cod.op.ADD, leftChild.var, rightChild.var, t);
                break;
            case NEG:
                cod.genera(cod.op.SUB, leftChild.var, rightChild.var, t);
                break;
            case TIMES:
                cod.genera(cod.op.PROD, leftChild.var, rightChild.var, t);
                break;
            case DIV:
                cod.genera(cod.op.DIV, leftChild.var, rightChild.var, t);
                break;
            case AND:
                cod.genera(cod.op.AND, leftChild.var, rightChild.var, t);
                break;
            case OR:
                cod.genera(cod.op.OR, leftChild.var, rightChild.var, t);
                break;
            case LT:
                gest_rel(cod.op.IFLT, t);
                break;
            case GT:
                gest_rel(cod.op.IFGT, t);
                break;
            case LEQ:
                gest_rel(cod.op.IFLE, t);
                break;
            case GEQ:
                gest_rel(cod.op.IFGE, t);
                break;
            case EQ:
                gest_rel(cod.op.IFEQ, t);
                break;
            case NEQ:
                gest_rel(cod.op.IFNE, t);
                break;
        }
        var = t;
    }
    
    private void gest_rel(cod.op op, int t) {
        int tag1 = cod.newTag();
        int tag2 = cod.newTag();
        
        cod.genera(op, leftChild.var, rightChild.var, 0); // if leftchild ⊕ rightchild goto tag1
        cod.replaceWithTag(2, tag1);
        
        cod.genera(cod.op.COPYLIT, 0, 0, t); // t = 0
        
        cod.genera(cod.op.GOTO, 0, 0, 0); // GOTO tag2
        cod.replaceWithTag(2, tag2);
        
        cod.setTag(tag1); // tag1 : skip
        cod.genera(cod.op.COPYLIT, -1, 0, t); // t = -1
        cod.setTag(tag2); // tag2 : skip
        
    }
}
