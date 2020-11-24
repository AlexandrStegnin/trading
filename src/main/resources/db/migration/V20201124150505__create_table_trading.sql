CREATE TABLE trading
(
    lot                  varchar(250)   NOT NULL,
    description          varchar(8000)  NULL     DEFAULT NULL,
    address              varchar(100)   NULL     DEFAULT NULL,
    trading_number       varchar(50)    NULL     DEFAULT NULL,
    efrsb_id             varchar(50)    NULL     DEFAULT NULL,
    auction_step         varchar(20)    NULL     DEFAULT NULL,
    deposit_amount       varchar(20)    NULL     DEFAULT NULL,
    trading_time         varchar(20)    NULL     DEFAULT NULL,
    accept_requests_date varchar(30)    NULL     DEFAULT NULL,
    url                  varchar(1000)  NULL     DEFAULT NULL,
    price                numeric(20, 2) NULL     DEFAULT NULL,
    seller               varchar(20)    NULL     DEFAULT NULL,
    creation_time        timestamp      NOT NULL DEFAULT now(),
    modified_time        timestamp      NULL,
    id                   serial         NOT NULL,
    PRIMARY KEY (id)
);
