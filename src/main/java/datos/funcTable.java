package datos;

import java.util.HashMap;

/**
 *
 * @author kjorda
 */
public class funcTable {
    public static class funcInfo {
        public int registerArgs = 0;
        public int stackArgs = 0;
        public int tempRegisters = 0;
        public int restoreRegisters = 1; // Previous base pointer
        public int stackSize; // in bytes. Return address + local variables that can't go on registers + saved s registers
    }
    
    public static String currentFunc; // Id of the current function, used to declare variables
    static HashMap<String, funcInfo> table;
    
    public funcInfo get(String s) {
        return table.get(s);
    }
    
    public void addArg(String s) {
        
    }
    
}
