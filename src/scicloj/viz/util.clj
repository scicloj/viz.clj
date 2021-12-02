

(ns scicloj.viz.util
  (:require [com.rpl.specter :as specter]))

(defn fmap
  [f m]
  (specter/transform [specter/MAP-VALS] f m))


(defmacro return-exception-digest [form]
  `(try ~form
        (catch Exception e#
          3
          (-> e#
              Throwable->map
              (select-keys [:cause :data])))))

