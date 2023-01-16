(ns ^:integration server.integration-test
  (:require [clojure.test :refer [use-fixtures deftest is testing]]
            [mount.core :as mount :refer [defstate]]
            [clojure.edn :as edn]
            [hikari-cp.core :as cp]
            [db.app-migrations :as m]
            [taoensso.timbre :as log]
            [server.app.routes :as sut]
            [db.core :as db]))


;; fixtures

(defstate test-config
  :start
  (-> "test/resources/config.edn"
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


(deftest test-app-routes
  (with-redefs [db/db test-db]

    (testing "/patient-list route"
      (let [params {:limit "10"
                    :page "1"
                    :get-pages "2"
                    :key "first"}
            res (sut/app {:uri "/patient-list"
                          :request-method :get
                          :params params})
            exp {:status 200
                 :headers {}
                 :body {:data '({:id #uuid "2700cb34-04bc-448a-8a48-000000000003"
                                 :name "Andrey"
                                 :last-name "Bragnikov"
                                 :sex "male"
                                 :birth-date "1987-02-28"
                                 :adress "Georgia, Kabuliti, St. Pushkina, 18"
                                 :oms-number "9745 984392"}
                                {:id #uuid "2700cb34-04bc-448a-8a48-000000000002"
                                 :name "Anton"
                                 :last-name "Gorobets"
                                 :sex "male"
                                 :birth-date "1986-11-27"
                                 :adress "Georgia, Batumi, St. Georgy Tseretelli, 11"
                                 :oms-number "8630 725432"}
                                {:id #uuid "2700cb34-04bc-448a-8a48-000000000001"
                                 :name "Alex"
                                 :last-name "Kruglyak"
                                 :sex "male"
                                 :birth-date "1988-03-27"
                                 :adress "Georgia, Batumi, St. Mayakovskaya, 23"
                                 :oms-number "3457 123241"}
                                {:id #uuid "2700cb34-04bc-448a-8a48-000000000004"
                                 :name "Alex"
                                 :last-name "Kruglyak"
                                 :sex "male"
                                 :birth-date "1988-03-27"
                                 :adress "Georgia, Batumi, St. Mayakovskaya, 23"
                                 :oms-number "3457 123999"})
                        :key "first"}}]
        (is (= 4 (count (-> res :body :data))))
        (is (= res exp))))

    (testing "/patient-list route with search-data"
      (let [params {:limit "10"
                    :page "1"
                    :get-pages "2"
                    :key "first"
                    :search-data {:name "Alex"}}
            res (sut/app {:uri "/patient-list"
                          :request-method :get
                          :params params})
            exp {:status 200
                 :headers {}
                 :body {:data '({:id #uuid "2700cb34-04bc-448a-8a48-000000000001"
                                 :name "Alex"
                                 :last-name "Kruglyak"
                                 :sex "male"
                                 :birth-date "1988-03-27"
                                 :adress "Georgia, Batumi, St. Mayakovskaya, 23"
                                 :oms-number "3457 123241"}
                                {:id #uuid "2700cb34-04bc-448a-8a48-000000000004"
                                 :name "Alex"
                                 :last-name "Kruglyak"
                                 :sex "male"
                                 :birth-date "1988-03-27"
                                 :adress "Georgia, Batumi, St. Mayakovskaya, 23"
                                 :oms-number "3457 123999"})
                        :key "first"}}]
        (is (= 2 (count (-> res :body :data))))
        (is (= res exp))))


    (testing "/patient-edit route"
      (let [params {:id #uuid "2700cb34-04bc-448a-8a48-000000000003"
                    :name "Andrea"
                    :last-name "Bragnikov"
                    :sex "female"
                    :birth-date "1987-02-28"
                    :adress "Georgia, Kabuliti, St. Pushkina, 18"
                    :oms-number "9745 984392"}
            res (sut/app {:uri "/patient-edit"
                          :request-method :put
                          :body-params params})
            list (sut/app {:uri "/patient-list"
                           :request-method :get
                           :params {:limit "10"
                                    :page "1"
                                    :get-pages "2"
                                    :key "first"
                                    :search-data {:name "Andrea"}}})
            exp-res {:status 202
                     :headers {}
                     :body {:patient #uuid "2700cb34-04bc-448a-8a48-000000000003"}}
            exp-list {:status 200
                      :headers {}
                      :body {:data '({:id #uuid "2700cb34-04bc-448a-8a48-000000000003"
                                      :name "Andrea"
                                      :last-name "Bragnikov"
                                      :sex "female"
                                      :birth-date "1987-02-28"
                                      :adress "Georgia, Kabuliti, St. Pushkina, 18"
                                      :oms-number "9745 984392"})
                             :key "first"}}]
        (is (= res exp-res))
        (is (= list exp-list))))


    (testing "/patient-delete route"
      (let [params {:id #uuid "2700cb34-04bc-448a-8a48-000000000003"}
            res (sut/app {:uri "/patient-delete"
                          :request-method :delete
                          :body-params params})
            list (sut/app {:uri "/patient-list"
                           :request-method :get
                           :params {:limit "10"
                                    :page "1"
                                    :get-pages "2"
                                    :key "first"}})
            exp-res {:status 204
                     :headers {}
                     :body {:id #uuid "2700cb34-04bc-448a-8a48-000000000003"}}
            exp-list {:status 200
                      :headers {}
                      :body {:data '({:id #uuid "2700cb34-04bc-448a-8a48-000000000002"
                                      :name "Anton"
                                      :last-name "Gorobets"
                                      :sex "male"
                                      :birth-date "1986-11-27"
                                      :adress "Georgia, Batumi, St. Georgy Tseretelli, 11"
                                      :oms-number "8630 725432"}
                                     {:id #uuid "2700cb34-04bc-448a-8a48-000000000001"
                                      :name "Alex"
                                      :last-name "Kruglyak"
                                      :sex "male"
                                      :birth-date "1988-03-27"
                                      :adress "Georgia, Batumi, St. Mayakovskaya, 23"
                                      :oms-number "3457 123241"}
                                     {:id #uuid "2700cb34-04bc-448a-8a48-000000000004"
                                      :name "Alex"
                                      :last-name "Kruglyak"
                                      :sex "male"
                                      :birth-date "1988-03-27"
                                      :adress "Georgia, Batumi, St. Mayakovskaya, 23"
                                      :oms-number "3457 123999"})
                             :key "first"}}]
        (is (= res exp-res))
        (is (= list exp-list))))


    (testing "/patient-create route"
      (let [params {:name "Andrea"
                    :last-name "Bragnikov"
                    :sex "female"
                    :birth-date "1987-02-28"
                    :adress "Georgia, Kabuliti, St. Pushkina, 18"
                    :oms-number "9745 984392"}
            res (sut/app {:uri "/patient-create"
                          :request-method :put
                          :body-params params})
            res-list (sut/app {:uri "/patient-list"
                           :request-method :get
                           :params {:limit "10"
                                    :page "1"
                                    :get-pages "2"
                                    :key "first"
                                    :search-data {:name "Andrea"}}})
            id (-> res
                   :body
                   :patient
                   :id)
            patient {:id id
                     :name "Andrea"
                     :last-name "Bragnikov"
                     :sex "female"
                     :birth-date "1987-02-28"
                     :adress "Georgia, Kabuliti, St. Pushkina, 18"
                     :oms-number "9745 984392"}
            exp-res {:status 201
                     :headers {}
                     :body {:patient patient}}
            exp-list {:status 200
                      :headers {}
                      :body {:data (list patient)
                             :key "first"}}]
        (is (uuid? id))
        (is (= res exp-res))
        (is (= res-list exp-list))))))