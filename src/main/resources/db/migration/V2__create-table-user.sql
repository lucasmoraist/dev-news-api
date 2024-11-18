CREATE TABLE IF NOT EXISTS user (
    id BINARY(26) PRIMARY KEY,
    name VARCHAR(180) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);