(ns app.components.pages
  (:require [app.state :as state]))

(defn pages 
  [page-number]
  [:div.pages
   [:button.page-btn 
    (when (> @page-number 1) 
      {:class "active" 
       :on-click #(swap! page-number dec)})
    "< BACK"]
   [:p.p-number @page-number]
   [:button.page-btn
    (when (<= (* @page-number 10) (:count @state/*patients))
      {:class "active"
       :on-click #(swap! page-number inc)})
    "NEXT >"]])