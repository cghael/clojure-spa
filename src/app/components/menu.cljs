(ns app.components.menu
  (:require [app.state :as state]
            [reagent.core :as r]
            [app.components.patient-editor :refer [patient-editor]]))


(defn menu
  []
  (let [*patient-window (r/atom false)
        initial-values {:id nil
                        :name ""
                        :s-name ""
                        :sex ""
                        :birth ""
                        :adress ""
                        :oms-number ""}
        *patient-values (r/atom initial-values)
        toggle-patient-window (fn [{:keys [active patient]}]
                                (reset! *patient-window active)
                                (reset! *patient-values patient))]
    (fn []
      [:div.menu
       [:div.menu-flex
        [:div.menu-buttons
         [:button.menu-btn.active
          {:on-click #(toggle-patient-window {:active true 
                                              :patient *patient-values})}
          "Create"]
         [:button.menu-btn (when @state/*activ-id {:class "active"})
          "Update"]
         [:button.menu-btn (when @state/*activ-id {:class "active"})
          "Delete"]]
        [patient-editor {:*patient-window *patient-window 
                         :*patient-values *patient-values
                         :initial-values initial-values
                         :toggle-patient-window toggle-patient-window}]
        [:div.menu-logo
         [:img.img__bottom {:src "img/footer-logo.png"
                            :alt "Patient list logo"}]]]])))