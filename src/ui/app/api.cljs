(ns ui.app.api
  (:require [ajax.core :refer [GET POST PUT DELETE]]
            [ajax.edn :refer [edn-response-format edn-read]]
            [ui.app.state :as state]
            [clojure.string :as string]))


(def server-uri "http://localhost:4000") ;;todo add to config

(defn- alert-pop-up
  [result]
  (reset! state/*alert result)
  (js/setTimeout #(reset! state/*alert nil) 3000))

;;
;; patient-list
;;

(defn- patient-list-handler
  [response]
  (.log js/console response) ;;todo delete
  (let [data (:data response)
        key (keyword (:key response))]
    (case key
      :first
      (reset! state/*patients {:previous-page []
                               :current-page (vec (take 10 data))
                               :next-page (vec (drop 10 data))})

      :back
      (swap! state/*patients assoc :previous-page data)

      :next
      (swap! state/*patients assoc :next-page data)
      
      :all
      (reset! state/*patients {:previous-page (vec (take 10 data))
                               :current-page (vec (take 10 (drop 10 data)))
                               :next-page (vec (drop 20 data))})

      (.log js/console "Else hapend"))))


(defn- patient-list-error-handler
  [{:keys [status status-text]}]
  (.log js/console (str "No patients found: " status " " status-text)))


(def what-page {:first {:get-pages 2
                        :limit 10 ;todo add to config
                        :page identity}
                :next {:get-pages 1
                       :limit 10
                       :page inc}
                :back {:get-pages 1
                       :limit 10
                       :page dec}
                :all {:get-pages 3
                      :limit 10
                       :page dec}})


(defn patient-list
  [page-key]
  (let [page-props (page-key what-page)
        limit (:limit page-props)
        page ((:page page-props) @state/*page)
        get-pages (:get-pages page-props)]
    (GET (str server-uri "/patient-list")
      {:params {:limit limit
                :page page
                :get-pages get-pages
                :key page-key
                :search-data @state/*search-filer}
       :handler patient-list-handler
       :error-handler patient-list-error-handler})))

;;
;; patient-edit
;;

(defn- patient-edit-handler
  [response]
  (reset! state/*activ-patient (:patient response))
  (alert-pop-up :success))


(defn- patient-edit-error-handler
  [{:keys [status status-text]}]
  (.log js/console (str "No patients found: " status " " status-text))
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
            :oms-number (string/trim oms-number)})
    (alert-pop-up :error)))


(defn patient-edit
  [patient-values]
  (PUT (str server-uri "/patient-edit")
    {:headers {"Content-Type" "application/edn"}
     :body patient-values
     :handler patient-edit-handler
     :error-handler patient-edit-error-handler}))

;;
;; patient-delete
;;

(defn- patient-delete-handler
  [response]
  (.log js/console response)
  (if (> @state/*page 1)
    (patient-list :all)
    (patient-list :first))
  (alert-pop-up :success))

(defn- patient-delete-error-handler
  [{:keys [status status-text]}]
  (.log js/console (str "No patients found: " status " " status-text))
  (alert-pop-up :error))

(defn patient-delete
  []
  (DELETE (str server-uri "/patient-delete")
    {:headers {"Content-Type" "application/edn"}
     :body {:id (:id @state/*activ-patient)}
     :handler patient-delete-handler
     :error-handler patient-delete-error-handler}))

;;
;; patient-create
;;

(defn- patient-create-handler
  [response]
  (.log js/console response)
  (if (> @state/*page 1)
    (patient-list :all)
    (patient-list :first)) ;;think about if create from != 1 page what about cash
  (alert-pop-up :success))

(defn- patient-create-error-handler
  [{:keys [status status-text]}]
  (.log js/console (str "No patients found: " status " " status-text))
  (alert-pop-up :error))

(defn patient-create
  [patient-values]
  (PUT (str server-uri "/patient-create")
    {:headers {"Content-Type" "application/edn"}
     :body patient-values
     :handler patient-create-handler
     :error-handler patient-create-error-handler}))