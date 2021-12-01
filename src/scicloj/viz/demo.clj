(ns viz.demo
  (:require [scicloj.notespace.v4.api :as notespace]
            [scicloj.viz.api :as viz]
            [tech.v3.dataset :as tmd]
            [scicloj.kindly.api :as kindly]
            [scicloj.kindly.kind :as kind]
            [scicloj.kindly.kindness :as kindness]))

(comment
  (notespace/restart!))

(scicloj.viz.config/use-tempfiles?)

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

(binding [scicloj.viz.config/*use-tempfiles?* true]
  (-> (viz/data "resources/data/mpg-with-typo.csv")
      (viz/type "point")
      (viz/x "displ")
      (viz/y "hwy")
      (viz/viz)))

(binding [scicloj.viz.config/*use-tempfiles?* false]
  (-> (viz/data "resources/data/mpg-with-typo.csv")
      (viz/type "point")
      (viz/x "displ")
      (viz/y "hwy")
      (viz/viz)))

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

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tmd/->dataset
    viz/data
    (viz/x "w")
    (viz/type "point")
    (viz/viz))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tmd/->dataset
    viz/data
    (viz/color :x)
    (viz/type "point")
    (viz/viz))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tmd/->dataset
    viz/data
    (viz/color :w)
    (viz/type "point")
    (viz/viz))

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


