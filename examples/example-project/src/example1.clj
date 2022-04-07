(ns example1
  (:require [scicloj.clay.v1.api :as clay]
            [scicloj.clay.v1.tools :as tools]
            [tablecloth.api :as tc]
            [scicloj.viz.api :as viz]))

(clay/start! {:tools [tools/clerk
                      tools/portal]})

(comment
  (clay/restart! {:tools [tools/clerk
                          tools/portal]}))

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x)
    viz/viz)
