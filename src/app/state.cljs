(ns app.state
  (:require [reagent.core :as r]))


(def *activ-id (r/atom nil))

(def *patients (r/atom {:count 16
                        :content {:p-01 {:id :p-01
                                         :name "Andrey"
                                         :s-name "Pospelov"
                                         :sex "male"
                                         :birth "21-03-1989"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "1234567890"}
                                  :p-02 {:id :p-02
                                         :name "Andrey"
                                         :s-name "Bragnikov"
                                         :sex "male"
                                         :birth "03-03-1989"
                                         :adress "Georgia, Kapuleti, Maraishvili, 128"
                                         :oms-number "1209348756"}
                                  :p-03 {:id :p-03
                                         :name "Vladislav"
                                         :s-name "Dudashvili"
                                         :sex "male"
                                         :birth "18-05-1992"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "0987654321"}
                                  :p-04 {:id :p-04
                                         :name "Anton"
                                         :s-name "Gorobets"
                                         :sex "male"
                                         :birth "20-11-1986"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "1111222233"}
                                  :p-05 {:id :p-05
                                         :name "Alex"
                                         :s-name "Kruglyak"
                                         :sex "male"
                                         :birth "27-03-1988"
                                         :adress "Georgia, Batumi, Mayakovskaya, 112"
                                         :oms-number "99988877"}
                                  :p-06 {:id :p-06
                                         :name "Andrey"
                                         :s-name "Pospelov"
                                         :sex "male"
                                         :birth "21-03-1989"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "1234567890"}
                                  :p-07 {:id :p-07
                                         :name "Andrey"
                                         :s-name "Bragnikov"
                                         :sex "male"
                                         :birth "03-03-1989"
                                         :adress "Georgia, Kapuleti, Maraishvili, 128"
                                         :oms-number "1209348756"}
                                  :p-08 {:id :p-08
                                         :name "Vladislav"
                                         :s-name "Dudashvili"
                                         :sex "male"
                                         :birth "18-05-1992"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "0987654321"}
                                  :p-09 {:id :p-09
                                         :name "Anton"
                                         :s-name "Gorobets"
                                         :sex "male"
                                         :birth "20-11-1986"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "1111222233"}
                                  :p-10 {:id :p-10
                                         :name "Alex"
                                         :s-name "Kruglyak"
                                         :sex "male"
                                         :birth "27-03-1988"
                                         :adress "Georgia, Batumi, Mayakovskaya, 112"
                                         :oms-number "99988877"}
                                  :p-11 {:id :p-11
                                         :name "Andrey"
                                         :s-name "Pospelov"
                                         :sex "male"
                                         :birth "21-03-1989"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "1234567890"}
                                  :p-12 {:id :p-12
                                         :name "Andrey"
                                         :s-name "Bragnikov"
                                         :sex "male"
                                         :birth "03-03-1989"
                                         :adress "Georgia, Kapuleti, Maraishvili, 128"
                                         :oms-number "1209348756"}
                                  :p-13 {:id :p-13
                                         :name "Vladislav"
                                         :s-name "Dudashvili"
                                         :sex "male"
                                         :birth "18-05-1992"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "0987654321"}
                                  :p-14 {:id :p-14
                                         :name "Andrey"
                                         :s-name "Pospelov-Gorobets-Kruglyak-Erorov"
                                         :sex "male"
                                         :birth "21-03-1989"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "1234567890"}
                                  :p-15 {:id :p-15
                                         :name "Andrey"
                                         :s-name "Bragnikov"
                                         :sex "male"
                                         :birth "03-03-1989"
                                         :adress "Georgia, Kapuleti, Maraishvili, 128"
                                         :oms-number "1209348756"}
                                  :p-16 {:id :p-16
                                         :name "Vladislav"
                                         :s-name "Dudashvili"
                                         :sex "male"
                                         :birth "18-05-1992"
                                         :adress "Georgia, Batumi, G. Tseritelli, 63"
                                         :oms-number "0987654321"}}}))