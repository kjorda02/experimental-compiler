package arbol.flow;

import arbol.node;
import arbol.decl_node;
import arbol.type.complexType;
import arbol.val.expr_node;
import datos.basicType;
import datos.cod;
import experimental_compiler.Main;

/**
 * Implementation of the for loop control structure
 */
public class for_node extends node {
    private node init;      // Initialization: can be decl_node or expr_node
    private expr_node cond; // Loop condition
    private expr_node incr; // Increment expression
    private stmts_node stmts;  // Loop body
    
    public for_node(node initialization, expr_node condition, expr_node increment, stmts_node statements) {
        init = initialization;
        cond = condition;
        incr = increment;
        stmts = statements;
        
        if (cond != null && (!(cond.type instanceof complexType.primitive) || 
            ((complexType.primitive) cond.type).btype != basicType.BOOL)) {
            Main.report_error("For loop condition must be boolean expression", cond);
            return;
        }
        
        error = false;
    }
    
    @Override
    public void gest() {
        if (error) return;
        
        // Generate initialization code
        if (init != null) {
            init.gest();
        }
        
        int condTag = cod.newTag();
        int endTag = cod.newTag();
        
        // Generate condition check
        cod.setTag(condTag);
        
        if (cond != null) {
            cond.gest();
            
            if (cond.value != null) { // Compile-time expression
                if (cond.value != 0) { // Infinite loop
                    stmts.gest();
                    if (incr != null) {
                        incr.gest();
                    }
                    cod.genera(cod.op.GOTO, 0, 0, 0);
                    cod.jmpTag(condTag);
                }
                return;
            }
            
            cod.genera(cod.op.IFEQ, cond.varNum, 0, 0);
            cod.setImmediate(false, true);
            cod.jmpTag(endTag);
        }
        
        // Generate loop body
        stmts.gest();
        
        // Generate increment code
        if (incr != null) {
            incr.gest();
        }
        
        // Jump back to condition
        cod.genera(cod.op.GOTO, 0, 0, 0);
        cod.jmpTag(condTag);
        
        // End label
        cod.setTag(endTag);
    }
}