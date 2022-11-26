(ns app.core
  (:require [reagent.core :as r]
            [app.components.content :refer [content]]
            [app.components.menu :refer [menu]]))


(defn app 
  []
  [:div.container
   [menu]
   [content]])


(defn ^:export main
  "Run application startup logic."
  []
  (r/render 
   [app] 
   (.getElementById js/document "app")))


(comment
  (shadow.cljs.devtools.api/nrepl-select :app)
  )