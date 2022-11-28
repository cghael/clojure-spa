(ns app.components.patient-editor
  (:require [app.state :as state]))


(defn patient-editor
  [{:keys [*patient-window *patient-values initial-values toggle-patient-window]}]
  (let []
    (fn []
      [:div.patient-window__underlay (when @*patient-window {:class "active"})
       [:div.patient-window #_(when @*patient-window {:class "active"})
        [:div.patient-window__body
         "Body"
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
          (str \u2713 " Save")]]]])))