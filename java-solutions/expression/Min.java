package expression;

public class Min<T> extends BinaryOperation<T> {
    public Min(CommonExpression<T> a, CommonExpression<T> b) {
        super(a, b);
    }

    public String toString() {
        return "min(" + super.a.toString() + " " + super.b.toString() + ")";
    }

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return a.evaluate(x, y, z).min(b.evaluate(x, y, z).number);
    }
}