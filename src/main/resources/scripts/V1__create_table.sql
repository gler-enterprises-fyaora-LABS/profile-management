-- Table for storing user type

CREATE TABLE IF NOT EXISTS user_type (
    did INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL, -- TYPE 1 = Registration Service Provider, TYPE 2 = Customer Provider
    description VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL
);
