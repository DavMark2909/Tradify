CREATE TABLE sectors (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(50) NOT NULL UNIQUE,
                         description VARCHAR(500) NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE company_profiles (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
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

CREATE TABLE products (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          sector_id BIGINT NOT NULL,
                          supplier_profile_id BIGINT NOT NULL,
                          title VARCHAR(255) NOT NULL,
                          description VARCHAR(1000),
                          price DECIMAL(19, 2) NOT NULL,
                          currency VARCHAR(10) NOT NULL,
                          unit_of_measure VARCHAR(50) NOT NULL,
                          available_quantity DECIMAL(19, 2) NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          created_at DATETIME NOT NULL,
                          PRIMARY KEY (id),
                          FOREIGN KEY (sector_id) REFERENCES sectors(id),
                          FOREIGN KEY (supplier_profile_id) REFERENCES company_profiles(id)
);

CREATE TABLE saved_items (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             buyer_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             created_at DATETIME NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (buyer_id) REFERENCES users(id),
                             FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE trade_agreements (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  buyer_profile_id BIGINT NOT NULL,
                                  seller_profile_id BIGINT NOT NULL,
                                  product_title VARCHAR(255) NOT NULL,
                                  purchase_price DECIMAL(19, 2) NOT NULL,
                                  currency VARCHAR(10) NOT NULL,
                                  quantity DECIMAL(19, 2) NOT NULL,
                                  unit_of_measure VARCHAR(50) NOT NULL,
                                  status VARCHAR(50) NOT NULL,
                                  created_at DATETIME NOT NULL,
                                  PRIMARY KEY (id),
                                  FOREIGN KEY (buyer_profile_id) REFERENCES company_profiles(id),
                                  FOREIGN KEY (seller_profile_id) REFERENCES company_profiles(id)
);

