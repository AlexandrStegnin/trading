ALTER TABLE trading ALTER COLUMN seller TYPE varchar(100) USING trading_time::varchar;
