(ns adventofcode.day07-test
  (:require [adventofcode.day07 :as sut]
            [midje.sweet :as midje]))

(def test-input (slurp "resources/input_day07.txt"))

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

(midje/facts "about parse-input"
             (let [[graph-weights graph] (sut/parse-input example-input)]
               (keys graph-weights) => (midje/just #{"qoyq" "ebii" "havc" "ugml"
                                                     "cntj" "jptl" "xhth" "pbga"
                                                     "gyxo" "tknk" "ktlj" "fwft"
                                                     "padx"})
               (graph-weights "pbga") => 66
               (graph-weights "havc") => 66
               (graph-weights "gyxo") => 61

               (keys graph) => (midje/just #{"qoyq" "ebii" "havc" "ugml"
                                             "cntj" "jptl" "xhth" "pbga"
                                             "gyxo" "tknk" "ktlj" "fwft"
                                             "padx"})
               (graph "fwft") => ["ktlj" "cntj" "xhth"]
               (graph "qoyq") => []))


(midje/facts "about starting-points"
             (let [points (sut/starting-points (second (sut/parse-input example-input)))]
               points => ["tknk"]))

(midje/facts "about task-1"
             (sut/task-1 test-input) => "eugwuhl")
