package expression;

public class MissingClosingParenthesisException extends ParsingException {
    public MissingClosingParenthesisException(final String result) {
        super("Missing closing parenthesis", result);
    }
}