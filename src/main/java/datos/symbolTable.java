package datos;

import java.util.ArrayList;
import java.util.HashMap;
import experimental_compiler.Main;

/**
 *
 * @author kjorda
 */
public class symbolTable {
    static class entry {
        val val;
        int scopeDecl;
        
        public entry(int n, val d) {
            val = d;
            scopeDecl = n;
        }
    }
    
    static class restore_entry {
        val val;
        int scopeDecl;
        String ident;
        
        public restore_entry(String id, int n, val d) {
            val = d;
            scopeDecl = n;
            ident = id;
        }
    }
    
    private static int n = 0;
    
    // ta[i] points to the last entry overwritten in scope i in the restore table
    private static int[] scopeTable = new int[100]; 
    private static ArrayList<restore_entry> restoreTable = new ArrayList<>();
    private static HashMap<String, entry> table = new HashMap<>();
   
    public static void clear() {
        restoreTable.clear();
        table.clear();
        n = 1;
    }
    
    public static void add(String id, val desc) {
        if (table.containsKey(id)) { // Check if the identifier is in use
            if (table.get(id).scopeDecl == n) // If it was declared in the current scope -> Error
                Main.report_error("Identifier \""+id+"\" is already in use in current scope.", null);
            
            // If it was declared in previous (parent) scope, we need to save it before overwriting it
            // so that we can restore it once we exit the current scope
            
            scopeTable[n]++; // Next free position
            
            restoreTable.add(scopeTable[n],
                    new restore_entry(id, table.get(id).scopeDecl, table.get(id).val));
            
        }
        table.put(id, new entry(n, desc)); // Now we can write or overwrite without worry
    }
    
    public static val get(String s) {
        return table.get(s).val;
    }
    
    public static void enterBlock() {
        n++;
        scopeTable[n] = scopeTable[n-1];
    }
    
    public static void exitBlock() {
        if (n==0) // We can't exit if we haven't entered o_O
            (new Exception("Error del compilador!")).printStackTrace();
        
        // Restore all declarations we've overwritten in the current scope
        for (int i = scopeTable[n]; i > scopeTable[n-1]; i--) {
            restore_entry entrada = restoreTable.get(i);
            table.put(entrada.ident, new entry(entrada.scopeDecl, entrada.val));
        }
        n--; // Go to previous scope
    }
}

