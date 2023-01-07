(ns server.handlers-test
  (:require [server.app.handlers :as sut]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))


(deftest patient-list-spec-test

  (testing "Patient-list spec testing without :search-params"
    (let [req {:params {:key "first"
                        :limit "10"
                        :get-pages "2"
                        :page "1"}}]
      (is (true? (s/valid? ::sut/patient-list-request req)))

      ;; testing without limit params
      (let [req (update req :params dissoc :limit)]
        (is (true? (s/valid? ::sut/patient-list-request req))))

      ;; testing without required :key param
      (let [req (update req :params dissoc :key)]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with incorrect :key param
      (let [req (assoc-in req [:params :key] "incorrect")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with incorrect non-number string :page param
      (let [req (assoc-in req [:params :page] "one1")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with zero-string :limit param
      (let [req (assoc-in req [:params :limit] "0")]
        (is (false? (s/valid? ::sut/patient-list-request req))))))


  (testing "Patient-list spec testing with :search-params map"
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
      (is (true? (s/valid? ::sut/patient-list-request req)))

      ;; testing with valid request with only couple search-params
      (let [req (assoc-in req [:params :search-data] {:name "Anton"
                                                      :last-name "Gorobets"
                                                      :sex "female"})]
        (is (true? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid :oms-number param
      (let [req (assoc-in req [:params :search-data :oms-number] "873 874583")]
        (is (false? (s/valid? ::sut/patient-list-request req))))
      (let [req (assoc-in req [:params :search-data :oms-number] "8733 874583a")]
        (is (false? (s/valid? ::sut/patient-list-request req))))
      (let [req (assoc-in req [:params :search-data :oms-number] "8733-874583")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid :birth-date param
      (let [req (assoc-in req [:params :search-data :birth-date] "02-30-2000")]
        (is (false? (s/valid? ::sut/patient-list-request req))))
      (let [req (assoc-in req [:params :search-data :birth-date] "2000 01 07")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid :sex param
      (let [req (assoc-in req [:params :search-data :sex] "transgender")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid not-string param
      (let [req (assoc-in req [:params :search-data :name] :anton)]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid not-string param
      (let [req (assoc-in req [:params :search-data :last-name] 1)]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid empty-string param
      (let [req (assoc-in req [:params :search-data :adress] "")]
        (is (false? (s/valid? ::sut/patient-list-request req)))))))


(deftest patient-edit-create-spec-test

  (testing "Patient-edit spec with body-params"
    (let [req {:body-params {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"
                             :name "Anton"
                             :last-name "Gorobets"
                             :sex "male"
                             :birth-date "1990-01-07"
                             :adress "Georgia, Batumi, Mayakovskaya St., 13"
                             :oms-number "4444 666666"}}]
      (is (true? (s/valid? ::sut/patient-edit-create-request req)))

      ;; testing with invalid id param
      (let [req (assoc-in req [:body-params :id] "2700cb34-04bc-448a-8a48-000000000021")]
        (is (false? (s/valid? ::sut/patient-edit-create-request req))))

      ;; testing with invalid :oms-number param
      (let [req (assoc-in req [:body-params :oms-number] "873 874583")]
        (is (false? (s/valid? ::sut/patient-list-request req))))
      (let [req (assoc-in req [:body-params :oms-number] "8733 874583a")]
        (is (false? (s/valid? ::sut/patient-list-request req))))
      (let [req (assoc-in req [:body-params :oms-number] "8733-874583")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid :birth-date param
      (let [req (assoc-in req [:body-params :birth-date] "02-30-2000")]
        (is (false? (s/valid? ::sut/patient-list-request req))))
      (let [req (assoc-in req [:body-params :birth-date] "2000 01 07")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid :sex param
      (let [req (assoc-in req [:body-params :sex] "transgender")]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid not-string param
      (let [req (assoc-in req [:body-params :name] :anton)]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid not-string param
      (let [req (assoc-in req [:body-params :last-name] 1)]
        (is (false? (s/valid? ::sut/patient-list-request req))))

      ;; testing with invalid empty-string param
      (let [req (assoc-in req [:body-params :adress] "")]
        (is (false? (s/valid? ::sut/patient-list-request req)))))))


(deftest patient-delete-spec-test
  
  (testing "With correct body-params"
    (let [req {:body-params {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"}}]
      (is (true? (s/valid? ::sut/patient-delete-request req)))
      
      (let [req (assoc-in req [:body-params :id] "2700cb34-04bc-448a-8a48-000000000021")]
        (is (false? (s/valid? ::sut/patient-edit-create-request req))))
      
      (let [req (assoc-in req [:body-params :id] "")]
        (is (false? (s/valid? ::sut/patient-edit-create-request req))))
      
      (let [req (assoc-in req [:body-params :id] nil)]
        (is (false? (s/valid? ::sut/patient-edit-create-request req))))
      )))