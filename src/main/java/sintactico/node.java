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
    public desc value;
    
    int resultVar = -1; // Points to the position of the variable where 
                        // the result value is stored in the variable table
    typeDesc type;
    
    public node(String name, desc val) {
        super(name, id++);
        value = val;
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