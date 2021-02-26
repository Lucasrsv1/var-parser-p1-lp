package varparser;

import varparser.lexical.Lexeme;
import varparser.lexical.LexicalAnalysis;
import varparser.lexical.LexicalException;
import varparser.lexical.TokenType;
import varparser.syntatic.SyntaticAnalysis;

/**
 * @author Lucas Rassilan Vilanova
 */
public class VarParser {
    public static boolean debugging = false;

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        if (args.length < 1 || ("-d".equals(args[0]) && args.length < 2)) {
            System.out.println("Usage: java -jar VarParser.jar [-d] [var file]");
            return;
        }
        
        String file = args[0];
        if ("-d".equals(args[0])) {
            file = args[1];
            debugging = true;
        }
        
        if (debugging)
            lexicalTest(file);

        try (LexicalAnalysis l = new LexicalAnalysis(file)) {
            SyntaticAnalysis s = new SyntaticAnalysis(l, debugging);
            s.start();
            System.out.println("Sim");
        } catch (LexicalException e) {
            System.out.println(e.getMessage());
            System.out.println("Não");
        } catch (Exception e) {
            System.err.println("Internal error: " + e.getMessage());
            for (StackTraceElement x : e.getStackTrace())
                System.err.println(x.toString());
        }
    }
    
    private static boolean checkType (TokenType type) {
        return !(type == TokenType.END_OF_FILE ||
                 type == TokenType.INVALID_TOKEN ||
                 type == TokenType.UNEXPECTED_EOF);
    }
    
    private static void lexicalTest (String file) {
        try (LexicalAnalysis l = new LexicalAnalysis(file)) {
            Lexeme lex = l.nextToken();
            while (checkType(lex.type)) {
                System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
                lex = l.nextToken();
            }

            switch (lex.type) {
                case INVALID_TOKEN:
                    System.out.printf("%02d: Lexema inválido [%s]\n", l.getLine(), lex.token);
                    break;
                case UNEXPECTED_EOF:
                    System.out.printf("%02d: Fim de arquivo inesperado\n", l.getLine());
                    break;
                default:
                    System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Internal error: " + e.getMessage());
            for (StackTraceElement x : e.getStackTrace())
                System.err.println(x.toString());
        }
    }
}
