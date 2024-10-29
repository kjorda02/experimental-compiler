package datos;

import java.util.HashMap;

/**
 *
 * @author kjorda
 */
public class symbolTable {
    private static HashMap<String, val> table = new HashMap<>();
    
    public static void add(String id, val val) {
        table.put(id, val);
    }
    
    public static val get(String s) {
        return table.get(s);
    }    
    
    public static Object getVal(String s) {
        return table.get(s).val;
    }
    
    public static Type getType(String s) {
        return table.get(s).type;
    }
    
    public static void modify(String id, Object val) {
        table.get(id).val = val;
    }
}

