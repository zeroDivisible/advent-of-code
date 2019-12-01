(ns adventofcode.day06
  (:require [adventofcode.core :as core]))

(def challenge-bank-layout (slurp "resources/input_day06.txt"))

(defn parse-input
  [input]
  (->> input
       core/to-lines
       core/split-numbers
       core/to-numbers
       flatten
       (into [])))

(defn fullest-bank
  [memory-banks]
  (let [highest-value (apply max memory-banks)]
    (first (keep-indexed #(if (= highest-value %2) %1) memory-banks))))

(defn redistribute
  [bank-no banks]
  (loop [to-redistribute (get banks bank-no)
         banks (assoc banks bank-no 0)
         next-bank (mod (inc bank-no)
                        (count banks))]
    (cond
      (zero? to-redistribute) banks
      :else (recur (dec to-redistribute)
                   (assoc banks next-bank (inc (get banks next-bank)))
                   (mod (inc next-bank)
                        (count banks))))))

(defn task-1
  [banks]
  (loop [seen-banks #{}
         banks banks]
    (let [fullest (fullest-bank banks)
          redistributed (redistribute fullest banks)]
      (cond
        (contains? seen-banks redistributed) (inc (count seen-banks))
        :else (recur (conj seen-banks redistributed)
                     redistributed)))))

;; TODO: this is really slow
(defn task-2
  [banks]
  (loop [counter 0
         seen-banks {}
         banks banks]
    (let [fullest (fullest-bank banks)
          redistributed (redistribute fullest banks)]
      (cond
        (contains? (set (keys seen-banks)) redistributed) (- counter (get seen-banks redistributed))
        :else (recur (inc counter)
                     (assoc seen-banks redistributed counter)
                     redistributed)))))
