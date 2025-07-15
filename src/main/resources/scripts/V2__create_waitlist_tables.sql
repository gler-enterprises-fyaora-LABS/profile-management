CREATE TABLE IF NOT EXISTS waitlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_type TINYINT NOT NULL, -- TYPE 1 = Service Provider, TYPE 2 = Customer, TYPE 3 - Investor
    email VARCHAR(150) NOT NULL,
    telnum VARCHAR(20),
    name VARCHAR(500),
    postcode VARCHAR(30),
    vendor_type TINYINT, -- 1 - Independent, 2 - Company
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS service_offered (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    description VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS waitlist_service (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    waitlist_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    FOREIGN KEY (waitlist_id) REFERENCES waitlist(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES service_offered(id) ON DELETE CASCADE
);
