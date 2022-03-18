package expression;

public abstract class BinaryOperation<T> implements CommonExpression<T> {
    protected CommonExpression<T> a;
    protected CommonExpression<T> b;

    public BinaryOperation(CommonExpression<T> a, CommonExpression<T> b) {
        this.a = a;
        this.b = b;
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