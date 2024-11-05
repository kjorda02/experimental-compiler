package sintactico;

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
    
    public Integer var = null;    // Used for expressions. points to the position of the variable where 
    public Integer displ = null;  // the result value is stored in the variable table
    public typeDesc type; // Also used for expressions
    public String dataType = null;
    
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