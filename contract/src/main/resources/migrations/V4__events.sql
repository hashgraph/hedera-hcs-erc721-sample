CREATE TABLE "event"
(
    -- consensus timestamp of the event
    -- links to the transaction as well
    -- nanoseconds since the UNIX epoch
    timestamp INT8 PRIMARY KEY,

    -- which event
    -- value is <Event>
    event     INT2 NOT NULL
);

CREATE INDEX ON "event" (event);

-- https://docs.timescale.com/latest/api
SELECT create_hypertable('event', 'timestamp', chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('event', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE event_constructed
(
    timestamp    INT8 PRIMARY KEY,

    token_name   TEXT NOT NULL,
    token_symbol TEXT NOT NULL
);

-- NOTE: This is explicitly not a hypertable because its meant to only ever have 1 record

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE event_approval
(
    timestamp           INT8 PRIMARY KEY,

    owner_address_id    INT8           NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    approved_address_id INT8           NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    token_id            NUMERIC(78, 0) NOT NULL
);

SELECT create_hypertable('event_approval', 'timestamp', chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('event_approval', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE event_approval_for_all
(
    timestamp           INT8 PRIMARY KEY,

    owner_address_id    INT8    NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    operator_address_id INT8    NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    approved            BOOLEAN NOT NULL
);

SELECT create_hypertable('event_approval_for_all', 'timestamp', chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('event_approval_for_all', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE event_transfer
(
    timestamp       INT8 PRIMARY KEY,

    -- NULL = 0x
    from_address_id INT8
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    -- NULL = 0x
    to_address_id   INT8
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    token_id        NUMERIC(78, 0) NOT NULL
);

SELECT create_hypertable('event_transfer', 'timestamp',
                         chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('event_transfer', 'unix_nano_now');
