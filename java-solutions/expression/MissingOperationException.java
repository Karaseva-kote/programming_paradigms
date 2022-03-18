package expression;

public class MissingOperationException extends ParsingException{
    public MissingOperationException(String result, int pos){
        super("Missing operation on position: " + pos, result);
    }
}
