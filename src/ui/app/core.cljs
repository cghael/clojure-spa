(ns ui.app.core
  (:require [reagent.core :as r]
            [ui.app.components.content :refer [content]]
            [ui.app.components.menu :refer [menu]]
            [ui.app.api :as api]))


(defn app 
  []
  [:div.container
   [menu]
   [content]])


(defn ^:export main
  "Run application startup logic."
  []
  (api/patient-list :first)
  (r/render 
   [app] 
   (.getElementById js/document "app")))


(comment
  (shadow.cljs.devtools.api/nrepl-select :app)
  )

;; TODO:
;; config параметры считывать а не хардкод
;; mount сделать для компонентов
;; database подключить
;; tests написать тесты для сервака