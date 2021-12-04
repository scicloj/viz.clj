;; **Viz.clj tutorial**

^:hidden
(comment)

;; **setup**
(ns viz.tutorial
  (:require [scicloj.viz.api :as viz]
            [scicloj.viz.templates :as viz.templates]
            [scicloj.viz.util :as util]
            [tablecloth.api :as tc]
            [tech.v3.datatype.functional :as fun]
            [tablecloth.pipeline :as tc-pipe]
            [aerial.hanami.templates :as ht]
            [scicloj.notespace.v4.api :as notespace]
            [scicloj.notespace.v4.run :as notespace.run]
            [scicloj.kindly.kind :as kind]
            [scicloj.kindly.api :as kindly]))

^:hidden
(defn restart! []
  (notespace/merge-config! {:note-layout :horizontal})
  (notespace/restart!)
  (gorilla-notes.core/merge-new-options! {:buttons?  false
                                          :dropdown? true}))
^:hidden
(comment
  (restart!)
  (notespace/render-as-html! "/tmp/page/index.html"))

;; **basic example**

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    (viz/data)
    (viz/type :point)
    (viz/mark-size 200)
    (viz/mark-color "firebrick")
    (viz/viz))

;; **specifying data sources**

;;
;; a sequence of maps
(-> (for [i (range 99)]
      {:x i
       :y (rand)})
    viz/data
    (viz/type "point")
    (viz/viz))

;; a local file
(-> (viz/data "resources/data/mpg.csv")
    (viz/type "point")
    (viz/x "displ")
    (viz/y "hwy")
    (viz/viz))

;; a file on the web
(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/type "point")
    viz/viz)

;; a tablecloth dataset
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/viz))

;; **connecting aesthetics to data**
;;
;; x axis
(-> {:w (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/x :w)
    (viz/type "point")
    (viz/viz))

;; y axis
(-> {:x (range 99)
     :z (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/y :z)
    (viz/type "point")
    (viz/viz))

;; color
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/color :x)
    (viz/type "point")
    (viz/viz))

;; size
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/size :x)
    (viz/type "point")
    (viz/viz))

;; **customizing mark properties**

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/mark-color "purple")
    (viz/viz))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :point)
    (viz/mark-size 500)
    (viz/viz))

;; **picking mark type**

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :point)
    (viz/viz))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :line)
    (viz/viz))

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :bar)
    (viz/viz))

;; **using Hanami templates**

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type ht/point-chart)
    (viz/viz))

;; **more examples**
(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/color "Beak Depth (mm)")
    (viz/type "point")
    viz/viz)

;; **transforming during the viz pipeline**
;;
;; some data, having numbers with different orders of magnitude
(-> {:x (range 10)
     :y (map #(Math/pow 10 %)
             (range 10))}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/mark-size 200)
    (viz/viz))

;; log transformation to linearize the situation
;; (we can use tablecloth.pipeline to transform data after we started the viz pipeline)
(-> {:x (range 10)
     :y (map #(Math/pow 10 %)
             (range 10))}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/mark-size 200)
    ((tablecloth.pipeline/update-columns [:y] fun/log10))
    (viz/viz))

;; **tweaking the Hanami substitutions**
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    ;; tweaking Hanami substitutions
    (assoc :BACKGROUND "#e9e6e3")
    (viz/viz))

;; **tweaking the resulting vega-lite spec**
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/viz)
    ;; tweaking vega-lite:
    (assoc :background "#e9e6e3"))

;; **layers**

;; same dataset
(-> {:x (range 9)
     :y (repeatedly 9 #(rand-int 5))}
    tc/dataset
    viz/data
    (viz/layer {:type ht/point-chart
                :color :x
                :size :y})
    (viz/layer {:type ht/line-chart})
    viz/viz)

;; different datasets
(-> (viz/layer {:type  ht/point-chart
                :color :x
                :size  :y
                :data (-> {:x (range 9)
                           :y (repeatedly 9 #(rand-int 5))}
                          tc/dataset)})
    (viz/layer {:type ht/line-chart
                :data (-> {:x (range 9)
                           :y (repeatedly 9 #(* 3 (rand)))}
                          tc/dataset)})
    viz/viz)

;; **regression layers**
;;
;; (computed in Clojure, not the browser)
(-> {:x (range 99)
     :y (map +
         (range 99)
         (repeatedly 99 #(* 20 (rand))))}
    tc/dataset
    viz/data
    (viz/layer {:type :point})
    (viz/regression-line)
    viz/viz)

;; **histograms**
;;
;; (computed in Clojure, not the browser)
(-> {:x (->> (fn []
               (->> (partial rand-int 2)
                    (repeatedly 200)
                    (reduce +)))
             (repeatedly 9999)
             (map #(* % %)))}
    tc/dataset
    viz/data
    (viz/type [:histogram {:bin-count 30}])
    viz/viz)


;; **bye**
:bye

