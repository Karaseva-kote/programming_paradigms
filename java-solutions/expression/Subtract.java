package expression;

public class Subtract<T> extends BinaryOperation<T> {
    public Subtract (CommonExpression<T> a, CommonExpression<T> b) {
        super(a, b);
    }

    public String toString() {
        return "(" + super.a.toString() + " - " + super.b.toString() + ")";
    }

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z){
        return a.evaluate(x, y, z).subtract(b.evaluate(x, y, z).number);
    }
}
