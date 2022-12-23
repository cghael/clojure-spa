(ns server.app.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [server.app.routes :as routes]
            [muuntaja.middleware :as middleware]))


(def app (-> routes/app 
             (middleware/wrap-format)
             (wrap-defaults (-> site-defaults
                                (assoc-in [:params :keywordize] true)
                                (assoc-in [:security :anti-forgery] false)))
             (wrap-cors :access-control-allow-origin [#"http://localhost:3000"]
                        :access-control-allow-methods [:get :put :post :delete])))


(defn server
  [port host]
  (jetty/run-jetty app {:host  host
                        :port  port
                        :join? false}))


(defn -main
  "Start point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "4000"))
        host (or (System/getenv "HOST") "127.0.0.1")]
    (server port host)
    (println (format "> Running server at %s port %d" host port))))
