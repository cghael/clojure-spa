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
                      (reset! state/*patients {:previous-page []
                                               :current-page (:previous-page @state/*patients)
                                               :next-page (:current-page @state/*patients)})
                      (when (> @state/*page 1) (api/patient-list :back)))})
    "< BACK"]
   [:p.p-number @state/*page]
   [:button.page-btn
    (when (seq (:next-page @state/*patients))
      {:class "active"
       :on-click #(do (swap! state/*page inc) 
                      (reset! state/*patients {:previous-page (:current-page @state/*patients)
                                               :current-page (:next-page @state/*patients)
                                               :next-page []}) 
                      (api/patient-list :next))})
    "NEXT >"]])