package datos;

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
        public int disp; // Displacement relative to base pointer. Cannot be known until optimization is done.
        public boolean inRegister; // If true, disp indicates the register number.
        public boolean isParameter; // If in register: a0-a7 for params, t0-t6 or s1-s11 for local variables
        public String name; // Name of the corresponding variable in the source code
        public int tmpNum; // Temporary variable number if it's a compiler-generated variable
        
        public varInfo(int p, int s, boolean i, String n) {
            parentFunc = p;
            size = s;
            isParameter = i;
            name = n;
        }
        
        public varInfo(int p, int s, boolean i, String n, int offset, boolean inReg) { // For parameters
            this(p, s, i, n);
            inRegister = inReg;
            disp = offset;
        }
    }
    
    private static ArrayList<varInfo> table = new ArrayList<>();
    static int vars = 0; // (nv)
    static int tmpVars = 0;
    
    public static varInfo get(int i) {
        return table.get(i);
    }
    
    public static int newvar(int parent, int size, String name, int offset, boolean inRegister) { // Function parameter
        table.add(new varInfo(parent, size, true, name, offset, inRegister)); // Add to arrayList in position vars
        return vars++;
    }
    
    public static int newvar(int parent, int size, String name) { // Source code variable, not a parameter
        table.add(new varInfo(parent, size, false, name));
        return vars++;
    }
    
    public static int newvar(int parent, int size) { // Compiler-generated variable, not a parameter
        table.add(new varInfo(parent, size, false, "t"+tmpVars++));
        return vars++;
    }
    
    public static String format(String s, int len) {
        if (s == null)
            s = "";
        for (int i = s.length(); i < len; i++) {
            s += " ";
        }
        return s;
    }
    
    public static String formatLoc(varInfo v, int len) {
        String s = "";
        if (v.inRegister) {
            if (v.isParameter) {
                s = "a"+v.disp;
            }
            else {
                if (v.disp < 10) 
                    s = "t"+v.disp;
                else 
                    s = "s"+(v.disp-10);
            }
        }
        else {
            if (v.parentFunc == -1) 
                s = ""+v.disp;
            else
                s = v.disp+"(FP)";
        }
        
        return format(s, len);
    }
    
    public static void outputVarTable(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("| "+format("num", 5)+format("name", 15)+format("parentFuncID", 15)+format("parentFunc", 15)+
                    format("size", 6)+format("location", 9)+format("inRegister", 11)+format("param", 6)+" |\n");
            for (int i = 0; i < table.size(); i++) {
                varInfo v = table.get(i);
                String parentFunc;
                
                writer.write("| "+format(""+i, 5)+format(v.name, 15)+format(""+v.parentFunc, 15)+format(funcTable.name(v.parentFunc), 15)+
                    format(""+v.size, 6)+formatLoc(v, 9)+format(""+v.inRegister, 11)+format(""+v.isParameter, 6)+" |\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}