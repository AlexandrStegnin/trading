CREATE TABLE cadaster
(
    id SERIAL,
    address varchar(255) NOT NULL,
    cad_number varchar(20) NOT NULL,
    type varchar(20) NOT NULL,
    area varchar(20) NOT NULL,
    category varchar(50) NOT NULL,
    creation_time TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_time TIMESTAMP NULL,
    modified_by VARCHAR(45) NOT NULL DEFAULT '',
    PRIMARY KEY (id)
);
