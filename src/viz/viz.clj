(ns viz.viz
  (:require [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [aerial.hanami.core :as hmi]
            [clojure.java.io :as io]
            [cheshire.core]
            [tablecloth.api :as tablecloth]
            [notespace.api :as notespace]
            [notespace.kinds :as kind]
            [viz.api :as api]
            [tech.v3.dataset :as tmd]))

^kind/vega
(hc/xform ht/point-chart
          :UDATA "data/cars.json"
          :X "Horsepower" :Y "Miles_per_Gallon" :COLOR "Origin")


^kind/vega
(-> (api/data "data/cars.json")
    (api/type "point")
    (api/x "Horsepower")
    (api/y "Miles_per_Gallon")
    (api/color "Origin")
    (api/viz))


(def mpg-data (tmd/->dataset "resources/data/mpg.csv"))

^kind/vega
(-> (api/data mpg-data)
    (api/type "point")
    (api/x "displ")
    (api/y "hwy")
    (api/viz))

^kind/vega
(-> (api/data "data/cars.json")
    (api/type "boxplot")
    (api/x "Horsepower")
    (api/y "Origin" {:type "nominal"})
    (api/color "Origin")
    (api/viz))


^kind/dataset
(-> "public/data/cars.json"
    io/resource
    slurp
    (cheshire.core/parse-string keyword)
    (tablecloth/dataset))

^kind/dataset
(tablecloth/dataset "resources/data/mpg.csv")

^kind/vega
(hc/xform ht/point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy")

^kind/vega
(hc/xform ht/point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy"
          :COLOR "class")

^kind/vega
(hc/xform ht/point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy"
          :SIZE "class")

^kind/vega
(hc/xform ht/point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy"
          :OPACITY "class")


(def point-chart
  (assoc ht/view-base
    :mark (merge ht/mark-base {:type "point"})))

^kind/vega
(hc/xform point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy"
          :SHAPE "class")

^kind/vega
(hc/xform ht/point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy"
          :MCOLOR "blue")

^kind/vega
(hc/xform ht/point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy"
          :HEIGHT 200, :WIDTH 150
          :COLUMN "class")

^kind/vega
(hc/xform ht/point-chart
          :FDATA "resources/data/mpg.csv"
          :X "displ" :Y "hwy"
          :HEIGHT 200, :WIDTH 150
          :COLUMN "cyl"
          :ROW "drv")

^kind/vega
(hc/xform ht/bar-chart
          :FDATA "resources/data/diamonds.csv"
          :X "cut")

