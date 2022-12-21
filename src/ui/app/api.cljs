(ns ui.app.api
  (:require [ajax.core :refer [GET POST PUT]]
            [ajax.edn :refer [edn-response-format edn-read]]
            [ui.app.state :as state]
            [clojure.string :as string]))


(def server-uri "http://localhost:4000") ;;todo add to config

;;
;; patient-list
;;

(defn patient-list-handler 
  [response]
  (.log js/console response) ;;todo delete
  (let [data (:data response)]
   (cond
    (empty? (:current-page @state/*patients))
    (reset! state/*patients {:previous-page []
                             :current-page (vec (take 10 data))
                             :next-page (vec (drop 10 data))})

    (and (empty? (:previous-page @state/*patients))
         (> @state/*page 1))
    (swap! state/*patients assoc :previous-page data)

    (empty? (:next-page @state/*patients))
    (swap! state/*patients assoc :next-page data)

    :else
    (.log js/console "Else hapend"))))


(defn patient-list-error-handler 
  [{:keys [status status-text]}]
  (.log js/console (str "No patients found: " status " " status-text)))


(def what-page {:first {:limit 20 ;todo add to config
                        :page identity}
                :next {:limit 10
                       :page inc}
                :back {:limit 10
                       :page dec}})


(defn patient-list
  [page-key]
  (let [limit (:limit (page-key what-page))
        page ((:page (page-key what-page)) @state/*page)]
    (GET (str server-uri "/patient-list")
      {:params {:limit limit
                :page page}
       :handler patient-list-handler
       :error-handler patient-list-error-handler})))

;;
;; patient-edit
;;

(defn patient-edit-handler
  [response]
  ;todo add message about success/unsuccess
  (let [patient (:patient response)]
    (if (= (:id patient) (:id @state/*activ-patient))
      (reset! state/*activ-patient patient)
      (do
        (.log js/console "ERROR db edit patient")
        (let [{:keys [id
                      name
                      s-name
                      sex
                      birth
                      adress
                      oms-number]} @state/*activ-patient
              page-users (:current-page @state/*patients)
              selected-patient (first (filter #(= id (:id %)) page-users))
              selected-index (.indexOf page-users selected-patient)]
          (swap! state/*patients
                 assoc-in
                 [:current-page selected-index]
                 {:id id
                  :name (string/trim name)
                  :s-name (string/trim s-name)
                  :sex (string/trim sex)
                  :birth birth
                  :adress (string/trim adress)
                  :oms-number (string/trim oms-number)}))))))


(defn patient-edit-error-handler
  [{:keys [status status-text]}]
  (.log js/console (str "No patients found: " status " " status-text)))


(defn patient-edit
  [patient-values]
  (PUT (str server-uri "/patient-edit")
    {:headers {"Content-Type" "application/edn"}
     :body patient-values
     :handler patient-edit-handler
     :error-handler patient-edit-error-handler}))
