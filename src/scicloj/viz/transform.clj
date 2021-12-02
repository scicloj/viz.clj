(ns scicloj.viz.transform
  (:require [tech.v3.dataset :as tmd]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.functional :as fun]))

;; based on the histogram of https://github.com/techascent/tech.viz
(defn bin
  ([values]
   (bin values {}))
  ([values {:keys [bin-count] :as options}]
   (let [n-values       (count values)
         minimum        (fun/reduce-min values)
         maximum        (fun/reduce-max values)
         bin-count      (int (or bin-count
                            (Math/ceil (Math/log n-values))))
         bin-width      (double (/ (- maximum minimum) bin-count))
         counts (dtype/make-container :int32 bin-count)]
     (doseq [v values]
       (let [bin-index (min (int (quot (- v minimum)
                                       bin-width))
                            (dec bin-count))]
         (->> bin-index
              counts
              inc
              (dtype/set-value! counts bin-index))))
     (-> {:count counts
          :left  (dtype/make-reader :float32 bin-count
                                   (+ minimum (* idx bin-width)))
          :right (dtype/make-reader :float32 bin-count
                                    (+ minimum (* (inc idx) bin-width)))}
         tmd/->dataset))))


(defn regression-line [dataset x y]
  (let [xs     (-> dataset (get x))
        ys     (-> dataset (get y))
        reg    (fun/linear-regressor xs ys)]
    (->> [(fun/reduce-min xs)
          (fun/reduce-max xs)]
         (mapv (fn [xval]
                 {x xval
                  y (reg xval)})))))

