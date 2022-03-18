package expression;

public interface CommonExpression<T> {
    MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z);
    String toString();
}