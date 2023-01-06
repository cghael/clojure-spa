(ns server.handlers-test
  (:require [server.app.handlers :as sut]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))


(deftest patient-list-spec-test
  
  (testing "Patient-list spec testing with valid request and search-params = null"
    (let [req {:params {:key "first"
                        :limit "10"
                        :get-pages "2"
                        :page "1"}}]
      (is (true? (s/valid? ::sut/patient-list-request req)))))
  
  (testing "Patient-list spec testing with valid request with full search-params map"
    (let [req {:params {:key "first"
                        :limit "10"
                        :get-pages "2"
                        :page "1"
                        :search-data {:oms-number "8765 765544"
                                      :birth-date "2000-03-17"
                                      :sex "male"
                                      :name "Anton"
                                      :adress "Batumi, Mayakovskaya St., 84"
                                      :last-name "Gorobets"}}}]
      (is (true? (s/valid? ::sut/patient-list-request req)))))
  
  (testing "Patient-list spec testing with valid request with couple search-params"
    (let [req {:params {:key "first"
                        :limit "10"
                        :get-pages "2"
                        :page "1"
                        :search-data {:name "Anton"
                                      :last-name "Gorobets"}}}]
      (is (true? (s/valid? ::sut/patient-list-request req)))))
  
  (testing "Patient-list spec testing without required keys"
    (let [req {:params {:limit "10"
                        :get-pages "2"
                        :page "1"}}]
      (is (false? (s/valid? ::sut/patient-list-request req)))))
  
 )