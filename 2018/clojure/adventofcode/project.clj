(defproject adventofcode "0.1.0"
  :description "adventofcode 2018, solutions in clojure"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :profiles {:dev {:dependencies [[midje "1.9.4"]]
                   :plugins [[lein-midje "3.2.1"]]}
             :kaocha {:dependencies [[lambdaisland/kaocha "0.0-319"]
                                     [lambdaisland/kaocha-cloverage "0.0-22"]]}}
  :aliases {"kaocha" ["with-profile" "+kaocha" "run" "-m" "kaocha.runner"]})
