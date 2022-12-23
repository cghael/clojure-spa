(ns ui.app.components.patient-editor
  (:require [ui.app.state :as state]))


(def card-name {:create "Create new patient"
                :edit "Edit patient's info"
                :search "Search patients"})

(def label_name {"name" "First name"
                 "s-name" "Second name"
                 "sex" "Sex"
                 "birth" "Birth date"
                 "adress" "Adress"
                 "oms-number" "OMS number"})


(defn form-return
  [{:keys [id type value *patient-values]}]
  [:div.form 
   [:label.form__label {:for id} (label_name id)]
   [:input.form__input {:type type
                        :id id
                        :value value 
                        :on-change #(swap! *patient-values assoc (keyword id) (.. % -target -value))
                        }]])


(defn patient-editor
  [{:keys [*patient-window *patient-values initial-values toggle-patient-window action]}]
  (let [{:keys [name s-name sex birth adress oms-number]} @*patient-values]
    [:div.patient-window__underlay (when @*patient-window {:class "active"})
     [:div.patient-window
      [:div.patient-window__body
       [:div.patient-window__status (card-name @*patient-window)] 
       [form-return {:id "name"
                     :type "text"
                     :value name
                     :*patient-values *patient-values}]
       [form-return {:id "s-name"
                     :type "text"
                     :value s-name
                     :*patient-values *patient-values}]
       [form-return {:id "sex"
                     :type "text"
                     :value sex
                     :*patient-values *patient-values}]
       [form-return {:id "birth"
                     :type "date"
                     :value birth
                     :*patient-values *patient-values}]
       [form-return {:id "adress"
                     :type "text"
                     :value adress
                     :*patient-values *patient-values}]
       [form-return {:id "oms-number"
                     :type "text"
                     :value oms-number
                     :*patient-values *patient-values}]]
      [:div.patient-window__footer
       [:button.window-btn {:style {:color "#861414"}
                            :on-click #(toggle-patient-window nil initial-values nil)}
        (str \u2717 " Cancel")]
       [:button.window-btn {:style {:color "#14861c"}
                            :on-click #(action @*patient-values)}
        (str \u2713 " Save")]]]]))