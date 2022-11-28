(ns app.components.patient-editor)


(defn form-return
  [{:keys [id type value *patient-values]}]
  [:div.form 
   [:label.form__label {:for id} id]
   [:input.form__input {:type type
                        :id id
                        :value value 
                        :on-change #(swap! *patient-values assoc (keyword id) (.. % -target -value))
                        }]])


(defn patient-editor
  [{:keys [status *patient-window *patient-values initial-values toggle-patient-window save-patient]}]
  (let [{:keys [name s-name sex birth adress oms-number]} @*patient-values]
    [:div.patient-window__underlay (when @*patient-window {:class "active"})
     [:div.patient-window
      [:div.patient-window__body
       [:div.patient-window__status status]
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
                            :on-click #(toggle-patient-window false initial-values)}
        (str \u2717 " Cancel")]
       [:button.window-btn {:style {:color "#14861c"}
                            :on-click #(save-patient @*patient-values)}
        (str \u2713 " Save")]]]]))