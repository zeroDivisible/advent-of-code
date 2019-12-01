(ns adventofcode.day05-test
  (:require [adventofcode.day05 :as sut]
            [midje.sweet :as midje]))

(def test-input (slurp "resources/input_day05.txt"))
(def example-input "0\n3\n0\n1\n-3")

(midje/facts "about jump"
             (sut/jump 0 [0 3 0 1 -3]) => [0 [1 3 0 1 -3]]
             (sut/jump 0 [1 3 0 1 -3]) => [1 [2 3 0 1 -3]]
             (sut/jump 1 [2 3 0 1 -3]) => [4 [2 4 0 1 -3]]
             (sut/jump 4 [2 4 0 1 -3]) => [1 [2 4 0 1 -2]])

(midje/facts "about parse-input"
             (sut/parse-input example-input) => [0 3 0 1 -3])

(midje/facts "about task-1"
             (let [input (sut/parse-input test-input)]

               ;; found the answer by running the code manually,
               ;; now let's drop a test not to break the logic
               ;; when refactoring
               (sut/task-1 input) => 378980))

(midje/facts "about task-2"
             (let [input (sut/parse-input test-input)]

               ;; found the answer by running this manually on the input
               (sut/task-2 input) => 26889114))
