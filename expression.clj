(defn constant [value] (fn [_] value))
(defn variable [v] (fn [args] (get args v)))
(defn operation [f] (fn [& operands] (fn [args] (apply f (mapv (fn [operand] (operand args)) operands)))))
(def min (operation clojure.core/min))
(def max (operation clojure.core/max))
(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation (fn [x y] (/ (double x) (double y)))))
(def negate (operation -))
(def med (operation (fn [& x] (nth (sort x) (/ (count x) 2)))))
(def avg (operation (fn [& x] (/ (apply + x) (count x)))))

(def operations
  {'+      add,
   '-      subtract,
   '*      multiply,
   '/      divide,
   'negate negate,
   'min    min,
   'max    max,
   'med    med,
   'avg    avg}
  )

(defn parseFunction [string]
      (letfn [
              (parse [expression]
                     (cond
                       (number? expression) (constant expression)
                       (symbol? expression) (variable (str expression))
                       (seq? expression) (apply (get operations (first expression)) (map parse (rest expression)))
                       )
                     )
              ]
             (parse (read-string string))
             )
      )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn proto-get [obj key]
      (cond
        (contains? obj key) (obj key)
        (contains? obj :prototype) (proto-get (obj :prototype) key)
        ))
(defn proto-call [this key & args]
      (apply (proto-get this key) this args))
(defn field [key]
      (fn [this] (proto-get this key)))
(defn method [key]
      (fn [this & args] (apply proto-call this key args)))
(defn constructor [ctor prototype]
      (fn [& args] (apply ctor {:prototype prototype} args)))

(def _value (field :value))
(def _variable (field :variable))
(def _args (field :args))
(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))
(def toStringSuffix (method :toStringSuffix))

(def Constant)
(def ConstProto
  {
   :evaluate       (fn [this args] (_value this))
   :toString       (fn [this] (format "%.1f" (_value this)))
   :diff           (fn [this var] (Constant 0))
   :toStringSuffix (fn [this] (format "%.1f" (_value this)))
   })
(defn constrConstant [this val]
      (assoc this :value val))
(def Constant (constructor constrConstant ConstProto))

(def Variable)
(def VarProto
  {
   :evaluate       (fn [this args] (get args (_variable this)))
   :toString       (fn [this] (str (_variable this)))
   :diff           (fn [this var] (if (= (_variable this) var) (Constant 1) (Constant 0)))
   :toStringSuffix (fn [this] (str (_variable this)))
   })
(defn constrVariable [this var]
      (assoc this :variable var))
(def Variable (constructor constrVariable VarProto))

(def _function (field :function))
(def _operands (field :operands))
(def _symbol (field :symbol))
(def _howToDiff (method :howToDiff))

(def Operation)
(def OperationProto
  {
   :evaluate       (fn [this args] (apply (_function this) (mapv (fn [operand] (evaluate operand args)) (_operands this))))
   :toString       (fn [this] (str "(" (_symbol this) " " (clojure.string/join " " (mapv (fn [operand] (toString operand)) (_operands this))) ")"))
   :diff           (fn [this var] (_howToDiff this var))
   :toStringSuffix (fn [this] (str "(" (clojure.string/join " " (mapv (fn [operand] (toStringSuffix operand)) (_operands this))) " " (_symbol this) ")"))
   })
(defn constrOperation [this operands function symbol diff]
      (assoc this :operands operands :function function :symbol symbol :howToDiff diff))
(def Operation (constructor constrOperation OperationProto))

(defn Add [& operands] (Operation operands + '+
                                  (fn [this var] (apply Add (mapv (fn [operand] (diff operand var)) (_operands this))))))
(defn Subtract [& operands] (Operation operands - '-
                                       (fn [this var] (apply Subtract (mapv (fn [operand] (diff operand var)) (_operands this))))))
(defn Multiply [& operands] (Operation operands * '*
                                       (fn [this var] (Add (Multiply (diff (nth (_operands this) 0) var) (nth (_operands this) 1))
                                                           (Multiply (diff (nth (_operands this) 1) var) (nth (_operands this) 0))))))
(defn Divide [& operands] (Operation operands (fn [x y] (/ x (double y))) '/
                                     (fn [this var] (Divide (Subtract (Multiply (diff (nth (_operands this) 0) var) (nth (_operands this) 1))
                                                                      (Multiply (diff (nth (_operands this) 1) var) (nth (_operands this) 0)))
                                                            (Multiply (nth (_operands this) 1) (nth (_operands this) 1))))))
(defn Negate [& operands] (Operation operands - 'negate (fn [this var] (Negate (diff (nth (_operands this) 0) var)))))
(defn Lg [& operands] (Operation operands (fn [a x] (/ (Math/log (Math/abs x)) (Math/log (Math/abs a)))) 'lg
                                 (fn [this var] (Divide (Subtract (Divide (Multiply (diff (nth (_operands this) 1) var) (Lg (Constant Math/E) (nth (_operands this) 0))) (nth (_operands this) 1))
                                                                  (Divide (Multiply (diff (nth (_operands this) 0) var) (Lg (Constant Math/E) (nth (_operands this) 1))) (nth (_operands this) 0)))
                                                        (Multiply (Lg (Constant Math/E) (nth (_operands this) 0)) (Lg (Constant Math/E) (nth (_operands this) 0)))))))
(defn Pw [& operands] (Operation operands (fn [a x] (Math/pow a x)) 'pw
                                 (fn [this var] (Multiply this (Add (Multiply (diff (nth (_operands this) 1) var) (Lg (Constant Math/E) (nth (_operands this) 0)))
                                                                    (Multiply (nth (_operands this) 1) (Divide (diff (nth (_operands this) 0) var) (nth (_operands this) 0))))))))
(defn And [& operands] (Operation operands #(Double/longBitsToDouble (bit-and (Double/doubleToLongBits %1) (Double/doubleToLongBits %2))) '& (fn [this var] (Constant 0))))
(defn Or [& operands] (Operation operands #(Double/longBitsToDouble (bit-or (Double/doubleToLongBits %1) (Double/doubleToLongBits %2))) '| (fn [this var] (Constant 0))))
(defn Xor [& operands] (Operation operands #(Double/longBitsToDouble (bit-xor (Double/doubleToLongBits %1) (Double/doubleToLongBits %2))) (symbol "^") (fn [this var] (Constant 0))))

(def operationsObj
  {'+           Add,
   '-           Subtract,
   '*           Multiply,
   '/           Divide,
   'negate      Negate,
   'pw          Pw,
   'lg          Lg
   '&           And,
   '|           Or,
   (symbol "^") Xor}
  )

(defn parse [expression]
      (cond
        (number? expression) (Constant expression)
        (symbol? expression) (Variable (str expression))
        (seq? expression) (apply (get operationsObj (first expression)) (map parse (rest expression)))
        )
      )

(defn parseObject [string] (parse (read-string string)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;start Korneev's part;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _show [result]
      (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                           "!"))
(defn tabulate [parser inputs]
      (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))
(defn _empty [value] (partial -return value))
(defn _char [p]
      (fn [[c & cs]]
          (if (and c (p c)) (-return c cs))))
(defn _map [f result]
      (if (-valid? result)
        (-return (f (-value result)) (-tail result))))
(defn _combine [f a b]
      (fn [str]
          (let [ar ((force a) str)]
               (if (-valid? ar)
                 (_map (partial f (-value ar))
                       ((force b) (-tail ar)))))))
(defn _either [a b]
      (fn [str]
          (let [ar ((force a) str)]
               (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
      (fn [input]
          (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value]
      (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
      (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn +or [p & ps]
      (reduce _either p ps))
(defn +opt [p]
      (+or p (_empty nil)))
(defn +star [p]
      (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;end Korneev's part;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn +symbol [p] (+map (partial apply symbol) p))
(def *digit (+char "0123456789."))
(def *all-chars (mapv char (range 0 128)))
(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
(def *ws (+ignore (+star *space)))
(defn *symbol [p] (+symbol (+seq (+str p))))
(defn *seq [begin arg sign end]
      (+seqn 1 (+char begin) (+seqf conj (+plus (+seqn 0 *ws arg *ws)) sign) *ws (+char end)))

(def read-suffix
  (let [*number (+map read-string (+str (+seqf cons (+opt (+char "-")) (+plus *digit))))
        *variable (*symbol (+seq (+char "xyz")))
        *sign (*symbol (+seq (+char "+-*/")))
        *negate (*symbol (+seq (+char "n") (+char "e") (+char "g") (+char "a") (+char "t") (+char "e")))
        *And (*symbol (+seq (+char "&")))
        *Or (*symbol (+seq (+char "|")))
        *Xor (*symbol (+seq (+char "^")))]
       (letfn [(*operation [] (*seq "(" (delay (*value)) (+or *negate *And *Or *Xor *sign) ")"))
               (*value [] (+or *number *variable (*operation)))]
              (+parser (+seqn 0 *ws (*value) *ws))
              )
       )
  )

(defn parseObjectSuffix [string] (parse (read-suffix string)))
