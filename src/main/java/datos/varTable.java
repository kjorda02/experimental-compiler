package datos;

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
        public boolean isParameter;
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
}