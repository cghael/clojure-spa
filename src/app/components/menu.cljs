(ns app.components.menu
  (:require [reagent.core :as r]))


(defn menu
  []
  [:div.menu
   [:p "Create"]
   [:p "Read"]
   [:p "Update"]
   [:p "Delete"]
   [:img.img__bottom {:src "img/footer-logo.png"
                      :alt "Patient list logo"}]])