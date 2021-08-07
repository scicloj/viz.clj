(ns scicloj.viz.api
  (:require [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [scicloj.viz.templates :as vt]
            [scicloj.viz.dataset] ; making sure datasets behave nicely in rendering
            [tech.v3.dataset :as tmd]
            [notespace.kinds :as kind])
  (:refer-clojure :exclude [type]))

(def map-of-templates {"point" ht/point-chart
                       "boxplot" vt/boxplot-chart})

(defn viz
  [{:keys [type] :as options}]
  (-> (apply hc/xform
             (if (map? type) type
                 ;; else -- lookup in cagalogue
                 (map-of-templates (name type)))
             (apply concat (dissoc options :type)))
      (kind/override kind/vega)))

(defn data
  "Pass the data as either url/file path (string) or dataset"
  [data]
  (cond (string? data)
        {:UDATA data}
        (instance? tech.v3.dataset.impl.dataset.Dataset data)
        {:DATA (tmd/mapseq-reader data)}
        :else {:DATA data}))

(defn type
  [viz-map, type]
  (assoc viz-map :type type))

(defn- set-coordinates
  ([viz-map, field-name, {:keys [type] :as options}, field-key]
   (merge viz-map
          {field-key field-name}
          (when type {(keyword (str (name field-key) "TYPE")) type})
          (dissoc options :type))))

(defn x
  ([viz-map, field-name]
   (x viz-map field-name {}))
  ([viz-map, field-name, options]
   (set-coordinates viz-map field-name options :X)))

(defn y
  ([viz-map, field-name]
  (y viz-map field-name {}))
  ([viz-map, field-name, options]
   (set-coordinates viz-map field-name options :Y)))

(defn color
  [viz-map, color]
  (assoc viz-map :COLOR color))
