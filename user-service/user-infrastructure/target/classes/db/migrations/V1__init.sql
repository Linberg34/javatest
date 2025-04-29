CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id),
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);

INSERT INTO users (id, email, password, active, username)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'admin@example.com', '$2a$10$8ZixjeJhKO1vBr5gWHkxHut1dPaIGaFzUv8oxxWovYu3kJO6qIuRu', true, 'batka'),
    ('00000000-0000-0000-0000-000000000002', 'client@example.com', '$2a$10$Yl.4/hYoIxzYAxzH8kl9H.PpG6TZFPAcJDMu4ZpTVPK2LkloiSCr2', true, 'sin');

INSERT INTO user_roles (user_id, role)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'ADMIN'),
    ('00000000-0000-0000-0000-000000000002', 'CLIENT');