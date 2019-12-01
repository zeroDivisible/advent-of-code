(ns adventofcode.day08
  (:require [adventofcode.core :as core]
            [clojure.string :as string]))

(def task-input (slurp "resources/input_day08.txt"))
(def test-input "b inc 5 if a > 1
a inc 1 if b < 5
c dec -10 if a >= 1
c inc -20 if c == 10")

(defn parse-input
  [input]
  (loop [parsed (->> input
                     core/to-lines
                     core/trim-lines
                     (map #(string/split % #"\s+"))
                     (map #(map string/trim %)))
         records []]
    parsed))

(def d (parse-input task-input))

(defn registers
  [records]
  (zipmap (map first records) (repeat 0)))

(defn true-condition?
  [registers test-reg condition test-amt]
  (eval (read-string (str "(" condition " " (get registers test-reg) " " test-amt ")"))))

(defn update-register
  [registers reg fun amt]
  (assoc registers reg (+ (get registers reg)
                          (if (= fun "inc")
                            amt
                            (- amt)))))

(defn task-1
  [input]
  (loop [r (registers input)
         ops input]
    (cond
      (empty? ops) (reduce max (vals r))
      :else (let [op (first ops)
                  reg (first op)
                  fun (second op)
                  amt (core/number (nth op 2))
                  test-reg (nth op 4)
                  condition (nth op 5)
                  condition (if (= condition "!=")
                              "not="
                              condition)
                  test-amt (core/number (nth op 6))]
              (recur (if (true-condition? r test-reg condition test-amt)
                       (update-register r reg fun amt)
                       r)
                     (rest ops))))))

(update-register {"b" 0 "c" 0} "b" "inc" 10)

;;(task-1 d)
;#(Long/parseLong (nth % 2)) #(nth % 4) #(nth % 5) #(Long/parseLong (nth % 6))]

(defn task-2
  [input]
  (loop [r (registers input)
         ops input
         current-max 0]
    (cond
      (empty? ops) current-max
      :else (let [op (first ops)
                  reg (first op)
                  fun (second op)
                  amt (core/number (nth op 2))
                  test-reg (nth op 4)
                  condition (nth op 5)
                  condition (if (= condition "!=")
                              "not="
                              condition)
                  test-amt (core/number (nth op 6))
                  updated (reduce max (vals (update-register r reg fun amt)))]
              (recur (if (true-condition? r test-reg condition test-amt)
                       (update-register r reg fun amt)
                       r)
                     (rest ops)
                     (if (true-condition? r test-reg condition test-amt)
                       (max updated current-max)
                       current-max))))))


;;(task-2 d)
