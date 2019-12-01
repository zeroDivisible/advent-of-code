(ns adventofcode.day07
  (:require [adventofcode.core :as core]
            [clojure.string :as string]))

;; needs cleaning

(defn parse-input
  [input]
  (let [input (->> input
                   core/to-lines
                   (map #(string/split % #"\s+->\s+"))
                   (map #(map string/trim %)))
        keys (->> input
                  (map first)
                  (map #(string/split % #"\s+")))
        key-names (map first keys)
        key-weights (->> keys
                         (map second)
                         (map #(re-find #"\d+" %))
                         (map #(Long/parseLong %)))
        vals (->> input
                  (map second)
                  (map #(if (nil? %)
                          []
                          (string/split % #",\s+"))))]
    [(zipmap key-names key-weights)
     (zipmap key-names vals)]))

(defn starting-points
  [k]
  (let [dependencies (flatten (vals k))]
    (loop [remaining-keys (keys k)
           points []]
      (cond
        (empty? remaining-keys) points
        (some #{(first remaining-keys)} dependencies) (recur (rest remaining-keys)
                                                             points)
        :else (recur (rest remaining-keys)
                     (conj points (first remaining-keys)))))))

;; task 1
;; very easy, we just need to find the root
(defn task-1
  [input]
  (first (starting-points (second (parse-input input)))))

(defn all-children
  [node dependencies]
  (loop [children []
         candidates (dependencies node)]
    (cond
      (empty? candidates) children
      :else (recur (conj children (first candidates))
                   (into (rest candidates) (dependencies (first candidates)))))))

(def example-input "pbga (66)
  xhth (57)
  ebii (61)
  havc (66)
  ktlj (57)
  fwft (72) -> ktlj, cntj, xhth
  qoyq (66)
  padx (45) -> pbga, havc, qoyq
  tknk (41) -> ugml, padx, fwft
  jptl (61)
  ugml (68) -> gyxo, ebii, jptl
  gyxo (61)
  cntj (57)")

(def input (parse-input example-input))

;; task 2 - now we really need to build the graph
(defn weight-with-children
  [weights graph]
  (let [starting (starting-points graph)
        nodes (apply disj (set (keys graph)) starting)]
    (zipmap nodes (apply map #(weights %) (map #(all-children % graph) nodes)))))
  ;(reduce + (map #(weights %) (conj (all-children node graph) node))))

(defn node-weights
  [weights graph]
  (loop [found-weights {}
         node (first (keys graph))
         remaining (rest (keys graph))]
    (cond
      (nil? node) found-weights
      :else (recur (assoc found-weights node (reduce + (map #(weights %) (conj (all-children node graph) node))))
                   (first remaining)
                   (rest remaining)))))

(defn children-weight
  [weights graph]
  (loop [children (starting-points graph)
         children-weights (map weights children)]
    (cond
      (= (count (set children-weights))
         (count children-weights)) (recur (first children)
                                          (conj (rest children) (graph (first children)))
                                          ))))

;;(weight-with-children (first graph) (second graph))
;;(map #(weight-with-children (first graph) (second graph)) (keys (second graph)))
;;(def test-input (slurp "resources/input_day07.txt"))
;;(def graph (parse-input test-input))
;;(def kids ((second graph) (first (starting-points (second graph)))))
;;(def w (node-weights (first graph) (second graph)))

(defn test-2
  [w graph]
  (loop [start (first (starting-points graph))
         kids (get graph start)
         weights (select-keys w kids)]
    (cond
      (= (count (frequencies (vals weights))) 1) [start kids]
      :else (let [fqs (frequencies (vals weights))
                  m (reduce min (vals fqs))
                  k (first (flatten (filter #(= (second %) m) fqs)))
                  f (first (flatten (filter #(= (second %) k) weights)))]
              (recur f
                     (get graph f)
                     (select-keys w (get graph f)))
              ))))

;; the answer
;;((first graph) "drjmjug") ; 420
