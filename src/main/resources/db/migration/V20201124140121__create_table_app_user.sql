CREATE TABLE app_user
(
    id SERIAL,
    login varchar(45) NOT NULL,
    password varchar(100) NOT NULL,
    email varchar(45) NOT NULL,
    PRIMARY KEY (id)
);
