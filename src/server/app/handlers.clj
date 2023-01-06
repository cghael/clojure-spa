(ns server.app.handlers
  (:require [server.app.api :as api]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.spec.alpha :as s]
            [clojure.pprint :refer [pprint]]))


;;
;; Spec
;;


(s/def ::ne-string
  (every-pred string? not-empty))

(s/def ::pos-number-string
  (s/and ::ne-string #(re-matches #"^[1-9]\d*" %)))

(s/def ::limit ::pos-number-string)
(s/def ::page ::pos-number-string)
(s/def ::get-pages ::pos-number-string)
(s/def ::key #(contains? #{"first" "back" "next" "all"} %))
(s/def ::id uuid?)
(s/def ::name ::ne-string)
(s/def ::last-name ::ne-string)
(s/def ::sex #(contains? #{"male" "female" "other"} %))
(s/def ::birth-date
  (s/and ::ne-string #(re-matches #"^\d{4}-\d{2}-\d{2}" %)))
(s/def ::adress ::ne-string)
(s/def ::oms-number
  (s/and ::ne-string #(re-matches #"^\d{4}\s\d{6}" %)))

(s/def ::patient-data
  (s/keys :req-un [::id ::name ::last-name ::sex ::birth-date ::adress ::oms-number]))

(s/def ::search-data
  (s/and map? not-empty
         (s/keys :opt-un [::name ::last-name ::sex ::birth-date ::adress ::oms-number])))

(s/def ::params
  (s/keys :req-un [::limit ::page ::get-pages ::key]
          :opt-un [::search-data]))

(s/def ::patient-list-request 
  (s/keys :req-un [::params]))


;;
;; patient-list
;;


(defn patient-list-handler
  [request]
  (log/info {:msg "Request patient-list" 
             :params (:params request)})
    (if (s/valid? ::patient-list-request request)
      (let [res-data (api/patient-list request)]
        (r/as-http res-data (:headers {"content-type" "application/edn"})))
      (r/as-http (r/as-incorrect (s/explain-data ::patient-list-request request)))))


;;
;; patient-edit
;;

(s/def ::patient-edit-request
  (s/keys :req-un [::body-params]
          ::body-params ::patient-data))

(defn patient-edit-handler
  [request]
  (log/info {:msg "Request patient-edit"
             :params request}) 
  (if (s/valid? ::patient-edit-request request)
    (let [res-data (api/patient-edit request)] 
      (r/as-http res-data (:headers {"content-type" "application/edn"})))
    (r/as-http (r/as-incorrect (s/explain-data ::patient-edit-request request)))))


;;
;; patient-delete
;;

(s/def ::patient-delete-request
  (s/keys :req-un [::body-params]
          ::body-params (s/keys :req-un [::id]
                                ::id #(seq %))))

(defn patient-delete-handler
  [request]
  (log/info {:msg "Request patient-delete"
             :params request}) 
(if (s/valid? ::patient-delete-request request)
  (let [res-data (api/patient-delete request)] 
    (r/as-http res-data (:headers {"content-type" "application/edn"})))
  (r/as-http (r/as-incorrect (s/explain-data ::patient-delete-request request)))))


;;
;; patient-create
;;

(s/def ::patient-create-request
  (s/keys :req-un [::body-params]
          ::body-params ::patient-data))

(defn patient-create-handler
  [request]
  (log/info {:msg "Request patient-create"
             :params request}) 
(if (s/valid? ::patient-create-request request)
  (let [res-data (api/patient-create request)] 
    (r/as-http res-data (:headers {"content-type" "application/edn"})))
  (r/as-http (r/as-incorrect (s/explain-data ::patient-create-request request)))))

(defn not-found
  [request]
  "404: Page not Found")