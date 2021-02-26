package varparser.lexical;

public enum TokenType {
    // SPECIALS
    UNEXPECTED_EOF,
    INVALID_TOKEN,
    END_OF_FILE,

    // SYMBOLS
    COMMA,                  // ,
    SEMICOLON,              // ;
    OPEN_CURLY_BRACES,      // {
    CLOSE_CURLY_BRACES,     // }

    // KEYWORDS
    STRUCT,         // struct
    UNION,          // union
    CHAR,           // char
    SHORT,          // short
    INT,            // int
    LONG,           // long
    FLOAT,          // float
    DOUBLE,         // double

    // OTHERS
    ID              // identifier
};
