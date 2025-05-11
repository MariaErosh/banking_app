-- Users
INSERT INTO "user" (id, name, date_of_birth, password) VALUES
(1, 'Иван Иванов', DATE '1990-01-01', '$2a$10$H4J5v.9XwtPbtzxg8K4pN.2gdBi2f7hUqTIlUuR/0ao2PwQTyVybi'),
(2, 'Мария Кузнецова', DATE '1985-05-15', '$2a$10$H4J5v.9XwtPbtzxg8K4pN.2gdBi2f7hUqTIlUuR/0ao2PwQTyVybi'),
(3, 'Алексей Кузнецов', DATE '1993-10-10', '$2a$10$H4J5v.9XwtPbtzxg8K4pN.2gdBi2f7hUqTIlUuR/0ao2PwQTyVybi');

-- Accounts
INSERT INTO account (id, user_id, balance, init_balance) VALUES
(1, 1, 1500.75, 1500.75),
(2, 2, 25000.00, 25000.00),
(3, 3, 320.10, 320.10);

-- Emails
INSERT INTO email_data (id, user_id, email) VALUES
(1, 1, 'ivan@example.com'),
(2, 2, 'maria.s@example.ru'),
(3, 2, 'maria.s@yandex.ru'),
(4, 3, 'alex.k@example.com');

-- Phones
INSERT INTO phone_data (id, user_id, phone) VALUES
(1, 1, '79201234567'),
(2, 2, '79207654321'),
(3, 2, '79207651234'),
(4, 3, '79209876543');
