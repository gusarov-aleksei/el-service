CREATE SCHEMA IF NOT EXISTS el_service AUTHORIZATION postgres;
SET SCHEMA 'el_service';

CREATE TABLE IF NOT EXISTS podcast (
                                       id SMALLSERIAL NOT NULL PRIMARY KEY,
                                       metadata jsonb,
                                       body json
);