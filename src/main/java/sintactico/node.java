package sintactico;

import datos.val;
import java_cup.runtime.ComplexSymbolFactory;

/**
 *
 * @author kjorda
 */
public class node extends ComplexSymbolFactory.ComplexSymbol {
    private static int id = 0;
    protected boolean empty;
    public val value;
    
    public node(String name, val val) {
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