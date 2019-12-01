(ns adventofcode.core
  (:require [clojure.string :as string]))

(defn to-digits
  "Given a number, return a vector with it's digits"
  [n]
  (->> n
       str
       (map str)
       (map #(Integer/parseInt %))
       (into [])))

(defn to-lines [x]
  (string/split-lines x))

(defn trim-lines [x]
  (map string/trim x))

(defn split-numbers [x]
  (map #(string/split %1 #"\s+") x))

(defn split-numbers [x]
  (map #(string/split %1 #"\s+") x))

(defn to-long
  [input]
  (Long/parseLong input))

(defn to-longs [x]
  (mapv #(mapv to-long %1) x))
