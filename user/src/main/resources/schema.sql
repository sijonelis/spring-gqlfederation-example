CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  username VARCHAR(64) NOT NULL,
  address_id   BIGINT);

CREATE TABLE addresses (
   id BIGINT PRIMARY KEY,
   city VARCHAR(64) NOT NULL,
   country VARCHAR(64) NOT NULL);

ALTER TABLE users
    ADD FOREIGN KEY (address_id)
        REFERENCES addresses(id);

CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    total NUMERIC NOT NULL,
    status SMALLINT NOT NULL,
    buyer_id BIGINT NOT NULL);

CREATE TABLE order_lines (
    id BIGINT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    eam VARCHAR(13) NOT NULL,
    price NUMERIC NOT NULL,
    quantity NUMERIC NOT NULL);

ALTER TABLE orders
    ADD FOREIGN KEY (buyer_id)
        REFERENCES users(id);

ALTER TABLE order_lines
    ADD FOREIGN KEY (order_id)
        REFERENCES orders(id);