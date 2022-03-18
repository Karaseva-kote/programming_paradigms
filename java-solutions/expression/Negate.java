package expression;

public class Negate<T> extends UnaryOperation<T> {
    public Negate(CommonExpression<T> a) {
        super(a);
    }

    public String toString() {
        return "-(" + super.a.toString() + ")";
    }

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return a.evaluate(x, y, z).negate();
    }
}