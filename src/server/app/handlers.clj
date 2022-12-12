(ns server.app.handlers 
  (:require [server.app.api :as api]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.pprint :refer [pprint]]))


(defn patient-list-handler
  [{{:keys [limit page]} :params :as request}]
  (log/info {:msg "Request patient-list" 
             :params (:params request)})
  (let [res-data (api/patient-list (Integer. limit) (Integer. page))]
    (r/as-http res-data (:headers {"content-type" "application/edn"}))))

(defn problem-by-id
  [request]
  (let [id (:id (:params request))]
    (str "Problem Page for " id)))

(defn next-problem
  [request]
  "Next Problem Page")

(defn hint
  [request]
  (let [id (:id (:params request))]
    (str "Hint for " id)))

(defn solution
  [request]
  (let [id (:id (:params request))]
    (str "Solution for " id)))

(defn not-found
  [request]
  "404: Page not Found")