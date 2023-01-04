(ns config
  (:require [mount.core :as mount :refer [defstate]]
            [clojure.edn :as edn]))

(defstate config
  :start
  (-> "resources/config.edn"
      slurp
      edn/read-string))