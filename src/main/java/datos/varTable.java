package datos;

import datos.funcTable.funcInfo;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author kjorda
 */
public class varTable {
    public static class varInfo {
        public int parentFunc; // Name of the function where it's declared
        public int size; // Size in bytes
        public int offset; // Displacement relative to base pointer. Cannot be known until optimization is done.
        public boolean inRegister; // If true, disp indicates the register number.
        public boolean isParameter; // Only kept for printing
        public String name; // Name of the corresponding variable in the source code
        public int tmpNum; // Temporary variable number if it's a compiler-generated variable
        
        public varInfo(int p, int s, boolean i, String n) {
            parentFunc = p;
            size = s;
            isParameter = i;
            name = n;
        }
    }
    
    private static ArrayList<varInfo> table = new ArrayList<>();
    static int vars = 0; // (nv)
    static int tmpVars = 0;
    
    public static varInfo get(int i) {
        return table.get(i);
    }
    
    public static int getOffset(int i) {
        return table.get(i).offset;
    }
    
    public static int newvar(int parent, int size, boolean isParam, String name) { // Source code variable
        table.add(new varInfo(parent, size, isParam, name));
        if (parent >= 0) {
            if (!isParam)
                funcTable.addVar(parent, vars);
        }
        return vars++;
    }
    
    public static int newvar(int parent, int size) { // Compiler-generated variable, not a parameter
        table.add(new varInfo(parent, size, false, "t"+tmpVars++));
        
        if (parent >= 0) {
            funcTable.addVar(parent, vars);
        }
        return vars++;
    }
    
    public static void allocateVarsGlobal() {
        for (varInfo v : table) {
            if (v.parentFunc == -1 && v.name == null) { // Compiler-generated variables outside any function
                funcInfo f = new funcInfo("");
                for (int num : f.vars) { // For each local variable
                    funcTable.allocateVar(f, num, false);
                }
            }
        }
    }
    
    public static String format(String s, int len) {
        if (s == null)
            s = "";
        for (int i = s.length(); i < len; i++) {
            s += " ";
        }
        return s;
    }
    
    public static void setAReg(int varNum, int reg) {
        table.get(varNum).inRegister = true;
        table.get(varNum).offset = reg;
    }
    
    public static void setTReg(int varNum, int reg) {
        table.get(varNum).inRegister = true;
        table.get(varNum).offset = reg+10;
    }
    
    public static void setSReg(int varNum, int reg) {
        table.get(varNum).inRegister = true;
        table.get(varNum).offset = reg+20;
    }
    
    public static void setStack(int varNum, int offset) {
        table.get(varNum).inRegister = false;
        table.get(varNum).offset = offset;
    }
    
    public static String reg(int n) {
        if (n < 10) 
            return "a"+n; // a0-a7
        if (n < 20) 
            return "t"+(n-10); // t0-t6
        
        return "s"+(n-20); // s1-s11
    }
    
    public static String loc(varInfo v) {
        String s = "";
        if (v.inRegister) {
            return reg(v.offset);
        }
        else {
            if (v.parentFunc == -1) 
                return v.name; // global var
            else
                return v.offset+"(fp)"; // local var
        }
    }
    
    public static void outputVarTable(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("| "+format("num", 5)+format("name", 15)+format("parentFuncID", 15)+format("parentFunc", 15)+
                    format("size", 6)+format("location", 9)+format("param", 6)+" |\n");
            for (int i = 0; i < table.size(); i++) {
                varInfo v = table.get(i);
                
                writer.write("| "+format(""+i, 5)+format(v.name, 15)+format(""+v.parentFunc, 15)+format(funcTable.name(v.parentFunc), 15)+
                    format(""+v.size, 6)+format(loc(v),9)+format(""+v.isParameter, 6)+" |\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}