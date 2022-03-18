package expression;

public class IllegalConstantException extends ParsingException {
    public IllegalConstantException(final String result, final String Const, final int pos) {
        super("Constant '" + Const + "' is not suitable for int at position: " + pos, result);
    }
}
