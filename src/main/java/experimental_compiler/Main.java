package experimental_compiler;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.flush();  
        System.out.print("> ");
        try {
            // Create a lexer that reads from standard input
            Lexer lexer = new Lexer(new InputStreamReader(System.in));
            
            // Create a parser that uses the lexer
            Parser p = new Parser(lexer);
            
            // Parse and evaluate the input
            Object result = p.parse(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
