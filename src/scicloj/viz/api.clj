(ns scicloj.viz.api
  (:require [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [scicloj.viz.templates :as vt]
            [scicloj.viz.dataset] ; making sure datasets behave nicely in rendering
            [tech.v3.dataset :as tmd]
            [scicloj.kindly.api :as kindly]
            [scicloj.kindly.kind :as kind]
            [scicloj.tempfiles.api :as tempfiles]
            [clojure.string :as string])
  (:refer-clojure :exclude [type]))

(def map-of-templates {"point" ht/point-chart
                       "boxplot" vt/boxplot-chart})

(defn viz
  [& options-maps]
  (let [options (apply merge options-maps)
        typ (:type options)]
    (-> (apply hc/xform
               (if (map? typ) typ
                   ;; else -- lookup in cagalogue
                   (map-of-templates (name typ)))
               (apply concat (dissoc options :type)))
        (kindly/consider kind/vega))))

(defn path->file-extension [path]
  (-> path
      (string/split #"\.")
      last
      (->> (str "."))))

;; (path->file-extension "data/cars.json")
;; ".json"

(defn as-url
  [data]
  (cond (string? data)
        ;; either a route or a path to a file
        (if (string/starts-with? data "http")
          ;; already a url!
          data
          ;; else -- a path
          (let [{:keys [path route]} (tempfiles/tempfile!
                                    (path->file-extension data))]

            ;; TODO -- do it more efficiently
            (->> data
                slurp
                (spit path))
            route))
        (instance? tech.v3.dataset.impl.dataset.Dataset data)
        ;; a tmd dataset
        (let [{:keys [path route]} (tempfiles/tempfile! ".csv")]
          (tmd/write! data path)
          route)))

(defn data
  "Pass the data as either url/file path (string) or dataset"
  [data]
  (cond (string? data)
        {:UDATA (as-url data)}
        (instance? tech.v3.dataset.impl.dataset.Dataset data)
        {:UDATA (as-url data)}
        :else {:DATA data}))

(defn type
  [viz-map, type]
  (assoc viz-map :type type))

(defn- set-coordinates
  ([viz-map, field-name, {:keys [type] :as options}, field-key]
   (merge viz-map
          {field-key (name field-name)}
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

