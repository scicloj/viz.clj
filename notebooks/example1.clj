(ns example1
  (:require [scicloj.notespace.v4.api :as notespace]
            [scicloj.viz.api :as viz]
            [tech.v3.dataset :as tmd]
            [scicloj.kindly.api :as kindly]
            [scicloj.kindly.kind :as kind]
            [scicloj.kindly.kindness :as kindness]))

(comment
  (notespace/restart!))

(+ 1 2)

(comment
  ;; troubleshoot:
  (notespace/restart-events!))

(-> (viz/data "resources/data/mpg.csv")
    (viz/type "point")
    (viz/x "displ")
    (viz/y "hwy")
    (viz/viz))
