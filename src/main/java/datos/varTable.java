package datos;

import java.util.ArrayList;

/**
 *
 * @author kjorda
 */
public class varTable {
    public static class varInfo {
        public int parentFunc;
        public boolean isParameter;
        
        public varInfo(int p, boolean i) {
            parentFunc = p;
            isParameter = i;
        }
    }
    
    private static ArrayList<varInfo> table = new ArrayList<>();
    static int num = 0; // (nv)
    
    public varInfo get(int i) {
        return table.get(i);
    }
    
    public static int newvar(int parentFuncIdx, boolean isParam) {
        table.add(new varInfo(parentFuncIdx, isParam));
        return num++;
    }
    
    public static int newvar(int reservedSpace, int parentFuncIdx, boolean isParam) {
        table.add(new varInfo(parentFuncIdx, isParam));
        int addr = num;
        num += reservedSpace;
        return addr;
    }
}
