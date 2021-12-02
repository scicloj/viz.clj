(ns viz.demo
  (:require [scicloj.notespace.v4.api :as notespace]
            [scicloj.viz.api :as viz]
            [scicloj.viz.util :as util]
            [tech.v3.dataset :as tmd]
            [tablecloth.api :as tc]
            [tablecloth.pipeline :as tc-pipe]
            [aerial.hanami.templates :as ht]
            [aerial.hanami.common :as hc]
            [scicloj.kindly.api :as kindly]
            [scicloj.kindly.kind :as kind]
            [scicloj.kindly.kindness :as kindness]))

(comment
  (notespace/restart!))

(scicloj.viz.config/use-tempfiles?)

(util/return-exception-digest
 (/ 1 0))

(binding [scicloj.viz.config/*use-tempfiles?* true]
  (-> (viz/data "resources/data/mpg.csv")
      (viz/type "point")
      (viz/x "displ")
      (viz/y "hwy")
      (viz/viz)))

(binding [scicloj.viz.config/*use-tempfiles?* false]
  (-> (viz/data "resources/data/mpg.csv")
      (viz/type "point")
      (viz/x "displ")
      (viz/y "hwy")
      (viz/viz)))

(util/return-exception-digest
 (binding [scicloj.viz.config/*use-tempfiles?* true]
   (-> (viz/data "resources/data/mpg-with-typo.csv")
       (viz/type "point")
       (viz/x "displ")
       (viz/y "hwy")
       (viz/viz))))

(util/return-exception-digest
  (binding [scicloj.viz.config/*use-tempfiles?* false]
    (-> (viz/data "resources/data/mpg-with-typo.csv")
        (viz/type "point")
        (viz/x "displ")
        (viz/y "hwy")
        (viz/viz))))

(binding [scicloj.viz.config/*use-tempfiles?* true]
  (-> {:x (range 99)
       :y (repeatedly 99 rand)}
      tmd/->dataset
      viz/data
      (viz/type "point")
      (viz/viz)))

(binding [scicloj.viz.config/*use-tempfiles?* false]
  (-> {:x (range 99)
       :y (repeatedly 99 rand)}
      tmd/->dataset
      viz/data
      (viz/type "point")
      (viz/viz)))

(binding [scicloj.viz.config/*use-tempfiles?* true]
  (-> (for [i (range 99)]
        {:x i
         :y (rand)})
      viz/data
      (viz/type "point")
      (viz/viz)))

(binding [scicloj.viz.config/*use-tempfiles?* false]
  (-> (for [i (range 99)]
        {:x i
         :y (rand)})
      viz/data
      (viz/type "point")
      (viz/viz)))

(binding [scicloj.viz.config/*use-tempfiles?* true]
  (-> "https://vega.github.io/vega-lite/data/penguins.json"
      viz/data
      (viz/x "Beak Length (mm)")
      (viz/y "Beak Depth (mm)")
      (viz/type "point")
      viz/viz))

(binding [scicloj.viz.config/*use-tempfiles?* false]
  (-> "https://vega.github.io/vega-lite/data/penguins.json"
      viz/data
      (viz/x "Beak Length (mm)")
      (viz/y "Beak Depth (mm)")
      (viz/type "point")
      viz/viz))

(util/return-exception-digest
  (-> {:x (range 99)
       :y (repeatedly 99 rand)}
      tmd/->dataset
      viz/data
      (viz/x "w")
      (viz/type "point")
      (viz/viz)))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tmd/->dataset
    viz/data
    (viz/color :x)
    (viz/type "point")
    (viz/viz))

(try
  (-> {:x (range 99)
       :y (repeatedly 99 rand)}
      tmd/->dataset
      viz/data
      (viz/color :w)
      (viz/type "point")
      (viz/viz))
  (catch Exception e e))

(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/type "point")
    viz/viz)

(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/color "Beak Depth (mm)") 
    (viz/type "point")
    viz/viz)


(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tmd/->dataset
    viz/data
    ((tc-pipe/add-column :z #(map + (:x %) (:y %))))
    (viz/y :z)
    (viz/type "point")
    (viz/viz))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tmd/->dataset
    viz/data
    ((tc-pipe/add-column :z #(map + (:x %) (:y %))))
    (viz/y :z)
    (viz/type "point")
    ;; tweaking Hanami substitutions
    (assoc :BACKGROUND "#e9e6e3")
    (viz/viz))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tmd/->dataset
    viz/data
    ((tc-pipe/add-column :z #(map + (:x %) (:y %))))
    (viz/y :z)
    (viz/type "point")
    (viz/viz)
    ;; tweaking vega-lite:
    (assoc :background "#e9e6e3"))


(-> {:data (-> {:a (range 99)
                     :b (repeatedly 99 rand)}
                    tmd/->dataset)
          :x :a
          :y :b
          :type ht/point-chart}
         viz/->viz-map
         viz/viz)

(util/return-exception-digest
 (-> {:data (-> {:a (range 99)
                 :b (repeatedly 99 rand)}
                tmd/->dataset)
      :x    :a
      :y    :b}
     viz/->viz-map
     viz/viz))

(util/return-exception-digest
 (-> {:data (-> {:a (range 99)
                 :b (repeatedly 99 rand)}
                tmd/->dataset)
      :x    :a
      :y    :b
      :z    :c
      :type ht/point-chart}
     viz/->viz-map
     viz/viz))

(-> (-> {:x (range 9)
         :y (repeatedly 9 rand)}
        tmd/->dataset)
    viz/data
    (viz/layer {:type ht/point-chart
                :color :x})
    (viz/layer {:type ht/line-chart})
    viz/viz)


