package expression;

public class Count<T> extends UnaryOperation<T> {
    public Count(CommonExpression<T> a) {
        super(a);
    }

    public String toString() {
        return "count(" + super.a.toString() + ")";
    }

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return a.evaluate(x, y, z).count();
    }
}