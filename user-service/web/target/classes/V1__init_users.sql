
INSERT INTO users (id, email, password, active)
VALUES
  ('00000000-0000-0000-0000-000000000001', 'admin@example.com', '$2a$10$8ZixjeJhKO1vBr5gWHkxHut1dPaIGaFzUv8oxxWovYu3kJO6qIuRu', true),
  ('00000000-0000-0000-0000-000000000002', 'client@example.com', '$2a$10$Yl.4/hYoIxzYAxzH8kl9H.PpG6TZFPAcJDMu4ZpTVPK2LkloiSCr2', true);

INSERT INTO user_roles (user_id, role)
VALUES
  ('00000000-0000-0000-0000-000000000001', 'ADMIN'),
  ('00000000-0000-0000-0000-000000000002', 'CLIENT');
