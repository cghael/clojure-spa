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
(s/def ::key #{"first" "back" "next" "all"})
(s/def ::id uuid?)
(s/def ::name ::ne-string)
(s/def ::last-name ::ne-string)
(s/def ::sex #{"male" "female" "other"})
(s/def ::birth-date
  (s/and ::ne-string #(re-matches #"^\d{4}-\d{2}-\d{2}" %)))
(s/def ::adress ::ne-string)
(s/def ::oms-number
  (s/and ::ne-string #(re-matches #"^\d{4}\s\d{6}" %)))

(s/def ::patient-data
  (s/keys :req-un [::name ::last-name ::sex ::birth-date ::adress ::oms-number]
          :opt-un [::id]))

(s/def ::search-data
  (s/and map? not-empty
         (s/keys :opt-un [::name ::last-name ::sex ::birth-date ::adress ::oms-number]))) 

(s/def ::params
  (s/keys :req-un [::page ::get-pages ::key]
          :opt-un [::limit ::search-data]))

(s/def ::patient-list-request 
  (s/keys :req-un [::params]))

(s/def ::body-params 
  (s/or :delete (s/and #(= 1 (count %))
                       (s/keys :req-un [::id]) )
        :edit-create ::patient-data))

(s/def ::patient-edit-create-request
  (s/keys :req-un [::body-params]))

(s/def ::patient-delete-request
  (s/keys :req-un [::body-params]))

(s/def ::->transform-list-request
  (s/conformer
   (fn [params]
     (try
       (-> params
           (update :limit (fn [x]
                            (if x 
                              (Integer/parseInt x) 
                              10)))
           (update :page #(Integer/parseInt %))
           (update :get-pages #(Integer/parseInt %)))
       (catch Exception e
         ::s/invalid)))))

(s/valid? ::patient-edit-create-request {:name "Andrea"
                                         :last-name "Bragnikov"
                                         :sex "female"
                                         :birth-date "1987-02-28"
                                         :adress "Georgia, Kabuliti, St. Pushkina, 18"
                                         :oms-number "9745 984392"})


;;
;; patient-list
;;


(defn patient-list-handler
  [request]
  (log/info {:msg "Request patient-list" 
             :params (:params request)})
    (if (s/valid? ::patient-list-request request)
      (let [params (s/conform ::->transform-list-request (:params request))]
        (if (s/invalid? params)
          (r/as-http (r/as-incorrect {:error "invalid request params"
                                      :request request}))
          (-> (api/patient-list params)
              (r/as-http (:headers {"content-type" "application/edn"})))))
      (r/as-http (r/as-incorrect (s/explain-data ::patient-list-request request)))))


;;
;; patient-edit
;;


(defn patient-edit-handler
  [request]
  (log/info {:msg "Request patient-edit"
             :params request}) 
  (if (s/valid? ::patient-edit-create-request request)
    (let [res-data (api/patient-edit (:body-params request))] 
      (r/as-http res-data (:headers {"content-type" "application/edn"})))
    (r/as-http (r/as-incorrect (s/explain-data ::patient-edit-create-request request)))))


;;
;; patient-delete
;;


(defn patient-delete-handler
  [request]
  (log/info {:msg "Request patient-delete"
             :params request}) 
(if (s/valid? ::patient-delete-request request)
  (let [res-data (api/patient-delete (:body-params request))] 
    (r/as-http res-data (:headers {"content-type" "application/edn"})))
  (r/as-http (r/as-incorrect (s/explain-data ::patient-delete-request request)))))


;;
;; patient-create
;;


(defn patient-create-handler
  [request]
  (log/info {:msg "Request patient-create"
             :params request}) 
(if (s/valid? ::patient-edit-create-request request)
  (let [res-data (api/patient-create (:body-params request))] 
    (r/as-http res-data (:headers {"content-type" "application/edn"})))
  (r/as-http (r/as-incorrect (s/explain-data ::patient-edit-create-request request)))))


(defn not-found
  [request]
  "404: Page not Found")