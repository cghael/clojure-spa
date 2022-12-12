(ns ui.app.components.pages
  (:require [ui.app.state :as state]
            [ui.app.api :as api]))

(defn pages 
  []
  [:div.pages
   [:button.page-btn 
    (when (> @state/*page 1) 
      {:class "active" 
       :on-click #(do (swap! state/*page dec)
                      (api/patient-list))})
    "< BACK"]
   [:p.p-number @state/*page]
   [:button.page-btn
    (when (seq (:next-page @state/*patients))
      {:class "active"
       :on-click #(do (swap! state/*page inc)
                      (api/patient-list))})
    "NEXT >"]])