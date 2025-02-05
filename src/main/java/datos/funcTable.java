package datos;

import arbol.type.complexType;
import static datos.varTable.format;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author kjorda
 */
public class funcTable {
    public static class funcInfo {
        String name;
        public int registerArgs = 0;
        public int tempRegisters = 0;
        public int restoreRegisters = 1; // Previous base pointer
        public int stackSize = 4; // in bytes. Return address + local variables that can't go on registers + saved s registers
        public int argsStackSize = 0; // To deallocate when returning. Size of all input arguments on the stack.
        
        public funcInfo(String n, int regArgs, int argsSize) {
            name = n;
            registerArgs = regArgs;
            argsStackSize = argsSize;
        }
    }
    
    public static int currentFunc = -1; // Id of the current function, used to declare variables. -1 = global scope
    static ArrayList<funcInfo> table = new ArrayList<>();
    
    public static int add(complexType.funcsig signature) {
        int registerArgs = 0;
        
        
        // Will contain value if primitive type. Pointer to pre-allocated space if complex type
        if (signature.returnType != null) {
            varTable.newvar(table.size(), signature.returnType.bytes, "returnval", 0, true);
            registerArgs = 1;
        }
        
        int offset = 0;
        for (int i = 0; i < signature.paramTypes.size(); i++) { // input only arguments (normal kind)
            complexType t = signature.paramTypes.get(i);
            int size = t.bytes;
            if ((t instanceof complexType.primitive || t instanceof complexType.pointer || // Datatype fits in a register and we still have registers left
                    signature.paramModes.get(i) == true) && registerArgs <= 7) { // Out parameters are always a pointer so they fit as well
                
                varTable.newvar(table.size(), size, signature.paramNames.get(i), registerArgs, true);
                registerArgs++;
            }
            else { // Stack allocated
                varTable.newvar(table.size(), size, signature.paramNames.get(i), offset, false);
                offset += size;
            }
        }
        
        table.add(new funcInfo(signature.name.value, registerArgs, offset));
        
        // We do not reserve space for out arguments, since the callee already has done so. 
        
        return table.size()-1;
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
    
    
    public static void outputFuncTable(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("| "+format("num", 5)+format("name", 15)+format("stackSize", 10)+format("argsSize", 9)+
                    format("s-regs", 7)+format("t-regs", 7)+format("a-regs", 7)+" |\n");
            for (int i = 0; i < table.size(); i++) {
                funcTable.funcInfo f = table.get(i);
                
                writer.write("| "+format(""+i, 5)+format(f.name, 15)+format(""+f.stackSize, 10)+format(""+f.argsStackSize, 9)+
                    format(""+f.restoreRegisters, 7)+format(""+f.tempRegisters, 7)+format(""+f.registerArgs, 7)+" |\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
