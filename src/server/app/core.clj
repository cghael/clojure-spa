(ns server.app.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [server.app.routes :as routes]
            [muuntaja.middleware :as middleware]
            [mount.core :as mount :refer [defstate]]
            [server.app.config :refer [config]]
            [taoensso.timbre :as log] 
            [db.core :refer [db]]
            [db.app-migrations :refer [migrations-up migrations-down]]))


(def app (-> routes/app
             (middleware/wrap-format)
             (wrap-defaults (-> site-defaults
                                (assoc-in [:params :keywordize] true)
                                (assoc-in [:security :anti-forgery] false)))
             (wrap-cors :access-control-allow-origin [#"http://localhost:3000"]
                        :access-control-allow-methods [:get :put :post :delete])))


(defstate server
  :start
  (let [server-config (:server config)]
    (log/info "Running server with config" server-config)
    (jetty/run-jetty app server-config))

  :stop
  (do (log/info "Stop server.")
      (.stop server)))


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
