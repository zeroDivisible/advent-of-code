(ns adventofcode.day01
  (:require [adventofcode.core :as core]))

(defn append-first-item [x]
  (let [digits (core/to-digits x)]
    (conj digits (first digits))))

(defn every-pair [x]
  (partition 2 1 x))

(defn only-matching-pairs [x]
  (filter #(= (first %)
              (second %))
          x))

(defn task-1 [x]
  (let [matching-digits (->> x
                             append-first-item
                             every-pair
                             only-matching-pairs
                             (map first))]
    (reduce +' matching-digits)))

(defn task-2 [x]
  (let [digits (core/to-digits x)
        length (count digits)
        step   (/ length 2)]
    (reduce +' (keep-indexed #(if (= (nth digits (mod (+ step %1) length))
                                    %2)
                               %2)
                            digits))))
