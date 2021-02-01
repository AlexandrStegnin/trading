ALTER TABLE trading ALTER COLUMN accept_requests_date TYPE varchar(100) USING accept_requests_date::varchar;
