package experimental_compiler.instructions;

import datos.*;
import datos.cod.*;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Implementation of goto and conditional branch instructions for RISC-V assembly
 */
public class goto_instructions {
    public static void translate(codigo3dirs c, BufferedWriter w) throws IOException {
        op o = c.operation;
        switch(o) {
            case GOTO:
                // Unconditional jump
                w.write("j "+c.jmpTag+"\n");
                break;

            case IFLT:
                // if op[0] < op[1] goto jmpTag
                if (c.src1imm() && c.src2imm()) {
                    // Both operands are immediate - evaluate at compile time
                    if (c.op[0] < c.op[1]) {
                        w.write("j "+c.jmpTag+"\n");  // Always jump
                    }
                    // Otherwise generate no code (never jump)
                } else if (c.src1imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("bgt "+src2(c.op[1])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else if (c.src2imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[1]+"\n");
                    w.write("blt "+src2(c.op[0])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else {
                    w.write("blt "+src1(c.op[0])+", "+src2(c.op[1])+", "+c.jmpTag+"\n");
                }
                break;

            case IFLE:
                // if op[0] <= op[1] goto jmpTag
                if (c.src1imm() && c.src2imm()) {
                    if (c.op[0] <= c.op[1]) {
                        w.write("j "+c.jmpTag+"\n");
                    }
                } else if (c.src1imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("bge "+src2(c.op[1])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else if (c.src2imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[1]+"\n");
                    w.write("ble "+src2(c.op[0])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else {
                    w.write("ble "+src1(c.op[0])+", "+src2(c.op[1])+", "+c.jmpTag+"\n");
                }
                break;

            case IFEQ:
                // if op[0] == op[1] goto jmpTag
                if (c.src1imm() && c.src2imm()) {
                    if (c.op[0] == c.op[1]) {
                        w.write("j "+c.jmpTag+"\n");
                    }
                } else if (c.src1imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("beq "+src2(c.op[1])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else if (c.src2imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[1]+"\n");
                    w.write("beq "+src2(c.op[0])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else {
                    w.write("beq "+src1(c.op[0])+", "+src2(c.op[1])+", "+c.jmpTag+"\n");
                }
                break;

            case IFNE:
                // if op[0] != op[1] goto jmpTag
                if (c.src1imm() && c.src2imm()) {
                    if (c.op[0] != c.op[1]) {
                        w.write("j "+c.jmpTag+"\n");
                    }
                } else if (c.src1imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("bne "+src2(c.op[1])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else if (c.src2imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[1]+"\n");
                    w.write("bne "+src2(c.op[0])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else {
                    w.write("bne "+src1(c.op[0])+", "+src2(c.op[1])+", "+c.jmpTag+"\n");
                }
                break;

            case IFGE:
                // if op[0] >= op[1] goto jmpTag
                if (c.src1imm() && c.src2imm()) {
                    if (c.op[0] >= c.op[1]) {
                        w.write("j "+c.jmpTag+"\n");
                    }
                } else if (c.src1imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("ble "+src2(c.op[1])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else if (c.src2imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[1]+"\n");
                    w.write("bge "+src2(c.op[0])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else {
                    w.write("bge "+src1(c.op[0])+", "+src2(c.op[1])+", "+c.jmpTag+"\n");
                }
                break;

            case IFGT:
                // if op[0] > op[1] goto jmpTag
                if (c.src1imm() && c.src2imm()) {
                    if (c.op[0] > c.op[1]) {
                        w.write("j "+c.jmpTag+"\n");
                    }
                } else if (c.src1imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[0]+"\n");
                    w.write("blt "+src2(c.op[1])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else if (c.src2imm()) {
                    w.write("li "+ins.tmpRegs[0]+", "+c.op[1]+"\n");
                    w.write("bgt "+src2(c.op[0])+", "+ins.tmpRegs[0]+", "+c.jmpTag+"\n");
                } else {
                    w.write("bgt "+src1(c.op[0])+", "+src2(c.op[1])+", "+c.jmpTag+"\n");
                }
                break;

            default:
                fun_instructions.translate(c, w);
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