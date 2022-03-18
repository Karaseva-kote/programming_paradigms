package expression;

import java.math.BigInteger;

public class MyBigInteger extends MyNumber<BigInteger> {
    public MyBigInteger(BigInteger num) {
        super(num);
    }

    public MyBigInteger add(BigInteger a) {
        return new MyBigInteger(number.add(a));
    }

    public MyBigInteger subtract(BigInteger a) {
        return new MyBigInteger(number.subtract(a));
    }

    public MyBigInteger divide(BigInteger a) {
        return new MyBigInteger(number.divide(a));
    }

    public MyBigInteger multiply(BigInteger a) {
        return new MyBigInteger(number.multiply(a));
    }

    public MyBigInteger max(BigInteger a) {
        return new MyBigInteger(number.max(a));
    }

    public MyBigInteger min(BigInteger a) {
        return new MyBigInteger(number.min(a));
    }

    public MyBigInteger negate() {
        return new MyBigInteger(number.negate());
    }

    public MyBigInteger count() {
        return new MyBigInteger(BigInteger.valueOf(number.bitCount()));
    }
}
