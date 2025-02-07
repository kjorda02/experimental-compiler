package experimental_compiler.instructions;

import datos.*;
import datos.cod.*;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Implementation of ALU instructions for RISC-V assembly
 */
public class alu_instructions {
    public static void translate(codigo3dirs c, BufferedWriter w) throws IOException {
        op o = c.operation;
        switch(o) {
            case ADD:
                if (c.src1imm())
                    w.write("addi "+dst(c.op[2])+", "+src2(c.op[1])+", "+c.op[0]+"\n");
                else if (c.src2imm())
                    w.write("addi "+dst(c.op[2])+", "+src1(c.op[0])+", "+c.op[1]+"\n");
                else
                    w.write("add "+dst(c.op[2])+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                c.saveDst();
                break;

            case SUB:
                if (c.src1imm() && c.src2imm()) {
                    // Both immediate - can compute at compile time
                    w.write("li "+dst(c.op[2])+", "+(c.op[0] - c.op[1])+"\n");
                }
                else if (c.src1imm()) {
                    // First operand immediate - load into tmp reg first
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("sub "+dst(c.op[2])+", "+ins.tmpRegs[0]+", "+src2(c.op[1])+"\n");
                }
                else if (c.src2imm()) {
                    // Current implementation using addi with negative is fine
                    w.write("addi "+dst(c.op[2])+", "+src1(c.op[0])+", -"+c.op[1]+"\n");
                }
                else {
                    w.write("sub "+dst(c.op[2])+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                }
                c.saveDst();
                break;

            case PROD:
                if (c.src1imm() && c.src2imm()) {
                    // Both immediate - compute at compile time
                    w.write("li "+dst(c.op[2])+", "+(c.op[0] * c.op[1])+"\n");
                }
                else if (c.src1imm()) {
                    // First operand immediate - load into tmp reg
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("mul "+dst(c.op[2])+", "+ins.tmpRegs[0]+", "+src2(c.op[1])+"\n");
                }
                else if (c.src2imm()) {
                    // Second operand immediate - load into tmp reg
                    w.write("li "+ins.tmpRegs[1]+", "+c.op[1]+"\n");
                    w.write("mul "+dst(c.op[2])+", "+src1(c.op[0])+", "+ins.tmpRegs[1]+"\n");
                }
                else {
                    w.write("mul "+dst(c.op[2])+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                }
                c.saveDst();
                break;

            case DIV:
                if (c.src1imm() && c.src2imm()) {
                    // Both immediate - compute at compile time
                    w.write("li "+dst(c.op[2])+", "+(c.op[0] / c.op[1])+"\n");
                }
                else if (c.src1imm()) {
                    // First operand immediate - load into tmp reg
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("div "+dst(c.op[2])+", "+ins.tmpRegs[0]+", "+src2(c.op[1])+"\n");
                }
                else if (c.src2imm()) {
                    // Second operand immediate - load into tmp reg 
                    w.write("li "+ins.tmpRegs[1]+", "+c.op[1]+"\n");
                    w.write("div "+dst(c.op[2])+", "+src1(c.op[0])+", "+ins.tmpRegs[1]+"\n");
                }
                else {
                    w.write("div "+dst(c.op[2])+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                }
                c.saveDst();
                break;

            case MOD:
                if (c.src1imm() && c.src2imm()) {
                    // Both immediate - compute at compile time
                    w.write("li "+dst(c.op[2])+", "+(c.op[0] % c.op[1])+"\n");
                }
                else if (c.src1imm()) {
                    // First operand immediate - load into tmp reg
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("rem "+dst(c.op[2])+", "+ins.tmpRegs[0]+", "+src2(c.op[1])+"\n");
                }
                else if (c.src2imm()) {
                    // Second operand immediate - load into tmp reg
                    w.write("li "+ins.tmpRegs[1]+", "+c.op[1]+"\n");
                    w.write("rem "+dst(c.op[2])+", "+src1(c.op[0])+", "+ins.tmpRegs[1]+"\n");
                }
                else {
                    w.write("rem "+dst(c.op[2])+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                }
                c.saveDst();
                break;

            case AND:
                if (c.src1imm())
                    w.write("andi "+dst(c.op[2])+", "+src2(c.op[1])+", "+c.op[0]+"\n");
                else if (c.src2imm())
                    w.write("andi "+dst(c.op[2])+", "+src1(c.op[0])+", "+c.op[1]+"\n");
                else
                    w.write("and "+dst(c.op[2])+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                c.saveDst();
                break;

            case OR:
                if (c.src1imm())
                    w.write("ori "+dst(c.op[2])+", "+src2(c.op[1])+", "+c.op[0]+"\n");
                else if (c.src2imm())
                    w.write("ori "+dst(c.op[2])+", "+src1(c.op[0])+", "+c.op[1]+"\n");
                else
                    w.write("or "+dst(c.op[2])+", "+src1(c.op[0])+", "+src2(c.op[1])+"\n");
                c.saveDst();
                break;

            case NEG:
                // NEG can be implemented as SUB from zero
                w.write("sub "+dst(c.op[2])+", "+"zero"+", "+src1(c.op[0])+"\n");
                c.saveDst();
                break;

            case NOT:
                // NOT can be implemented as XORI with -1 (all ones)
                w.write("xori "+dst(c.op[2])+", "+src1(c.op[0])+", -1\n");
                c.saveDst();
                break;
                
            default:
                System.out.println("ERROR: INSTRUCCION "+o.toString()+" NO IMPLEMENTADA");
        }
    }
    
    private static String src1(int n) throws IOException {
        return ins.src1(n);
    }
    
    private static String src2(int n) throws IOException {
        return ins.src2(n);
    }
    
    private static String dst(int n) {
        return ins.dst(n);
    }
    
    private static void saveDst(int n) throws IOException {
        ins.saveDst(n);
    }
}