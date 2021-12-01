

(ns scicloj.viz.util
  (:require [com.rpl.specter :as specter]))

(defn fmap
  [f m]
  (specter/transform [specter/MAP-VALS] f m))



