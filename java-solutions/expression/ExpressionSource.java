package expression;

public interface ExpressionSource {
    boolean hasNext();

    char next();

    int getPosition();

    String getResult(int pos);
}
