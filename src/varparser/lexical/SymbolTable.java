package varparser.lexical;

import java.util.Map;
import java.util.HashMap;

public class SymbolTable {
    private final Map<String, TokenType> st;

    public SymbolTable () {
        st = new HashMap<>();

        // SYMBOLS
        st.put(",", TokenType.COMMA);
        st.put(";", TokenType.SEMICOLON);
        st.put("{", TokenType.OPEN_CURLY_BRACES);
        st.put("}", TokenType.CLOSE_CURLY_BRACES);

        // KEYWORDS
        st.put("struct", TokenType.STRUCT);
        st.put("union", TokenType.UNION);
        st.put("char", TokenType.CHAR);
        st.put("short", TokenType.SHORT);
        st.put("int", TokenType.INT);
        st.put("long", TokenType.LONG);
        st.put("float", TokenType.FLOAT);
        st.put("double", TokenType.DOUBLE);
    }

    public boolean contains (String token) {
        return st.containsKey(token);
    }

    public TokenType find (String token) {
        return this.contains(token) ? st.get(token) : TokenType.ID;
    }
}
