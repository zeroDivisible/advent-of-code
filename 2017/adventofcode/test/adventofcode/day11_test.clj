(ns adventofcode.day11-test
  (:require [adventofcode.day11 :as sut]
            [midje.sweet :as midje]))

(def input (slurp "resources/input_day11.txt"))

(midje/facts "about parse-input"
             (sut/parse-input "s,n") => [:s, :n])

(midje/facts "about task-1"
             (sut/task-1 (sut/parse-input input)) => 773)

(midje/facts "about task-2"
             (sut/task-2 (sut/parse-input input)) => 1560)
