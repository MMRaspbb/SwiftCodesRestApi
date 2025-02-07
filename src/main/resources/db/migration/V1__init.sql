CREATE TABLE banks
(
    id SERIAL PRIMARY KEY,
    address VARCHAR(500),
    bank_name VARCHAR(500) NOT NULL,
    country_iso2 VARCHAR(2) NOT NULL,
    country_name VARCHAR(50) NOT NULL,
    is_headquarter BOOLEAN NOT NULL,
    swift_code VARCHAR(11) UNIQUE NOT NULL
);

CREATE TABLE bank_relationships
(
    id SERIAL PRIMARY KEY ,
    headquarter_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL
);

ALTER TABLE bank_relationships
    ADD CONSTRAINT headquarter_to_branch
        FOREIGN KEY (headquarter_id)
            REFERENCES banks(id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE,
    ADD CONSTRAINT branch_to_headquarters
        FOREIGN KEY (branch_id)
            REFERENCES banks(id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE;