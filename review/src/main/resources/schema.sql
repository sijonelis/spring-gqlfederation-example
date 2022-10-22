CREATE TABLE reviews (
  id BIGINT PRIMARY KEY,
  body VARCHAR(2048) NOT NULL,
  author_id BIGINT NOT NULL,
  product_ean VARCHAR(13) NOT NULL,
  rating INT NOT NULL);

