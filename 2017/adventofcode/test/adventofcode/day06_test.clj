(ns adventofcode.day06-test
  (:require [adventofcode.day06 :as sut]
            [midje.sweet :as midje]))

(def input (slurp "resources/input_day06.txt"))

(midje/facts "about task-1"
             (sut/task-1 (sut/parse-input input)) => 12841)

(midje/facts "about task-2"
             (sut/task-2 (sut/parse-input input)) => 8038)
