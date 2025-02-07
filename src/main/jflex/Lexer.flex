package experimental_compiler;
import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.*;
import datos.*;
import arbol.*;

%%
%class Lexer
%unicode
%cup
%line
%column

%{
    private ComplexSymbol symbol(int type) {
        Location left = new Location(yyline, yycolumn);
        Location right = new Location(yyline, yycolumn+yytext().length());
        return new ComplexSymbol(sym.terminalNames[type], type, left, right);
    }
    
    private ComplexSymbol symbol(int type, Object value) {
        Location left = new Location(yyline, yycolumn);
        Location right = new Location(yyline, yycolumn+yytext().length());
        return new ComplexSymbol(sym.terminalNames[type], type, left, right, new terminal_node(value, left, right));
    }
%}

newl = \n
whitespace = [ \n\t\r\f]
longcomment = "/*"([^*]|"*"[^/])*"*/"

NUMBER = [0-9]+
ID = [a-zA-Z][a-zA-Z0-9]*


%%

"int"       { return symbol(sym.INT, basicType.INT); }
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

"="         { return symbol(sym.ASS); }
"+"         { return symbol(sym.PLUS); }
"-"         { return symbol(sym.NEG); }
"*"         { return symbol(sym.MULT); }
"/"         { return symbol(sym.DIV); }
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

[^]             { throw new Error("Illegal character <" + yytext() + ">"); }

