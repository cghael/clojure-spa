(ns server.handlers-test
  (:require [server.app.handlers :as sut]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))


(deftest patient-list-spec-test

  (testing "Testing patient-list without search-params"
    (let [ok-request {:params {:key "first"
                               :limit "10"
                               :get-pages "2"
                               :page "1"}}]
      (is (true? (s/valid? ::sut/patient-list-request ok-request)))

      (testing "without :limit key"
        (let [ok-request (update ok-request :params dissoc :limit)]
          (is (true? (s/valid? ::sut/patient-list-request ok-request)))))

      (testing "without nessesary key"
        (let [n-keys #{:key :page :get-pages}]
          (doseq [k n-keys]
            (testing (str k)
              (let [request (update ok-request :params dissoc k)]
                (is (false? (s/valid? ::sut/patient-list-request request))))))))

      (testing "with bad params:"
        (let [error-variations [[{:key "incorrect"} ":key param incorrect"]
                                [{:key nil} ":key param nil"]
                                [{:key ""} ":page empty string"]
                                [{:page "one1"} ":page non number string"]
                                [{:page "0"} ":page zero string"]
                                [{:page ""} ":page empty string"]
                                [{:limit "one1"} ":limit non number string"]
                                [{:limit "0"} ":limit zero string"]
                                [{:limit "-1"} ":limit negative number string"]
                                [{:get-pages "one1"} ":get-pages non number string"]
                                [{:get-pages "0"} ":get-pages zero string"]
                                [{:get-pages ""} ":get-pages empty string"]]]
          (doseq [[params description] error-variations]
            (testing description
              (let [request (merge-with into ok-request {:params params})]
                (is (false? (s/valid? ::sut/patient-list-request request))))))))))


  (testing "Patient-list conformed spec"

    ;; testing with all params
    (let [params {:key "first"
                  :limit "10"
                  :get-pages "2"
                  :page "1"}
          res (s/conform ::sut/->transform-list-request params)
          exp {:key "first"
               :limit 10
               :get-pages 2
               :page 1}]
      (is (= res exp)))

    ;; testing without :limit
    (let [params {:key "first"
                  :get-pages "2"
                  :page "1"}
          res (s/conform ::sut/->transform-list-request params)
          exp {:key "first"
               :limit 10
               :get-pages 2
               :page 1}]
      (is (= res exp)))

    ;; testing with invalid params
    (let [params {:key "first"
                  :limit "10"
                  :get-pages ""
                  :page "one"}
          res (s/conform ::sut/->transform-list-request params)]
      (is (s/invalid? res))))


  (testing "Patient-list spec testing with :search-params map"
    (let [ok-request {:params {:key "first"
                               :limit "10"
                               :get-pages "2"
                               :page "1"
                               :search-data {:oms-number "8765 765544"
                                             :birth-date "2000-03-17"
                                             :sex "male"
                                             :name "Anton"
                                             :adress "Batumi, Mayakovskaya St., 84"
                                             :last-name "Gorobets"}}}]
      (is (true? (s/valid? ::sut/patient-list-request ok-request)))


      (testing "with only couple search-params"
        (let [request (assoc-in ok-request [:params :search-data] {:name "Anton"
                                                                   :last-name "Gorobets"
                                                                   :sex "female"})]
          (is (true? (s/valid? ::sut/patient-list-request request)))))


      (testing "with invalid :oms-number values:"
        (let [invalid-values [["888 888888" "{3} {6}"]
                              ["8888 88888" "{4} {5}"]
                              ["8888-988888" "{4}-{6}"]
                              ["" "empty string"]
                              [nil "nil"]
                              ["a765 765544" "not numbers string"]]]
          (doseq [[value description] invalid-values]
            (testing description
              (let [request (assoc-in ok-request [:params :search-data :oms-number] value)]
                (is (false? (s/valid? ::sut/patient-list-request request))))))))

      (testing "with invalid :birth-date values:"
        (let [invalid-values [["02-30-2000" "{2}-{2}-{4}"]
                              ["2000 01 07" "{4} {2} {2}"]
                              ["2000:01:07" "{4}:{2}:{2}"]
                              ["200a-01-07" "not numbers string"]
                              ["" "empty string"]
                              [nil "nil"]]]
          (doseq [[value description] invalid-values]
            (testing description
              (let [request (assoc-in ok-request [:params :search-data :birth-date] value)]
                (is (false? (s/valid? ::sut/patient-list-request request))))))))

      (testing "with invalid :sex values:"
        (let [invalid-values [["unknown" "unknown value"]
                              ["" "empty string"]
                              [nil "nil"]]]
          (doseq [[value description] invalid-values]
            (testing description
              (let [request (assoc-in ok-request [:params :search-data :sex] value)]
                (is (false? (s/valid? ::sut/patient-list-request request))))))))

      (testing "with invalid :name/last-name/adress values:"
        (let [keys [:name :last-name :adress]
              invalid-values [[1 "number value"]
                              [:key "key value"]
                              ["" "empty string"]
                              [nil "nil"]]]
          (doseq [[value description] invalid-values
                  key keys]
            (testing (str key " = " description)
              (let [request (assoc-in ok-request [:params :search-data key] value)]
                (is (false? (s/valid? ::sut/patient-list-request request)))))))))))


(deftest patient-edit-create-spec-test

  (testing "Patient-edit spec with body-params"
    (let [ok-request {:body-params {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"
                                    :name "Anton"
                                    :last-name "Gorobets"
                                    :sex "male"
                                    :birth-date "1990-01-07"
                                    :adress "Georgia, Batumi, Mayakovskaya St., 13"
                                    :oms-number "4444 666666"}}]
      (is (true? (s/valid? ::sut/patient-edit-create-request ok-request)))


      (testing "With lost param"
        (let [keys #{:id :name :last-name :sex :birth-date :adress :oms-number}]
          (doseq [k keys]
            (testing (str k)
              (let [request (update ok-request :body-params dissoc k)]
                (is (false? (s/valid? ::sut/patient-edit-create-request request))))))))


      (testing "with invalid id param"
        (let [invalid-values [["2700cb34-04bc-448a-8a48-000000000021" "not uuid inst"]
                              [270 "int value"]
                              ["" "empty string"]
                              [nil "nil value"]]]
          (doseq [[value description] invalid-values]
            (testing description
              (let [request (assoc-in ok-request [:body-params :id] value)]
                (is (false? (s/valid? ::sut/patient-edit-create-request request)))))))))))


(deftest patient-delete-spec-test

  (testing "With correct body-params"
    (let [req {:body-params {:id #uuid "2700cb34-04bc-448a-8a48-000000000021"}}]
      (is (true? (s/valid? ::sut/patient-delete-request req)))

      (let [req (assoc-in req [:body-params :id] "2700cb34-04bc-448a-8a48-000000000021")]
        (is (false? (s/valid? ::sut/patient-edit-create-request req))))

      (let [req (assoc-in req [:body-params :id] "")]
        (is (false? (s/valid? ::sut/patient-edit-create-request req))))

      (let [req (assoc-in req [:body-params :id] nil)]
        (is (false? (s/valid? ::sut/patient-edit-create-request req)))))))