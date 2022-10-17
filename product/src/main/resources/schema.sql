CREATE TABLE products (
  id UUID PRIMARY KEY,
  ean VARCHAR(13) UNIQUE NOT NULL,
  name VARCHAR(64) NOT NULL,
  unit_price NUMERIC NOT NULL,
  unit_weight NUMERIC NOT NULL);

CREATE INDEX idx_ean ON products(ean);

CREATE TABLE kotlin_products (
  ean VARCHAR(13) PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  unit_price NUMERIC NOT NULL,
  unit_weight NUMERIC NOT NULL);

