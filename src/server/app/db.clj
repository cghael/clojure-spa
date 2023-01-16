(ns server.app.db
  (:require [ninja.unifier.response :as r]
            [taoensso.timbre :as log] 
            [db.core :refer [db]]
            [clojure.java.jdbc :as jdbc]))


;; patient-list


(defn- create-where-string
  [final-string params]
  (let [head (first params)
        tail (rest params)]
    (if (empty? tail)
      (str final-string head)
      (recur (str final-string head " and ") tail))))


(defn- add-where-params
  [query-vec search-data]
  (if (nil? search-data)
    query-vec
    (let [search-vals (vals search-data)
          search-params (map (fn [k]
                               (-> (name k)
                                   (#(if (= k :birth_date)
                                       (str % " = to_timestamp(?, 'YYYY-MM-DD')")
                                       (str % " = ?"))))) (keys search-data))
          where-string (create-where-string " where " search-params)]
      (-> query-vec
          (update 0 #(str % where-string))
          (into search-vals)))))


(defn- prepare-query-vec
  [{:keys [limit offset search-data]}]
  (-> ["select * from spa.patients"]
      (add-where-params search-data)
      (update 0 #(str % " order by last_name, name limit ? offset ?"))
      (conj limit offset)))


(defn patient-list
  [req-params]
  (try
    (let [query-vec (prepare-query-vec req-params)
          res (jdbc/query db query-vec)]
      (r/as-success {:data res}))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-list"
                  :params e})
      (r/as-error {:error "Database is unavailable"}))))


;; patient-edit


(defn patient-edit
  [req-params id]
  (try
    (let [res (jdbc/update! db :spa.patients req-params ["id = ?" id])]
      (if (empty? res)
        (r/as-error {:patient id})
        (r/as-success {:patient id})))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-update"
                  :params e})
      (r/as-error {:error "Database is unavailable"}))))


;; patient-delete


(defn patient-delete
  [id]
  (try
    (let [res (jdbc/delete! db :spa.patients ["id = ?" id])]
      (if (empty? res)
        (r/as-error {:id id})
        (r/as-success {:id id})))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-delete"
                  :params e})
      (r/as-error {:error "Database is unavailable"}))))


;; patient-create


(defn patient-create
  [req-params]
  (try
    (let [res (jdbc/insert! db :spa.patients req-params)]
      (r/as-success {:patient (first res)}))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-delete"
                  :params e})
      (r/as-error {:error e}))))