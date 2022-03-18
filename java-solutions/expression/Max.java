package expression;

public class Max<T> extends BinaryOperation<T> {
    public Max(CommonExpression<T> a, CommonExpression<T> b) {
        super(a, b);
    }

    public String toString() {
        return "max(" + super.a.toString() + " " + super.b.toString() + ")";
    }

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return a.evaluate(x, y, z).max(b.evaluate(x, y, z).number);
    }
}