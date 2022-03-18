package expression;

public class StringSource implements ExpressionSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length(); 
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public int getPosition() {
        return pos;
    }

    @Override
    public String getResult(int pos) {
        return data.substring(0, pos);
    }
}
