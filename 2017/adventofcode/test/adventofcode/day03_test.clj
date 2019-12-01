(ns adventofcode.day03-test
  (:require [adventofcode.day03 :as sut]
            [midje.sweet :as midje]))

(def user-specific-input 361527)

(midje/facts "about update-point"
             (sut/update-point [0 0] :x 1 +) => [1 0]
             (sut/update-point [0 0] :x 2 -) => [-2 0]
             (sut/update-point [0 0] :y 1 +) => [0 1])

(midje/facts "about coords-around-point"
             (sut/coords-around-point [0 0]) => #{[-1 1] [0 1] [1 1]
                                                  [1 0] [1 -1] [0 -1]
                                                  [-1 -1] [-1 0]})

(midje/facts "about coords"
             (sut/coords 1) => [0 0]
             (sut/coords 2) => [1 0]
             (sut/coords 3) => [1 1]
             (sut/coords 4) => [0 1]
             (sut/coords 5) => [-1 1]
             (sut/coords 361527) => [301 25])

(midje/facts "about task-1"
             (sut/task-1 1) => 0
             (sut/task-1 12) => 3
             (sut/task-1 23) => 2

             ;; This is the answer to my puzzle
             (sut/task-1 user-specific-input) => 326)

;; Task 2
;;
;; This is the first 10 elements of the spiral described in
;;
;;  https://adventofcode.com/2017/day/3
(def first-10-elements {[0 0]   1
                        [1 0]   1
                        [1 1]   2
                        [0 1]   4
                        [-1 1]  5
                        [-1 0]  10
                        [-1 -1] 11
                        [0 -1]  23
                        [1 -1]  25
                        [2 -1]  26})

(midje/facts "about sum-around-point"
             (sut/sum-around-point [1 0] {[0 0] 1}) => 1
             (sut/sum-around-point [1 1] {[0 0] 1
                                          [1 0] 1}) => 2
             (sut/sum-around-point [2 -1] first-10-elements) => 26)

(midje/facts "about task-2"

             ;; this is the answer to my puzzle.
             (sut/task-2 user-specific-input) => 363010)
