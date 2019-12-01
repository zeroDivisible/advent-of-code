(ns adventofcode.day13
  (:require [clojure.string :as string]
            [adventofcode.core :as core]))

(def input (slurp "resources/input_day13.txt"))

(def example-input "0: 3
  1: 2
  4: 4
  6: 4")

(defn to-number [x] (Long/parseLong x))

(defn parse-input
  [input]
  (apply hash-map (->> input
                       string/trim
                       string/split-lines
                       (map string/trim)
                       (map #(string/split % #": "))
                       (mapv #(mapv to-number %))
                       flatten)))

(def d (parse-input input))

(defn create-cycles
  [input]
  (into {}
        (mapv (fn [[x y]]
                [x
                 (cycle (concat (range y)
                         (range (- y 2) 0 -1)))]) input)))

(defn move-cycle
  [cycles]
  (into {}
        (mapv (fn [[k c]]
                [k
                 (rest c)])
              cycles)))

(defn move-through-firewall
  [firewall cycles]
  (let [final-layer (apply max (keys firewall))]
    (loop [current-position 0
           caught-at #{}
           cycles cycles]
      (cond
        (> current-position final-layer) ;(reduce + (map #(* %
                                         ;                  (firewall %))
                                         ;              caught-at))
                                         (empty? caught-at)


        (not (empty? caught-at)) false
        (nil? (cycles current-position)) (recur (inc current-position)
                                                  caught-at
                                                  (move-cycle cycles))
        :else (recur (inc current-position)
                     (if (zero? (first (cycles current-position)))
                       (conj caught-at current-position)
                       caught-at)
                     (move-cycle cycles))))))

;; (move-through-firewall d) - answer is 1960
(defn how-much-delay
  [firewall]
  (loop [delay 0
         cycles (create-cycles firewall)]
    (cond
      (true? (move-through-firewall firewall cycles)) delay
      :else (recur (inc delay)
                   (move-cycle cycles)))))

;; (prn (how-much-delay d)) => 3903378 is the answer
