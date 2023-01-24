(ns ^:unit server.api-test
  (:require [server.app.api :as sut]
            [server.app.db :as db]
            [ninja.unifier.response :as r]
            [clojure.test :refer :all]
            [java-time.api :as jt]))


(deftest transform-response-map-test
  (testing "Transform response map with date instance and keys with underscore"
    (let [m '({:test_key "test value"
               :birth_date #inst "1990-01-07T00:00:00.000000000-00:00"}
              {:another_test_key "with value"
               :date #inst "1999-02-02T00:00:00.000000000-00:00"})
          exp '({:test-key "test value"
                 :birth-date "1990-01-07"}
                {:another-test-key "with value"
                 :date "1999-02-02"})]
      (is (= (sut/transform-response-map m) exp)))))


(deftest transform-map-keys-test
  (testing "Keys with dash returns with underscore"
    (let [m {:key "key value"
             :key-dash "key-dash value"}
          exp {:key "key value"
               :key_dash "key-dash value"}]
      (is (= (sut/transform-map-keys m) exp)))))


(deftest api-functions-test

  (testing "Testing patient-list"
    (with-redefs [db/patient-list (fn [x] (r/as-success {:data x}))
                  sut/transform-response-map (fn [x] x)]

      (let [params {:limit 10
                    :page 1
                    :get-pages 2
                    :key "first"}
            exp {:data {:limit 20
                        :offset 0
                        :search-data nil}
                 :key "first"}]
        (is (= exp (sut/patient-list params))))

      (let [params {:limit 10
                    :page 1
                    :get-pages 2
                    :key "first"
                    :search-data {:oms-number "3333 444444"}}
            exp {:data {:limit 20
                        :offset 0
                        :search-data {:oms_number "3333 444444"}}
                 :key "first"}]
        (is (= exp (sut/patient-list params))))

      (let [params {:limit 20
                    :page 2
                    :get-pages 3
                    :key "next"}
            exp {:data {:limit 60
                        :offset 20
                        :search-data nil}
                 :key "next"}]
        (is (= exp (sut/patient-list params))))))


  (testing "Testing patient-edit"
    (with-redefs [db/patient-edit (fn [m id]
                                    (r/as-success {:patient (assoc m :id id)}))]

      (let [params {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"
                    :name "Anton"
                    :last-name "Gorobets"
                    :sex "male"
                    :birth-date "1990-01-07"
                    :adress "Georgia, Batumi, Mayakovskaya St., 13"
                    :oms-number "4444 666666"}
            exp {:patient {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"
                           :name "Anton"
                           :last-name "Gorobets"
                           :sex "male"
                           :birth-date (jt/local-date "yyyy-MM-dd" "1990-01-07")
                           :adress "Georgia, Batumi, Mayakovskaya St., 13"
                           :oms-number "4444 666666"}}
            res (sut/patient-edit params)]
        (is (not (r/error? res)))
        (is (= res exp)))))


  (testing "Testing patient-delete"
    (with-redefs [db/patient-delete (fn [id] (r/as-success {:id id}))]

      (let [params {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"}
            exp {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"}]
        (is (= (sut/patient-delete params) exp)))))


  (testing "Testing patient-create"
    (with-redefs [db/patient-create (fn [m] (r/as-success {:patient m}))]

      (let [params {:name "Anton"
                    :last-name "Gorobets"
                    :sex "male"
                    :birth-date "1990-01-07"
                    :adress "Georgia, Batumi, Mayakovskaya St., 13"
                    :oms-number "4444 666666"}
            res (sut/patient-create params)
            id (-> res :patient :id)
            exp {:patient {:name "Anton"
                           :last-name "Gorobets"
                           :sex "male"
                           :birth-date (jt/local-date "yyyy-MM-dd" "1990-01-07")
                           :adress "Georgia, Batumi, Mayakovskaya St., 13"
                           :oms-number "4444 666666"}}
            exp (assoc-in exp [:patient :id] id)]
        (is (not (r/error? res)))
        (is (not (nil? id)))
        (is (= res exp))))))
