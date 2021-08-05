(ns viz.templates
  (:require [aerial.hanami.templates :as ht]))

(def boxplot-chart
  (assoc ht/view-base
    :mark (merge ht/mark-base {:type "boxplot"})))
