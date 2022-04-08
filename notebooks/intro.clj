;; # Viz.clj tutorial

;; ## What is this?

;; [Viz.clj](https://github.com/scicloj/viz.clj) is a (work-in-progress) Clojure library for easy data visualizations.

;; ### Where does it fit in the Clojure visual landscape?

;; Viz.clj mostly built on top of [Hanami](https://github.com/jsa-aerial/hanami)'s [template substitutions](https://github.com/jsa-aerial/hanami#templates-substitution-keys-and-transformations) for generating [Vega-Lite](https://vega.github.io/vega-lite/) visualizations.

;; Since it generates Hanami substitutions, which then generate Vega-Lite specs, one may use it without losing any of the power and expressivity of Hanami and Vega-Lite. It can be seen as a shortcut for generating plots in common use cases, which can stay out of the way when one needs to do more sophisticated plotting.

;; It uses [Kindly](https://github.com/scicloj/kindly) to specify in what kinds of ways things should be viewed. Therefore, it is especially convenient to use from [Clay](https://github.com/scicloj/clay), which uses Kindly's kinds to direct various Clojure tools how to render things.

;; For example, Viz.clj would make sure to mark the kind of [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) (and [Tablecloth](https://github.com/scicloj/tablecloth)) datasets appropriately, so that Clay can render them nicely as tables.

;; However, it is not limited to use with Kindly and Clay. Most of it would work in any Clojure tool that knows how to render Vega-Lite visualizations.

;; ### How was it created?

;; Viz.clj's development has been based was based on a research process by Ashima Panjwani, that was based on studying data-visualization APIs in various libraries and languages, as well as gradually evolving the Viz.clj API through testing with users at the [Scicloj study & dev groups](https://scicloj.github.io/docs/community/groups/).

;; That process is not considered finished yet.

;; ## Intro talk

;; [Viz.clj - Clojure data visualization for beginners - Ashima Panjwani - re:Clojure 2021](https://www.youtube.com/watch?v=9Jfw7cTzbAs)

;; ## Setup
(ns scicloj.viz.intro
  (:require [scicloj.viz.api :as viz]
            [scicloj.viz.templates :as viz.templates]
            [scicloj.viz.util :as util]
            [tablecloth.api :as tc]
            [tech.v3.datatype.functional :as fun]
            [tablecloth.pipeline :as tc-pipe]
            [aerial.hanami.templates :as ht]
            [scicloj.kindly.v2.kind :as kind]
            [scicloj.kindly.v2.api :as kindly]
            [scicloj.clay.v1.api :as clay]
            [scicloj.clay.v1.tools :as tools]
            [nextjournal.clerk :as clerk]))

(clay/start! {:tools [tools/clerk]})

(comment
  (clay/restart! {:tools [tools/clerk tools/portal]}))

;; ## Basic examples

;; Here are a couple of examples of creating plots.

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x)
    viz/viz)

(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/color "Beak Depth (mm)")
    (viz/type "point")
    viz/viz)

;; Throughout this tutorial, we will gradually discover how they work.

;; ## How does it work?

;; Throughout the pipeline of transformations, Viz.clj progresses a Clojure map containing the necessary details for plotting.

;; Most of those details are [template substitutions](https://github.com/jsa-aerial/hanami#templates-substitution-keys-and-transformations) of [Hanami](https://github.com/jsa-aerial/hanami), but the beginner user does not need to know that.

;; (As we'll see below, datasets are kept under a special key, complying with the [Metamorph](https://github.com/scicloj/metamorph) approach, but the beginner user does not need to know that.)

;; The plot type is also kept under a special key.

;; The final `viz/viz` call combines all the details and applies the Hanami substitutions, to get a Vega-Lite plot.

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data)

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point))

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200))

;; The final result is marked to have the Vega kind, and just renders accordingly.

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x)
    viz/viz)

;; Let us render it as a plain Clojure data structure, using the naive kind:

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x)
    viz/viz
    kind/naive)

;; ## Just using Hanami

;; Just like using Hanami allows one to use the full flexibility of Vega-Lite (but the beginner user may not need to worry about it too much), using Viz.clj allows one to use the full flexibility of Hanami (but the beginner user does not need to worry about it too much).

;; Fot those who care about it, Viz.clj can be seen as a way to generate and apply Hanami Template substitutions in a dataset-friendly way (see examples with datasets below).

;; (Thus, it also provides a dydactic way of getting many useful examples of Hanami substitions.)

;; For example, let us see the Hanami substitutions comprising the above plot:

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x))

;; We just learned about the `:COLOR` and `:SIZE` keys.

;; It is possible to just pass them directly as substitutions at the final `viz/viz` call:

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/viz {:COLOR "x"
              :SIZE 200}))

;; To make it even closer to a Hanami call, we can use the corresponding Hanami template instead of the shorthand `:point`:

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type ht/point-chart)
    (viz/viz {:COLOR "x"
              :SIZE 200}))

;; In the case of plain data (e.g., vector of maps rather than a dataset), that could be also written this way, passing the data as a substitution as well:

(-> {}
    (viz/type ht/point-chart)
    (viz/viz {:DATA [{:x 1 :y 2}
                     {:x 2 :y 4}
                     {:x 3 :y 9}]
              :COLOR "x"
              :SIZE 200}))

;; Alternatively:

(viz/viz {:viz/type ht/point-chart
          :DATA [{:x 1 :y 2}
                 {:x 2 :y 4}
                 {:x 3 :y 9}]
          :COLOR "x"
          :SIZE 200})

;; This last way of using `Viz.clj` can be seen as just a thin convenience on top of calling Hanami directly.

;; ## Datasets

;; Viz.clj defines sensible rendering behaviour for datasets in Clay:

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset)

;; ## Main principles of plot creation

;; ### Specifying data sources

;; The `viz/data` function allows to specify the data source for a plot. Several options are supported:

;;
;; a sequence of maps
(-> (for [i (range 99)]
      {:x i
       :y (rand)})
    viz/data
    (viz/type "point")
    viz/viz)

;; a local file
(-> (viz/data "resources/data/mpg.csv")
    (viz/type "point")
    (viz/x "displ")
    (viz/y "hwy")
    viz/viz)

;; a file on the web
(-> "https://vega.github.io/vega-lite/data/penguins.json"
    viz/data
    (viz/x "Beak Length (mm)")
    (viz/y "Beak Depth (mm)")
    (viz/type "point")
    viz/viz)

;; a dataset (of tech.ml.dataset / Tablecloth)
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    viz/viz)

;; ### Connecting aesthetics to data

;; x axis
(-> {:w (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/x :w)
    (viz/type "point")
    viz/viz)

;; y axis
(-> {:x (range 99)
     :z (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/y :z)
    (viz/type "point")
    viz/viz)

;; color
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/color :x)
    (viz/type "point")
    viz/viz)

;; size
(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/size :x)
    (viz/type "point")
    viz/viz)

;; ### Customizing mark properties

;; color

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/mark-color "purple")
    viz/viz)

;; size

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :point)
    (viz/mark-size 500)
    viz/viz)

;; ### Picking the mark type

;; point

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :point)
    viz/viz)

;; line

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :line)
    viz/viz)

;; bar

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type :bar)
    viz/viz)

;; a type determined by a Hanami chart template

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type ht/point-chart)
    viz/viz)

;; ## Transforming during the viz pipeline

;; Let us plot some data, having numbers with different orders of magnitude.
(-> {:x (range 10)
     :y (map #(Math/pow 10 %)
             (range 10))}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/mark-size 200)
    viz/viz)

;; A log transformation would help in linarizing the situation.

;; We can use tablecloth.pipeline to transform data after the start of the viz pipeline, since the internal data representation is compatible with [Metamorph](https://github.com/scicloj/metamorph).

(-> {:x (range 10)
     :y (map #(Math/pow 10 %)
             (range 10))}
    tc/dataset
    viz/data
    (viz/type "point")
    (viz/mark-size 200)
    ((tablecloth.pipeline/update-columns [:y] fun/log10))
    viz/viz)

;; The reason it works is that the main dataset is held under a special key, following the Metamorph approach.

(-> {:x (range 10)
     :y (map #(Math/pow 10 %)
             (range 10))}
    tc/dataset
    viz/data
    keys)

;; ## Using more powerful transofmations

;; ### Tweaking the Hanami template substitutions

;; Let us tweak the following plot:

(-> {:u (range 99)
     :v (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/x :u)
    (viz/y :v)
    (viz/type "point")
    viz/viz)

;; Just before the call to the `viz/viz` function, what we have is a representation of a [template substitutions](https://github.com/jsa-aerial/hanami#templates-substitution-keys-and-transformations) of [Hanami](https://github.com/jsa-aerial/hanami) (plus some additional special fields of Viz.clj):

(-> {:u (range 99)
     :v (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/x :u)
    (viz/y :v)
    (viz/type "point"))

;; We can use the full expressivity of Hanami to tweak those template substitutions. For example, let us have a new background color substitution:

(-> {:u (range 99)
     :v (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/x :u)
    (viz/y :v)
    (viz/type "point")
    (assoc :BACKGROUND "#e9e6e3"))

;; Now, we can pass it to `viz/viz` again and get the plot:

(-> {:u (range 99)
     :v (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/x :u)
    (viz/y :v)
    (viz/type "point")
    (assoc :BACKGROUND "#e9e6e3")
    viz/viz)

;; ### Tweaking the resulting vega-lite spec

;; The value returned by `viz/viz` is a Vega-Lite spec (represented as a Clojure data structure). Due to Kindly metadata, it renders as a plot in Clay.

;; Let us assign the naive kind to it, so that it renders as a plain data structure:

(-> {:u (range 99)
     :v (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/x :u)
    (viz/y :v)
    (viz/type "point")
    viz/viz
    kind/naive)

;; Now, let us tweak the Vega-Lite structure with a new background color:

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    viz/viz
    ;; tweaking vega-lite:
    (assoc :background "#e9e6e3")
    kind/naive)

(-> {:x (range 99)
     :y (repeatedly 99 rand)}
    tc/dataset
    viz/data
    (viz/type "point")
    viz/viz
    ;; tweaking vega-lite:
    (assoc :background "#e9e6e3"))

;; ## Layers

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

;; ### regression layers
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

;; ## histograms
;;
;; (computed in Clojure, not in the browser)
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

:bye

(comment
  (clerk/build-static-app!
   {:paths ["notebooks/intro.clj"]}))
