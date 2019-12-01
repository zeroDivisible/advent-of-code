(ns adventofcode.day12
  (:require [clojure.string :as string]))

(def input (slurp "resources/input_day12.txt"))
(def example-input "0 <-> 2
  1 <-> 1
  2 <-> 0, 3, 4
  3 <-> 2, 4
  4 <-> 2, 3, 6
  5 <-> 6
  6 <-> 4, 5
  ")

(defn parse-input
  [input]
  (let [input (->> input
                   string/trim
                   string/split-lines
                   (map string/trim)
                   (map #(string/split % #" <-> ")))]
    (loop [input input
           rules {}]
      (cond
        (empty? input) rules
        :else (let [rule (first input)
                    program (first rule)
                    connected-to (->> (second rule)
                                      (#(string/split % #", "))
                                      (into #{}))]
                (recur (rest input)
                       (assoc rules program connected-to)))))))

(defn connected-to
  [program rules]
  (loop [connected #{program}
         to-check (rules program)
         checked #{program}
         ]
    (cond
      (empty? to-check) connected
      :else (let [rule (first to-check)
                  next-rule (rules rule)
                  checked (conj checked rule)]
              (recur (conj connected rule)
                     (if (nil? next-rule)
                       (rest to-check)
                       (into #{} (remove #(checked %) (into (set (rest to-check)) next-rule))))
                     checked)))))

;(def r (parse-input input))
;(count (into #{} (map #(connected-to % r) (keys r)))) ;; 189 is the answer to task 2
;(count (connected-to "0" r)) ;; 130 is the answer to task 1
