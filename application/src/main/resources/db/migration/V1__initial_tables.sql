CREATE TABLE sectors (
    id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE company_profiles (
    id BIGINT NOT NULL,
    sector_id BIGINT NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(1000) NOT NULL,
    is_buyer BOOLEAN NOT NULL,
    is_supplier BOOLEAN NOT NULL,
    is_logistics BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sector_id) REFERENCES sectors(id)
);

CREATE TABLE users (
    id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    username VARCHAR(20) UNIQUE,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES company_profiles(id)
);
