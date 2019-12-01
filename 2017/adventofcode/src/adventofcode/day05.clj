(ns adventofcode.day05
  (:require [adventofcode.core :as core]
            [clojure.string :as string]))

;; problem https://adventofcode.com/2017/day/5
;; task 1
(defn parse-input
  [input]
  (into [] (->> input
                core/to-lines
                core/trim-lines
                (map core/number))))

(defn jump [instruction-pointer instructions]
  (let [new-pointer (instructions instruction-pointer)]
    [(+ new-pointer instruction-pointer)
     (assoc instructions instruction-pointer (inc new-pointer))]))

(defn task-1 [instructions]
  (let [instructions-length (count instructions)]
    (loop [steps 0
           pointer 0
           instructions instructions]
      (cond
        (>= (int pointer) (int instructions-length)) steps
        :else (recur (inc steps)
                     (first (jump pointer instructions))
                     (second (jump pointer instructions)))))))

;; task 2
(defn jump-2 [^long instruction-pointer instructions]
  (let [^long new-pointer (instructions instruction-pointer)
        new-offset (+ new-pointer instruction-pointer)]
    [new-offset (assoc! instructions
                       instruction-pointer
                       (if (>= new-pointer 3)
                         (dec new-pointer)
                         (inc new-pointer)))]))

(defn task-2
  [instructions]
  (let [instructions-length (count instructions)]
    (loop [steps 0
           pointer 0
           instructions (transient instructions)]
      (if (>= (int pointer) (int instructions-length))
        steps
        (let [[new-pointer new-instructions] (jump-2 pointer instructions)]
          (recur (long (inc steps))
                 new-pointer
                 new-instructions))))))
