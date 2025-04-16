public enum TokenType {
    // Keywords
    INT, BOOLEAN, STRING, IF, ELSE, WHILE, FOR, PRINT, TRUE, FALSE, NOT, AND, OR,

    // Identifiers and literals
    IDENTIFIER, NUMBER, STRING_LITERAL,

    // Operators
    PLUS, MINUS, STAR, SLASH, ASSIGN,
    PLUS_ASSIGN, MINUS_ASSIGN, MUL_ASSIGN, DIV_ASSIGN,
    EQ, GT, LT, GTE, LTE,
    INCREMENT, DECREMENT,
    QUESTION, COLON,

    // Delimiters
    LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, COMMA,

    // Special
    EOF, INVALID
}