package expression;

public class MissingOperandException extends ParsingException {
    public MissingOperandException(final String result, final int ind) {
        super("Missing operand at position " + ind, result);
    }
}
