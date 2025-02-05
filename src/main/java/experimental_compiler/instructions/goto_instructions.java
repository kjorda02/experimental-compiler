package experimental_compiler.instructions;

import datos.*;
import datos.cod.*;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author kjorda
 */
public class goto_instructions {
    public static void translate(codigo3dirs c, BufferedWriter w) throws IOException {
        op o = c.operation;
        switch(o) {
            case GOTO:
                break;
            case IFLT:
                break;
            case IFLE:
                break;
            case IFEQ:
                break;
            case IFNE:
                break;
            case IFGE:
                break;
            case IFGT:
                break;

            default:
                fun_instructions.translate(c, w);
        }
    }
}
