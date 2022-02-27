(ns ch5
  (:require [clojure.string :as str]))


;; 1. You used (comp :intelligence :attributes) to create a function that returns a character’s intelligence. 
;; Create a new function, attr, that you can call like (attr :intelligence) and that does the same thing. 
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(defn attr
  [attribute character]
  (get-in character [:attributes attribute]))

(attr :intelligence character)

;; 2. Implement the comp function. 

(defn my-comp
  ([f] (identity f))
  ([f g]
   (fn [& args]
     (f (apply g args))))
  ([f g & rest-functions]
   (fn [& args]
     (apply (my-comp f (apply my-comp g rest-functions)) args)
     )))

((my-comp #(str % "1")) "hi")

((my-comp #(str % "2") #(str % "1")) "hi")

((my-comp #(str % "3") #(str % "2") #(str % "1")) "hi")

((my-comp #(str % "4") #(str % "3") #(str % "2") #(str % "1")) "hi")

;; 3. Implement the assoc-in function. Hint: use the assoc function and define its parameters as [m [k & ks] v]. 

(defn my-assoc-in
  [m [k & ks] v]
  (if ks
    (assoc m k (my-assoc-in (get m k {}) (into [] ks) v))
    (assoc m k v)))

(my-assoc-in {} [:cookie :monster :vocals] "Finntroll")
(def cookie-monster (my-assoc-in {} [:cookie :monster :vocals] "Finntroll"))
(my-assoc-in cookie-monster [:cookie :monster :idk] "something-else")
(assoc-in cookie-monster [:cookie :monster :idk] "something-else")

;; 4. Look up and use the update-in function. 

(update-in cookie-monster [:cookie :monster :vocals] #(str % " updated with extra args: " (str/join ", " %&)) "extra arg 1" "extra arg 2")

;; 5. Implement update-in.

(defn my-update-in
  [m [k & ks] f & args]
  (if ks
    (assoc m k (apply my-update-in (get m k {}) (into [] ks) f args))
    (assoc m k (apply f (k m) args))))

(my-update-in cookie-monster [:cookie :monster :vocals] #(str % " updated with extra args: " (str/join ", " %&)) "extra arg 1" "extra arg 2")