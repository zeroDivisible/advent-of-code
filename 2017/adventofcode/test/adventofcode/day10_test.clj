(ns adventofcode.day10-test
  (:require [adventofcode.day10 :as sut]
            [midje.sweet :as midje]))

(def input (slurp "resources/input_day10.txt"))

(midje/facts "about parse-input"
             (sut/parse-input "1,22,3") => [1, 22, 3])

(midje/facts "about task-1"
             (sut/task-1 input) => 29240)

(midje/facts "about task-2"
             (sut/task-2 input) => "4db3799145278dc9f73dcdbc680bd53d")
