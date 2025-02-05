package experimental_compiler.instructions;

import datos.*;
import datos.cod.codigo3dirs;
import datos.cod.op;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author kjorda
 */
public class instructions {
    public static void generate(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            globals(writer);
            
            codigo3dirs c = cod.fetch(0);
            for (int i = 1; c != null; i++) {
                translate(c, writer);
                
                c = cod.fetch(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void globals(BufferedWriter w) throws IOException {
        w.write("");
    }
    
    public static void translate(codigo3dirs c, BufferedWriter w) throws IOException {
        op o = c.operation;
        switch (o) {
            case SKIP:
                break;
            case COPY:
                break;
            case IDX_VAL:
                break;
            case IDX_ASS:
                break;
            default:
                goto_instructions.translate(c, w);
        }
    }
}
