;; # viz.clj

;; ## Ashima Panjwani

;; ### re:Clojure, Dec. 3-4 2021

:_

;; # setup
(ns viz.clean
  (:require [scicloj.viz.api :as viz]
            [scicloj.viz.util :as util]
            [tablecloth.api :as tc]
            [tablecloth.pipeline :as tc-pipe]
            [aerial.hanami.templates :as ht]))

^:hidden
(comment
  (require '[scicloj.notespace.v4.api :as notespace]
           '[scicloj.notespace.v4.run :as notespace.run])
  (notespace/merge-config! {:note-layout   :horizontal
                            :ignore-nrepl? true})
  (notespace/restart!)
  (gorilla-notes.core/merge-new-options! {:buttons?  false
                                          :dropdown? true})
  (notespace.run/run-ns! "src/scicloj/viz/clean.clj"))


;; # data from a file
(-> (viz/data "resources/data/mpg.csv")
    (viz/type "point")
    (viz/x "displ")
    (viz/y "hwy")
    (viz/viz))

;; # missing file
(util/return-exception-digest
 (-> (viz/data "resources/data/mpg-with-typo.csv")
     (viz/type "point")
     (viz/x "displ")
     (viz/y "hwy")
     (viz/viz)))

;; # data from a file on the web
(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/type "point")
    viz/viz)

;; # a t.m.d dataset
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/viz))

;; # a sequence of maps
(-> (for [i (range 99)]
      {:x i
       :y (rand)})
    viz/data
    (viz/type "point")
    (viz/viz))

;; # missing column

(util/return-exception-digest
  (-> {:x (range 99)
       :y (repeatedly 99 rand)}
      tc/dataset
      viz/data
      (viz/x "w")
      (viz/type "point")
      (viz/viz)))

;; # color

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/color :x)
    (viz/type "point")
    (viz/viz))

;; # more color
(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/color "Beak Depth (mm)")
    (viz/type "point")
    viz/viz)

;; # transforming during the viz pipeline
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    ((tc-pipe/add-column :z #(map + (:x %) (:y %))))
    (viz/y :z)
    (viz/type "point")
    (viz/viz))

;; # tweaking Hanami substitutions
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    ;; tweaking Hanami substitutions
    (assoc :BACKGROUND "#e9e6e3")
    (viz/viz))

;; # tweaking vega-lite
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/viz)
    ;; tweaking vega-lite:
    (assoc :background "#e9e6e3"))

;; # using an options map
(-> {:data  (-> {:a (range 99)
                 :b (repeatedly 99 rand)}
               tc/dataset)
     :x     :a
     :y     :b
     :color :a
     :size  :b
     :type  ht/point-chart}
    viz/->viz-map
    viz/viz)

;; # missing type
(util/return-exception-digest
 (-> {:data (-> {:a (range 99)
                 :b (repeatedly 99 rand)}
                tc/dataset)
      :x    :a
      :y    :b}
     viz/->viz-map
     viz/viz))

;; # including Hanami options
(util/return-exception-digest
 (-> {:data   (-> {:a (range 99)
                   :b (repeatedly 99 rand)}
                tc/dataset)
      :x      :a
      :y      :b
      :MCOLOR "violet"
      :type   ht/point-chart}
     viz/->viz-map
     viz/viz))

;; # layers
(-> {:x (range 9)
     :y (repeatedly 9 rand)}
    tc/dataset
    viz/data
    (viz/layer {:type ht/point-chart
                :color :x
                :size :y})
    (viz/layer {:type ht/line-chart})
    viz/viz)

;; # layers with different datasets
(-> (viz/layer {:type  ht/point-chart
                :color :x
                :size  :y
                :data (-> {:x (range 9)
                           :y (repeatedly 9 rand)}
                          tc/dataset)})
    (viz/layer {:type ht/line-chart
                :data (-> {:x (range 9)
                           :y (repeatedly 9 #(* 3 (rand)))}
                          tc/dataset)})
    viz/viz)

;; # regression layers
(-> {:x (range 99)
     :y (map +
             (range 99)
             (repeatedly 99 #(* 20 (rand))))}
    tc/dataset
    viz/data
    (viz/layer {:type :point})
    (viz/regression-layer {:x :x
                           :y :y
                           :MCOLOR "red"})
    viz/viz)

;; # bye
:bye
