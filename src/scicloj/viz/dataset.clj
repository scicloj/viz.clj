(ns scicloj.viz.dataset
  (:require [tech.v3.dataset.impl.dataset]
            [tech.v3.dataset :as tmd]
            [scicloj.tempfiles.api :as tempfiles]
            [scicloj.kindly.v2.api :as kindly]
            [scicloj.kindly.v2.kindness :as kindness]))

(extend-protocol kindness/Kindness
  tech.v3.dataset.impl.dataset.Dataset
  (kind [this]
    :kind/dataset))

(kindly/define-kind-behaviour!
  :kind/dataset
  {:portal.viewer (fn [v]
                    [:portal.viewer/table
                     (seq (tmd/mapseq-reader v))])})

(kindly/define-kind-behaviour!
  :kind/dataset
  {:clerk.viewer (fn [v]
                   #:nextjournal{:value {:head (tmd/column-names v)
                                         :rows (vec (tmd/rowvecs v))}
                                 :viewer :table})})

(defn dataset? [data]
  (instance? tech.v3.dataset.impl.dataset.Dataset data))

(defn dataset->url [dataset]
  (let [{:keys [path route]} (tempfiles/tempfile! ".csv")]
    (tmd/write! dataset path)
    route))

(defn dataset-like? [data]
  (or (and (sequential? data)
           (every? map? data))
      (and (map? data)
           (every? sequential? data))))

(defn throw-if-column-missing [viz-map column-name]
  (let [dataset (:metamorph/data viz-map)]
    (if dataset
      (if (get dataset column-name)
        viz-map
        (throw (ex-info "Missing column"
                        {:column-name column-name})))
      ;; no data -- nothing to check
      viz-map)))
