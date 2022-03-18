package expression;

public class MissingOpeningParenthesisException extends ParsingException {
    public MissingOpeningParenthesisException(final String result) {
        super("Missing opening parenthesis", result);
    }
}
