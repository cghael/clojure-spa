(ns server.app.api
  (:require [server.app.patients :as db]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.pprint :refer [pprint]]))


(defn patient-list
  [limit page]
  (let [res-data (into {} (take limit (drop (* limit (dec page)) @db/*patients)))]
    (if (empty? res-data)
      (do
        (log/error "Patients not found.")
        (r/as-not-found res-data))
      (do
        (log/info "Patients found successfully.")
        (r/as-success res-data)))))