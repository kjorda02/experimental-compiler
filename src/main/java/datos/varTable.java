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
        public String parentFunc; // Name of the function where it's declared
        public int size; // Size in bytes
        public int disp; // Displacement relative to base pointer. Cannot be known until optimization is done.
        public boolean inRegister; // If true, disp indicates the register number.
        public boolean isParameter; // If in register: a0-a7 for params, t0-t6 or s1-s11 for local variables
        public String name; // Name of the corresponding variable in the source code
        public int tmpNum; // Temporary variable number if it's a compiler-generated variable
        
        public varInfo(String p, int s, boolean i, String n) { // Source code variable
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
    
    public static int newvar(String parent, int size, boolean isParam, String name) { // Source code variable
        table.add(new varInfo(parent, size, isParam, name)); // Add to arrayList in position vars
        return vars++;
    }
    
    public static int newvar(String parent, int size, boolean isParam) { // Compiler-generated variable
        table.add(new varInfo(parent, size, isParam, "t"+tmpVars++));
        return vars++;
    }
    
    public static int newvar(String parent, int size, String name) { // Source code variable, not a parameter
        table.add(new varInfo(parent, size, false, name)); // 
        return vars++;
    }
    
    public static int newvar(String parent, int size) { // Compiler-generated variable, not a parameter
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
    
    public static void outputVarTable(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("| "+format("num", 5)+format("name", 15)+format("parentFunc", 15)+
                    format("size", 6)+format("displ", 6)+format("inRegister", 11)+format("param", 6)+" |\n");
            for (int i = 0; i < table.size(); i++) {
                varInfo v = table.get(i);
                writer.write("| "+format(""+i, 5)+format(v.name, 15)+format(v.parentFunc, 15)+
                    format(""+v.size, 6)+format(""+v.disp, 6)+format(""+v.inRegister, 11)+format(""+v.isParameter, 6)+" |\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}