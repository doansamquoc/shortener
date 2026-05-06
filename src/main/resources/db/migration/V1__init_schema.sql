CREATE TYPE Role AS ENUM('GUEST', 'USER', 'ADMIN');

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(16) UNIQUE NOT NULL,
    password   VARCHAR(255)       NOT NULL,
    email      VARCHAR(64) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    role       Role      DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE urls
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT,
    actual_url    TEXT                NOT NULL,
    short_code    VARCHAR(255) UNIQUE NOT NULL,
    title         VARCHAR(255),
    total_clicks  BIGINT    DEFAULT 0,
    last_click_at TIMESTAMP DEFAULT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clicks
(
    id           BIGSERIAL PRIMARY KEY,
    url_id       BIGINT NOT NULL,
    ip_address   VARCHAR(45),
    user_agent   TEXT,
    referer      TEXT,
    country_code VARCHAR(2),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE urls
    ADD CONSTRAINT fk_urls_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE clicks
    ADD CONSTRAINT fk_clicks_urls FOREIGN KEY (url_id) REFERENCES urls (id) ON DELETE CASCADE;