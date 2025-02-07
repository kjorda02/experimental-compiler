package experimental_compiler.instructions;

import datos.*;
import datos.cod.*;
import datos.funcTable.funcInfo;
import datos.varTable.varInfo;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author kjorda
 */
public class fun_instructions {
    static int arg = 0;
    static funcInfo callF = null;
    
    public static void translate(codigo3dirs c, BufferedWriter w) throws IOException {
        op o = c.operation;
        funcInfo f = null;
        if (ins.currentFunc == -1) {
            if (c.operation != op.PMB)
                return;
        }
        else {
            f = funcTable.get(ins.currentFunc);
        }
        switch(o) {
            case PMB:
                arg = 0;
                ins.currentFunc = c.op[2];
                f = funcTable.get(ins.currentFunc);
                ins.setTmpRegs(f.tmpRegs);
                w.write("addi sp, sp, -"+f.stackSize+"\n"); // Make space for the stack frame
                w.write("sw ra, "+(f.stackSize-4)+"(sp) ; -4(fp)\n"); // Save return address
                w.write("sw fp, "+(f.stackSize-8)+"(sp) ; -8(fp)\n"); // Save previous frame pointer
                w.write("addi fp, sp, "+f.stackSize+"\n"); // Then set its new value
                
                // Save s1-s11 registers
                for (int i = 1; i < f.used_s_regs; i++) { // s0/fp already saved, so we start at 1
                    w.write("sw s"+i+", -"+(8+i*4)+"(fp)\n");
                }
                
                break;
            case CALL:
                arg = 0;
                w.write("call "+funcTable.get(c.op[2]).name);
                break;
            case RTN:
                // Restore s1-s11 registers
                for (int i = 1; i < f.used_s_regs; i++) { // s0/fp already saved, so we start at 1
                    w.write("lw s"+i+", -"+(8+i*4)+"(fp)\n");
                }
                
                w.write("lw fp, "+(f.stackSize-8)+"(sp)\n"); // Restore previous frame pointer
                w.write("lw ra, "+(f.stackSize-4)+"(sp)\n"); // Restore return address
                w.write("addi sp, sp, "+f.stackSize+"\n"); // Pop the stack frame
                w.write("jr ra\n");
                //ins.currentFunc = -1;
                break;
            case INIT_PARAMS:
                w.write("addi sp, sp, -"+funcTable.get(c.op[2]).argsStackSize+"\n");
                callF = funcTable.get(c.op[2]);
                break;
            case PARAM:
                varInfo dst = varTable.get(callF.params.get(arg));
                if (dst.size <= 4) {
                    if (dst.inRegister)
                        w.write("mv "+varTable.reg(dst.offset)+", "+ins.src1(c.op[2])+"\n");
                    else
                        w.write("sw "+ins.src1(c.op[2])+", "+dst.offset+"(sp)\n");
                }
                else { // Need to do copy loop for larger parameters
                    // TODO...
                }
                arg++;
                break;
                
            default:
                alu_instructions.translate(c, w);
        }
    }
}
