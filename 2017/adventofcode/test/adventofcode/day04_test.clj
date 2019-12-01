(ns adventofcode.day04-test
  (:require [adventofcode.day04 :as sut]
            [midje.sweet :as midje]))

(def test-input (slurp "resources/input_day04.txt"))

(midje/facts "about lines-as-words"
             (sut/line-as-words "aa bb cc dd ee") => ["aa" "bb" "cc" "dd" "ee"]
             (sut/line-as-words "aa bb cc dd ee aa") => ["aa" "bb" "cc" "dd" "ee" "aa"])

(midje/facts "about valid-passphrase"
             (sut/valid-passphrase? ["aa" "bb" "cc" "dd" "ee"]) => true
             (sut/valid-passphrase? ["aa" "bb" "cc" "dd" "ee" "aa"]) => false
             (sut/valid-passphrase? ["aa" "bb" "cc" "dd" "ee" "aaa"]) => true)

(midje/facts "about task-1"
             ;; that's my answer, found it manually after running the function
             ;; on input
             (sut/task-1 test-input) => 383)


(midje/facts "about task-2"
             ;; that's my answer, found it manually after running the function
             ;; on input
             (sut/task-2 test-input) => 265)
