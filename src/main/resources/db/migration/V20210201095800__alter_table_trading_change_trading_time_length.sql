ALTER TABLE trading ALTER COLUMN trading_time TYPE varchar(100) USING trading_time::varchar;
