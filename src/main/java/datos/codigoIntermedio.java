package datos;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kjorda
 */
public class codigoIntermedio {
    public enum Operacion {
        COPY(""),
        ADD("+"),
        SUB("-"),
        PROD("*"),
        DIV("/"),
        MOD("%"),
        NEG("-"),
        AND("and"),
        OR("||"),
        NOT("not "),
        IND_VAL(""),
        IND_ASS(""),
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
        
        private Operacion(String s) {
            str = s;
        }
    }
    
    public class codigo3dirs {
        public Operacion op;
        public long op1;
        public long op2;
        public long dst;
        public int tagnum = -1;
        
        codigo3dirs(Operacion op, long op1, long op2, long dst) {
            this.op = op;
            this.op1 = op1;
            this.op2 = op2;
            this.dst = dst;
        }
        
        
        public String toString(ArrayList<codigo3dirs> codigo) {
            String s = "";
            if (tagnum != -1)
                s += "e"+tagnum+":";
            
            s += "\t";
            String o;
            switch(op) {
                case ADD:
                case SUB:
                case PROD:
                case DIV:
                case MOD:
                case AND:
                case OR: // dst = op1 ⊕ op2
                    s += "t"+dst+" = t"+op1+" "+op.str+" t"+op2;
                    break;
                case COPY:
                case NEG:
                case NOT: // dst = ⊕ op1
                    s += "t"+dst+" = "+op.str+"t"+op1;
                    break;
                case IND_VAL: // dst = op1[op2]
                    s += "t"+dst+" = t"+op1+"[t"+op2+"]";
                    break;
                case IND_ASS: // dst[op2] = op1
                    s += "t"+dst+"[t"+op2+"] = t"+op1;
                    break;
                case GOTO: // goto dst
                    s += "goto e"+codigo.get((int)dst).tagnum;
                    break;
                case IFLT:
                case IFLE:
                case IFEQ:
                case IFNE:
                case IFGE:
                case IFGT: // if op1 ⊕ op2 goto dst
                    s += "if t"+op1+" "+op.str+" t"+op2+" goto "+codigo.get((int)dst).tagnum;
                    break;
                case PMB:
                case CALL:
                case RTN:
                    s += op.str+dst; // TODO: Get function name from the table
                    break;
                case PARAM_S:
                    s += "param_s t"+dst; // param_s dst
                    break;
                case PARAM_C:
                    s += "param_c t"+op1+"[t"+op2+"]"; // param_c op1[op2]
                    break;
            }
            return s;
        }
    }
    
    ArrayList<codigo3dirs> codigo;
    //ArrayList<Integer> tags;
    int currentDir = 0;
    int currTagNum = 0;
    
    public codigoIntermedio() {
        codigo = new ArrayList<>();
    }
    
    public void genera(Operacion op, long op1, long op2, long dst) {
        codigo.add(currentDir, new codigo3dirs(op, op1, op2, dst));
        currentDir++;
    }
    
    public int generaTag() {
        //tags.add(currentDir); // Tag in position currTagNum
        codigo.get(currentDir).tagnum = currTagNum;
        //return currTagNum++;
        return currentDir;
    }
    
//    public int getTagAddr(int tagNum) {
//        return tags.get(tagNum);
//    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < currentDir; i++) {
            s.append(i);
            s.append(" ");
            s.append(codigo.get(i).toString(codigo));
        }
        
        return s.toString();
    }
    
}
