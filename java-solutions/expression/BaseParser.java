package expression;

public class BaseParser {
    private final ExpressionSource source;
    protected char ch;

    protected BaseParser(final ExpressionSource source) {
        this.source = source;
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean check(final char c) {
        return ch == c;
    }

    protected boolean check(final String value) {
        for (char c : value.toCharArray()) {
            if (ch != c)
                return false;
            nextChar();
        }
        return true;
    }

    /*protected ParsingException error(final String message) {
        return source.error(message);
    }

    protected EvaluatingException error(final String message) {
        return source.error(message);
    }*/

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected int getPosition() { return source.getPosition(); }

    protected String getResult() { return source.getResult(getPosition()); }
}