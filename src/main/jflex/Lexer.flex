package experimental_compiler;
import java_cup.runtime.*;

%%
%class Lexer
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, value);
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

NUMBER = [0-9]+
ID = [a-zA-Z][a-zA-Z0-9]*

%%

/* keywords */
"print"        { return symbol(sym.PRINT); }
"="            { return symbol(sym.ASSIGN); }

/* operators */
"+"            { return symbol(sym.PLUS); }
"-"            { return symbol(sym.MINUS); }
"*"            { return symbol(sym.TIMES); }
"/"            { return symbol(sym.DIVIDE); }
"("            { return symbol(sym.LPAREN); }
")"            { return symbol(sym.RPAREN); }
";"            { return symbol(sym.SEMI); }

{NUMBER}       { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }
{ID}           { return symbol(sym.ID, yytext()); }

{WhiteSpace}   { /* ignore */ }

[^]            { throw new Error("Illegal character <" + yytext() + ">"); }