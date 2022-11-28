(ns app.components.menu
  (:require [app.state :as state]
            [reagent.core :as r]
            [app.components.patient-editor :refer [patient-editor]]
            [clojure.string :as string]))


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
                                (reset! *patient-values patient))
        save-patient (fn [{:keys [id name s-name sex birth adress oms-number]}]
                       (let [id-number (:patient-number @state/*patients)
                             id (or id (str "p-" (inc id-number)))
                             _ (.log js/console id)]
                         (swap! state/*patients assoc-in [:content id] {:id id #_(or id (str "p-" (inc id-number)))
                                                                        :name (string/trim name)
                                                                        :s-name (string/trim s-name)
                                                                        :sex (string/trim sex)
                                                                        :birth birth
                                                                        :adress (string/trim adress)
                                                                        :oms-number (string/trim oms-number)})
                         (swap! state/*patients update :patient-number inc)
                         (toggle-patient-window {:active false
                                                 :patient initial-values}))
                       (.log js/console (:patient-number @state/*patients))
                       (.log js/console (:content @state/*patients)))]
    (fn []
      [:div.menu
       [:div.menu-flex
        [:div.menu-buttons
         [:button.menu-btn.active
          {:on-click #(toggle-patient-window {:active true
                                              :patient initial-values})}
          "Create"]
         [:button.menu-btn (when @state/*activ-id {:class "active"})
          "Update"]
         [:button.menu-btn (when @state/*activ-id {:class "active"})
          "Delete"]]
        [patient-editor {:status "Add new patient"
                         :*patient-window *patient-window
                         :*patient-values *patient-values
                         :initial-values initial-values
                         :toggle-patient-window toggle-patient-window
                         :save-patient save-patient}]
        [:div.menu-logo
         [:img.img__bottom {:src "img/footer-logo.png"
                            :alt "Patient list logo"}]]]])))