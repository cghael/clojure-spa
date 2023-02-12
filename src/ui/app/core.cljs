(ns ui.app.core
  (:require [reagent.core :as r]
            [ui.app.components.content :refer [content]]
            [ui.app.components.menu :refer [menu]]
            [ui.app.api :as api]
            [ui.app.state :as state]
            [shadow.resource :as rc]
            [clojure.edn :as edn]))


(defn keywordize-map
  [m]
  (zipmap (map keyword (keys m)) (vals m)))

(defn get-config []
  (let [config (-> "ui-config.edn"
                   rc/inline
                   edn/read-string
                   keywordize-map)
        uri (or (:uri config) "http://88.210.3.43:4000")
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