package experimental_compiler;
import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.*;
import datos.*;
import arbol.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

%%
%class Lexer
%unicode
%cup
%line
%column

%{
    private BufferedWriter tokenLog;
    
    public void initializeTokenLog(String filename) {
        try {
            tokenLog = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            System.err.println("Error creating token log file: " + e.getMessage());
        }
    }
    
    private void logToken(ComplexSymbol sym) {
        if (tokenLog == null) return;
        
        try {
            StringBuilder sb = new StringBuilder();
            // Token name with padding
            sb.append(String.format("%-20s", experimental_compiler.sym.terminalNames[sym.sym]));
            
            // Position information
            sb.append(String.format(" (line %d, col %d) to (line %d, col %d)", 
                sym.getLeft().getLine(), sym.getLeft().getColumn(),
                sym.getRight().getLine(), sym.getRight().getColumn()));
            
            // Value if present
            if (sym.value instanceof terminal_node) {
                terminal_node<?> node = (terminal_node<?>) sym.value;
                if (node.value != null) {
                    sb.append(" Value: ").append(node.value);
                }
            }
            
            tokenLog.write(sb.toString());
            tokenLog.newLine();
            tokenLog.flush();
        } catch (IOException e) {
            System.err.println("Error writing to token log: " + e.getMessage());
        }
    }
    
    public void closeTokenLog() {
        if (tokenLog != null) {
            try {
                tokenLog.close();
            } catch (IOException e) {
                System.err.println("Error closing token log: " + e.getMessage());
            }
        }
    }

    private ComplexSymbol symbol(int type) {
        Location left = new Location(yyline, yycolumn);
        Location right = new Location(yyline, yycolumn+yytext().length());
        ComplexSymbol sym = new ComplexSymbol(experimental_compiler.sym.terminalNames[type], type, left, right);
        logToken(sym);
        return sym;
    }
    
    private ComplexSymbol symbol(int type, Object value) {
        Location left = new Location(yyline, yycolumn);
        Location right = new Location(yyline, yycolumn+yytext().length());
        ComplexSymbol sym = new ComplexSymbol(experimental_compiler.sym.terminalNames[type], type, left, right, 
            new terminal_node(value, left, right));
        logToken(sym);
        return sym;
    }
%}

newl = \n
whitespace = [ \n\t\r\f]
linecomment = "//".*\n
longcomment = "/*"([^*]|"*"[^/])*"*/"
char = '([^'\\\n]|\\[\'\\nt])' 

NUMBER = [0-9]+
ID = [a-zA-Z][a-zA-Z0-9]*

%%

"int"       { return symbol(sym.INT, basicType.INT); }
"char"      { return symbol(sym.CHAR, basicType.INT); }
"bool"      { return symbol(sym.BOOL, basicType.BOOL); }
"string"    { return symbol(sym.STRING); }
"struct"    { return symbol(sym.STRUCT); }
"type"      { return symbol(sym.TYPE); }
"if"        { return symbol(sym.IF); }
"else"      { return symbol(sym.ELSE); }
"while"     { return symbol(sym.WHILE); }
"for"       { return symbol(sym.FOR); }
"switch"    { return symbol(sym.SWITCH); }
"case"      { return symbol(sym.CASE); }
"break"     { return symbol(sym.BREAK); }
"const"     { return symbol(sym.CONST, null); }
"out"       { return symbol(sym.OUT); }
"void"      { return symbol(sym.VOID); }
"return"    { return symbol(sym.RETURN, null); }
"print"    { return symbol(sym.PRINT, null); }
"putChar"    { return symbol(sym.PUTCHAR, null); }
"input"    { return symbol(sym.INPUT, null); }


"="         { return symbol(sym.ASS); }
"+"         { return symbol(sym.PLUS); }
"-"         { return symbol(sym.NEG); }
"*"         { return symbol(sym.MULT); }
"/"         { return symbol(sym.DIV); }
"%"         { return symbol(sym.MOD); }
"("         { return symbol(sym.LPAREN); }
")"         { return symbol(sym.RPAREN, null); }
";"         { return symbol(sym.SEMI); }


"["         { return symbol(sym.LBRAC); }
"]"         { return symbol(sym.RBRAC); }
"{"         { return symbol(sym.LKEY); }
"}"         { return symbol(sym.RKEY); }


"!"         { return symbol(sym.NOT); }
"&&"        { return symbol(sym.AND); }
"||"        { return symbol(sym.OR); }

"<"         { return symbol(sym.LT); }
">"         { return symbol(sym.GT); }
"<="        { return symbol(sym.LEQ); }
">="        { return symbol(sym.GEQ); }
"=="        { return symbol(sym.EQ); }
"!="        { return symbol(sym.NEQ); }

","             { return symbol(sym.COMMA); }
"."             { return symbol(sym.DOT); }

{NUMBER}        { return symbol(sym.INTLIT, (int) Integer.parseInt(yytext())); }
"true"|"false"  { return symbol(sym.BOOLLIT, Boolean.parseBoolean(yytext())); }
{ID}            { return symbol(sym.ID, yytext()); }

{whitespace}    { }
{linecomment}    { }

[^]             { throw new Error("Illegal character <" + yytext() + ">"); }

{char}          { 
    String content = yytext().substring(1, yytext().length()-1);
    
    if (content.startsWith("\\")) {
        switch(content.charAt(1)) {
            case 'n': return symbol(sym.CHARLIT, (int) '\n');
            case 't': return symbol(sym.CHARLIT, (int) '\t');
            case '\\': return symbol(sym.CHARLIT, (int) '\\');
            case '\'': return symbol(sym.CHARLIT, (int) '\'');
            default: throw new Error("Invalid escape sequence");
        }
    }
    return symbol(sym.CHARLIT, (int) content.charAt(0));
}