(ns scicloj.viz.api
  (:require [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [scicloj.viz.templates :as vt]
            [scicloj.kindly.api :as kindly]
            [scicloj.kindly.kind :as kind]
            [scicloj.tempfiles.api :as tempfiles]
            [clojure.string :as string]
            [tech.v3.dataset :as tmd]
            [scicloj.viz.dataset :as dataset] ; making sure datasets behave nicely in rendering
            [scicloj.viz.config :as config]
            [scicloj.viz.paths :as paths])
  (:refer-clojure :exclude [type]))

(def map-of-templates {"point" ht/point-chart
                       "boxplot" vt/boxplot-chart})

(defn viz
  [base-options & args]
  (let [arg1 (first args)
        additional-options (cond (nil? arg1) {}
                                 (map? arg1)     (apply merge args)
                                 (keyword? arg1) (apply hash-map args))
        options (merge base-options
                       additional-options)
        typ (:type options)
        template (if (map? typ) typ
                     ;; else -- lookup in cagalogue
                     (map-of-templates (name typ)))]
   (when (nil? typ)
      (throw (ex-info "Missing viz type" {})))
    (-> options
        (dissoc :type)
        (->> (apply concat)
             (apply hc/xform template))
        (kindly/consider kind/vega))))


(defn data-without-tempfiles [data]
  (cond (string? data)          (if (paths/url? data) {:UDATA data}
                                    ;; not a url -- assuming a local path
                                    (let [file-type (paths/file-type "csv")]
                                      (case file-type
                                        "csv" {:DATA (-> data
                                                         paths/throw-if-not-exists!
                                                         slurp)
                                               :DFMT {:type file-type}}
                                        (throw (ex-info "Unsupported file type"
                                                        {:file-type file-type})))))
        (dataset/dataset? data) {:DATA (tmd/mapseq-reader data)
                                 :metamorph/data data}
        :else                   {:DATA data}))

(defn data-with-tempfiles [data]
  (cond (string? data) (if (paths/url? data)            {:UDATA data}
                           ;; not a url -- assuming a local path
                           (let [{:keys [path route]} (tempfiles/tempfile!
                                                       (str "." (paths/file-type data)))]
                             ;; TODO -- do it more efficiently
                             (->> data
                                  paths/throw-if-not-exists!
                                  slurp
                                  (spit path))
                             {:UDATA route}))
        (dataset/dataset? data)      {:UDATA (let [{:keys [path route]} (tempfiles/tempfile! ".csv")]
                                               (tmd/write! data path)
                                               route)
                                      :metamorph/data data}
        (dataset/dataset-like? data) (-> data
                                         tmd/->dataset
                                         data-with-tempfiles)
        :else                        {:DATA data}))


(defn data
  "Pass the data as either url/file path (string) or dataset"
  [data]
  (if (config/use-tempfiles?)
    (data-with-tempfiles data)
    (data-without-tempfiles data)))

(defn type
  [viz-map type]
  (assoc viz-map :type type))

(defn- set-coordinates
  ([viz-map field-name, {:keys [type] :as options}, field-key]
   (-> viz-map
       (dataset/throw-if-column-missing field-name)
       (merge {field-key (name field-name)}
              (when type {(keyword (str (name field-key) "TYPE")) type})
              (dissoc options :type)))))

(defn x
  ([viz-map field-name]
   (x viz-map field-name {}))
  ([viz-map field-name, options]
   (set-coordinates viz-map field-name options :X)))

(defn y
  ([viz-map field-name]
  (y viz-map field-name {}))
  ([viz-map field-name, options]
   (set-coordinates viz-map field-name options :Y)))

(defn color
  [viz-map field-name]
  (-> viz-map
      (dataset/throw-if-column-missing field-name)
      (assoc :COLOR (name field-name))))

