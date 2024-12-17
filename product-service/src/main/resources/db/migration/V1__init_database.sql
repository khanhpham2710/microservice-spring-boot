CREATE TABLE categories (
   id INT NOT NULL AUTO_INCREMENT,
   description VARCHAR(255) DEFAULT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   name VARCHAR(255) DEFAULT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE products (
   id INT NOT NULL AUTO_INCREMENT,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   available_quantity DOUBLE NOT NULL,
   description VARCHAR(255) DEFAULT NULL,
   name VARCHAR(255) DEFAULT NULL,
   price DECIMAL(10, 2) DEFAULT NULL,
   category_id INT DEFAULT NULL,
   created_by VARCHAR(255) DEFAULT "admin",
   last_update_by INT,
   PRIMARY KEY (id),
   CONSTRAINT FK_category FOREIGN KEY (category_id) REFERENCES categories (id)
);