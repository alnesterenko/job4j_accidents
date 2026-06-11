INSERT INTO users (username, enabled, password, authority_id)
VALUES ('root', true, '$2a$10$wY1twJhMQjGVxv4y5dBC5ucCBlzkzT4FIGa4FNB/pS9GaXC2wm9/W',
(SELECT id FROM authorities WHERE authority = 'ROLE_ADMIN'));