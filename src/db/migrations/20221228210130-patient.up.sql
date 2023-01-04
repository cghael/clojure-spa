CREATE TABLE IF NOT EXISTS spa.patients (
  id          uuid        PRIMARY KEY NOT NULL,
  name        text                    NOT NULL,
  last_name   text                    NOT NULL,
  sex         text                    DEFAULT 'unknown',
  birth_date  timestamp               DEFAULT '1970-01-01 00:00:00',
  adress      text                    DEFAULT 'unknown',
  oms_number  text UNIQUE             NOT NULL
);