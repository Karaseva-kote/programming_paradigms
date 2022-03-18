package expression.generic;

import expression.*;

import java.math.BigInteger;
import java.util.function.Function;

public class GenericTabulator implements Tabulator {
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        switch (mode) {
            case "i": {
                return tabulate(integer -> new MyInteger(integer), new ExpressionParser<Integer>(string -> new MyInteger(Integer.parseInt(string))), expression, x1, x2, y1, y2, z1, z2);
            }
            case "d": {
                return tabulate(integer -> new MyDouble(integer.doubleValue()), new ExpressionParser<Double>(string -> new MyDouble(Double.parseDouble(string))), expression, x1, x2, y1, y2, z1, z2);
            }
            case "bi": {
                return tabulate(integer -> new MyBigInteger(new BigInteger(String.valueOf(integer))), new ExpressionParser<BigInteger>(string -> new MyBigInteger(new BigInteger(string))), expression, x1, x2, y1, y2, z1, z2);
            }
            default: {
                throw new Exception("Unknowable mode: " + mode);
            }
        }
    }

    public <T> Object[][][] tabulate(Function<Integer, MyNumber<T>> f, Parser<T> parser, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        CommonExpression<T> expr = parser.parse(expression);
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = expr.evaluate(f.apply(i), f.apply(j), f.apply(k)).number;
                    } catch (Exception e) {
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return result;
    }
}
