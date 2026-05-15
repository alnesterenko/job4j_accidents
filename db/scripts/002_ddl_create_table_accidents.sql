CREATE TABLE IF NOT EXISTS accidents (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    text TEXT,
    address VARCHAR(255),
    type_id INT NOT NULL,
    CONSTRAINT fk_accident_type FOREIGN KEY (type_id) REFERENCES accident_type(id)
);