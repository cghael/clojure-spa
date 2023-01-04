(ns server.app.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [server.app.routes :as routes]
            [muuntaja.middleware :as middleware]
            [mount.core :as mount :refer [defstate]]
            [config :refer [config]]
            [taoensso.timbre :as log]))


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
