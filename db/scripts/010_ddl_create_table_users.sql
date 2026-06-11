CREATE TABLE  IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN DEFAULT true,
    authority_id INT NOT NULL REFERENCES authorities(id)
);