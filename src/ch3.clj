(ns ch3
  (:require [clojure.string :as str]))

;; ex 1

(str "hey " "there")
(vector 1 2 3)
(list 4 5 6)
(hash-map :a 1 :b 2)
(hash-set 0 1 0 2 1)


;; ex 2

(defn add-100 [x] (+ x 100))
(add-100 9)

;; ex 3

(defn dec-maker [x] (fn [y] (- y x)))

(def dec9 (dec-maker 9))

(dec9 10)

;; ex 4

(defn mapset [fn v] (set (map fn v)))

(mapset inc [1 1 2 2])

;; ex 5 and 6

(def asym-hobbit-body-parts [
                             {:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])
                             


(defn body-part-multiplier
  [body-part n]
  (map (fn
         [i]
         {:name (str (str/replace (:name body-part) #"^left-" "") (+ i 1))
          :size (:size body-part)})
       (range n)))

(defn symmetrize-radial-body-parts
  "given a seq of body parts (which have a :name and value:) and a multiplier n, 
   returns a seq of transformed body parts where any non-singular body part 
   (in this case, any body part whose :name doesn't start with 'left')
   will be multiplied into n different body parts"
  [asymetric-body-parts n]
  (reduce
   (fn [full-body-parts body-part]
     (into full-body-parts
           (if (str/starts-with? (:name body-part) "left-")
             (body-part-multiplier body-part n)
             [body-part])))
   []
   asymetric-body-parts))


(symmetrize-radial-body-parts asym-hobbit-body-parts 3)