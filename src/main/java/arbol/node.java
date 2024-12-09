package arbol;

import datos.*;
import datos.desc.*;
import java_cup.runtime.ComplexSymbolFactory;

/**
 *
 * @author kjorda
 */
public class node extends ComplexSymbolFactory.ComplexSymbol {
    private static int id = 0;
    protected boolean empty;
    
    public Integer varNum = null;    // (nv) Points to a variable in the variable table
    
    
    public node(String name) {
        super(name, id++);
        this.empty = false;
    }
    
    public node() {
        super("", id++);
        empty = true;
    }

    public boolean isEmpty() {
        return empty;
    }
    
    public void gest() {
        
    }
    
 }