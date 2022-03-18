package expression;

public class DivisionByZeroException extends EvaluatingException {
    public DivisionByZeroException(final String expr) {
        super("Can't divide on zero: " + expr);
    }
}