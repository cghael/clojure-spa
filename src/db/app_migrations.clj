(ns db.app-migrations
  (:require [clojure.java.jdbc :as jdbc]
            [migratus.core :as migratus]))


(defn migrations-down
  [config db]
  (let [mirgate-config (:migratus config)
        table-name (-> config
                       :migratus
                       :migration-table-name)
        query-str (str "select id from " table-name)
        table-ids (map :id
                       (jdbc/query db query-str))]
    (migratus/down mirgate-config table-ids)))


(defn migrations-up
  [config]
  (let [mirgate-config (:migratus config)]
    ;; (migratus/init mirgate-config)
    (migratus/migrate mirgate-config)))
