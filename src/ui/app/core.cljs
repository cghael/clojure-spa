(ns ui.app.core
  (:require [reagent.core :as r]
            [ui.app.components.content :refer [content]]
            [ui.app.components.menu :refer [menu]]
            [ui.app.api :as api]
            [clojure.java.io :as io]
            [aero.core :refer [read-config]]
            [ui.app.state :as state]))

(defn get-config []
  (let [config (-> "config.edn"
                   clojure.java.io/resource
                   read-config
                   :ui)
        uri (or (:uri config) "http://localhost:4000")
        limit (or (:limit config) 10)]
    (reset! state/*config {:uri uri 
                           :limit limit})))


(defn app 
  []
  [:div.container
   [menu]
   [content]])


(defn ^:export main
  "Run application startup logic."
  []
  (get-config)
  (api/patient-list :first)
  (r/render 
   [app] 
   (.getElementById js/document "app")))


(comment
  (shadow.cljs.devtools.api/nrepl-select :app)
  )