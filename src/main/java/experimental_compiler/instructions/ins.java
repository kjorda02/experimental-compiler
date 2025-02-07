package experimental_compiler.instructions;

import datos.*;
import datos.cod.codigo3dirs;
import datos.cod.op;
import datos.varTable.varInfo;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author kjorda
 */
public class ins {
    static BufferedWriter w;
    static int currentFunc = -1;
    static String[] tmpRegs = new String[2];
    
    public static void generate(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            w = writer;
            globals();
            
            codigo3dirs c = cod.fetch(0);
            for (int i = 1; c != null; i++) {
                writer.write("; "+c.toString()+"\n");
                translate(c);
                c = cod.fetch(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void globals() throws IOException {
        w.write("");
    }
    
    public static void setTmpRegs(int[] regs) {
        for (int i = 0; i < regs.length; i++) {
            tmpRegs[i] = varTable.reg(regs[i]);
        }
    }
    
    // Returns a string with the register of a source operand, given it's variable number
    // If the variable was not originally in a register, it generates the code to load it in one
    public static String src(int varNum, boolean secondOp) throws IOException {
        varInfo v = varTable.get(varNum);
        if (v.inRegister) {
            return varTable.reg(v.offset);
        }
        
        String reg = tmpRegs[0];
        if (secondOp) {
            reg = tmpRegs[1];
        }
        
        if (v.parentFunc == -1) { // Global variable
            w.write("la "+reg+", "+v.name+"\n"); // Load address of tag in tmp1
            w.write("sw "+reg+", 0("+reg+")\n"); // Get value at said address
        }
        else { // Local variable
            w.write("lw "+reg+", "+v.offset+"(fp)\n");
        }
        
        return reg;
    }
    
    public static String src1(int varNum) throws IOException {
        return src(varNum, false);
    }
    
    public static String src2(int varNum) throws IOException {
        return src(varNum, true);
    }
    
    // Returns a string with the register of the destination operand, given the variable number
    public static String dst(int varNum) {
        varInfo v = varTable.get(varNum);
        if (v.inRegister)
            return varTable.reg(v.offset);
        else
            return tmpRegs[0];
    }
    
    // Saves the value in tmpReg[0] to the location of a variable, given by varnum
    public static void saveDst(int varNum) throws IOException {
        varInfo v = varTable.get(varNum);
        if (v.inRegister)
            return;
        
        if (v.parentFunc == -1) { // Global variable
            w.write("la "+tmpRegs[1]+", "+v.name+"\n"); // Load address of tag in tmp1
            w.write("sw "+tmpRegs[0]+", 0("+tmpRegs[1]+")\n"); // Copy dst operand in tmp0 to adderss in tmp1
        }
        else { // Local variable
            w.write("sw "+tmpRegs[0]+", "+varTable.reg(v.offset)+"(fp)\n");
        }
    }
    
    public static void translate(codigo3dirs c) throws IOException {
        op o = c.operation;
        switch (o) {
            case SKIP: // ----------------------------------------------
                w.write(c.jmpTag+":\n");
                break;
            case COPY: // ----------------------------------------------
                if (c.src1imm())
                    w.write("li "+dst(c.op[2])+", "+c.op[0]+"\n");
                else
                    w.write("mv "+dst(c.op[2])+", "+src1(c.op[0])+"\n");
                c.saveDst();
                break;
            case IDX_VAL: // ----------------------------------------------
                // dst = src1[src2]
                w.write("add "+tmpRegs[0]+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                w.write("sw "+dst(c.op[2])+", 0("+tmpRegs[0]+")\n");
                saveDst(c.op[2]);
                break;
            case IDX_ASS: // ----------------------------------------------
                // dst[src2] = src1
                w.write("add "+tmpRegs[0]+", "+src1(c.op[2])+", "+src2(c.op[1])+"\n");
                w.write("lw "+dst(c.op[0])+", 0("+tmpRegs[0]+")\n");
                saveDst(c.op[0]);
                break;
            default:
                goto_instructions.translate(c, w);
        }
    }
}
