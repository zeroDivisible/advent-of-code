(ns adventofcode.day03)
;;; Solutions to https://adventofcode.com/2017/day/3

;; this is my user specific input to this puzzle
(def input-1 361527)

;; I'm using a simple vector [:x :y] as an abstraction
;; for a point on a grid, with first item being coordinate x
;; and second item being coordinate y.

;; Task 1

(defn update-point
  ;; Given a point, and coordinate to update (either :x or :y),
  ;; returns new point, which we'd move to by applying operation
  ;; op to the current value of that coordinate and step.
  ;;
  ;; Example:
  ;;
  ;;   (update-point [0 0] :x 1 +) => [1 0]
  [point coord step op]
  (if (= coord :x)
    (assoc point 0 (op (first point) step))
    (assoc point 1 (op (second point) step))))

;; Task 1 & Task 2 are based on operations on a spiral
;; as visualized here:
;;
;; | 17 | 16 | 15 | 14 | 13 |
;; | 18 |  5 |  4 |  3 | 12 |
;; | 19 |  6 |  1 |  2 | 11 |
;; | 20 |  7 |  8 |  9 | 10 |
;; | 21 | 22 | 23 | 24 | 25 |
;;
;; Starting at 1, next number is 2, then 3, 4, 5, etc.
;;
;; First task - find the Manhattan Distance to point identified
;; in input-1

;; interesting observation - when we're moving from the central point,
;; we're doing 1 step right, then 1 step up, 2 steps left, 2 steps down,
;; 3 steps right, 3 steps up, basically moving by:
;;
;;   1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6,
;;
;; steps in given directions:
;;
;;  [:x +], [:y +], [:x -], [:y -]
;;
;; Defining two cycles for this.

(defn cycle-directions
  ;; Returns a cycle for the directions in which we're moving
  []
  (cycle [[:x +] [:y +] [:x -] [:y -]]))

(defn cycle-steps
  ;; Returns a cycle of functions, where the first one doesn't
  ;; change the value of the object, and the second on adds one
  ;; to it. This can the be used to produce sequences like
  ;;
  ;;   1, 1, 2, 2, 3, 3, 4, 4...
  []
  (cycle [#(+ 0 %) #(inc %)]))

(defn coords
  ;; Returns coordinates of the x-th point on the grid, i.e.
  ;;
  ;;   (coords 1) => [0 0]
  ;;   (coords 2) => [1 0]
  ;;   (coords 5) => [-1 0]
  [x]
  (loop [current-square 1
         point          [0 0]
         directions     (cycle-directions)
         step           1
         next-steps     (cycle-steps)]
    (if (= current-square x)
      point
      (let [at-most   (if (> (+ current-square step) x)
                        (- x current-square)
                        step)
            [dir op]  (first directions)
            next-step ((first next-steps) step)]
        (recur (+ current-square at-most)
               (update-point point dir at-most op)
               (rest directions)
               next-step
               (rest next-steps))))))

(defn task-1
  ;; Returns the Manhattan distance from origin of the grid ([0 0])
  ;; to the x-th point in the grid.
  ;;
  ;; Firstly finds the coordinates for given point, then calculates the
  ;; sum of absolute values for each coordinate
  [x]
  (reduce + (map #(Math/abs %1) (coords x))))

;; (after solving the problem with above, I found out that there's a better way
;;  but that was my original solution)

;; Task 2

;; This was a bit trickier - we need to summarize the values as we're traveling
;; through the spiral. Couldn't think of anything fancy, so did the simplest thing
;; and I'm building the spiral as I'm going through it, stopping when the value
;; in given field is greater than my input.

(defn coords-around-point
  ;; Given a point, returns a set of all the surrounding points.
  [point]
  (let [funs  [#(identity %) #(inc %) #(dec %)]
        [x y] point]
    (into #{} (filter #(not= % point)
                      (for [fun-x funs
                            fun-y funs]
                        [(fun-x x) (fun-y y)])))))

(defn sum-around-point
  ;; Given a map capping a coordinate to a value stored at that coordinate,
  ;; returns the sum of items around given point (excluding that point)
  [point m]
  (let [coords (coords-around-point point)]
    (reduce + (map #(get m %1 0) coords))))

(defn generate-spiral [limit]
  (loop [square         1
         point          [0 0]
         value-at-point 1
         m              { point value-at-point }
         directions     (cycle-directions)
         step           1
         counter        1
         next-steps     (cycle-steps)]
    (cond
      (> value-at-point limit) m
      :else                    (let [dir             (first directions)
                                     s               (first next-steps)
                                     updated         (update-point point (first dir) 1 (second dir))
                                     summarised      (sum-around-point updated m)
                                     remaining-steps (dec step)
                                     zero-steps-remainig (= remaining-steps 0)]
                                 (recur (inc square)
                                        updated
                                        summarised
                                        (assoc m updated summarised)
                                        (if zero-steps-remainig (rest directions) directions)
                                        (if zero-steps-remainig (s counter) (dec step))
                                        (if zero-steps-remainig (s counter) counter)
                                        (if zero-steps-remainig (rest next-steps) next-steps))))))

;; answer to task 2 is 363010
(defn task-2
  ;; We're generating a spiral, stopping after reaching a value greater
  ;; than x, and then we select the maximum value from the spiral - which
  ;; is the first number greater than x (and the last insert in the spiral)
  [x]
  (reduce max (vals (generate-spiral x))))
