package expression;

public class MyDouble extends MyNumber<Double>{
    public MyDouble(Double num) {
        super(num);
    }

    public MyDouble add(Double a) {
        return new MyDouble(number + a);
    }

    public MyDouble subtract(Double a) {
        return new MyDouble(number - a);
    }

    public MyDouble divide(Double a) {
        return new MyDouble(number / a);
    }

    public MyDouble multiply(Double a) {
        return new MyDouble(number * a);
    }

    public MyDouble max(Double a) {
        return new MyDouble(Math.max(number, a));
    }

    public MyDouble min(Double a) {
        return new MyDouble(Math.min(number, a));
    }

    public MyDouble negate() {
        return new MyDouble((-1) * number);
    }

    public MyDouble count() {
        return new MyDouble((double)Long.bitCount(Double.doubleToLongBits(number)));
    }
}
