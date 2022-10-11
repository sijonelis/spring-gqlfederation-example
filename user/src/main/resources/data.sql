INSERT INTO addresses (id, city, country) VALUES
   (1, 'Washington', 'US'),
   (2, 'Ice Kingdom', 'Fairyland'),
   (3, '213ADD2F', 'Mars');

INSERT INTO users (id, name, username, address_id) VALUES
  (1, 'John Wick', 'johnybravo87', 1),
  (2, 'Elza', 'tooc0ldforu', 2),
  (3, 'El Kunga', 'sleepyhead', 3);

insert into orders (id, total, status, buyer_id) VALUES
  (1, 25.0, 1, 1);

insert into order_lines (id, eam, quantity, price, order_id) VALUES
    (1, '1234567890122', 1, 25.0, 1)