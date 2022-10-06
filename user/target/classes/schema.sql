CREATE TABLE users (
  id          BIGINT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  username VARCHAR(64) NOT NULL,
  address_id   BIGINT);

CREATE TABLE addresses (
   id          BIGINT PRIMARY KEY,
   city VARCHAR(64) NOT NULL,
   country VARCHAR(64) NOT NULL);

ALTER TABLE users
    ADD FOREIGN KEY (address_id)
        REFERENCES addresses(id)
