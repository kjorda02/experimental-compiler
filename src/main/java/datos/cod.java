package datos;

import java.util.ArrayList;

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
        IFLE(">"),
        IFEQ("=="),
        IFNE("!="),
        IFGE(">="),
        IFGT(">"),
        PMB("pmb "),
        CALL("call "),
        RTN("rtn "),
        PARAM_S(""),
        PARAM_C("");
        
        String str;
        
        private op(String s) {
            str = s;
        }
    }
    
    public static class codigo3dirs {
        public op operation;
        public long[] op = new long[3];
        public int imm = -1;  // -1: no immediates, 0: op1 is immediate, 1: op2 is immediate, 2: both op1 and op2 are immediate (only idx_ass)
        
        codigo3dirs(op op, long op1, long op2, long dst) {
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
                return ""+op[0]; 
            else
                return "t"+op[0];
        }
        private String op2() {
            if (imm == 1 || imm == 2)
                return ""+op[1];
            else
                return "t"+op[1];
        }
        private String dst() {
            return "t"+op[2];
        }
        
        public String toString(ArrayList<Integer> tagNums) {
            String s = "";
            switch(operation) {
                case ADD:
                case SUB:
                case PROD:
                case DIV:
                case MOD:
                case AND:
                case OR: // dst = op1 ⊕ op2
                    s += dst()+" = "+op1()+" "+operation.str+" "+op2();
                    break;
                case NEG:
                case NOT: // dst = ⊕ op1
                    s += dst()+" = "+operation.str+" "+op1();
                    break;
                case COPY:
                    s += dst()+" = "+op1();
                    break;
                case IDX_VAL: // dst = op1[op2]
                    s += dst()+" = "+op1()+"["+op2()+"]";
                    break;
                case IDX_ASS: // dst[op2] = op1
                    s += dst()+"["+op2()+"] = "+op1();
                    break;
                case GOTO: // goto dst
                    s += "goto "+str(op[2]); // get tag of the destination operand
                    break;
                case IFLT:
                case IFLE:
                case IFEQ:
                case IFNE:
                case IFGE:
                case IFGT: // if op1 ⊕ op2 goto dst
                    s += "if "+op1()+" "+operation.str+" "+op2()+" goto "+str(op[2]);
                    break;
                case PMB:
                case CALL:
                case RTN:
                    s += operation.str+dst(); // TODO: Get function name from the table
                    break;
                case PARAM_S:
                    s += "param_s "+dst(); // param_s dst
                    break;
                case PARAM_C:
                    s += "param_c "+op1()+"["+op2()+"]"; // param_c op1[op2]
                    break;
            }
            return s;
        }
        
        private String str(long addr) {
            if (tagNums.get((int)addr) == -1 || addr >= tagNums.size())
                return ""+addr;
            else 
                return "e"+tagNums.get((int)addr);
        }
    }
    
    private static ArrayList<codigo3dirs> codigo = new ArrayList<>();
    private static ArrayList<Integer> tags = new ArrayList<>();
    private static ArrayList<Integer> tagNums = new ArrayList<>(); // Tag number for each address (if it has a tag associated)
    private static ArrayList<ArrayList<Integer[]>> tagUsages = new ArrayList<>();
    private static int currentAddr = 0;
    private static int currTagNum = 0;
    
    public static void init() {
        tagNums.add(-1); // tagNums[0]
    }
    
    public static void genera(op op, long op1, long op2, long dst) {
        tagNums.add(-1); // tagNums[currentAddr+1]
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
    
    public static void replaceWithTag(int pos, int tagNum) {
        if (tagNum < 0) 
            return;
            
        if (tags.get(tagNum) != -1) // The tag has already been set
            codigo.get(currentAddr-1).op[pos] = tags.get(tagNum);
        else // Add this to the list of usages so that the correct address can be added when the tag is defined
            tagUsages.get(tagNum).add(new Integer[]{currentAddr-1, pos});
    }
    
    public static int newTag() {
        tags.add(-1); // tags[currTagNum]
        tagUsages.add(new ArrayList<>()); // tagUsages[currTagNum]
        return currTagNum++;
    }
    
    public static void setTag(int tagNum) {
        tags.set(tagNum, currentAddr);
        tagNums.set(currentAddr, tagNum);
        
        // Replace usages with correct address
        for (Integer[] usage : tagUsages.get(tagNum)) {
            codigo.get(usage[0]).op[usage[1]] = currentAddr;
        }
    }
    
//    public int getTagAddr(int tagNum) {
//        return tags.get(tagNum);
//    }
    
    public static String toStr() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < currentAddr; i++) {
            s.append(i);
            s.append(" ");
            if (tagNums.get(i) != -1) {
                s.append("e");
                s.append(tagNums.get(i));
                s.append(":");
            }
            s.append("\t");
            s.append(codigo.get(i).toString(tagNums));
            s.append("\n");
        }
        if (tagNums.get(currentAddr) != -1) {
            s.append(currentAddr);
            s.append(" e");
            s.append(tagNums.get(currentAddr));
            s.append(":");
        }
        
        return s.toString();
    }
    
}
