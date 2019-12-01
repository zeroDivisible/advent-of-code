(ns adventofcode.day11
  (:require [clojure.string :as string]))

(defn parse-input
  [input]
  (->> input
       string/trim
       (#(string/split % #","))
       (map keyword)))

(defn move-hex
  [point direction]
    (case direction
      :ne [(inc (point 0)) (dec (point 1))]
      :se [(inc (point 0)) (point 1)]
      :s  [(point 0) (inc (point 1))]
      :sw [(dec (point 0)) (inc (point 1))]
      :nw [(dec (point 0)) (point 1)]
      :n  [(point 0) (dec (point 1))]))

(defn distance-from-origin
  [point]
  (Math/max (Math/abs (get point 0)) (Math/abs (get point 1))))

(defn traverse-grid-from-origin
  [directions]
  (loop [point        [0 0]
         furthest-distance 0
         directions   directions]
    (cond
      (empty? directions) [(distance-from-origin point)
                           furthest-distance]
      :else               (let [new-point        (move-hex point (first directions))
                                current-distance (distance-from-origin new-point)]
                            (recur new-point
                                   (if (>= current-distance furthest-distance)
                                     current-distance
                                     furthest-distance)
                                   (rest directions))))))

(defn task-1
  [input]
  (first (traverse-grid-from-origin input)))

(defn task-2
  [input]
  (second (traverse-grid-from-origin input)))
