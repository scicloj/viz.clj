(ns scicloj.viz.dataset
  (:require [tech.v3.dataset.impl.dataset]
            [notespace.behavior]
            [notespace.view]))

(extend-type tech.v3.dataset.impl.dataset.Dataset
  notespace.behavior/Behaving
  (->behavior [this]
    {:render-src?   true
     :value->hiccup #'notespace.view/dataset->md-hiccup}))
