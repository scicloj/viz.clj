(defproject org.scicloj/viz.clj "0.1.1"
  :description "Data Visualization in Clojure"
  :url "https://github.com/scicloj/viz.clj"
  :scm {:name "git"
        :url "https://github.com/scicloj/viz.clj"}
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-tools-deps "0.4.5"]
            [lein-codox "0.10.7"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]

  :lein-tools-deps/config {:config-files [:install :user :project]}

  :profiles {:dev {:lein-tools-deps/config {:aliases [:dev]}
                   :aliases {"clj-kondo" ["run" "-m" "clj-kondo.main"]
                             "lint" ["do"
                                     ["cljfmt" "check"]
                                     ["run" "-m" "clj-kondo.main" "--lint" "src:test"]]}
                   :plugins [[lein-midje "3.2.1"]
                             [lein-cljfmt "0.7.0"]]
                   :repl-options {:nrepl-middleware [scicloj.notespace.v4.nrepl/middleware]}}})

