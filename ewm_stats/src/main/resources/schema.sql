CREATE TABLE IF NOT EXISTS hits (
    hits_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app       VARCHAR(50)                             NOT NULL,
    uri       VARCHAR(254),
    ip        VARCHAR(50),
    timestamp TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_hits PRIMARY KEY (hits_id)
);