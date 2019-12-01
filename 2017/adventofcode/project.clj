(defproject adventofcode "0.1.0-SNAPSHOT"
  :description "Solutions to adventofcode"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :profiles {:dev {:dependencies [[midje "1.9.4"]
                                  [criterium "0.4.4"]]
                   :plugins [[lein-midje "3.2.1"]]}})
