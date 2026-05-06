CREATE TYPE Role AS ENUM('GUEST', 'USER', 'ADMIN');

CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(16) UNIQUE NOT NULL,
    password  VARCHAR(255)       NOT NULL,
    email     VARCHAR(64) UNIQUE NOT NULL,
    createdAt TIMESTAMP DEFAULT NOW(),
    updatedAt TIMESTAMP DEFAULT NOW()
);

CREATE TABLE user_roles
(
    user_id   BIGINT NOT NULL PRIMARY KEY,
    role      Role      DEFAULT 'USER',
    createdAt TIMESTAMP DEFAULT NOW(),
    updatedAt TIMESTAMP DEFAULT NOW()
);

CREATE TABLE urls
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT,
    actual_url TEXT              NOT NULL,
    short_code VARCHAR(255) UNIQUE NOT NULL,
    createdAt  TIMESTAMP DEFAULT NOW(),
    updatedAt  TIMESTAMP DEFAULT NOW()
);

CREATE TABLE url_metas
(
    id        BIGSERIAL PRIMARY KEY,
    url_id    BIGINT UNIQUE NOT NULL,
    clicks    BIGINT    DEFAULT 0,
    title     VARCHAR(255),
    createdAt TIMESTAMP DEFAULT NOW(),
    updatedAt TIMESTAMP DEFAULT NOW()
);

CREATE TABLE clicks
(
    id           BIGSERIAL PRIMARY KEY,
    url_id       BIGINT NOT NULL,
    ip_address   VARCHAR(45),
    user_agent   TEXT,
    referer      TEXT,
    country_code VARCHAR(2),
    createdAt    TIMESTAMP DEFAULT NOW(),
    updatedAt    TIMESTAMP DEFAULT NOW()
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE urls
    ADD CONSTRAINT fk_urls_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE url_metas
    ADD CONSTRAINT fk_url_metas_urls FOREIGN KEY (url_id) REFERENCES urls (id) ON DELETE CASCADE;

ALTER TABLE clicks
    ADD CONSTRAINT fk_clicks_urls FOREIGN KEY (url_id) REFERENCES urls (id) ON DELETE CASCADE;