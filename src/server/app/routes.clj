(ns server.app.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as compojure-route]
            [server.app.handlers :as h]))


(defroutes app
  (GET "/" request h/patient-list-handler)
  (GET "/problem/:id" request h/problem-by-id)
  (GET "/next" request h/next-problem)
  (GET "/hint/:id" request h/hint)
  (GET "/solution/:id" request h/solution)

  (compojure-route/not-found h/not-found))