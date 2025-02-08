package experimental_compiler;
import arbol.node;
import java_cup.runtime.ComplexSymbolFactory.*;
import java.io.*;
import java_cup.runtime.Symbol;
import datos.cod;
import datos.funcTable;
import datos.symbolTable;
import datos.varTable;
import experimental_compiler.instructions.ins;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    static final int ERRORINDENT = 5;
    static final String PATH = "resources/prog.txt";
    static String[] lines; // For printing errors
    static boolean error = false;
    
    public static void main(String[] args) {
        System.out.flush();
        try {
            lines = Files.readAllLines(Paths.get(PATH))
                                .toArray(new String[0]);
            
            symbolTable.clear();
            // Create a lexer that reads from standard input
            Lexer lexer = new Lexer(new FileReader(PATH));
            lexer.initializeTokenLog("resources/tokens.txt");
            
            // Create a parser that uses the lexer
            Parser p = new Parser(lexer);
            
            // Parse and evaluate the input
            Object result = p.parse();
            
            if (error) 
                return;
                
            // Optimize    
            funcTable.allocateAllVars();
            varTable.allocateVarsGlobal();
            System.out.println(cod.toStr());
            varTable.outputVarTable("resources/variable_table.txt");
            funcTable.outputFuncTable("resources/function_table.txt");
            symbolTable.outputSymbolTable("resources/symbolTable.txt");
            cod.generate("resources/intermedio.txt");
            ins.generate("resources/output.s");
        } catch (Exception e) {
            System.out.println("");
        }
    }
  
    public static void report_error(String message, Object info) {
        error = true;
        Location l, r;
        l = r = null;
        if (info instanceof ComplexSymbol) {
            ComplexSymbol token = (ComplexSymbol) info;
            l = token.getLeft();
            r = token.getRight();
        }
        else if (info instanceof Symbol) {
            StringBuilder errorMsg = new StringBuilder();
            if (((Symbol)info).sym == 0)
                errorMsg.append("ERROR: Unexpected end of file.\n");
            else if(((Symbol)info).sym == 1)
                errorMsg.append("ERROR: Unexpected error.\n");
            
            // Print to console and append to error log
            System.err.print(errorMsg);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/errores.txt"))) {
                writer.write(errorMsg.toString());
            }catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        else if (info instanceof node) {
            node nodo = (node) info;
            l = nodo.left;
            r = nodo.right;
        }
        report_error(message, l, r);
    }
    
    public static void report_error(String message, Location l, Location r) {
        error = true;
        StringBuilder msg = new StringBuilder();
        msg.append("[");
        msg.append(l.getLine()+1);
        msg.append(":");
        msg.append(l.getColumn()+1);
        msg.append("] ERROR: ");
        msg.append(message);
        msg.append("\n");
        
        int indent = String.valueOf(r.getLine()).length()+1;
        for (int i = l.getLine(); i <= r.getLine(); i++) {
            
            if (i == l.getLine() && i != r.getLine()) {
                for (int j = 0; j < l.getColumn()+ERRORINDENT+2; j++) {msg.append(" ");}
                for (int j = l.getColumn(); j < lines[i].length(); j++) {msg.append("v");}
                msg.append("\n");
            }
            
            for (int j = indent; j < ERRORINDENT; j++) {msg.append(" ");} // spaces before number
            
            String s = String.valueOf(i+1);
            msg.append(s); // print number
            msg.append(" | ");
            msg.append(lines[i]);
            msg.append("\n");
            
            if (l.getLine() == r.getLine()) {
                for (int j = 0; j < l.getColumn()+ERRORINDENT+2; j++) {msg.append(" ");}
                for (int j = l.getColumn(); j < r.getColumn(); j++) {msg.append("^");}
                msg.append("\n");
            }
            else if (i == r.getLine()) {
                for (int j = 0; j < ERRORINDENT+2; j++) {msg.append(" ");}
                for (int j = 0; j < r.getColumn(); j++) {msg.append("^");}
                msg.append("\n");
            }
        }
 
        System.err.println(msg);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/errores.txt"))) {
            writer.write(msg.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}