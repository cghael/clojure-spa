(ns app.components.patient-list
  (:require [app.state :as state]
            [reagent.core :as r]))

(defn patient-list
  [page-number]
  (let [activ-id (r/atom nil)
        toggle-activ (fn [id]
                       (if (= id @activ-id)
                         (reset! activ-id nil)
                         (reset! activ-id id)))]
    (fn []
      [:div.patients
       (let [display-patients (take 10 (drop (* 10 (- @page-number 1)) (:content @state/*patients)))
             add-fields (- 10 (count display-patients))] ;;TODO add fields
         (doall (for [{:keys [id name s-name sex birth adress oms-number] :as patient} (vals display-patients)]
                  [:div.patient {:key id}
                   [:div.patient-main (if (= id @activ-id)
                                        {:class "active" :on-click #(toggle-activ id)}
                                        {:on-click #(toggle-activ id)})
                    [:div id]
                    [:div s-name]
                    [:div name]
                    [:div oms-number]
                    [:div.open-hidden (when (= id @activ-id) {:style {:visibility "visible"
                                                                      :transform "rotate(180deg)"}}) "\u02C5"]]
                   [:div.panel (when (= @activ-id id) {:style ;; TODO переделать на .getElementById (третья форма компонентов)
                                                       {:max-height (.-scrollHeight (first (.getElementsByClassName js/document "panel")))}})
                    [:div.panel-grid
                     [:div.free-column]
                    [:div #_{:style {:padding "15px 0 15px 0"}} "Adress:"]
                    [:div #_{:style {:padding "15px 0 15px 0"}} adress]
                    [:div #_{:style {:padding "15px 0 15px 0"}}  "Birth date:"]
                    [:div #_{:style {:padding "15px 0 15px 0"}} birth]
                    [:div #_{:style {:padding "15px 0 15px 0"}} "Sex:"]
                    [:div #_{:style {:padding "15px 0 15px 0"}} sex]]]])))])))