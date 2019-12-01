(ns adventofcode.day09)

(defn parse-input
  [input]
  input)

;; please no
(defn score-input
  [input metric]
  (loop [input         input
         opened-groups []
         score         0
         in-garbage    false
         cancel-this   false
         garbage-count 0]
    (cond
      cancel-this (recur (rest input)
                         opened-groups
                         score
                         in-garbage
                         false
                         garbage-count)
      :else       (let [current-char     (first input)
                        starting-garbage (= \< current-char)
                        opening-group    (= \{ current-char)
                        closing-group    (= \} current-char)

                        cancelling-next (= \! current-char)

                        closing-garbage (= \> current-char)

                        continue-garbage (cond
                                           (or starting-garbage in-garbage) true
                                           :else                            false)

                        can-open (cond
                                   continue-garbage false
                                   opening-group    true
                                   :else            false)

                        can-close (cond
                                    (and closing-group
                                         (not-empty opened-groups)) true
                                    continue-garbage                false
                                    :else                           false)

                        opened-groups (cond
                                        continue-garbage opened-groups
                                        can-open         (conj opened-groups \{)
                                        can-close        (pop opened-groups)
                                        :else            opened-groups)

                        score (cond
                                continue-garbage score
                                can-close        (+ score (inc (count opened-groups)))
                                :else            score)

                        can-increase-garbage (cond
                                               cancelling-next  false
                                               (and starting-garbage
                                                    in-garbage) true
                                               (and closing-garbage
                                                    in-garbage) false
                                               :else            in-garbage
                                               )]
                    (cond
                      (nil? current-char) #dbg (case metric
                                            :score score
                                            :garbage-count garbage-count
                                            nil)
                      :else               (recur (rest input)
                                                 opened-groups
                                                 score
                                                 (if closing-garbage
                                                   false
                                                   continue-garbage)
                                                 cancelling-next
                                                 (if can-increase-garbage
                                                   (inc garbage-count)
                                                   garbage-count)))))))

(defn task-1
  [input]
  (score-input input :score))

(defn task-2
  [input]
  (score-input input :garbage-count))
