(ns app.components.content
  (:require [reagent.core :as r]
            [app.components.header :refer [header]]
            [app.components.pages :refer [pages]]
            [app.components.patient-list :refer [patient-list]]))


(defn content
  []
  (let [page-number (r/atom 1)]
    (fn []
      [:div.content
       [header]
       [patient-list page-number]
       [pages page-number]])))