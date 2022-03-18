package expression;

public class UnknownSymbolException extends ParsingException {
    public UnknownSymbolException(final String result, final char c, final int pos) {
        super("Unknown symbol '" + c + "' at position: " + pos, result);
    }
}
