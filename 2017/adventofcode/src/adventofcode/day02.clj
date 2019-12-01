(ns adventofcode.day02
  (:require [adventofcode.core :as core]
            [clojure.string :as string]))

(defn number [x]
  (Long/parseLong x))

(defn split-numbers [x]
  (map #(string/split %1 #"\s+") x))

(defn to-numbers [x]
  (map #(map number %1) x))

(defn lines-as-numbers [input]
  (->> input
       core/to-lines
       core/trim-lines
       split-numbers
       (into [])
       to-numbers))

(defn task-1 [x]
  (let [lines (lines-as-numbers x)
        differences (map #(- (apply max %1)
                             (apply min %1)) lines)]
    (reduce +' differences)))

(defn only-divisible [item]
  (for [x item
        y item
        :when (and (integer? (/ x y))
                   (not= x y))]
    [x y]))

(defn task-2 [x]
  (let [lines (->> x
                   lines-as-numbers
                   (map only-divisible)
                   (map flatten)
                   (map #(reduce / %)))]
    (reduce +' lines)))
