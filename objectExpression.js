"use strict"

function Operation(args) {
    this.args = args;
}
Operation.prototype.evaluate = function (x, y, z) {
    let argEvaluate = this.args.map(a => a.evaluate(x, y, z));
    return this.operation(...argEvaluate);
};
Operation.prototype.toString = function () {
    return this.args.join(' ') + ' ' + this.sign;
};
Operation.prototype.prefix = function () {
    return '(' + this.sign + ' ' + this.args.map(x => x.prefix()).join(' ') + ')';
};
function makeOperation(operation, sign) {
    function Oper(...args) {
        Operation.call(this, [...args]);
    }
    Oper.prototype = Object.create(Operation.prototype);
    Oper.prototype.operation = operation;
    Oper.prototype.sign = sign;
    return Oper;
}

function Const(val) {
    this.evaluate = function (x, y, z) {
        return val
    };
    this.toString = function () {
        return String(val)
    };
    this.prefix = function () {
        return String(val);
    };
}

function Variable(variable) {
    this.evaluate = function (x, y, z) {
        return (variable === "x") ? x : (variable === "y") ? y : z
    };
    this.toString = function () {
        return variable
    };
    this.prefix = function () {
        return variable;
    };
}

let Add = makeOperation((x, y) => x + y, '+');

let Subtract = makeOperation((x, y) => x - y, '-');

let Multiply = makeOperation((x, y) => x * y, '*');

let Divide = makeOperation((x, y) => x / y, '/');

let Negate = makeOperation(x => -x, 'negate');

let Sinh = makeOperation(x => Math.sinh(x), 'sinh');

let Cosh = makeOperation(x => Math.cosh(x), 'cosh');

let Sum = makeOperation((...args) => args.reduce(function (sum, current) { return sum + current;}, 0), 'sum');

let Avg = makeOperation((...args) =>
                    (args.reduce(function (sum, current) { return sum + current;}, 0)/args.length), 'avg');

const BINOPERATION = {"-": Subtract, "+": Add, "*": Multiply, "/": Divide};
const VARIABLE = {'x': 'x', 'y': 'y', 'z': 'z'};

function ParsingError(message) {
    this.message = message;
}

ParsingError.prototype = Object.create(Error.prototype);

function MissingClosingParenthesisError(position) {
    ParsingError.call(this, "Expect ). Position: " + position);
}

function MissingOpeningParenthesisError() {
    ParsingError.call(this, "There are more ')' than '('");
}

function MissingArgumentError(position) {
    ParsingError.call(this, "Expect argument. Position: " + position);
}

function UnknownSymbolError(position) {
    ParsingError.call(this, "Unknown symbol. Position: " + position);
}

function InvalidNumberError(position) {
    ParsingError.call(this, "Invalid number. Position: " + position);
}

function MissingOperationError(position) {
    ParsingError.call(this, "Missing operation. Position: " + position);
}

function ExcessiveInfoError(position) {
    ParsingError.call(this, "Expect end of expression. Position: " + position);
}

function parsePrefix(string) {
    let ch = 0;
    function parse() {
        skipWhitespace();
        if (string[ch] === '(') {
            ch++;
            let expr = parseOperation();
            skipWhitespace();
            if (string[ch] === ')') {
                ch++;
                return expr;
            }
            throw new MissingClosingParenthesisError(ch);
        } else if (string[ch] === '-') {
            ch++;
            if (isDigit()) {
                return new Const(parseConst('-'));
            }
            throw new InvalidNumberError(ch);
        } else if (isDigit()) {
            return new Const(parseConst(''));
        } else if (string[ch] in VARIABLE) {
            return parseVariable();
        }
        if (endLogicalPart()) {
            throw new MissingArgumentError(ch);
        }
        throw new UnknownSymbolError(ch);
    }

    function parseOperation() {
        skipWhitespace();
        if (string[ch] in BINOPERATION) {
            let op = string[ch++];
            let args = parse2Arg();
            return new BINOPERATION[op](args.a, args.b);
        } else if (string[ch] === 's') {
            ch++;
            check('um');
            return new Sum(...parseArg());
        } else if (string[ch] === 'n') {
            ch++;
            check('egate');
            return new Negate(parse1Arg());
        } else if (string[ch] === 'a') {
            ch++;
            check('vg');
            return new Avg(...parseArg());
        }
        throw new MissingOperationError(ch);
    }

    function isDigit() {
        return '0' <= string[ch] && string[ch] <= '9';
    }

    function startLogicalPart() {
        return string[ch] === ' ' || string[ch] === '(';
    }

    function endLogicalPart() {
        return ch === string.length || string[ch] === ')';
    }

    function parseArg() {
        let args = [];
        skipWhitespace();
        while(!endLogicalPart()) {
            args.push(parse());
            skipWhitespace();
        }
        return args;
    }

    function parse2Arg() {
        return {a: parse1Arg(), b: parse()};
    }

    function parse1Arg() {
        if (startLogicalPart()) {
            return parse();
        }
        if (endLogicalPart())
            throw new MissingArgumentError(ch);
        throw new UnknownSymbolError(ch);
    }

    function parseVariable() {
        let variable = string[ch++];
        if (startLogicalPart() || endLogicalPart()) {
            return new Variable(variable);
        }
        throw new UnknownSymbolError(ch);
    }
    
    function parseConst(sign) {
        while ('0' <= string[ch] && string[ch] <= '9') {
            sign += string[ch++];
        }
        if (startLogicalPart() || endLogicalPart()) {
            return parseInt(sign);
        }
        throw new InvalidNumberError(ch);
    }

    function skipWhitespace() {
        while (string[ch] === ' ') {
            ch++;
        }
    }

    function check(pat) {
        let ind = 0;
        while ((string[ch] === pat[ind]) && !(ch === string.length) && !(ind === pat.length)) {
            if (!(string[ch++] === pat[ind++]))
                throw new UnknownSymbolError(ch);
        }
        if (!(ind === pat.length))
            throw new UnknownSymbolError(ch);
    }

    let result = parse();
    skipWhitespace();
    if (ch === string.length)
        return result;
    if (string[ch] === ')')
        throw new MissingOpeningParenthesisError();
    throw new ExcessiveInfoError(ch);
}