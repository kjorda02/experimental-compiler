package datos;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kjorda
 */
public class symbolTable { // (ts)
    static class entry {
        desc val;
        int scopeDecl;
        
        public entry(int n, desc d) {
            val = d;
            scopeDecl = n;
        }
    }
    
    static class restore_entry {
        desc val;
        int scopeDecl;
        String ident;
        
        public restore_entry(String id, int n, desc d) {
            val = d;
            scopeDecl = n;
            ident = id;
        }
    }
    
    private static int currScope = 0; // (n)
    
    // ta[i] points to the last entry overwritten in scope i in the restore table
    private static int[] scopeTable = new int[100]; // (ta)
    private static ArrayList<restore_entry> restoreTable = new ArrayList<>(); // (te)
    private static HashMap<String, entry> table = new HashMap<>(); // (td)
   
    public static void clear() {
        restoreTable.clear();
        table.clear();
        currScope = 0;
        scopeTable[currScope] = -1;
    }
    
    public static boolean add(String id, desc desc) {
        if (table.containsKey(id)) { // Check if the identifier is in use
            if (table.get(id).scopeDecl == currScope) // If it was declared in the current scope -> Error
                return true;
            
            // If it was declared in previous (parent) scope, we need to save it before overwriting it
            // so that we can restore it once we exit the current scope
            
            scopeTable[currScope]++; // Next free position
            
            restoreTable.add(scopeTable[currScope],
                    new restore_entry(id, table.get(id).scopeDecl, table.get(id).val));
            
        }
        table.put(id, new entry(currScope, desc)); // Now we can write or overwrite without worry
        return false;
    }
    
    public static desc get(String s) {
        entry e = table.get(s);
        if (e == null)
            return null;
        return e.val;
    }
    
    public static void enterBlock() {
        currScope++;
        scopeTable[currScope] = scopeTable[currScope-1];
    }
    
    public static void exitBlock() {
        if (currScope==0) // We can't exit if we haven't entered o_O
            (new Exception("Error del compilador!")).printStackTrace();
        
        // Restore all declarations we've overwritten in the current scope
        for (int i = scopeTable[currScope]; i > scopeTable[currScope-1]; i--) {
            restore_entry entrada = restoreTable.get(i);
            table.put(entrada.ident, new entry(entrada.scopeDecl, entrada.val));
        }
        currScope--; // Go to previous scope
    }
}

