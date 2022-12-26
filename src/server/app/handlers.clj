(ns server.app.handlers
  (:require [server.app.api :as api]
            [ninja.unifier.response :as r]
            [taoensso.timbre :as log]
            [clojure.spec.alpha :as s]
            [clojure.pprint :refer [pprint]]))


;;
;; Common spec
;;

(s/def ::positive-int
  #(pos-int? %))

(s/def ::ne-string
  (every-pred string? not-empty))

(s/def ::ne-string-of-digits
  (s/and ::ne-string #(re-matches #"\d+" %)))

(s/def ::patient-data
  (s/keys :req-un [::id ::name ::s-name ::sex ::birth ::adress ::oms-number]
          ::id #(seq %)
          ::name string?
          ::s-name string?
          ::sex string?
          ::birth string?
          ::adress string?
          ::oms-number string?))


;;
;; patient-list
;;

(s/def ::patient-list-request-params
  (s/keys :req-un [::limit ::page ::get-pages ::key ::search-data]
          ::limit ::ne-string-of-digits
          ::page ::ne-string-of-digits
          ::get-pages ::ne-string-of-digits
          ::key ::ne-string-of-digits
          ::search-data (s/or
                         #(= % "null")
                         (s/keys :req-un [::name ::s-name ::sex ::birth ::adress ::oms-number]
                                 ::name string?
                                 ::s-name string?
                                 ::sex string?
                                 ::birth string?
                                 ::adress string?
                                 ::oms-number string?))))

(s/def ::patient-list-request 
  (s/keys :req-un [::params] 
          ::params ::patient-list-request-params))

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