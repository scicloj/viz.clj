(ns scicloj.viz.dataset
  (:require [tech.v3.dataset.impl.dataset]
            [scicloj.kindly.kindness :as kindness]
            [scicloj.kindly.view]))

(extend-type tech.v3.dataset.impl.dataset.Dataset
  kindness/Kindness
  (->behavior [this]
    {:render-src?   true
     :value->hiccup #'scicloj.kindly.view/dataset->md-hiccup}))
