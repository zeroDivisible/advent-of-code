(ns adventofcode.day06
  (:require [adventofcode.core :as core]
            [clojure.string :as string]))

(def example-input (slurp "resources/day_06_example.txt"))
(def example-input (slurp "resources/day_06_input.txt"))

(defn parse-input
  "Parses the input string into a collection of coordinates"
  [input]
  (->> input
       string/split-lines
       (mapv #(re-seq #"\d+" %1))
       core/to-longs))

(defn build-map
  "Creates starting map with initial values of points"
  [input]
  (into {} (map #(vector %1 %1) input)))

(defn view-boundries
  "Finds the upper boundries of the view"
  [view]
  (let [expanded-coords (keys view)
        xs (map first expanded-coords)
        ys (map second expanded-coords)]
    [(apply max xs) (apply max ys)]))

(defn expand-point
  "Generates a list of coordinates which are 1 unit away
  from passed point (according to manhattan distance)"
  [point limit]
  (let [[x y] point
        [limit-x limit-y] limit]
    (->> (list [(dec x) y]
               [x (dec y)]
               [(inc x) y]
               [x (inc y)])
         (filter #(and (>= (first %) 0)
                       (>= (second %) 0)))
         (filter #(and (<= (first %) limit-x)
                       (<= (second %) limit-y)))
         (into #{}))))

;; 012345678
;;0...A.....
;;1.....C...
;;2.B......D
;;
;; 012345678
;;0..aAac...
;;1.b.acCc.d
;;2bBb..c.dD
;;
;; 012345678
;;0. aAacc.d
;;1bb acCc d
;;2bBb cc dD
;;
;; 012345678
;;0  aAacc d
;;1bb acCc d
;;2bBb cc dD

(expand-view expanded-once)
(defn update-contesting-expansions
  [points]
  (reduce (fn [coll elem]
            (if (and (contains? coll (key elem))
                     (not= (get coll (key elem))
                           (second elem)))
              (assoc coll (key elem) nil)
              (merge coll elem)
              )) {} points))

(defn expand-view
  "Given a view, expands each of the already existing
  points to neighouring points"
  [view]
  (let [limit (view-boundries view)
        expanded-coords (->> view
                             keys
                             (map #(vector (expand-point %1 limit) (view %1)))
                             (map #(zipmap (first %) (repeat (second %))))
                             (apply concat)
                             (filter #(not (contains? view (key %)))))
        with-empty-spaces (update-contesting-expansions expanded-coords)]
    (merge view with-empty-spaces)))

(defn part-1
  [input]
    (loop [view (expand-view (build-map (parse-input input)))
           updated-view (expand-view view)]
      (cond
        (= view updated-view) (answer-1 updated-view)
        :else (recur updated-view (expand-view updated-view)))))

(defn answer-1
  [view]
  (let [[limit-x limit-y] (view-boundries view)
        boundry-coords (filter #(or (= 0 (first %))
                                    (= 0 (second %))
                                    (= limit-x (first %))
                                    (= limit-y (second %))) (keys view))
        value-at-boundries (into #{} (vals (select-keys view boundry-coords)))
        values (frequencies (filter #(not (contains? value-at-boundries %)) (vals view)))
        ]
    (apply max values)))

(def dd (part-1 example-input))

;; part 2
(defn manhattan
  [a b]
  (let [[xa ya] a
        [xb yb] b]
    (+ (Math/abs (- xa xb))
       (Math/abs (- ya yb)))))

(defn max-coords
  [input]
  [(apply max (map first input)) (apply max (map second input))])

(max-coords (parse-input example-input))

(defn part-2
  [input]
  (let [points (parse-input input)
        [limit-x limit-y] (max-coords points)]
    (for [x (range (inc limit-x))
          y (range (inc limit-y))
          :let [v (map #(manhattan [x y] %) points)]]
      v)))

;; part-2 answer
;; (count (filter #(<= % 10000) (map #(reduce + %) (part-2 example-input))))
;;
;; (37318)
