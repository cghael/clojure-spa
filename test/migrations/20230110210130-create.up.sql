CREATE TABLE IF NOT EXISTS spa.patients (
  id          uuid        PRIMARY KEY NOT NULL,
  name        text                    NOT NULL,
  last_name   text                    NOT NULL,
  sex         text                    DEFAULT 'unknown',
  birth_date  timestamp               DEFAULT '1970-01-01 00:00:00',
  adress      text                    DEFAULT 'unknown',
  oms_number  text UNIQUE             NOT NULL
);
--;;

INSERT INTO spa.patients (id, name, last_name, sex, birth_date, adress, oms_number) 
VALUES ('2700cb34-04bc-448a-8a48-000000000001', 'Alex', 'Kruglyak', 'male', '1988-03-27', 'Georgia, Batumi, St. Mayakovskaya, 23', '3457 123241'); 
--;; 

INSERT INTO spa.patients (id, name, last_name, sex, birth_date, adress, oms_number) 
VALUES ('2700cb34-04bc-448a-8a48-000000000002', 'Anton', 'Gorobets', 'male', '1986-11-27', 'Georgia, Batumi, St. Georgy Tseretelli, 11', '8630 725432'); 
--;;

INSERT INTO spa.patients (id, name, last_name, sex, birth_date, adress, oms_number) 
VALUES ('2700cb34-04bc-448a-8a48-000000000003', 'Andrey', 'Bragnikov', 'male', '1987-02-28', 'Georgia, Kabuliti, St. Pushkina, 18', '9745 984392'); 
--;; 

INSERT INTO spa.patients (id, name, last_name, sex, birth_date, adress, oms_number) 
VALUES ('2700cb34-04bc-448a-8a48-000000000004', 'Alex', 'Kruglyak', 'male', '1988-03-27', 'Georgia, Batumi, St. Mayakovskaya, 23', '3457 123999'); 
--;; 