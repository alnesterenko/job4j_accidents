CREATE TABLE IF NOT EXISTS accident_rule_links (
    accident_id INT NOT NULL,
    rule_id INT NOT NULL,
    PRIMARY KEY (accident_id, rule_id),
    CONSTRAINT fk_accident FOREIGN KEY (accident_id) REFERENCES accidents(id) ON DELETE CASCADE,
    CONSTRAINT fk_rule FOREIGN KEY (rule_id) REFERENCES rule(id) ON DELETE CASCADE
);