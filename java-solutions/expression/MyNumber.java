package expression;

public abstract class MyNumber<T> {
    public T number;

    public MyNumber(T num) {
        number = num;
    }

    public abstract MyNumber<T> add(T a);

    public abstract MyNumber<T> subtract(T a);

    public abstract MyNumber<T> divide(T a);

    public abstract MyNumber<T> multiply(T a);

    public abstract MyNumber<T> max(T a);

    public abstract MyNumber<T> min(T a);

    public abstract MyNumber<T> negate();

    public abstract MyNumber<T> count();
}
