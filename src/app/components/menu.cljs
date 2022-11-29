(ns app.components.menu
  (:require [app.state :as state]
            [reagent.core :as r]
            [app.components.delete-modal :refer [delete-modal]]
            [app.components.patient-editor :refer [patient-editor]]
            [clojure.string :as string]))


(defn menu
  []
  (let [*patient-window (r/atom false)
        *patient-delete (r/atom false)
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
        toggle-delete-window #(reset! *patient-delete %)
        save-patient (fn [{:keys [id name s-name sex birth adress oms-number]}]
                       (let [id-number (:patient-number @state/*patients)
                             id (or id (str "p-" (inc id-number)))]
                         (swap! state/*patients assoc-in [:content id] {:id id
                                                                        :name (string/trim name)
                                                                        :s-name (string/trim s-name)
                                                                        :sex (string/trim sex)
                                                                        :birth birth
                                                                        :adress (string/trim adress)
                                                                        :oms-number (string/trim oms-number)})
                         (swap! state/*patients update :patient-number inc)
                         (toggle-patient-window {:active false
                                                 :patient initial-values})))
        delete-patient (fn []
                         (swap! state/*patients update-in [:content] dissoc (:id @state/*activ-id))
                         (swap! state/*patients update :patient-number dec)
                         (reset! state/*activ-id nil)
                         (toggle-delete-window false))]
    (fn []
      [:div.menu
       [:div.menu-flex
        [:div.menu-buttons
         [:button.menu-btn.active
          {:on-click #(toggle-patient-window {:active true
                                              :patient initial-values})}
          "Create"]
         [:button.menu-btn (when @state/*activ-id
                             {:class "active"
                              :on-click #(toggle-patient-window {:active true
                                                                 :patient @state/*activ-id})})
          "Edit"]
         [:button.menu-btn (when @state/*activ-id
                             {:class "active"
                              :on-click #(toggle-delete-window true)})
          "Delete"]]
        [patient-editor {:status "Patient's card"
                         :*patient-window *patient-window
                         :*patient-values *patient-values
                         :initial-values initial-values
                         :toggle-patient-window toggle-patient-window
                         :save-patient save-patient}]
        (.log js/console @*patient-delete)
        [delete-modal {:*patient-delete *patient-delete
                       :toggle-delete-window toggle-delete-window
                       :delete-patient delete-patient}]
        [:div.menu-logo
         [:img.img__bottom {:src "img/footer-logo.png"
                            :alt "Patient list logo"}]]]])))