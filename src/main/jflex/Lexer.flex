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

newl = \n
whitespace = [ \t\r\f]
longcomment = "/*"([^*]|"*"[^/])*"*/"

NUMBER = [0-9]+
ID = [a-zA-Z][a-zA-Z0-9]*


%%

"exit"        { return symbol(sym.EXIT); }

"="           { return symbol(sym.ASS); }
"+"           { return symbol(sym.PLUS); }
"-"           { return symbol(sym.NEG); }
"*"           { return symbol(sym.MULT); }
"/"           { return symbol(sym.DIV); }
"("           { return symbol(sym.LPAREN); }
")"           { return symbol(sym.RPAREN); }
";"           { return symbol(sym.SEMI); }

"!"           { return symbol(sym.NOT); }
"&&"           { return symbol(sym.AND); }
"||"           { return symbol(sym.OR); }

"<"           { return symbol(sym.LT); }
">"           { return symbol(sym.GT); }
"<="           { return symbol(sym.LEQ); }
">="           { return symbol(sym.GEQ); }
"=="           { return symbol(sym.EQ); }
"!="           { return symbol(sym.NEQ); }

{NUMBER}       { return symbol(sym.INTLIT, Integer.parseInt(yytext())); }
"true"|"false" { return symbol(sym.BOOLLIT, Boolean.parseBoolean(yytext())); }
{ID}           { return symbol(sym.ID, yytext()); }


{whitespace}   { }
{newl}         { System.out.print("> "); }

[^]            { throw new Error("Illegal character <" + yytext() + ">"); }

