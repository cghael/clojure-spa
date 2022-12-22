(ns ui.app.components.alert
  (:require [ui.app.state :as state]))


(def message {:success "Success!"
              :error "Something goes wrong..."})


(defn alert
  []
  [:div.alert (case @state/*alert 
                :success {:class "active" 
                          :style {:background-color "#47698e"}}
                :error {:class "active"
                        :style {:background-color "#f44336"}}
                :dunno)
   [:div.closebtn {:on-click #(reset! state/*alert nil)}
    (get message @state/*alert)]])