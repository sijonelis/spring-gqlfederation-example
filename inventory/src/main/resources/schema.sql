CREATE TABLE product_inventories (
  id BIGINT PRIMARY KEY,
  ean VARCHAR(13) NOT NULL,
  open_stock  NUMERIC NOT NULL DEFAULT 0,
  reserved_stock NUMERIC NOT NULL DEFAULT 0,
  sold_stock NUMERIC NOT NULL DEFAULT 0);

