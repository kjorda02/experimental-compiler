package datos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        
        ArrayList<String> removeIDs = new ArrayList<>();
        for (String id : table.keySet()) { // Get leftover ids declared in this block
            if (table.get(id).scopeDecl == currScope) {
                removeIDs.add(id);
            }
        }
        
        for (String id : removeIDs) { // To avoid concurrentModificationException. Cannot remove while iterating
            table.remove(id);
        }
        
        currScope--; // Go to previous scope
    }
    
    public static void outputSymbolTable(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("| " + format("identifier", 15) + format("scope", 6) + format("type", 40) + format("details", 30) + " |\n");

            // Sort identifiers for consistent output
            ArrayList<String> sortedIds = new ArrayList<>(table.keySet());
            Collections.sort(sortedIds);

            for (String id : sortedIds) {
                entry e = table.get(id);
                String typeStr = "";
                String details = "";

                // Determine type and details based on descriptor type
                if (e.val instanceof desc.variable) {
                    desc.variable var = (desc.variable) e.val;
                    typeStr = var.type.toString();
                    details = "var #" + var.varNum;
                }
                else if (e.val instanceof desc.constant) {
                    desc.constant cons = (desc.constant) e.val;
                    typeStr = cons.type.toString();
                    details = "const value=" + cons.value;
                }
                else if (e.val instanceof desc.type) {
                    desc.type type = (desc.type) e.val;
                    typeStr = "type";
                    details = type.type.toString();
                }
                else if (e.val instanceof desc.function) {
                    desc.function func = (desc.function) e.val;
                    StringBuilder sigStr = new StringBuilder();
                    if (func.signature.returnType != null) {
                        sigStr.append(func.signature.returnType.toString());
                    } else {
                        sigStr.append("void");
                    }
                    sigStr.append(" ");
                    sigStr.append(func.signature.name.value);
                    sigStr.append("(");
                    for (int i = 0; i < func.signature.paramTypes.size(); i++) {
                        if (i > 0) sigStr.append(", ");
                        if (func.signature.paramModes.get(i)) {
                            sigStr.append("out ");
                        }
                        sigStr.append(func.signature.paramTypes.get(i).toString());
                    }
                    sigStr.append(")");
                    typeStr = sigStr.toString();
                    details = "func #" + func.funcNum + (func.defined ? " (defined)" : " (declared)");
                }

                writer.write("| " + format(id, 15) + 
                                  format(String.valueOf(e.scopeDecl), 6) + 
                                  format(typeStr, 40) + 
                                  format(details, 30) + " |\n");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method for formatting strings with fixed width
    private static String format(String s, int len) {
        if (s == null) {
            s = "";
        }
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < len) {
            sb.append(" ");
        }
        return sb.toString();
    }
}

