ALTER TABLE trading ALTER COLUMN auction_step TYPE varchar(100) USING auction_step::varchar;
