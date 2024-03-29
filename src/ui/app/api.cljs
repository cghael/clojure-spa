(ns ui.app.api
  (:require [ajax.core :refer [GET POST PUT DELETE]] 
            [ui.app.state :as state]
            [clojure.string :as string]))


(def server-uri (:uri @state/*config))

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
                        :page identity}
                :next {:get-pages 1 
                       :page inc}
                :back {:get-pages 1 
                       :page dec}
                :all {:get-pages 3 
                       :page dec}})


(defn- delete-empty-values
  [m]
  (when m
    (reduce-kv (fn [acc k v]
                 (if (empty? v)
                   acc
                   (assoc acc k v)))
               {} m)))


(defn patient-list
  [page-key]
  (let [page-props (page-key what-page)
        params {:limit (:limit @state/*config)
                :page ((:page page-props) @state/*page)
                :get-pages (:get-pages page-props)
                :key page-key}
        _ (swap! state/*search-filer delete-empty-values)
        params (if @state/*search-filer
                 (assoc params :search-data @state/*search-filer)
                 params)]
    (GET (str server-uri "/patient-list")
      {:params params
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
                last-name
                sex
                birth-date
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
            :last-name (string/trim last-name)
            :sex (string/trim sex)
            :birth-date birth-date
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
    (patient-list :first))
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