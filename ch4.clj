(ns fwpd.core
  (:require [clojure.string :as str]))
(def filename "suspects.csv")
(slurp filename)

(def vamp-keys [:name :glitter-index])

(defn str->int [str] (Integer. str) )

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse 
  "Convert a CSV into rows of columns"
  [csv]
  (map #(str/split % #",") ;; split rows into values
  (str/split csv #"\n")));; split csv into rows

(parse (slurp filename))
(def file (slurp filename))

(str/split (slurp filename) #"\n")

(defn mapify 
  "returns a seq of maps like {:name 'Edward Cullen' :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row] 
         (reduce (fn [row-map [vamp-key value]] ;; arg ex: ([:name :gllitter-index] ["Edward Cullen" "10"])
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows)
  )

(mapify (parse (slurp filename)))

(defn glitter-filter 
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(glitter-filter 3 (mapify (parse (slurp filename))))

;; ex1

(def suspects (glitter-filter 3 (mapify (parse (slurp filename)))))

(map :name suspects)

;; ex2


(defn append [suspects new-suspect] (conj suspects new-suspect))
(append suspects {:name "isabel" :glitter-index 10})

;; ex3 

(defn validate 
  [validators record] 
  (every?
   (fn [[key validator]] 
     (validator (key record)))
   validators))

(validate {:name string? :glitter-index number?} {:name "isabel" :glitter-index 10})

;; ex4

(def list-of-maps (mapify (parse (slurp filename))))

(defn make-csv 
  "given a list of maps, creates csv string"
  [list-of-maps]
  (reduce (fn [res-string map-item]
            (str res-string (str/join "," (vals map-item)) "\n"))
          ""
          list-of-maps))

(make-csv list-of-maps)