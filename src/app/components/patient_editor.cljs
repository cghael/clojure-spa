(ns app.components.patient-editor
  (:require [app.state :as state]))


(defn form-return
  [{:keys [id type value *patient-values]}]
  [:div.form
   [:lable.form__lable #_{:for id} id]
   [:input.form__input {:type type
                      :id id
                      :value value}]])


(defn patient-editor
  [{:keys [status *patient-window *patient-values initial-values toggle-patient-window]}]
  (let [{:keys [name s-name sex birth adress oms-number]} @*patient-values]
    [:div.patient-window__underlay (when @*patient-window {:class "active"})
     [:div.patient-window
      [:div.patient-window__body
       [:div.patient-window__status status]
       [form-return {:id "Name"
                     :type "text"
                     :value name
                     :*patient-values *patient-values}]
       [form-return {:id "Second name"
                     :type "text"
                     :value s-name
                     :*patient-values *patient-values}]
       [form-return {:id "Sex"
                     :type "text"
                     :value sex
                     :*patient-values *patient-values}]
       [form-return {:id "Birthday date"
                     :type "text"
                     :value birth
                     :*patient-values *patient-values}]
       [form-return {:id "Adress"
                     :type "text"
                     :value adress
                     :*patient-values *patient-values}]
       [form-return {:id "OMS number"
                     :type "text"
                     :value oms-number
                     :*patient-values *patient-values}]
      ;;  [:div.form
      ;;   [:lable.form__lable "Name"]
      ;;   [:input.form__input {:type "text"
      ;;                        :id "name"
      ;;                        :value "Pipa"}]]
          ;; [:input]
          ;; [:input]
          ;; [:input]
          ;; [:input]
          ;; [:input]
          ;; [:input]
       ]
      [:div.patient-window__footer
       [:button.window-btn {:style {:color "#861414"}
                            :on-click #(toggle-patient-window false initial-values)}
        (str \u2717 " Cancel")]
       [:button.window-btn {:style {:color "#14861c"}}
        (str \u2713 " Save")]]]]))