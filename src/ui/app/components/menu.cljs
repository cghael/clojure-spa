(ns ui.app.components.menu
  (:require [ui.app.state :as state]
            [reagent.core :as r]
            [ui.app.components.delete-modal :refer [delete-modal]]
            [ui.app.components.patient-editor :refer [patient-editor]]
            [ui.app.components.alert :refer [alert]]
            [clojure.string :as string]
            [ui.app.api :as api]))


(def *patient-window (r/atom false))

(def *patient-delete (r/atom false))

(def initial-values {:id nil
                     :name ""
                     :s-name ""
                     :sex ""
                     :birth ""
                     :adress ""
                     :oms-number ""})

(def *patient-values (r/atom initial-values))

(defn toggle-patient-window
  [{:keys [active patient]}]
  (reset! *patient-window active)
  (reset! *patient-values patient))

(defn save-patient
  [{:keys [id name s-name sex birth adress oms-number]}]
  (let [selected-index (.indexOf (:current-page @state/*patients) @state/*activ-patient)]
    (swap! state/*patients
           assoc-in
           [:current-page selected-index]
           {:id id
            :name (string/trim name)
            :s-name (string/trim s-name)
            :sex (string/trim sex)
            :birth birth
            :adress (string/trim adress)
            :oms-number (string/trim oms-number)})
    (api/patient-edit @*patient-values)
    (toggle-patient-window {:active false
                            :patient initial-values})))

(defn toggle-delete-window
  [value]
  (reset! *patient-delete value))

(defn delete-patient
  []
  (swap! state/*patients update-in [:content] dissoc (:id @state/*activ-patient))
  (swap! state/*patients update :patient-number dec) ;;TODO patient-number no more
  (reset! state/*activ-patient nil)
  (toggle-delete-window false))


(defn menu
  []
  [:div.menu
   [:div.menu-flex
    [:div.menu-buttons
     [:button.menu-btn.active ;;TODO logic
      {:on-click #(toggle-patient-window {:active true
                                          :patient initial-values})}
      "Search"]
     [:button.menu-btn.active
      {:on-click #(toggle-patient-window {:active true
                                          :patient initial-values})}
      "Create"]
     [:button.menu-btn (when @state/*activ-patient
                         {:class "active"
                          :on-click #(toggle-patient-window {:active true
                                                             :patient @state/*activ-patient})})
      "Edit"]
     [:button.menu-btn (when @state/*activ-patient
                         {:class "active"
                          :on-click #(toggle-delete-window true)})
      "Delete"]]
    ;; [alert]
    [patient-editor {:status "Patient's card"
                     :*patient-window *patient-window
                     :*patient-values *patient-values
                     :initial-values initial-values
                     :toggle-patient-window toggle-patient-window
                     :save-patient save-patient}]
    [delete-modal {:*patient-delete *patient-delete
                   :toggle-delete-window toggle-delete-window
                   :delete-patient delete-patient}]
    [:div.menu-logo
     [:img.img__bottom {:src "img/footer-logo.png"
                        :alt "Patient list logo"}]]]])