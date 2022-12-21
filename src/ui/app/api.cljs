(ns ui.app.api
  (:require [ajax.core :refer [GET POST PUT]]
            [ajax.edn :refer [edn-response-format edn-read]]
            [ui.app.state :as state]))


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
  (.log js/console response)
  (.log js/console (:patient response))
  (.log js/console @state/*activ-id)
  (.log js/console (@state/*activ-id @state/*patients))
  (when (not= (:patient response) (:id @state/*activ-id))
    (.log js/console "ERROR")))


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
