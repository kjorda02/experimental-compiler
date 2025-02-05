package datos;

import arbol.type.complexType;
import static datos.varTable.format;
import datos.varTable.varInfo;
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
        String name;
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
        ArrayList<Integer> params = new ArrayList<>();
        
        public funcInfo(String n) {
            name = n;
        }
    }
    
    public static int currentFunc = -1; // Id of the current function, used to declare variables. -1 = global scope
    static ArrayList<funcInfo> table = new ArrayList<>();
    
    public static void addVar(int funcID, int varID) {
        table.get(funcID).vars.add(varID);
    }
    
    public static void addParam(int funcID, int paramID) {
        table.get(funcID).params.add(paramID);
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
        
        boolean[] prev_used_a_regs = Arrays.copyOf(f.unsafe_a_regs, f.unsafe_a_regs.length);
        boolean[] prev_used_t_regs = Arrays.copyOf(f.unsafe_t_regs, f.unsafe_t_regs.length);
        int prev_used_s_regs = f.used_s_regs;
        
        for (int num : f.params) { // For each parameter
            varInfo v = varTable.get(num);
            allocateVar(f, num, true);
        }
        
        for (int num : f.vars) { // For each local variable
            varInfo v = varTable.get(num);
            allocateVar(f, num, true);
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
                if (!caller.unsafe_a_regs[i] && f.used_a_regs[i]) {
                    caller.unsafe_a_regs[i] = true;
                    changes = true;
                }
            }
            
            for (int i = 0; i < f.used_t_regs.length; i++) {
                if (!caller.unsafe_t_regs[i] && f.used_t_regs[i]) {
                    caller.unsafe_t_regs[i] = true;
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
    public static void allocateVar(funcInfo f, int varID, boolean isParam) {
        varInfo v = varTable.get(varID);
        if (v.size > 4) {
            allocateStack(f, varID, isParam);
            return;
        }
        
        for (int i = 0; i < f.used_a_regs.length; i++) { // Allocate on a-register
            if (!f.used_a_regs[i]) {
                varTable.setAReg(varID, i);
                f.used_a_regs[i] = true;
                return;
            }
        }
        
        for (int i = 0; i < f.used_t_regs.length; i++) { // Allocate on t-register
            if (!f.used_t_regs[i]) {
                varTable.setTReg(varID, i);
                f.used_t_regs[i] = true;
                return;
            }
        }
        
        if (f.total_unsafe_regs + (12-f.used_s_regs) != 3) { // Always leave 3 registers for operations with stack variables
            varTable.setSReg(varID, f.used_s_regs++); // Allocate on s-register
            return;
        }
        
        // Last resort option: Store the variable on the stack 
        // (will need to be copied to/from one of the 3 registers reserved for this purpose)
        allocateStack(f, varID, isParam);
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
    
    public funcInfo get(int id) {
        return table.get(id);
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
            varTable.newvar(id, signature.returnType.bytes, true, "returnval");
        }
        
        for (int i = 0; i < signature.paramTypes.size(); i++) {
            complexType t = signature.paramTypes.get(i);
            int size = t.bytes;
            if (signature.paramModes.get(i) == true) // Out argument mode
                size = 4; // Only takes in a pointer, caller has reserved space for return value
            
            varTable.newvar(id, size, true, signature.paramNames.get(i));
        }
        
        // We do not reserve space for out arguments, since the callee already has done so. 
        return table.size()-1;
    }
    
    
    public static void outputFuncTable(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("| "+format("num", 5)+format("name", 15)+format("stackSize", 10)+format("argsSize", 9)+
                    format("s-regs", 7)+format("t-regs", 7)+format("a-regs", 7)+" |\n");
            for (int i = 0; i < table.size(); i++) {
                funcTable.funcInfo f = table.get(i);
                
                writer.write("| "+format(""+i, 5)+format(f.name, 15)+format(""+f.stackSize, 10)+format(""+f.argsStackSize, 9)+
                    format(""+f.used_s_regs, 7)+format(""+f.used_t_regs, 7)+format(""+f.used_a_regs, 7)+" |\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
