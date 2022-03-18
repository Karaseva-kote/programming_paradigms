package expression;

import java.util.Set;
import java.util.function.Function;

public class ExpressionParser<T> implements Parser<T> {
    private static final Set<Character> OPERATIONS = Set.of('*', '+', '/', '-');
    private final Function<String, MyNumber<T>> f;

    public ExpressionParser(Function<String, MyNumber<T>> f) {
        this.f = f;
    }

    public CommonExpression<T> parse(final String source) throws MissingClosingParenthesisException, MissingOperandException,
            UnknownSymbolException, IllegalConstantException, MissingOpeningParenthesisException, MissingOperationException {

        return parse(new StringSource(source));
    }

    public CommonExpression<T> parse(ExpressionSource source) throws MissingClosingParenthesisException, MissingOperandException,
            UnknownSymbolException, IllegalConstantException, MissingOpeningParenthesisException, MissingOperationException {
        return new ParserExpression(source).parseExpression();
    }

    private class ParserExpression extends BaseParser {
        public ParserExpression(final ExpressionSource source) {
            super(source);
            nextChar();
        }

        public CommonExpression<T> parseExpression() throws IllegalConstantException, MissingClosingParenthesisException,
                MissingOperandException, UnknownSymbolException, MissingOpeningParenthesisException, MissingOperationException {
            final CommonExpression<T> result = parsePrior3();
            if (test('\0')) {
                return result;
            }
            if (ch == ')')
                throw new MissingOpeningParenthesisException(getResult());
            if (between('0', '9') || ch == '(' || ch == 'x' || ch == 'y' || ch == 'z')
                throw new MissingOperationException(getResult(), getPosition());
            if (ch == 'c') {
                if (check("count"))
                    throw new MissingOperationException(getResult(), getPosition());
            }
            throw new UnknownSymbolException(getResult(), ch, getPosition());
        }

        private CommonExpression<T> parsePrior3() throws MissingClosingParenthesisException, MissingOperandException,
                UnknownSymbolException, IllegalConstantException {
            skipWhitespace();
            CommonExpression<T> first = parsePrior2();
            while (ch == 'm') {
                CommonExpression<T> second;
                nextChar();
                if (ch == 'i') {
                    if (check("in")) {
                        second = parsePrior2();
                        first = new Min<T>(first, second);
                    } else
                        throw new UnknownSymbolException(getResult(), ch, getPosition());
                } else if (ch == 'a') {
                    if (check("ax")) {
                        second = parsePrior2();
                        first = new Max<T>(first, second);
                    } else
                        throw new UnknownSymbolException(getResult(), ch, getPosition());
                }
            }
            skipWhitespace();
            return first;
        }

        private CommonExpression<T> parsePrior2() throws MissingClosingParenthesisException, MissingOperandException,
                UnknownSymbolException, IllegalConstantException {
            skipWhitespace();
            CommonExpression<T> first = parsePrior1();
            while (ch == '-' || ch == '+') {
                CommonExpression<T> second;
                if (ch == '+') {
                    nextChar();
                    second = parsePrior1();
                    first = new Add<T>(first, second);
                } else {
                    nextChar();
                    second = parsePrior1();
                    first = new Subtract<T>(first, second);
                }
            }
            skipWhitespace();
            return first;
        }

        private CommonExpression<T> parsePrior1() throws MissingClosingParenthesisException, MissingOperandException,
                UnknownSymbolException, IllegalConstantException {
            skipWhitespace();
            CommonExpression<T> first;
            first = parseElement();
            while (ch == '*' || ch == '/') {
                CommonExpression<T> second;
                if (ch == '*') {
                    nextChar();
                    second = parseElement();
                    first = new Multiply<T>(first, second);
                } else {
                    nextChar();
                    second = parseElement();
                    first = new Divide<T>(first, second);
                }
            }
            skipWhitespace();
            return first;
        }

        private CommonExpression<T> parseElement() throws MissingClosingParenthesisException, MissingOperandException,
                UnknownSymbolException, IllegalConstantException {
            skipWhitespace();
            CommonExpression<T> first;
            if (test('(')) {
                first = parsePrior0();
            } else if (test('-')) {
                if (between('0', '9')) {
                    first = parseConst("-");
                } else {
                    CommonExpression<T> other = parseElement();
                    first = new Negate<T>(other);
                }
            } else if (between('0', '9')) {
                first = parseConst("");
            } else if (ch == 'x' || ch == 'y' || ch == 'z') {
                first = parseVariable();
            } else if (ch == 'c') {
                if (check("count") && (ch == ' ' || ch == '(' || ch == '-')) {
                    CommonExpression<T> arg = parseElement();
                    first = new Count<T>(arg);
                } else
                    throw new UnknownSymbolException(getResult(), ch, getPosition());
            } else if (OPERATIONS.contains(ch) || ch == '\0') {
                throw new MissingOperandException(getResult(), getPosition());
            } else if (ch == 'm') {
                nextChar();
                if (ch == 'i') {
                    if (check("in"))
                        throw new MissingOperandException(getResult(), getPosition());
                    else
                        throw new UnknownSymbolException(getResult(), ch, getPosition());
                } else if (ch == 'a') {
                    if (check("ax"))
                        throw new MissingOperandException(getResult(), getPosition());
                    else
                        throw new UnknownSymbolException(getResult(), ch, getPosition());
                } else
                    throw new UnknownSymbolException(getResult(), ch, getPosition());
            } else
                throw new UnknownSymbolException(getResult(), ch, getPosition());
            skipWhitespace();
            return first;
        }

        private CommonExpression<T> parsePrior0() throws MissingClosingParenthesisException, MissingOperandException,
                UnknownSymbolException, IllegalConstantException {
            skipWhitespace();
            CommonExpression<T> first;
            first = parsePrior3();
            if (!check(')'))
                throw new MissingClosingParenthesisException(getResult());
            nextChar();
            skipWhitespace();
            return first;
        }

        private CommonExpression<T> parseConst(String type) throws IllegalConstantException {
            final StringBuilder sb = new StringBuilder(type);
            copyInteger(sb);
            skipWhitespace();
            try {
                return new Const<>(f.apply(sb.toString()));
            } catch (NumberFormatException e) {
                throw new IllegalConstantException(getResult(), sb.toString(), getPosition());
            }
        }

        private void copyInteger(final StringBuilder sb) {
            while (between('0', '9')) {
                sb.append(ch);
                nextChar();
            }
        }

        private CommonExpression<T> parseVariable() {
            final StringBuilder sb = new StringBuilder();
            sb.append(ch);
            nextChar();
            skipWhitespace();
            return new Variable<T>(sb.toString());
        }

        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}

