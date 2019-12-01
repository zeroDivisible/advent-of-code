(ns adventofcode.day09-test
  (:require [adventofcode.day09 :as sut]
            [midje.sweet :as midje]))

(def input (slurp "resources/input_day09.txt"))

(midje/facts "about parse-input"
             (sut/score-input "{}" :score) => 1
             (sut/score-input "{{{}}}" :score) => 6
             (sut/score-input "{{{},{},{{}}}}" :score) => 16
             (sut/score-input "{<{},{},{{}}>}" :score) => 1
             (sut/score-input "{{<ab>},{<ab>},{<ab>},{<ab>}}" :score) => 9
             (sut/score-input "{{<!!>},{<!!>},{<!!>},{<!!>}}" :score) => 9
             (sut/score-input "{{<a!>},{<a!>},{<a!>},{<ab>}}" :score) => 3)

(midje/facts "about task-1"
             (sut/task-1 "{}")     => 1
             (sut/task-1 "{{{}}}") => 6
             (sut/task-1 "{{<a!>},{<a!>},{<a!>},{<ab>}}") => 3
             (sut/task-1 (sut/parse-input input)) => 14212)

(midje/facts "about task-2"
             (sut/task-2 "{}") => 0
             (sut/task-2 "{<>}") => 0
             (sut/task-2 (sut/parse-input"<{o\"i!a,<{i<a>")) => 10
             (sut/task-2 (sut/parse-input input)) => 6569)
