(ns server.app.config
  (:require [mount.core :as mount :refer [defstate]]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))


(defstate config
  :start
  (-> "config.edn"
      io/resource
      io/reader
      slurp
      edn/read-string))