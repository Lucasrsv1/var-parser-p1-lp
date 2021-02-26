package varparser.syntatic;

import java.io.IOException;

import varparser.lexical.Lexeme;
import varparser.lexical.TokenType;
import varparser.lexical.LexicalAnalysis;
import varparser.lexical.LexicalException;

public class SyntaticAnalysis {
    private Lexeme current;
    private final LexicalAnalysis lex;
    private final boolean debugging;

    public SyntaticAnalysis (LexicalAnalysis lex, boolean debugging) throws LexicalException, IOException {
        this.lex = lex;
        this.current = lex.nextToken();
        this.debugging = debugging;
    }

    public void start () throws LexicalException, IOException {
        procDecl();
        eat(TokenType.END_OF_FILE);
    }

    private void advance () throws LexicalException, IOException {
        if (this.debugging)
            System.out.println("Advanced (\"" + current.token + "\", " + current.type + ")");

        current = lex.nextToken();
    }

    private void eat (TokenType type) throws LexicalException, IOException {
        if (this.debugging)
            System.out.println("Expected (..., " + type + "), found (\"" + current.token + "\", " + current.type + ")");

        if (type == current.type)
            advance();
        else
            showError();
    }

    private void showError () {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.out.println("Não");
        System.exit(1);
    }

    // <decl>   ::= { (<complx> | <simple>) ';' }
    private void procDecl () throws LexicalException, IOException {
        while (isCmpType() || isType()) {
            if (isCmpType())
                procComplx();
            else
                procSimple();

            eat(TokenType.SEMICOLON);
        }
    }

    // <simple> ::= <type> <var-list>
    private void procSimple () throws LexicalException, IOException {
        procType();
        procVarList();
    }

    // <var-list>   ::= <var> { ',' <var> }
    private void procVarList () throws LexicalException, IOException {
        procVar();

        while (this.current.type == TokenType.COMMA) {
            eat(TokenType.COMMA);
            procVar();
        }
    }

    // <type>   ::= char | short | int | long | float | double
    private void procType () throws LexicalException, IOException {
        if (isType())
            eat(this.current.type);
        else
            showError();
    }

    // <var>    ::= <id>
    private void procVar () throws LexicalException, IOException {
        eat(TokenType.ID);
    }

    // <complx> ::= <cmp-type> [ <id> ] '{' <decl> '}' <var-list>
    private void procComplx () throws LexicalException, IOException {
        procCmpType();
        if (this.current.type == TokenType.ID)
            eat(TokenType.ID);
        
        eat(TokenType.OPEN_CURLY_BRACES);
        procDecl();
        
        eat(TokenType.CLOSE_CURLY_BRACES);
        procVarList();
    }

    // <cmp-type>   ::= struct | union
    private void procCmpType () throws LexicalException, IOException {
        if (isCmpType())
            eat(this.current.type);
        else
            showError();
    }

    private boolean isType () {
        return current.type == TokenType.CHAR ||
               current.type == TokenType.SHORT ||
               current.type == TokenType.INT ||
               current.type == TokenType.LONG ||
               current.type == TokenType.FLOAT ||
               current.type == TokenType.DOUBLE;
    }
    
    private boolean isCmpType () {
        return current.type == TokenType.STRUCT || current.type == TokenType.UNION;
    }
}
