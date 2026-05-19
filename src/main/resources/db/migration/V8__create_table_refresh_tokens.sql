CREATE TABLE refresh_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    token      VARCHAR(255) NOT NULL,
    revoked    BOOLEAN   DEFAULT FALSE,
    expires_at TIMESTAMP    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT fk_refresh_tokens_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

