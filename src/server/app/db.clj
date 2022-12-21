(ns server.app.db
  (:require [server.app.patients :refer [*patients]]
            [ninja.unifier.response :as r]))


(defn patient-list
  [limit page]
  (r/as-success {:data (into [] (take limit (drop (* limit (dec page)) (vals @*patients))))}))


(defn patient-edit
  [params]
  (let [id (:id params)]
    (swap! *patients assoc id params)
    (r/as-accepted {:patient (id @*patients)})))