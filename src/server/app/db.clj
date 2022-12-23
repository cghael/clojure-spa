(ns server.app.db
  (:require [server.app.patients :refer [*patients]]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.pprint :refer [pprint]]))


(defn patient-list
  [limit page get-pages search-map]
  (if search-map
    (let [search-keys (keys search-map)
          filtered-list (filter (fn [m]
                                  (every? true?
                                          (map #(= (% search-map) (% m))
                                               search-keys))) (vals @*patients))]
      (r/as-success {:data (into [] (take (* limit get-pages) (drop (* limit (dec page)) filtered-list)))}))
    (r/as-success {:data (into [] (take (* limit get-pages) (drop (* limit (dec page)) (vals @*patients))))})))


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