/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sintactico;

import java_cup.runtime.ComplexSymbolFactory;

/**
 *
 * @author kjorda
 */
public class node extends ComplexSymbolFactory.ComplexSymbol {
    private static int idAutoIncrement = 0;
    protected boolean empty;
    
    public node(String name, int id) {
        super(name, id);
        this.empty = false;
    }
    
    public node() {
        super("", idAutoIncrement++);
        empty = true;
    }

    public boolean isEmpty() {
        return empty;
    }
    
 }