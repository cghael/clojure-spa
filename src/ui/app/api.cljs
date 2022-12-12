(ns ui.app.api
  (:require [ajax.core :refer [GET]]
            [ajax.edn :refer [edn-response-format edn-read]]
            [ui.app.state :as state]))


(def server-uri "http://localhost:4000/")

(defn patient-list-handler [response]
  (.log js/console response)
  (reset! state/*patients {:current-page (take 10 response)
                           :next-page (drop 10 response)}))

(defn patient-list-error-handler [{:keys [status status-text]}]
  (.log js/console (str "No patients found: " status " " status-text))
  (let [next-page (:next-page @state/*patients)]
    (when (seq next-page)
       (reset! state/*patients {:current-page next-page
                                :next-page {}}))))

(defn patient-list
  []
  (GET server-uri
    {:params {:limit (* 2 10)
              :page @state/*page}
     :handler patient-list-handler
     :error-handler patient-list-error-handler}))