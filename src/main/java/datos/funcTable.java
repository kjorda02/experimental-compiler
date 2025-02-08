package datos;

import arbol.type.complexType;
import static datos.varTable.format;
import datos.varTable.varInfo;
import static datos.varTable.vars;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author kjorda
 */
public class funcTable {
    public static class funcInfo {
        public String name;
        public boolean[] used_a_regs = new boolean[8];
        public boolean[] unsafe_a_regs = new boolean[8]; // Is updated by the callees when they allocate their variables
        public boolean[] used_t_regs = new boolean[7];
        public boolean[] unsafe_t_regs = new boolean[7]; // Is updated by the callees when they allocate their variables
        int total_unsafe_regs = 0;
        public int used_s_regs = 1; // Base pointer
        public int stackSize = 4; // in bytes. Return address + local variables that can't go on registers + saved s registers
        public int argsStackSize = 0; // To deallocate when returning. Size of all input arguments on the stack.
        
        ArrayList<Integer> callers = new ArrayList<>(); // List of functions that have called this one
        ArrayList<Integer> vars = new ArrayList<>();
        public ArrayList<Integer> params = new ArrayList<>();
        public int returnVar = -1;
        public int[] tmpRegs = new int[2]; // Registers for operating with values in memory
        
        public funcInfo(String n) {
            name = n;
        }
    }
    
    public static int currentFunc = -1; // Id of the current function, used to declare variables. -1 = global scope
    static ArrayList<funcInfo> table = new ArrayList<>();
    
    public static void addVar(int funcID, int varID) {
        if (varID==-1) {
                (new Exception()).printStackTrace();
            }
        table.get(funcID).vars.add(varID);
    }
    
    public static void addCaller(int callee) {
        table.get(callee).callers.add(currentFunc);
    }
    
    public static void allocateAllVars() {
        for (int i = 0; i < table.size(); i++) {
            allocateVars(i);
        }
    }
    
    public static boolean allocateVars(int funcID) {
        funcInfo f = table.get(funcID);
        
        boolean[] prev_used_a_regs = Arrays.copyOf(f.used_a_regs, f.used_a_regs.length);
        boolean[] prev_used_t_regs = Arrays.copyOf(f.used_t_regs, f.used_t_regs.length);
        int prev_used_s_regs = f.used_s_regs;
        
        Arrays.fill(f.used_a_regs, false);
        Arrays.fill(f.used_t_regs, false);
        f.used_s_regs = 1; // 1 s-reg always used for frame pointer
        f.stackSize = 8;
        f.argsStackSize = 0;
        // Don't need to clear the unsafe registers. If they were previously unsafe they'll be unsafe now.
        
        for (int num : f.params) { // For each parameter
            allocateVar(f, num, true);
        }
        
        for (int num : f.vars) { // For each local variable
            if (varTable.get(num).size <= 4)
                allocateVar(f, num, false);
        }
        
        for (int num : f.vars) { // These go on the stack
            if (varTable.get(num).size > 4)
                allocateVar(f, num, false);
        }
        
        if (f.returnVar != -1) {
            varTable.setAReg(f.returnVar, 0); // Set return value to a0. Same register can still be used for argument
            f.used_a_regs[0] = true;
        }
        
        for (int i = 0; i < 2; i++) { // Allocate 2 temporary registers
            f.tmpRegs[i] = allocateTmpReg(f);
        }
        
        if (Arrays.equals(prev_used_a_regs, f.used_a_regs)
            && Arrays.equals(prev_used_t_regs, f.used_t_regs)
            && prev_used_s_regs == f.used_s_regs) 
            return false; // No changes 
        
        // TODO: Update caller's unsafe registers
        for (int num : f.callers) { // For each function that calls this one
            funcInfo caller = table.get(num);
            boolean changes = false;
            
            for (int i = 0; i < f.used_a_regs.length; i++) {
                if (!caller.unsafe_a_regs[i] && (f.used_a_regs[i] || f.unsafe_a_regs[i])) {
                    caller.unsafe_a_regs[i] = true;
                    caller.total_unsafe_regs++;
                    changes = true;
                }
            }
            
            for (int i = 0; i < f.used_t_regs.length; i++) {
                if (!caller.unsafe_t_regs[i] && (f.used_t_regs[i] || f.unsafe_a_regs[i])) {
                    caller.unsafe_t_regs[i] = true;
                    caller.total_unsafe_regs++;
                    changes = true;
                }
            }
            
            if (changes)
                allocateVars(num); // Re-allocate its varibles since they might've been in unsafe registers
        }
        return true;
    }
    
    /*
    We'll keep 3 registers reserved for operations with variables on the stack, so that even if all 3 operands are in the stack, 
    they can be moved to and from these registers. Those can be unsafe registers since they're never used across function calls. 
    Then use the following criteria to assign registers:
    1. Mark all a-registers t-registers used by functions that are called as unsafe (each entry in the function table 
        will keep track of which registers are used)
    2. Start assigning by a-registers that aren't marked as unsafe. Assign to arguments first, 
        so that they stay in their own register if it's not unsafe.
    3. Assign t-regs that aren't marked unsafe
    4. Assign s-regs
        - If num of unsafe registers + num of s-regs left == 3, don't assign s-reg, continue to 5
    5. Assign locations on stack
    */
    public static int allocateVar(funcInfo f, int varID, boolean isParam) {
        if (varID != -1) {
            varInfo v = varTable.get(varID);
            if (v.size > 4) {
                allocateStack(f, varID, isParam);
                return 0;
            }
        }
        
        
        for (int i = 0; i < f.used_a_regs.length; i++) { // Allocate on a-register
            if (!f.used_a_regs[i] && !f.unsafe_a_regs[i]) {
                f.used_a_regs[i] = true;
                
                if (varID != -1)
                    varTable.setAReg(varID, i);
                
                return i;
            }
        }
        
        if (!isParam) {
            for (int i = 0; i < f.used_t_regs.length; i++) { // Allocate on t-register
                if (!f.used_t_regs[i] && !f.unsafe_t_regs[i]) {
                    f.used_t_regs[i] = true;
                    if (varID != -1)
                        varTable.setTReg(varID, i);
                    
                    return i+10;
                }
            }

            if (f.total_unsafe_regs + (12-f.used_s_regs) > 2 && f.used_s_regs < 12) { // Always leave 3 registers for operations with stack variables
                f.stackSize += 4;
                if (varID != -1)
                    varTable.setSReg(varID, f.used_s_regs); // Allocate on s-register
                
                return 20+f.used_s_regs++;
            }
        }
        
        // Last resort option: Store the variable on the stack 
        // (will need to be copied to/from one of the 3 registers reserved for this purpose)
        if (varID==-1) {
            (new Exception()).printStackTrace();
        }
        allocateStack(f, varID, isParam);
        return 0;
    }
    
    public static void allocateStack(funcInfo f, int varID, boolean isParam) {
        int size = varTable.get(varID).size;
        if (isParam) {
            varTable.setStack(varID, f.argsStackSize);
            f.argsStackSize += size;
            return;
        }
        
        // Local variable
        varTable.setStack(varID, -f.stackSize-size); // Grows toward negative addresses
        f.stackSize += size;
    }
    
    public static int allocateTmpReg(funcInfo f) {
        for (int i = 0; i < f.unsafe_a_regs.length; i++) { // Allocate on a-reg
            if (!f.used_a_regs[i] && f.unsafe_a_regs[i]) {
                f.used_a_regs[i] = true;
                return i;
            }
        }
        
        for (int i = 0; i < f.unsafe_t_regs.length; i++) { // Allocate on t-reg
            if (!f.used_t_regs[i] && f.unsafe_t_regs[i]) {
                f.used_t_regs[i] = true;
                return i+10;
            }
        }
        
        return 20+f.used_s_regs++;
    }
    
    public static funcInfo get(int id) {
        return table.get(id);
    }
    
    public static funcInfo getCurrent() {
        return table.get(currentFunc);
    }
    
    public void addArg(String s) {
        
    }
    
    public static String name(int id) {
        if (id == -1)
            return "";
        else
            return table.get(id).name;
    }
    
    
    public static int add(complexType.funcsig signature) {
        funcInfo f = new funcInfo(signature.name.value);
        table.add(f);
        int id = table.size()-1;
        
        // Will contain value if primitive type. Pointer to pre-allocated space if complex type
        if (signature.returnType != null) {
            if (signature.returnType instanceof complexType.primitive || signature.returnType instanceof complexType.pointer)
                f.returnVar = varTable.newvar(id, signature.returnType.bytes, true, "returnval");
            else
                f.returnVar = varTable.newvar(id, 4, true, "returnval");
        }
        
        for (int i = 0; i < signature.paramTypes.size(); i++) {
            complexType t = signature.paramTypes.get(i);
            int size = t.bytes;
            if (signature.paramModes.get(i) == true) // Out argument mode
                size = 4; // Only takes in a pointer, caller has reserved space for return value
            
            int num = varTable.newvar(id, size, true, signature.paramNames.get(i));
            f.params.add(num);
        }
        
        // We do not reserve space for out arguments, since the callee already has done so. 
        return table.size()-1;
    }
    
    
    public static void outputFuncTable(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("| "+format("num", 5)+format("name", 15)+format("stackSize", 10)+format("argsSize", 9)+
                    format("a-regs", 26)+format("t-regs", 23)+format("s-regs", 40)+format("temp regs", 9)+" |\n");
            for (int i = 0; i < table.size(); i++) {
                funcTable.funcInfo f = table.get(i);
                
                writer.write("| "+format(""+i, 5)+format(f.name, 15)+format(""+f.stackSize, 10)+format(""+f.argsStackSize, 9));
                
                String s = "";
                for (int j = 0; j < f.used_a_regs.length; j++) {
                    if (f.used_a_regs[j])
                        s += "a"+j+" ";
                }
                writer.write(format(s, 26));
                
                s = "";
                for (int j = 0; j < f.used_t_regs.length; j++) {
                    if (f.used_t_regs[j])
                        s += "t"+j+" ";
                }
                writer.write(format(s, 23));
                
                s = "";
                for (int j = 0; j < f.used_s_regs; j++) {
                    s += "s"+j+" ";
                }
                writer.write(format(s, 40));
                
                s = "";
                for (int j = 0; j < f.tmpRegs.length; j++) {
                    s += varTable.reg(f.tmpRegs[j])+" ";
                }
                writer.write(format(s, 9));
                
                writer.write(" |\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
