(ns server.app.patients)


(def *patients (atom {:p-01 {:id :p-01
                             :name "Andrey"
                             :last-name "Pospelov"
                             :sex "male"
                             :birth-date "21-03-1989"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1234567890"}
                      :p-02 {:id :p-02
                             :name "Andrey"
                             :last-name "Bragnikov"
                             :sex "male"
                             :birth-date "03-03-1989"
                             :adress "Georgia, Kapuleti, Maraishvili, 128"
                             :oms-number "1209348756"}
                      :p-03 {:id :p-03
                             :name "Vladislav"
                             :last-name "Dudashvili"
                             :sex "male"
                             :birth-date "18-05-1992"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "0987654321"}
                      :p-04 {:id :p-04
                             :name "Anton"
                             :last-name "Gorobets"
                             :sex "male"
                             :birth-date "20-11-1986"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1111222233"}
                      :p-05 {:id :p-05
                             :name "Alex"
                             :last-name "Kruglyak"
                             :sex "male"
                             :birth-date "27-03-1988"
                             :adress "Georgia, Batumi, Mayakovskaya, 112"
                             :oms-number "99988877"}
                      :p-06 {:id :p-06
                             :name "Andrey"
                             :last-name "Pospelov"
                             :sex "male"
                             :birth-date "21-03-1989"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1234567890"}
                      :p-07 {:id :p-07
                             :name "Andrey"
                             :last-name "Bragnikov"
                             :sex "male"
                             :birth-date "03-03-1989"
                             :adress "Georgia, Kapuleti, Maraishvili, 128"
                             :oms-number "1209348756"}
                      :p-08 {:id :p-08
                             :name "Vladislav"
                             :last-name "Dudashvili"
                             :sex "male"
                             :birth-date "18-05-1992"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "0987654321"}
                      :p-09 {:id :p-09
                             :name "Anton"
                             :last-name "Gorobets"
                             :sex "male"
                             :birth-date "20-11-1986"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1111222233"}
                      :p-10 {:id :p-10
                             :name "Alex"
                             :last-name "Kruglyak"
                             :sex "male"
                             :birth-date "27-03-1988"
                             :adress "Georgia, Batumi, Mayakovskaya, 112"
                             :oms-number "99988877"}
                      :p-11 {:id :p-11
                             :name "Andrey"
                             :last-name "Pospelov"
                             :sex "male"
                             :birth-date "21-03-1989"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1234567890"}
                      :p-12 {:id :p-12
                             :name "Andrey"
                             :last-name "Bragnikov"
                             :sex "male"
                             :birth-date "03-03-1989"
                             :adress "Georgia, Kapuleti, Maraishvili, 128"
                             :oms-number "1209348756"}
                      :p-13 {:id :p-13
                             :name "Vladislav"
                             :last-name "Dudashvili"
                             :sex "male"
                             :birth-date "18-05-1992"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "0987654321"}
                      :p-14 {:id :p-14
                             :name "Andrey"
                             :last-name "Pospelov-Gorobets-Kruglyak-Erorov"
                             :sex "male"
                             :birth-date "21-03-1989"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1234567890"}
                      :p-15 {:id :p-15
                             :name "Andrey"
                             :last-name "Bragnikov"
                             :sex "male"
                             :birth-date "03-03-1989"
                             :adress "Georgia, Kapuleti, Maraishvili, 128"
                             :oms-number "1209348756"}
                      :p-16 {:id :p-16
                             :name "Vladislav"
                             :last-name "Dudashvili"
                             :sex "male"
                             :birth-date "18-05-1992"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "0987654321"}
                      :p-17 {:id :p-17
                             :name "Andrey"
                             :last-name "Pospelov"
                             :sex "male"
                             :birth-date "21-03-1989"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1234567890"}
                      :p-18 {:id :p-18
                             :name "Andrey"
                             :last-name "Bragnikov"
                             :sex "male"
                             :birth-date "03-03-1989"
                             :adress "Georgia, Kapuleti, Maraishvili, 128"
                             :oms-number "1209348756"}
                      :p-19 {:id :p-19
                             :name "Vladislav"
                             :last-name "Dudashvili"
                             :sex "male"
                             :birth-date "18-05-1992"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "0987654321"}
                      :p-20 {:id :p-20
                             :name "Anton"
                             :last-name "Gorobets"
                             :sex "male"
                             :birth-date "20-11-1986"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1111222233"}
                      :p-21 {:id :p-21
                             :name "Alex"
                             :last-name "Kruglyak"
                             :sex "male"
                             :birth-date "27-03-1988"
                             :adress "Georgia, Batumi, Mayakovskaya, 112"
                             :oms-number "99988877"}
                      :p-22 {:id :p-22
                             :name "Andrey"
                             :last-name "Pospelov"
                             :sex "male"
                             :birth-date "21-03-1989"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "1234567890"}
                      :p-23 {:id :p-23
                             :name "Andrey"
                             :last-name "Bragnikov"
                             :sex "male"
                             :birth-date "03-03-1989"
                             :adress "Georgia, Kapuleti, Maraishvili, 128"
                             :oms-number "1209348756"}
                      :p-24 {:id :p-24
                             :name "Vladislav"
                             :last-name "Dudashvili"
                             :sex "male"
                             :birth-date "18-05-1992"
                             :adress "Georgia, Batumi, G. Tseritelli, 63"
                             :oms-number "0987654321"}}))