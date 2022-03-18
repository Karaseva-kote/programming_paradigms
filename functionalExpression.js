"use strict";

const cnst = value => (x, y, z) => value;
const pi = (x, y, z) => Math.PI;
const e = (x, y, z) => Math.E;
const variable = q => (x, y, z) => (q === "x") ? x : (q === "y") ? y : z;
const parse = input => input.trim() === "x" ? variable("x") : cnst(+input);

let binOp = (c) => (a, b) => (x, y, z) => c(a(x, y, z), b(x, y, z));
let unarOp = (c) => (a) => (x, y, z) => c(a(x, y, z));
let subtract = binOp((a, b) => a - b);
let add = binOp((a, b) => a + b);
let multiply = binOp((a, b) => a * b);
let divide = binOp((a, b) => a / b);
let negate = unarOp((a) => -a);
let sin = unarOp((a) => Math.sin(a));
let cos = unarOp((a) => Math.cos(a));
