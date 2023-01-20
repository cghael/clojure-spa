(ns db.core
  (:require [server.app.config :refer [config]]
            [mount.core :as mount :refer [defstate]]
            [hikari-cp.core :as cp]))


(defstate db
  :start 
  (let [pool-config (:pool config)
        pool (cp/make-datasource pool-config)]
    {:datasource pool})
  
  :stop 
  (-> db :datasource cp/close-datasource))
