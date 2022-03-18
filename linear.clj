(defn everyVector [data] (every? vector? data))
(defn everyNumber [data] (every? number? data))
(defn checkVectors [data] (every? everyNumber data))
(defn checkMatrix [data] (every? everyVector data))
(defn checkSize [data] (and (> (count data) 0) (apply = (mapv count data))))

(defn op [f] (fn [& vectors]
                 {:pre [(everyVector vectors)
                        (checkSize vectors)
                        (or (checkVectors vectors) (checkMatrix vectors))]}
                 (apply mapv f vectors)))
(def v+ (op +))
(def v- (op -))
(def v* (op *))
(defn scalar [v1 v2] (reduce + (v* v1 v2)))
(defn getCoordinate [v1 v2 a b] (- (* (get v1 a) (get v2 b)) (* (get v1 b) (get v2 a))))
(defn BinVect [v1 v2] [(getCoordinate v1 v2 1 2) (getCoordinate v1 v2 2 0) (getCoordinate v1 v2 0 1)])
(defn vect [& vectors]
      {:pre [(everyVector vectors)
             (apply = 3 (mapv count vectors))
             (checkVectors vectors)
             (checkSize vectors)]
       }
      (reduce BinVect vectors))
(defn v*s [v & s]
      {:pre [(vector? v)
             (checkVectors [v])
             (everyNumber s)]
       }
      ((op (apply partial * s)) v))

(def m+ (op v+))
(def m- (op v-))
(def m* (op v*))
(defn m*s [m & s]
      {:pre [(everyNumber s)]}
      ((op (fn [v] (apply v*s v s))) m))
(defn m*v [m v] ((op (partial scalar v)) m))
(defn transpose [m]
      {:pre [(everyVector m)
             (checkMatrix [m])]}
      (apply mapv vector m))
(defn BinMul [m1 m2] (transpose ((op (partial m*v m1)) (transpose m2))))
(defn m*m [& m] (reduce BinMul m))

(defn opShapeless [f] (fn [& vectors]
                          {:pre [(or (everyVector vectors) (everyNumber vectors))]}
                          (reduce (fn [v1 v2] (if (not(vector? v1)) (f v1 v2) (mapv (opShapeless f) v1 v2))) vectors)))
(def s+ (opShapeless +))
(def s- (opShapeless -))
(def s* (opShapeless *))
