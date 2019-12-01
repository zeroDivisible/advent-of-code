(ns adventofcode.day05
  (:require [adventofcode.core :as core]
            [clojure.string :as string]))

(def example-input (first (string/split-lines (slurp "resources/day_05_input.txt"))))

(defn reacts?
  [a b]
  (and a b
       (= (string/upper-case a)
          (string/upper-case b))
       (not= a b)))

(defn polymer-reaction
  [polymer]
  (reduce #(if (reacts? %2 (peek %1))
               (pop %1)
               (conj %1 %2))
          [] polymer))

(defn part-1 [input]
  (count (polymer-reaction input)))

;; answer to part 1
;; (part-1 example-input)

;; part 2
(defn find-types
  "Given a polymer, find all the types in it"
  [polymer]
  (into #{} (map string/lower-case (into [] polymer))))

(defn generate-polymers
  [input]
  (let [types (find-types input)]
    (map #(string/replace input
                          (re-pattern (str "(?i)"
                                           %1))
                          "") types)))

(defn part-2
  [input]
  (let [input (string/join (polymer-reaction input))
        polymers (generate-polymers input)]
    (apply min (map #(count (polymer-reaction %1)) polymers))))

;; answer to part-2
;; (part-2 example-input)
