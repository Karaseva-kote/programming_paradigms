package expression;

public class ParsingException extends Exception {
    public ParsingException(final String message, final String result) {
        super(message + "  Result of parsing: " + result);
    }
}
