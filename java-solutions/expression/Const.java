package expression;

public class Const<T> implements CommonExpression<T> {
    private MyNumber<T> num;

    public Const(MyNumber<T> a) {
        this.num = a;
    }

    public String toString() {
        return num.toString();
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

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return num;
    }
}
