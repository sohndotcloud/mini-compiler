import java.util.*;

public class GrammelLexer {
    private final String input;
    private int pos = 0;

    private static final Map<String, TokenType> keywords = Map.ofEntries(
            Map.entry("int", TokenType.INT),
            Map.entry("boolean", TokenType.BOOLEAN),
            Map.entry("string", TokenType.STRING),
            Map.entry("if", TokenType.IF),
            Map.entry("else", TokenType.ELSE),
            Map.entry("while", TokenType.WHILE),
            Map.entry("for", TokenType.FOR),
            Map.entry("print", TokenType.PRINT),
            Map.entry("true", TokenType.TRUE),
            Map.entry("false", TokenType.FALSE),
            Map.entry("not", TokenType.NOT),
            Map.entry("and", TokenType.AND),
            Map.entry("or", TokenType.OR)
    );

    public GrammelLexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char ch = input.charAt(pos);

            if (Character.isWhitespace(ch)) {
                pos++;
                continue;
            }

            // Identifiers or keywords
            if (Character.isLetter(ch) || ch == '_') {
                String word = consumeWhile(c -> Character.isLetterOrDigit(c) || c == '_');
                tokens.add(new Token(keywords.getOrDefault(word, TokenType.IDENTIFIER), word));
                continue;
            }

            // Numbers
            if (Character.isDigit(ch)) {
                String number = consumeWhile(c -> Character.isDigit(c) || c == '.');
                tokens.add(new Token(TokenType.NUMBER, number));
                continue;
            }

            // Strings
            if (ch == '"') {
                pos++; // skip opening quote
                StringBuilder sb = new StringBuilder();
                sb.append("\'");
                while (pos < input.length() && input.charAt(pos) != '"') {
                    sb.append(input.charAt(pos++));
                }
                sb.append("\'");
                pos++; // skip closing quote
                tokens.add(new Token(TokenType.STRING_LITERAL, "'\"'"));
                tokens.add(new Token(TokenType.STRING_LITERAL, sb.toString()));
                tokens.add(new Token(TokenType.STRING_LITERAL, "'\"'"));
                continue;
            }

            // Multi-char operators
            switch (ch) {
                case '+':
                    if (peek() == '+') {
                        pos += 2; tokens.add(new Token(TokenType.INCREMENT, "++"));
                    } else if (peek() == '=') {
                        pos += 2; tokens.add(new Token(TokenType.PLUS_ASSIGN, "+="));
                    } else {
                        pos++; tokens.add(new Token(TokenType.PLUS, "+"));
                    }
                    break;
                case '-':
                    if (peek() == '-') {
                        pos += 2; tokens.add(new Token(TokenType.DECREMENT, "--"));
                    } else if (peek() == '=') {
                        pos += 2; tokens.add(new Token(TokenType.MINUS_ASSIGN, "-="));
                    } else {
                        pos++; tokens.add(new Token(TokenType.MINUS, "-"));
                    }
                    break;
                case '*':
                    if (peek() == '=') {
                        pos += 2; tokens.add(new Token(TokenType.MUL_ASSIGN, "*="));
                    } else {
                        pos++; tokens.add(new Token(TokenType.STAR, "*"));
                    }
                    break;
                case '/':
                    if (peek() == '=') {
                        pos += 2; tokens.add(new Token(TokenType.DIV_ASSIGN, "/="));
                    } else {
                        pos++; tokens.add(new Token(TokenType.SLASH, "/"));
                    }
                    break;
                case '=':
                    if (peek() == '=') {
                        pos += 2; tokens.add(new Token(TokenType.EQ, "=="));
                    } else {
                        pos++; tokens.add(new Token(TokenType.ASSIGN, "="));
                    }
                    break;
                case '>':
                    if (peek() == '=') {
                        pos += 2; tokens.add(new Token(TokenType.GTE, ">="));
                    } else {
                        pos++; tokens.add(new Token(TokenType.GT, ">"));
                    }
                    break;
                case '<':
                    if (peek() == '=') {
                        pos += 2; tokens.add(new Token(TokenType.LTE, "<="));
                    } else {
                        pos++; tokens.add(new Token(TokenType.LT, "<"));
                    }
                    break;
                case '?': pos++; tokens.add(new Token(TokenType.QUESTION, "?")); break;
                case ':': pos++; tokens.add(new Token(TokenType.COLON, ":")); break;
                case '(': pos++; tokens.add(new Token(TokenType.LPAREN, "\'(\'")); break;
                case ')': pos++; tokens.add(new Token(TokenType.RPAREN, "\')\'")); break;
                case '{': pos++; tokens.add(new Token(TokenType.LBRACE, "\'{\'")); break;
                case '}': pos++; tokens.add(new Token(TokenType.RBRACE, "\'}\'")); break;
                case ';': pos++; tokens.add(new Token(TokenType.SEMICOLON, "\';\'")); break;
                case ',': pos++; tokens.add(new Token(TokenType.COMMA, "\',\'")); break;
                default:
                    pos++; tokens.add(new Token(TokenType.INVALID, Character.toString(ch)));
                    break;
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private char peek() {
        return (pos + 1 < input.length()) ? input.charAt(pos + 1) : '\0';
    }

    private String consumeWhile(java.util.function.Predicate<Character> predicate) {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && predicate.test(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        return sb.toString();
    }
}
