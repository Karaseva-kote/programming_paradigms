package expression;

public class OverflowException extends EvaluatingException {
    public OverflowException(String e) {
        super("Overflow: " + e + " more than int");
    }
}
