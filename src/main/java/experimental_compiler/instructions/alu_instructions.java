package experimental_compiler.instructions;

import datos.*;
import datos.cod.*;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author kjorda
 */
public class alu_instructions {
    public static void translate(codigo3dirs c, BufferedWriter w) throws IOException {
        op o = c.operation;
        switch(o) {
            case ADD:
                break;
            case SUB:
                break;
            case PROD:
                break;
            case DIV:
                break;
            case MOD:
                break;
            case AND:
                break;
            case OR:
                break;
            case NEG:
                break;
            case NOT:
                break;
                
            default:
                System.out.println("ERROR: INSTRUCCION "+o.toString()+" NO IMPLEMENTADA");
        }
    }
}
