(ns server.app.api
  (:require [server.app.db :as db]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [clojure.instant :as instant]
            [java-time.api :as jt]))


;; patient-list


(defn transform-response-map
  "Transforms map sequence keys from underscore to dash 
   and convert date instance to string 'yyyy-MM-dd'"
  [m]
  (walk/postwalk (fn [x]
                   (cond
                     (instance? java.util.Date x)
                     (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") x)

                     (keyword? x)
                     (keyword (string/replace (name x) #"_" "-"))

                     :else x))
                 m))


(defn transform-map-keys
  "Transforms keys from dash to underscore"
  [m]
  (reduce-kv (fn [acc k v]
               (let [k (-> (name k)
                           (string/replace #"-" "_")
                           keyword)]
                 (assoc acc k v))) {} m))


(defn patient-list
  [{:keys [limit page get-pages key search-data] :as params}]
  (let [query-limit (* limit get-pages)
        query-offset (* (dec page) limit)
        search-data (when search-data (transform-map-keys search-data)) 
        res-data (db/patient-list {:limit query-limit
                                   :offset query-offset
                                   :search-data search-data})]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-list error" {:res-data res-data
                                            :req-params (:params params)})
        (r/as-unavailable res-data))
      (let [res-data (-> res-data
                         (update :data #(reduce (fn [acc m]
                                                  (conj acc (transform-response-map m))) 
                                                {} %))
                         (assoc :key key))]
        (log/info "db/patient-list success." res-data)
        (r/as-success res-data)))))


;; patient-edit


(defn patient-edit
  [params]
  (let [id (:id params)
        req-params (-> params
                       (dissoc :id)
                       (update :birth-date #(jt/local-date "yyyy-MM-dd" %))
                       transform-map-keys)
        res-data (db/patient-edit req-params id)]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-edit error" {:res-data res-data
                                            :req-params params})
        (r/as-unavailable (assoc res-data :error "DB error")))
      (let [res-data (update res-data :patient transform-response-map)]
        (log/info "db/patient-edit success." res-data)
        (r/as-accepted res-data)))))


;; patient-delete


(defn patient-delete
  [params]
  (let [delete-id (:id params)
        res-data (db/patient-delete delete-id)]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-delete error" {:res-data res-data
                                              :req-params (:body-params params)})
        (r/as-unavailable (assoc res-data :error "DB error")))
      (do
        (log/info "db/patient-delete success." res-data)
        (r/as-deleted res-data)))))


;; patient-create


(defn patient-create
  [params]
  (let [id (random-uuid)
        req-params (-> params
                       (assoc :id id)
                       (update :birth-date #(jt/local-date "yyyy-MM-dd" %))
                       transform-map-keys)
        res-data (db/patient-create req-params)]
    (if (r/-error? res-data)
      (do
        (log/error "db/patient-create error" {:res-data res-data
                                              :req-params params})
        (r/as-unavailable (assoc res-data :error "DB error")))
      (let [res-data (update res-data :patient transform-response-map)]
        (log/info "db/patient-create success." res-data)
        (r/as-created res-data)))))