(ns adventofcode.day06-test
  (:require [adventofcode.day06 :as sut]
            [clojure.test :refer :all]
            [midje.sweet :as midje]))

(def example-input (slurp "resources/day_06_example.txt"))
(def test-input (slurp "resources/day_06_input.txt"))

(deftest parse-input
  (is (= '([1 1]
             [1 6]
             [8 3]
             [3 4]
             [5 5]
             [8 9])
         (sut/parse-input example-input))))

(def parsed-input (sut/parse-input example-input))

(deftest build-map
  (is (= {[1 1] [1 1]
          [1 6] [1 6]
          [8 3] [8 3]
          [3 4] [3 4]
          [5 5] [5 5]
          [8 9] [8 9]}
         (sut/build-map parsed-input))))

(deftest view-boundries
  (let [view (sut/build-map parsed-input)]
    (is (= [8 9]
           (sut/view-boundries view)))))

(deftest expand-point
  (testing "expanding from origin"
    (is (= #{[0 1] [1 0]}
           (sut/expand-point [0 0] [9 9]))))
  (testing "expanding a normal point"
    (is (= #{[0 1] [1 0] [2 1] [1 2]}
           (sut/expand-point [1 1] [9 9]))))
  (testing "expanding against a limit"
    (is (= #{[1 2] [2 1]}
           (sut/expand-point [2 2] [2 2])))))

(deftest expand-view
  (testing "expanding against limits"
    (is (= {[0 0] [0 0]
            [0 1] [0 0]
            [1 0] [0 0]
            [2 1] [2 1]
            [2 0] [2 1]
            [1 1] [2 1]}
           (sut/expand-view {[0 0] [0 0]
                             [2 1] [2 1]}))))
  (testing "expanding four points"
    (is (= {[3 0] [3 0] ;; A
            [2 0] [3 0]
            [4 0] [3 0]
            [3 1] [3 0]

            [5 1] [5 1] ;; C
            [5 0] [5 1]
            [4 1] [5 1]
            [6 1] [5 1]
            [5 2] [5 1]

            [1 2] [1 2] ;; B
            [1 1] [1 2]
            [0 2] [1 2]
            [2 2] [1 2]

            [8 2] [8 2] ;; D
            [7 2] [8 2]
            [8 1] [8 2]
            }
           (sut/expand-view {[3 0] [3 0]
                             [5 1] [5 1]
                             [1 2] [1 2]
                             [8 2] [8 2]}))))
  (testing "expanding four points second time"
    (is (= {[3 0] [3 0] ;; A
            [2 0] [3 0]
            [4 0] [3 0]
            [3 1] [3 0]

            [5 1] [5 1] ;; C
            [5 0] [5 1]
            [4 1] [5 1]
            [6 1] [5 1]
            [5 2] [5 1]
            [6 0] [5 1]
            [4 2] [5 1]

            [1 2] [1 2] ;; B
            [1 1] [1 2]
            [0 2] [1 2]
            [2 2] [1 2]
            [0 1] [1 2]

            [8 2] [8 2] ;; D
            [7 2] [8 2]
            [8 1] [8 2]
            [8 0] [8 2]

            [1 0] nil   ;; empties
            [2 1] nil
            [3 2] nil
            [6 2] nil
            [7 1] nil
            }
           (sut/expand-view {[3 0] [3 0]
                             [2 0] [3 0]
                             [4 0] [3 0]
                             [3 1] [3 0]
                             [5 1] [5 1]
                             [5 0] [5 1]
                             [4 1] [5 1]
                             [6 1] [5 1]
                             [5 2] [5 1]
                             [1 2] [1 2]
                             [1 1] [1 2]
                             [0 2] [1 2]
                             [2 2] [1 2]
                             [8 2] [8 2]
                             [7 2] [8 2]
                             [8 1] [8 2]
                             }))))
  (testing "expanding four points third time"
    (is (= {[3 0] [3 0] ;; A
            [2 0] [3 0]
            [4 0] [3 0]
            [3 1] [3 0]

            [5 1] [5 1] ;; C
            [5 0] [5 1]
            [4 1] [5 1]
            [6 1] [5 1]
            [5 2] [5 1]
            [6 0] [5 1]
            [4 2] [5 1]

            [1 2] [1 2] ;; B
            [1 1] [1 2]
            [0 2] [1 2]
            [2 2] [1 2]
            [0 1] [1 2]

            [8 2] [8 2] ;; D
            [7 2] [8 2]
            [8 1] [8 2]
            [8 0] [8 2]

            [1 0] nil   ;; empties
            [2 1] nil
            [3 2] nil
            [6 2] nil
            [7 1] nil
            [0 0] nil
            [7 0] nil
            }
           (sut/expand-view {[3 0] [3 0] ;; A
                             [2 0] [3 0]
                             [4 0] [3 0]
                             [3 1] [3 0]

                             [5 1] [5 1] ;; C
                             [5 0] [5 1]
                             [4 1] [5 1]
                             [6 1] [5 1]
                             [5 2] [5 1]
                             [6 0] [5 1]
                             [4 2] [5 1]

                             [1 2] [1 2] ;; B
                             [1 1] [1 2]
                             [0 2] [1 2]
                             [2 2] [1 2]
                             [0 1] [1 2]

                             [8 2] [8 2] ;; D
                             [7 2] [8 2]
                             [8 1] [8 2]
                             [8 0] [8 2]

                             [1 0] nil   ;; empties
                             [2 1] nil
                             [3 2] nil
                             [6 2] nil
                             [7 1] nil
                             }))))
  )
