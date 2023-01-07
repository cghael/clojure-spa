(ns server.app.api
  (:require [server.app.db :as db]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.pprint :refer [pprint]]))


(defn patient-list
  [{{:keys [limit page get-pages key search-data]} :params :as request}]
  (let [limit' (if limit (Integer. limit) 10)
        page' (Integer. page)
        get-pages' (Integer. get-pages)
        search-map (when search-data
                    (reduce (fn [acc [k v]]
                              (if (empty? v)
                                acc
                                (assoc acc k v))) {} search-data))
        res-data (db/patient-list limit' page' get-pages' search-map)
        res-data (assoc res-data :key key)]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-list error" {:res-data res-data 
                                            :req-params (:params request)})
        (r/as-error res-data))
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


(defn patient-delete
  [request]
  (let [delete-id (:id (:body-params request))
        res-data (db/patient-delete delete-id)]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-delete error" {:res-data res-data 
                                              :req-params (:body-params request)})
        (r/as-error (assoc res-data :error "DB error")))
      (do
        (log/info "db/patient-delete success." res-data)
        (r/as-success res-data)))))


(defn patient-create
  [request]
  (let [params (:body-params request)
        res-data (db/patient-create params)]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-create error" {:res-data res-data 
                                              :req-params params})
        (r/as-error (assoc res-data :error "DB error")))
      (do
        (log/info "db/patient-create success." res-data)
        (r/as-success res-data)))))