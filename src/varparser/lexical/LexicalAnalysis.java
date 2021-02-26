package varparser.lexical;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PushbackInputStream;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

public class LexicalAnalysis implements AutoCloseable {
    private int line;
    private final SymbolTable st;
    private PushbackInputStream input;

    public LexicalAnalysis (String filename) throws LexicalException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            throw new LexicalException("Unable to open file");
        }

        st = new SymbolTable();
        line = 1;
    }

    @Override
    public void close () throws IOException {
        input.close();
    }

    public int getLine () {
        return this.line;
    }

    public Lexeme nextToken () throws LexicalException, IOException {
        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);

        int state = 1;
        while (state != 3 && state != 4) {
            int c = getc();
            
            switch (state) {
                case 1:
                    if (c == ' ' || c == '\t' || c == '\r') {
                        state = 1;
                    } else if (c == '\n') {
                        this.line++;
                        state = 1;
                    } else if (c == ';' || c == ',' || c == '{' || c == '}') {
                        lex.token += (char) c;
                        state = 3;
                    } else if (c == '_' || isLetter(c)) {
                        lex.token += (char) c;
                        state = 2;
                    } else if (c == -1) {
                        lex.type = TokenType.END_OF_FILE;
                        state = 4;
                    } else {
                        lex.token += (char) c;
                        lex.type = TokenType.INVALID_TOKEN;
                        state = 4;
                    }
                    break;
                case 2:
                    if (c == '_' || isLetter(c) || isDigit(c)) {
                        lex.token += (char) c;
                        state = 2;
                    } else if (c == -1) {
                        lex.type = TokenType.UNEXPECTED_EOF;
                        state = 4;
                    } else {
                        ungetc(c);
                        state = 3;
                    }
                    break;
                default:
                    throw new LexicalException("Unreachable");
            }
        }

        if (state == 3)
            lex.type = st.find(lex.token);

        return lex;
    }

    private int getc () throws LexicalException {
        try {
            return input.read();
        } catch (IOException e) {
            throw new LexicalException("Unable to read file");
        }
    }

    private void ungetc (int c) throws LexicalException {
        if (c != -1) {
            try {
                input.unread(c);
            } catch (IOException e) {
                throw new LexicalException("Unable to ungetc");
            }
        }
    }
}
