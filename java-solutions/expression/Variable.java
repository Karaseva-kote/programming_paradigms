package expression;

public class Variable<T> implements CommonExpression<T> {
    private String variable;

    public Variable(String str) {
        this.variable = str;
    }

    public String toString() {
        return variable;
    }

    @Override
    public boolean equals(Object a) {
        if (a == this) {
            return true;
        }
        if (a == null || a.getClass() != this.getClass()) {
            return false;
        }
        return variable.equals(a.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        if (variable.equals("x"))
            return x;
        if (variable.equals("y"))
            return y;
        if (variable.equals("z"))
            return z;
        throw new EvaluatingException("unknowable variable");
    }
}
