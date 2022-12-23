(ns ui.app.components.delete-modal 
  (:require [ui.app.state :as state]))


(defn delete-modal
  [{:keys [*patient-delete-modal toggle-delete-window delete-patient]}]
  (let [patient (str (:name @state/*activ-patient) " " (:s-name @state/*activ-patient))]
    [:div.patient-window__underlay (when @*patient-delete-modal {:class "active"})
     [:div.patient-delete
      [:div.patient-delete__body
       (str "Do you really want to remove " patient " from the list?")]
      [:div.patient-delete__footer
       [:button.window-btn {:style {:color "#861414"}
                            :on-click #(toggle-delete-window false)}
        (str \u2717 " No")]
       [:button.window-btn {:style {:color "#14861c"}
                            :on-click #(delete-patient)}
        (str \u2713 " Yes")]]]]))