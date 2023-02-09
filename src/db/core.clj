(ns db.core
  (:require [server.app.config :refer [config]]
            [mount.core :as mount :refer [defstate]]
            [hikari-cp.core :as cp]
            [taoensso.timbre :as log]))


(defstate db
  :start 
  (let [pool-config (:pool config)
        pool (cp/make-datasource pool-config)
        _ (log/info pool)]
    {:datasource pool})
  
  :stop 
  (-> db :datasource cp/close-datasource))
