package datos;

import experimental_compiler.instructions.ins;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kjorda
 */
public class cod {
    public static enum op {
        COPY("copy "),
        ADD("+"),
        SUB("-"),
        PROD("*"),
        DIV("/"),
        MOD("%"),
        NEG("-"),
        AND("and"),
        OR("||"),
        NOT("not "),
        IDX_VAL(""),
        IDX_ASS(""),
        GOTO(""),
        IFLT("<"),
        IFLE("<="),
        IFEQ("=="),
        IFNE("!="),
        IFGE(">="),
        IFGT(">"),
        PMB("pmb "),
        CALL("call "),
        RTN("rtn "),
        PARAM(""),
        INIT_PARAMS(""),
        SKIP("");
        
        String str;
        
        private op(String s) {
            str = s;
        }
    }
    
    public static class codigo3dirs {
        public op operation;
        public int[] op = new int[3];
        public int imm = -1;  // -1: no immediates, 0: op1 is immediate, 1: op2 is immediate, 2: both op1 and op2 are immediate (only idx_ass)
        public String jmpTag;
        
        codigo3dirs(op op, int op1, int op2, int dst) {
            this.operation = op;
            this.op[0] = op1;
            this.op[1] = op2;
            this.op[2] = dst;
        }
        
        public void setImmediate(int i) {
            imm = i;
        }
        
        private String op1() {
            if (imm == 0 || imm == 2)
                return ""+op[0]; // If it's an immediate value, just print it directly
            else
                return varTable.get(op[0]).name; // Prints the source-code name or t<n> if it's a compiler-generated variable
        }
        private String op2() {
            if (imm == 1 || imm == 2)
                return ""+op[1];
            else
                return varTable.get(op[1]).name;
        }
        private String dest() {
            if (jmpTag != null)
                return jmpTag;
            else
                return varTable.get(op[2]).name;
        }
        
        public boolean src1imm() {
            return imm == 0 || imm == 2;
        }
        
        public boolean src2imm() {
            return imm == 1 || imm == 2;
        }
        
        public void saveDst() throws IOException {
            ins.saveDst((int)op[2]);
        }
        
        @Override
        public String toString() {
            String s = "";
            switch(operation) {
                case ADD:
                case SUB:
                case PROD:
                case DIV:
                case MOD:
                case AND:
                case OR: // dst = op1 ⊕ op2
                    s += dest()+" = "+op1()+" "+operation.str+" "+op2();
                    break;
                case NEG:
                case NOT: // dst = ⊕ op1
                    s += dest()+" = "+operation.str+" "+op1();
                    break;
                case COPY:
                    s += dest()+" = "+op1();
                    break;
                case IDX_VAL: // dst = op1[op2]
                    s += dest()+" = "+op1()+"["+op2()+"]";
                    break;
                case IDX_ASS: // dst[op2] = op1
                    s += dest()+"["+op2()+"] = "+op1();
                    break;
                case GOTO: // goto dst
                    s += "goto "+dest(); // get tag of the destination operand
                    break;
                case IFLT:
                case IFLE:
                case IFEQ:
                case IFNE:
                case IFGE:
                case IFGT: // if op1 ⊕ op2 goto dst
                    s += "if "+op1()+" "+operation.str+" "+op2()+" goto "+dest();
                    break;
                case PMB:
                case CALL:
                case RTN:
                    s += operation.str+((int)op[2]); // TODO: Get function name from the table
                    break;
                case PARAM:
                    s += "param_s "+dest(); // param_s dst
                    break;
                case SKIP:
                    s += jmpTag+":";
                    break;
                case INIT_PARAMS:
                    s += "init_params";
            }
            return s;
        }
    }
    
    private static ArrayList<codigo3dirs> codigo = new ArrayList<>();
    private static int currentAddr = 0;
    private static int tagNum = 0;
    
    public static void genera(op op, int op1, int op2, int dst) {
        codigo.add(new codigo3dirs(op, op1, op2, dst)); // codigo[currentAddr]
        currentAddr++;
    }
    
    public static void setImmediate(boolean op1, boolean op2) {
        if (op1 && op2)
            codigo.get(currentAddr-1).setImmediate(2);
        else if (op1)
            codigo.get(currentAddr-1).setImmediate(0);
        else if (op2)
            codigo.get(currentAddr-1).setImmediate(1);
    }
    
    public static int newTag() {
        return tagNum++;
    }
    
    public static codigo3dirs fetch(int i) {
        if (i < codigo.size())
            return codigo.get(i);
        return null;
    }
    
    public static void jmpTag(int num) {
        codigo.get(currentAddr-1).jmpTag = "e"+num;
    }
    
    public static void setTag(int num) {
        codigo.add(new codigo3dirs(op.SKIP, 0, 0, 0));
        codigo.get(currentAddr++).jmpTag = "e"+num;
    }
    
    public static void setTag(String name) {
        codigo.add(new codigo3dirs(op.SKIP, 0, 0, 0));
        codigo.get(currentAddr++).jmpTag = name;
    }
    
    public static String toStr() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < currentAddr; i++) {
            s.append(i);
            s.append(" \t");
            if (codigo.get(i).operation != op.SKIP)
                s.append("\t");
            
            s.append(codigo.get(i).toString());
            s.append("\n");
        }
        
        return s.toString();
    }
}
