(ns db.app-migrations
  (:require ;; [config :refer [config]]
            ;; [db.core :refer [db]]
            [clojure.java.jdbc :as jdbc]
            [migratus.core :as migratus]))


(defn migrations-down
  [config db]
  (let [mirgate-config (:migratus config)
        table-ids (map :migrations/id
                       (jdbc/query db ["SELECT id FROM ?" (-> config
                                                              :migratus
                                                              :migration-table-name)]))]
    (run! #(migratus/down mirgate-config %) table-ids)))


(defn migrations-up
  [config]
  (let [mirgate-config (:migratus config)]
    (migratus/init mirgate-config)
    (migratus/migrate mirgate-config)))

;; (defn migrations-down
;;   [config]
;;   (migrate-down config))