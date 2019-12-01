(ns adventofcode.day04
  (:require [adventofcode.core :as core]
            [clojure.string :as string])
  (:import [java.time LocalDateTime ZoneOffset Duration]))

(defn to-utc-datetime
  "Parses a string with format 'yyyy-MM-dd hh:mm' to an UTC
  LocalDateTimeobject "
  [string]
  (LocalDateTime/ofInstant (.toInstant (.parse (new java.text.SimpleDateFormat "yyyy-MM-dd hh:mm") string))
                           (ZoneOffset/UTC)))

(defn extract-data
  [line]
  (let [[date reminder] (rest (re-matches #"\[(.*)\] (.*)" line))
        date (to-utc-datetime date)
        action (cond
                 (= reminder "wakes up") :wakes_up
                 (= reminder "falls asleep") :falls_asleep)
        id (if (nil? action) (first (rest (re-matches #"Guard (.*) begins shift" reminder))))]
    {:date date
     :id id
     :action action}))

(def example-input (string/split-lines (slurp "resources/day_04_input.txt")))
;;(def example-input (string/split-lines (slurp "resources/day_04_example.txt")))
(def parsed (sort-by :date (map extract-data example-input)))

;; assumes that input logs are sorted
(defn add-id [logs]
  (loop [logs-with-id []
         record (first logs)
         records (rest logs)
         last-id (if-not (nil? (:id record)) (:id record))]
    (let [log-with-id (assoc record :id last-id)
          logs-with-id (if (nil? (:action log-with-id))
                         logs-with-id
                         (conj logs-with-id log-with-id))]
      (cond
        (empty? records) logs-with-id
        :else (recur logs-with-id
                     (first records)
                     (rest records)
                     (if (:id (first records))
                       (:id (first records))
                       last-id))))))

(defn minutes-between
  [from to]
  (.toMinutes (Duration/between from to)))

(defn minute-ranges
  [sleep-record]
  (range (.getMinute (:date (first sleep-record)))
         (.getMinute (:date (second sleep-record)))))

(def filtered-records (add-id parsed))

(defn logs-per-guard
  [records]
  (into {} (for [guard-ids (into #{} (map :id records))
                 :let [filtered (filterv #(= (:id %1) guard-ids) records)
                       correct-keys (mapv #(select-keys %1 [:date :action]) filtered)
                       partitioned (partition 2 correct-keys)
                       ranges (map minute-ranges partitioned)]]
             [guard-ids ranges])))

(def minute-logs (logs-per-guard filtered-records))

(defn most-asleep
  [logs]
  (let [asleep (reduce-kv #(assoc %1 %2 (reduce + (map count %3)))
                          {}
                          logs)
        max-asleep (apply max (vals asleep))
        id-max-asleep (first (filter (comp #{max-asleep} asleep) (keys asleep)))]
    [id-max-asleep max-asleep]))

(def id-most-asleep (first (most-asleep minute-logs)))
(def minutes-asleep (nth (most-asleep minute-logs) 1))

(defn most-asleep-minute
  [id logs]
  (let [logs (get logs id)
        freqs (frequencies (flatten logs))
        most-popular-minute (apply max (vals freqs))]
    (first (filter (comp #{most-popular-minute} freqs) (keys freqs)))))

(def minute (most-asleep-minute id-most-asleep minute-logs))

;; answer to the first part
(* (Long/parseLong (get (re-matches #"#(.+)" id-most-asleep) 1)) minute)

;; part 2
(defn most-frequently-asleep
  [logs]
  (let [asleep (reduce-kv #(assoc %1 %2 (frequencies (flatten %3)))
                          {}
                          logs)
        max-asleep (reduce-kv #(assoc %1 %2 (apply max (vals %3)))
                              {}
                              asleep)
        max-occurence (apply max (vals max-asleep))
        max-id (first (filter (comp #{max-occurence} max-asleep) (keys max-asleep)))
        selected-logs (get asleep max-id)
        max-minute (first (filter (comp #{max-occurence} selected-logs) (keys selected-logs)))]
    [max-id max-minute]))

(most-frequently-asleep minute-logs)
(def id-most-frequent (first (most-frequently-asleep minute-logs)))
(def most-frequent-minute (nth (most-frequently-asleep minute-logs) 1))

(* (Long/parseLong (get (re-matches #"#(.+)" id-most-frequent) 1)) most-frequent-minute)
