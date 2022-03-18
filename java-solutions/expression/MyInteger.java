package expression;

public class MyInteger extends MyNumber<Integer> {

    public MyInteger(Integer num) {
        super(num);
    }

    public MyInteger add(Integer a) {
        if (number >= 0) {
            if (Integer.MAX_VALUE - number >= a) {
                return new MyInteger(number + a);
            } else
                throw new OverflowException(this.toString());
        } else {
            if (Integer.MIN_VALUE - number <= a) {
                return new MyInteger(number + a);
            } else
                throw new OverflowException(this.toString());
        }
    }

    public MyInteger subtract(Integer a) {
        if (a <= 0) {
            if (Integer.MAX_VALUE + a >= number)
                return new MyInteger(number - a);
            else
                throw new OverflowException(this.toString());
        } else {
            if (Integer.MIN_VALUE + a <= number)
                return new MyInteger(number - a);
            else
                throw new OverflowException(this.toString());
        }
    }

    public MyInteger divide(Integer a) {
        if (number == Integer.MIN_VALUE && a == -1)
            throw new OverflowException(this.toString());
        if (a != 0)
            return new MyInteger(number / a);
        else
            throw new DivisionByZeroException(this.toString());
    }

    public MyInteger multiply(Integer a) {
        if (number > 0 && a > 0 && Integer.MAX_VALUE / number < a) {
            throw new OverflowException(number + " * " + a);
        }
        if (number > 0 && a < 0 && Integer.MIN_VALUE / number > a) {
            throw new OverflowException(number + " * " + a);
        }
        if (number < 0 && a > 0 && Integer.MIN_VALUE / a > number) {
            throw new OverflowException(number + " * " + a);
        }
        if (number < 0 && a < 0 && Integer.MAX_VALUE / number > a) {
            throw new OverflowException(number + " * " + a);
        }
        return new MyInteger(number * a);
    }

    public MyInteger max(Integer a) {
        return new MyInteger(Math.max(number, a));
    }

    public MyInteger min(Integer a) {
        return new MyInteger(Math.min(number, a));
    }

    public MyInteger negate() {
        if (number == Integer.MIN_VALUE) {
            throw new OverflowException(this.toString());
        }
        return new MyInteger((-1) * number);
    }

    public MyInteger count() {
        return new MyInteger(Integer.bitCount(number));
    }
}
