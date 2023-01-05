(ns server.app.db
  (:require [server.app.patients :refer [*patients]]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.pprint :refer [pprint]]
            [db.core :refer [db]]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [clj-time.coerce :as t]))


(defn transform-query-data
  [m]
  (walk/postwalk (fn [x]
                   (cond
                     (instance? java.util.Date x)
                     (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") x)

                     (keyword? x)
                     (keyword (string/replace (name x) #"_" "-"))

                     :else x))
                 m))


(defn transform-update-map
  [m]
  (walk/postwalk (fn [x]
                   (cond
                     (keyword? x)
                     (keyword (string/replace (name x) #"-" "_"))

                     (and (instance? java.lang.String x)
                          (re-find #"\d{4}-\d{2}-\d{2}" x))
                     (t/to-timestamp (t/from-string x))

                     :else x))
                 m))


(defn create-where-string
  [final-string params]
  (let [head (first params)
        tail (rest params)]
    (if (empty? tail)
      (str final-string head)
      (recur (str final-string head " and ") tail))))


(defn add-where-params
  [query-vec search-map]
  (if (nil? search-map)
    query-vec
    (let [search-vals (vals search-map)
          search-params (map (fn [k]
                               (-> (name k)
                                   (string/replace #"-" "_")
                                   (#(if (= k :birth-date)
                                       (str % " = to_timestamp(?, 'YYYY-MM-DD')")
                                       (str % " = ?"))))) (keys search-map))
          where-string (create-where-string " where " search-params)]
      (-> query-vec
          (update 0 #(str % where-string))
          (into search-vals)))))


(defn prepare-query-vec
  [limit page get-pages search-map]
  (let [query-limit (* limit get-pages)
        query-offset (* (dec page) limit)
        query-vec ["select * from spa.patients"]]
    (-> query-vec
        (add-where-params search-map)
        (update 0 #(str % " order by last_name, name limit ? offset ?"))
        (conj query-limit query-offset))))


;; patient-list


(defn patient-list
  [limit page get-pages search-map]
  (try
    (let [query-vec (prepare-query-vec limit page get-pages search-map)
          res (jdbc/query db query-vec)
          transformed-res (map #(transform-query-data %) res)]
      (r/as-success {:data transformed-res}))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-list"
                  :params e})
      (r/as-unavailable {:error "Database is unavailable"}))))


;; patient-edit


(defn patient-edit
  [params]
  (try
    (let [id (:id params)
          params (-> params
                     (dissoc :id)
                     (transform-update-map))
          res (jdbc/update! db :spa.patients params ["id = ?" id])]
      (if (= res '(1))
        (r/as-accepted {:patient id})
        (r/as-not-found {:patient id})))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-update"
                  :params e})
      (r/as-unavailable {:error "Database is unavailable"}))))


;; patient-delete


(defn patient-delete
  [id]
  (try
    (let [res (jdbc/delete! db :spa.patients ["id = ?" id])]
      (if (= res '(1))
        (r/as-deleted {:id id})
        (r/as-not-found {:id id})))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-delete"
                  :params e})
      (r/as-unavailable {:error "Database is unavailable"}))))


;; patient-create


(defn patient-create
  [params]
  (try
    (let [id (random-uuid)
          params (-> params
                     (assoc :id id)
                     transform-update-map)
          res (jdbc/insert! db :spa.patients params)]
      (if res
        (r/as-created {:patient res})
        (r/as-conflict {:patient res})))
    (catch Throwable e
      (log/error {:msg "ERROR db.patient-delete"
                  :params e})
      (r/as-unavailable {:error "Database is unavailable"}))))