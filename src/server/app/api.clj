(ns server.app.api
  (:require [server.app.db :as db]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.pprint :refer [pprint]]))


(defn patient-list
  [{{:keys [limit page]} :params :as request}] 
  (let [limit' (Integer. limit) ;; todo move coersion to handler level
        page' (Integer. page)
        res-data (db/patient-list limit' page')]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-list error" {:res-data res-data 
                                            :req-params (:params request)})
        (r/as-error (assoc res-data :error "DB error")))
      (do
        (log/info "db/patient-list success." res-data)
        (r/as-success res-data)))))


(defn patient-edit
  [request]
  (let [params (:body-params request)
        res-data (db/patient-edit params)]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-edit error" {:res-data res-data
                                            :req-params params})
        (r/as-error (assoc res-data :error "DB error")))
      (do
        (log/info "db/patient-edit success." res-data)
        (r/as-success res-data)))))