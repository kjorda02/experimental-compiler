package experimental_compiler.instructions;

import datos.cod.codigo3dirs;
import datos.cod.op;
import java.io.BufferedWriter;
import java.io.IOException;

public class io_instructions {
    public static void translate(codigo3dirs c, BufferedWriter w) throws IOException {
        op o = c.operation;
        switch(o) {
            case PRINT_CHAR:
                // Save registers that will be clobbered
                w.write("# Start print char sequence\n");
                w.write("addi sp, sp, -24\n");
                w.write("sw a0, 0(sp)\n");
                w.write("sw a1, 4(sp)\n");
                w.write("sw a2, 8(sp)\n");
                w.write("sw a7, 12(sp)\n");
                
                // Store character on stack
                w.write("sb " + ins.src1(c.op[0]) + ", 16(sp)\n");
                
                // Setup write syscall
                w.write("li a0, 1\n");         // stdout file descriptor
                w.write("addi a1, sp, 16\n");  // address of char to print
                w.write("li a2, 1\n");         // length (1 byte)
                w.write("li a7, 64\n");        // sys_write syscall
                w.write("ecall\n");
                
                // Restore registers
                w.write("lw a0, 0(sp)\n");
                w.write("lw a1, 4(sp)\n");
                w.write("lw a2, 8(sp)\n");
                w.write("lw a7, 12(sp)\n");
                w.write("addi sp, sp, 24\n");
                w.write("# End print char sequence\n");
                break;

            case PRINT_BUF:
                // Save registers that will be clobbered
                w.write("# Start print buffer sequence\n");
                w.write("addi sp, sp, -16\n");
                w.write("sw a0, 0(sp)\n");
                w.write("sw a1, 4(sp)\n");
                w.write("sw a2, 8(sp)\n");
                w.write("sw a7, 12(sp)\n");
                
                // Setup write syscall
                w.write("li a0, 1\n");              // stdout file descriptor
                if (c.src1imm()) {
                    w.write("li a1, " + c.op[0] + "\n");  // immediate buffer address
                } else {
                    w.write("mv a1, " + ins.src1(c.op[0]) + "\n");  // buffer address from register
                }
                if (c.src2imm()) {
                    w.write("li a2, " + c.op[1] + "\n");  // immediate length
                } else {
                    w.write("mv a2, " + ins.src2(c.op[1]) + "\n");  // length from register
                }
                w.write("li a7, 64\n");             // sys_write syscall
                w.write("ecall\n");
                
                // Restore registers
                w.write("lw a0, 0(sp)\n");
                w.write("lw a1, 4(sp)\n");
                w.write("lw a2, 8(sp)\n");
                w.write("lw a7, 12(sp)\n");
                w.write("addi sp, sp, 16\n");
                w.write("# End print buffer sequence\n");
                break;
                
            case INPUT_CHAR:
                w.write("# Start input char sequence\n");
                w.write("addi sp, sp, -24\n");
                w.write("sw a0, 0(sp)\n");
                w.write("sw a1, 4(sp)\n");
                w.write("sw a2, 8(sp)\n");
                w.write("sw a7, 12(sp)\n");
                
                // Setup read syscall for one character
                w.write("li a0, 0\n");         // stdin file descriptor
                w.write("addi a1, sp, 16\n");  // buffer on stack
                w.write("li a2, 1\n");         // read 1 byte
                w.write("li a7, 63\n");        // sys_read syscall
                w.write("ecall\n");
                
                // Load the character into destination
                w.write("lb " + ins.dst(c.op[2]) + ", 16(sp)\n");
                
                // Restore registers
                w.write("lw a0, 0(sp)\n");
                w.write("lw a1, 4(sp)\n");
                w.write("lw a2, 8(sp)\n");
                w.write("lw a7, 12(sp)\n");
                w.write("addi sp, sp, 24\n");
                w.write("# End input char sequence\n");
                
                c.saveDst();
                break;
                
            default:
                alu_instructions.translate(c, w);
        }
    }
}