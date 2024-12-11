package experimental_compiler;
import java_cup.runtime.ComplexSymbolFactory.*;
import java.io.*;
import java_cup.runtime.Symbol;
import datos.cod;

public class Main {
    public static void main(String[] args) {
        System.out.flush();
        try {
            cod.init();
            // Create a lexer that reads from standard input
            Lexer lexer = new Lexer(new FileReader("resources/prog.txt"));
            
            // Create a parser that uses the lexer
            Parser p = new Parser(lexer);
            
            // Parse and evaluate the input
            Object result = p.parse(); 
            System.out.println(cod.toStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void report_error(String message, Object info) {
        StringBuilder msg = new StringBuilder("ERROR");
        if (info instanceof Symbol) {
            System.out.println("BRRRRRRRRRRRRRRRRRR");
            ComplexSymbol token = (ComplexSymbol)info;
            Location l = token.getLeft();
            
            if (l != null) {
                msg.append(" (fila: ")
                   .append(l.getLine())
                   .append(", columna: ")
                   .append(l.getColumn())
                   .append(")");
            }
        }
        msg.append(": ").append(message);
        
        System.err.println(msg);
    }
}