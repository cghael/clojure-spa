(ns ui.app.components.patient-list
  (:require [ui.app.state :as state]))

(defn patient-list
  []
  (let [toggle-activ (fn [{:keys [id name s-name sex birth adress oms-number]}]
                       (if (= id (:id @state/*activ-patient))
                         (reset! state/*activ-patient nil)
                         (reset! state/*activ-patient {:id id
                                                  :name name
                                                  :s-name s-name
                                                  :sex sex
                                                  :birth birth
                                                  :adress adress
                                                  :oms-number oms-number})))]
    (fn []
      [:div.patients
       (let [display-patients (:current-page @state/*patients)]
         (doall (for [{:keys [id name s-name sex birth adress oms-number] :as patient} display-patients]
                  [:div.patient {:key id}
                   [:div.patient-main (if (= id (:id @state/*activ-patient))
                                        {:class "active" :on-click #(toggle-activ patient)}
                                        {:on-click #(toggle-activ patient)})
                    [:div id]
                    [:div s-name]
                    [:div name]
                    [:div {:style {:overflow "hidden"}} oms-number]
                    [:div.open-hidden (when (= id (:id @state/*activ-patient)) 
                                        {:style {:visibility "visible" 
                                                 :transform "rotate(180deg)"}}) 
                     "\u02C5"]]
                   [:div.panel (when (= (:id @state/*activ-patient) id) {:style ;; TODO переделать на .getElementById (третья форма компонентов) 
                                                                    {:max-height (.-scrollHeight (first (.getElementsByClassName js/document "panel")))}})
                    [:div.panel-grid
                     [:div.free-column]
                    [:div "Adress:"]
                    [:div adress]
                    [:div  "Birth date:"]
                    [:div birth]
                    [:div "Sex:"]
                    [:div sex]]]])))])))