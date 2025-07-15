CREATE TABLE "user" (
    id BIGINT PRIMARY KEY,
    name VARCHAR(500),
    date_of_birth DATE,
    password VARCHAR(500)
);

CREATE TABLE account (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance NUMERIC(19, 2) NOT NULL CHECK (balance >= 0),
    init_balance DECIMAL(19,2) NOT NULL CHECK (init_balance >= 0),
    currency_code VARCHAR(3) NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE email_data (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    email VARCHAR(200) UNIQUE,
    CONSTRAINT fk_email_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE phone_data (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    phone VARCHAR(13) UNIQUE,
    CONSTRAINT fk_phone_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE SEQUENCE user_seq START WITH 10 INCREMENT BY 50;
CREATE SEQUENCE account_seq START WITH 10 INCREMENT BY 50;
CREATE SEQUENCE email_data_seq START WITH 10 INCREMENT BY 50;
CREATE SEQUENCE phone_data_seq START WITH 10 INCREMENT BY 50;