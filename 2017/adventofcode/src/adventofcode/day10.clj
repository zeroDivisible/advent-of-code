(ns adventofcode.day10
  (:require [clojure.string :as string]
            [adventofcode.core :as core]))

(defn parse-input
  [input]
  (->> input
       string/trim
       (#(string/split % #","))
       (map #(Long/parseLong %))
       (into [])))

(defn hash-round
  [input lengths skip-size current-pos]
  (loop [input       input
         lengths     lengths
         skip-size   skip-size
         current-pos current-pos]
    (cond
      (empty? lengths) [input skip-size current-pos]
      :else            (let [current-length (first lengths)
                             reversed    (reverse (take current-length (drop current-pos (cycle input))))
                             input          (loop [input input
                                                   items reversed
                                                   p     current-pos]
                                              (cond
                                                (empty? items) input
                                                :else          (recur (assoc input p (first items))
                                                                      (rest items)
                                                                      (mod (inc p)
                                                                           (count input)))))]
                         (recur input
                                (rest lengths)
                                (inc skip-size)
                                (mod (+ current-pos current-length skip-size)
                                     (count input)))))))

;; task-1
(defn task-1
  [task-input]
  (let [numbers (into [] (take 256 (range 256)))
        lengths (parse-input task-input)

        ;; we're just doing a single round of hashing here
        [hashed _ _] (hash-round numbers lengths 0 0)]
    (* (hashed 0)
       (hashed 1))))

(defn hash-rounds
  [input lengths]
  (loop [input input
         lengths lengths
         skip-size 0
         current-pos 0
         rounds 64]
    (cond
      (zero? rounds) input
      :else (let [[input skip-size current-pos] (hash-round input lengths skip-size current-pos)]
              (recur input
                     lengths
                     skip-size
                     current-pos
                     (dec rounds))))))

(defn task-2
  [task-input]
  (let [numbers (into [] (take 256 (range 256)))
        lengths (concat (map int (string/trim task-input))
                        [17, 31, 73, 47, 23])
        hashed  (hash-rounds numbers lengths)
        parts   (partition 16 hashed)
        xord    (map #(reduce bit-xor %) parts)
        as-hex (map #(format "%02x" %) xord)]
    (apply str as-hex)))
