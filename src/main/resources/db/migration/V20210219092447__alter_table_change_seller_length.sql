ALTER TABLE trading ALTER COLUMN seller TYPE varchar(255) USING trading_time::varchar;
