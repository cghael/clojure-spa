(ns core
  (:require [config :refer [config]]
            [db.core :refer [db]]
            [server.app.core :refer [server]]
            [taoensso.timbre :as log]
            [mount.core :as mount]
            [db.app-migrations :refer [migrations-up migrations-down]]))


(defn stop!
  []
  (let [_ (migrations-down config db)
        status (mount/stop #'server #'db)]
    status))

(defn start!
  []
  (let [status (mount/start #'config #'db #'server)
        _ (.addShutdownHook (Runtime/getRuntime) (Thread. stop!)) 
        _ (migrations-up config) 
        _ (log/info status)]
    status))

(defn -main
  "Start point"
  [& args]
  (start!))