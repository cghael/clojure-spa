(ns ui.app.components.menu
  (:require [ui.app.state :as state]
            [reagent.core :as r]
            [ui.app.components.delete-modal :refer [delete-modal]]
            [ui.app.components.patient-editor :refer [patient-editor]] 
            [clojure.string :as string]
            [ui.app.api :as api]))


(def *patient-window (r/atom nil))

(def *patient-delete-modal (r/atom false))

(def initial-values {:id nil
                     :name ""
                     :s-name ""
                     :sex ""
                     :birth ""
                     :adress ""
                     :oms-number ""})

(def *patient-values (r/atom initial-values))

(def *card-action (r/atom nil))

(defn toggle-patient-window
  [{:keys [window-type patient action]}]
  (reset! *patient-window window-type)
  (reset! *patient-values patient)
  (reset! *card-action action))

(defn save-patient
  [{:keys [id name s-name sex birth adress oms-number]}]
  (if (nil? id)
      ;; Create patient
    (api/patient-create @*patient-values)
      ;; Edit patient
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
      (api/patient-edit @*patient-values)))
  (toggle-patient-window {:window-type nil
                          :patient initial-values
                          :action nil}))

(defn search-patient
  [search-data] 
  (reset! state/*patients {:current-page []
                           :next-page []
                           :previous-page []}) 
  (reset! state/*activ-patient nil)
  (reset! state/*page 1)
  (reset! state/*search-filer (dissoc search-data :id))
  (api/patient-list :first)
  (toggle-patient-window {:window-type nil
                          :patient initial-values
                          :action nil}))

(defn toggle-delete-window
  [value]
  (reset! *patient-delete-modal value))

(defn delete-patient
  [] 
  (api/patient-delete)
  (reset! state/*activ-patient nil)
  (toggle-delete-window false))


(defn menu
  []
  [:div.menu
   [:div.menu-flex
    [:div.menu-buttons
     [:button.menu-btn.active ;;TODO logic
      {:on-click #(toggle-patient-window {:window-type :search
                                          :patient initial-values
                                          :action search-patient})}
      "Search"]
     [:button.menu-btn.active
      {:on-click #(toggle-patient-window {:window-type :create
                                          :patient initial-values
                                          :action save-patient})}
      "Create"]
     [:button.menu-btn (when @state/*activ-patient
                         {:class "active"
                          :on-click #(toggle-patient-window {:window-type :edit
                                                             :patient @state/*activ-patient
                                                             :action save-patient})})
      "Edit"]
     [:button.menu-btn (when @state/*activ-patient
                         {:class "active"
                          :on-click #(toggle-delete-window true)})
      "Delete"]] 
    [patient-editor {:*patient-window *patient-window
                     :*patient-values *patient-values
                     :initial-values initial-values
                     :toggle-patient-window toggle-patient-window
                     :action @*card-action}]
    [delete-modal {:*patient-delete-modal *patient-delete-modal
                   :toggle-delete-window toggle-delete-window
                   :delete-patient delete-patient}]
    [:div.menu-logo
     [:img.img__bottom {:src "img/footer-logo.png"
                        :alt "Patient list logo"}]]]])