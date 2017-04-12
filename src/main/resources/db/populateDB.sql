DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);
INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100000, '2017-04-07T9:00', 'Завтрак', 800),
  (100000, '2017-04-07T14:00', 'Обед', 1000),
  (100000, '2017-04-07T19:00', 'Ужин', 700),
  (100000, '2017-04-08T9:00', 'Завтрак', 850),
  (100000, '2017-04-08T14:00', 'Обед', 1200),
  (100000, '2017-04-08T18:30', 'Ужин', 800),
  (100000, '2017-04-09T9:30', 'Завтрак', 800),
  (100000, '2017-04-09T15:10', 'Обед', 1000),
  (100000, '2017-04-09T19:00', 'Ужин', 200);
