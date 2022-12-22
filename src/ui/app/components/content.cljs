(ns ui.app.components.content
  (:require [ui.app.components.header :refer [header]]
            [ui.app.components.pages :refer [pages]]
            [ui.app.components.patient-list :refer [patient-list]]
            [ui.app.components.alert :refer [alert]]))


(defn content
  []
  [:div.content
   [header]
   [alert]
   [patient-list]
   [pages]])