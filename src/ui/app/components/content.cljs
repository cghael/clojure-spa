(ns ui.app.components.content
  (:require [ui.app.components.header :refer [header]]
            [ui.app.components.pages :refer [pages]]
            [ui.app.components.patient-list :refer [patient-list]]))


(defn content
  []
  [:div.content
   [header]
   [patient-list]
   [pages]])