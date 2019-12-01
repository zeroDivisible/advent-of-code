(ns adventofcode.day04
  (:require [adventofcode.core :as core]
            [clojure.string :as string]))

(defn split-words [x]
  (string/split x #"\s+"))

(defn line-as-words [x]
  (->> x
       split-words
       (map string/trim)
       flatten))

(defn valid-passphrase?
  [words]
  (= (count words)
     (count (distinct words))))

(defn task-1
  "Given input from the file, how many passphrases are valid?"
  [input]
  (let [phrases (->> input
                     core/to-lines
                     (map line-as-words)
                     (map valid-passphrase?))]
    (count (filter true? phrases))))

;; task 2
(defn task-2
  "Given input from the file, how many passphrases are valid?"
  [input]
  (let [phrases (->> input
                     core/to-lines
                     (map line-as-words)
                     (map #(map sort %))
                     (map valid-passphrase?))]
    (count (filter true? phrases))))
