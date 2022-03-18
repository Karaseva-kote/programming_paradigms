package expression;

public abstract class UnaryOperation<T> implements CommonExpression<T> {
    protected CommonExpression<T> a;

    public UnaryOperation (CommonExpression<T> a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object a) {
        if (a == this) {
            return true;
        }
        if (a == null || a.getClass() != this.getClass()) {
            return false;
        }
        return this.toString().equals(a.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
