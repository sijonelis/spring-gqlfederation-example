CREATE TABLE products (
  id          BIGINT PRIMARY KEY,
  ean VARCHAR(13) NOT NULL,
  name VARCHAR(64) NOT NULL,
  unit_price NUMERIC NOT NULL,
  unit_weight NUMERIC NOT NULL);

