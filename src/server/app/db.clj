(ns server.app.db
  (:require [server.app.patients :refer [*patients]]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.pprint :refer [pprint]]
            [db.core :refer [db]]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [clojure.walk :as walk]))


(defn transform-data
  [m]
  (walk/postwalk (fn [x]
                   (cond
                     (instance? java.util.Date x)
                     (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") x)
                     
                     (keyword? x)
                     (keyword (string/replace (name x) #"_" "-"))
                     
                     :else x))
                 m))


(defn patient-list
  [limit page get-pages search-map]
  (if search-map
    (let [search-keys (keys search-map)
          filtered-list (filter (fn [m]
                                  (every? true?
                                          (map #(= (% search-map) (% m))
                                               search-keys))) (vals @*patients))]
      (r/as-success {:data (into [] (take (* limit get-pages) (drop (* limit (dec page)) filtered-list)))}))
    (let [res (jdbc/query db ["select * from spa.patients limit ? offset ?" (* limit get-pages) (* (dec page) limit)])
          res (map #(transform-data %) res)
          _ (pprint res)]
      (r/as-success {:data res})
      #_(r/as-success {:data (into [] (take (* limit get-pages) (drop (* limit (dec page)) (vals @*patients))))}))))


(defn patient-edit
  [params]
  (let [id (:id params)]
    (swap! *patients assoc id params)
    (r/as-accepted {:patient (id @*patients)})))


(defn patient-delete
  [id]
  (swap! *patients dissoc id)
  (r/as-deleted {:id id}))


(defn patient-create
  [params]
  (let [id (->> (into (sorted-map) @*patients)
                last
                first
                str
                (re-find #"\d+")
                Integer.
                inc
                (str "p-")
                keyword)
        params (assoc params :id id)]
    (swap! *patients assoc id params)
    (r/as-created {:patient params})))