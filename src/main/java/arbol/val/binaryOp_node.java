package arbol.val;
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
        
        if (leftChild.value != null && rightChild.value != null){  // CONSTANT EXPRESSION
            type = leftChild.type;
            value = evalConst(); // Will update type if it's not the same as child's
        }
    }
    
    public binaryOp_node(unaryOp_node n) {
        super(n.left, n.right);
        leftChild = n;
        op = OP.NONE;
        if (leftChild.value != null) {
            value = leftChild.value;
            type = leftChild.type;
        }
    }
    
    private long evalConst() {
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
                type = new complexType.primitive(null, basicType.BOOL);
                return leftChild.value<rightChild.value ? -1 : 0;
            case GT:
                type = new complexType.primitive(null, basicType.BOOL);
                return leftChild.value>rightChild.value ? -1 : 0;
            case LEQ:
                type = new complexType.primitive(null, basicType.BOOL);
                return leftChild.value<=rightChild.value ? -1 : 0;
            case GEQ:
                type = new complexType.primitive(null, basicType.BOOL);
                return leftChild.value>=rightChild.value ? -1 : 0;
            case EQ:
                type = new complexType.primitive(null, basicType.BOOL);
                return leftChild.value==rightChild.value ? -1 : 0;
            case NEQ:
                type = new complexType.primitive(null, basicType.BOOL);
                return leftChild.value!=rightChild.value ? -1 : 0;
        }
        return 0;
    }
    
    @Override
    public void gest() { // TODO: CHECK IF ASSIGNING TYPES AT CODE GENERATION TIME IS NECESSARY
        if (value != null) // DO NOT GENERATE CODE FOR COMPILE TIME EXPRESSIONS
            return;
        
        if (leftChild.value == null)
            leftChild.gest();
        else {
            int t = varTable.newvar(0, false);
            cod.genera(cod.op.COPYLIT, leftChild.value, 0, t);
            leftChild.varNum = t;
        }
        
        type = leftChild.type;
        if (op == OP.NONE) { // binary_expr -> unary_expr
            varNum = leftChild.varNum;
            value = leftChild.value;
            return;
        }
        
        if (rightChild.value == null)
            rightChild.gest();
        else {
            int t = varTable.newvar(0, false);
            cod.genera(cod.op.COPYLIT, rightChild.value, 0, t);
            rightChild.varNum = t;
        }
        
        // TYPE CHECKING -------------------------------------------------------
        // Not allowing operator overloading for now
        if (!(leftChild.type instanceof complexType.primitive)) {
            Main.report_error("Cannot perform binary operation \""+op.toString()+"\" with left operand of non-primitive type \""+leftChild.type.toString()+"\"", this);
            return;
        }
        if (!(rightChild.type instanceof complexType.primitive)) {
            Main.report_error("Cannot perform binary operation \""+op.toString()+"\" with right operand of non-primitive type \""+rightChild.type.toString()+"\"", this);
            return;
        }
        
        basicType leftType = ((complexType.primitive) leftChild.type).btype;
        basicType rightType = ((complexType.primitive) rightChild.type).btype;
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
                if (leftType != basicType.INT) {
                    Main.report_error("Invalid type \""+leftChild.type.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                if (rightType != basicType.INT) {
                    Main.report_error("Invalid type \""+rightChild.type.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                break;
            case AND:
            case OR:
                if (leftType != basicType.BOOL) {
                    Main.report_error("Invalid type \""+leftChild.type.toString()+"\" for left argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                if (rightType != basicType.BOOL) {
                    Main.report_error("Invalid type \""+rightChild.type.toString()+"\" for right argument of binary operator \""+op.toString()+"\"", this);
                    return;
                }
                break;
            case EQ:
            case NEQ:
                if (leftType != rightType) {
                    Main.report_error("Cannot compare expressions of differing types \""+leftChild.type.toString()+"\" and \""+rightChild.type.toString()+"\"", value);
                    return;
                }
        }
        empty = false;
        
        // EXPRESSION EVALUATION -----------------------------------------------
        int t = varTable.newvar(0, false);
        switch(op){
            case PLUS:
                cod.genera(cod.op.ADD, leftChild.varNum, rightChild.varNum, t);
                break;
            case NEG:
                cod.genera(cod.op.SUB, leftChild.varNum, rightChild.varNum, t);
                break;
            case TIMES:
                cod.genera(cod.op.PROD, leftChild.varNum, rightChild.varNum, t);
                break;
            case DIV:
                cod.genera(cod.op.DIV, leftChild.varNum, rightChild.varNum, t);
                break;
            case AND:
                cod.genera(cod.op.AND, leftChild.varNum, rightChild.varNum, t);
                break;
            case OR:
                cod.genera(cod.op.OR, leftChild.varNum, rightChild.varNum, t);
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
        varNum = t;
    }
    
    private void gest_rel(cod.op op, int t) {
        int tag1 = cod.newTag();
        int tag2 = cod.newTag();
        
        cod.genera(op, leftChild.varNum, rightChild.varNum, 0); // if leftchild ⊕ rightchild goto tag1
        cod.replaceWithTag(2, tag1);
        
        cod.genera(cod.op.COPYLIT, 0, 0, t); // t = 0
        
        cod.genera(cod.op.GOTO, 0, 0, 0); // GOTO tag2
        cod.replaceWithTag(2, tag2);
        
        cod.setTag(tag1); // tag1 : skip
        cod.genera(cod.op.COPYLIT, -1, 0, t); // t = -1
        cod.setTag(tag2); // tag2 : skip
    }
}
