package expression;

public class WrongArgumentException extends EvaluatingException {
    public WrongArgumentException(String e) {
        super("Wrong arguments in expression: " + e);
    }
}
