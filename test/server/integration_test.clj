(ns ^:integration server.integration-test
  (:require [clojure.test :refer [use-fixtures deftest is]]
            [mount.core :as mount :refer [defstate]]
            [clojure.edn :as edn]
            [hikari-cp.core :as cp]
            [db.app-migrations :as m]
            [taoensso.timbre :as log]))


;; fixtures

(defstate test-config
  :start
  (-> "resources/config.edn"
      slurp
      edn/read-string))


(defstate test-db
  :start
  (let [pool-config (:pool test-config)
        pool (cp/make-datasource pool-config)]
    {:datasource pool})

  :stop
  (-> test-db :datasource cp/close-datasource))


(defn stop!
  []
  (let [_ (m/migrations-down test-config test-db)
        status (mount/stop #'test-db)]
    status))


(defn start!
  []
  (let [status (mount/start #'test-config #'test-db)
        _ (m/migrations-up test-config)
        _ (log/info status)]
    status))


(defn with-fix-db
  [f]
  (start!)
  (try
    (f)
    (catch Exception e
      (throw e))
    (finally
      (stop!))))


;; tests


(use-fixtures :once with-fix-db)

