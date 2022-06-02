(ns ch10
  (:require [clojure.string :as str]))

;; 1. Create an atom with the initial value 0, use swap! to increment it a couple of times, and then dereference it.

(def counter (atom 0))
(swap! counter inc)
(swap! counter inc)
(print @counter)

;; 2. Create a function that uses futures to parallelize the task of downloading random quotes from
;; http://www.braveclojure.com/random-quote using (slurp "http://www.braveclojure.com/random-quote"). 
;; The futures should update an atom that refers to a total word count for all quotes. 
;; The function will take the number of quotes to download as an argument and return the atom’s final value. 
;; Keep in mind that you’ll need to ensure that all futures have finished before returning the atom’s final value. 

(defn get-quote-word-count [_] (count (str/split (slurp "http://138.201.159.94:8090/quote") #" ")))

;; parallelizes downloading quotes without using an atom...seems like the simplier solution to me
(defn quote-word-count-no-atom
  [n]
  (reduce + (pmap get-quote-word-count (take n (range)))))

(quote-word-count-no-atom 10)



;; actually following directions and using an atom

(defn get-word-count [] (count (str/split (slurp "http://138.201.159.94:8090/quote") #" ")))

(defn quote-word-count
  [n]
  (let [total-word-count (atom 0)]
    (dorun (pmap (fn [_] (swap! total-word-count (fn [curr-count] (+ curr-count (get-word-count))))) (take n (range))))
    @total-word-count))

(quote-word-count 10)

;;alternative
(defn quote-word-count
  [n]
  (let [total-word-count (atom 0)]
    (dorun (map deref (repeatedly n (fn [] (future (swap! total-word-count (fn [curr-count] (+ curr-count (get-word-count)))))))))
    @total-word-count))

;; 3. Create representations of two characters in a game. 
;; The first character has 15 hit points out of a total of 40. 
;; The second character has a healing potion in his inventory. 
;; Use refs and transactions to model the consumption of the healing potion and the first character healing.

(def p1 (ref  {:hit-points 15}))
(def p2 (ref {:healing-potion true}))


(defn heal-other-player [healer receiver]
  (if (healer :healing-potion)
    (dosync
     (alter receiver assoc :hit-points 40)
     (alter healer assoc :healing-potion false))
    (throw (IllegalStateException. "healer must possess healing potion to heal other player"))))


(heal-other-player p2 p1)
(print p1)
(print p2)
