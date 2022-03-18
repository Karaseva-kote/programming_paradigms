package expression;

public class Add<T> extends BinaryOperation<T> {
    public Add(CommonExpression<T> a, CommonExpression<T> b) {
        super(a, b);
    }

    public String toString() {
        return "(" + super.a.toString() + " + " + super.b.toString() + ")";
    }

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return a.evaluate(x, y, z).add(b.evaluate(x, y, z).number);
    }
}
