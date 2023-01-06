(ns server.app.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as compojure-route]
            [server.app.handlers :as h]))


(defroutes app
  (GET "/patient-list" request h/patient-list-handler)
  (PUT "/patient-edit" request h/patient-edit-handler)
  (DELETE "/patient-delete" request h/patient-delete-handler)
  (PUT "/patient-create" request h/patient-create-handler)
  (compojure-route/not-found h/not-found))