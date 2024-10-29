package datos;

import java.util.ArrayList;

/**
 *
 * @author kjorda
 */
public class codigoIntermedio {
    public enum Operacion {
        COPY,
        ADD,
        SUB,
        PROD,
        DIV,
        MOD,
        NEG,
        AND,
        OR,
        NOT,
        IND_VAL,
        IND_ASS,
        GOTO,
        IFLT,
        IFLE,
        IFEQ,
        IFNE,
        IFGE,
        IFGT,
        PMB,
        CALL,
        RTN,
        PARAM_S,
        PARAM_C
    }
    
    public class codigo3dirs {
        public Operacion op;
        public long op1;
        public long op2;
        public long dst;
    }
    
    ArrayList<codigo3dirs> codigo;
    
    public codigoIntermedio() {
        codigo = new ArrayList<>();
    }
    
    public void genera(Operacion op, long op1, long op2, long dst) {
        
    }
}
