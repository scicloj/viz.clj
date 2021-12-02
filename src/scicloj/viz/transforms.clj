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


(defn regression-line [xs ys]
  (let [reg    (fun/linear-regressor xs ys)
        fx     (first xs)
        two-xs [fx
                (->> xs
                     (drop-while (fn [x] (= x fx)))
                     first)]]
    (tmd/->dataset {:x two-xs
                    :y (map reg two-xs)})))
