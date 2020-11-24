-- de-duplicate address values
CREATE TABLE address
(
    id      BIGSERIAL PRIMARY KEY,
    address BYTEA NOT NULL UNIQUE
);

-- // ------------------------------------------------------------------------------------------------------------------

-- link each relevant address of the transaction to the transaction timestamp
-- used to efficiently provide: GET /{address}/transaction
CREATE TABLE address_transaction
(
    address_id            INT8 NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    transaction_timestamp INT8 NOT NULL,

    PRIMARY KEY (address_id, transaction_timestamp)
);

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE "transaction"
(
    -- nanoseconds since the UNIX epoch
    timestamp                   INT8 PRIMARY KEY,

    -- was this function successful ? 0 -> success, !0 -> failure
    -- <Status>
    status                      INT2 NOT NULL,

    -- which function
    -- value is <FunctionBody.DataCase>
    function                    INT2 NOT NULL,

    -- address of the caller
    caller_address_id           INT8 NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    -- account num for the Hedera (tm) operator
    -- used in conjunction with <hedera_valid_start_nanos> for the client to ask the status of a function
    hedera_operator_account_num INT8 NOT NULL,

    -- valid start time for the transaction
    -- nanos since the UNIX epoch
    hedera_valid_start_nanos    INT8 NOT NULL
);

CREATE INDEX ON "transaction" (hedera_operator_account_num, hedera_valid_start_nanos);

CREATE INDEX ON "transaction" (status);

CREATE INDEX ON "transaction" (function);

-- https://docs.timescale.com/latest/api
SELECT create_hypertable('transaction', 'timestamp', chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('transaction', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE transaction_constructor
(
    timestamp    INT8 PRIMARY KEY,

    token_name   TEXT NOT NULL,
    token_symbol TEXT NOT NULL,
    base_uri     TEXT
);

-- NOTE: This is explicitly not a hypertable because its meant to only ever have 1 record

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE transaction_approve
(
    timestamp          INT8 PRIMARY KEY,

    spender_address_id INT8           NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    token_id           NUMERIC(78, 0) NOT NULL
);

SELECT create_hypertable('transaction_approve', 'timestamp', chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('transaction_approve', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE transaction_set_approval_for_all
(
    timestamp           INT8 PRIMARY KEY,

    operator_address_id INT8    NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    approved            BOOLEAN NOT NULL
);

SELECT create_hypertable('transaction_set_approval_for_all', 'timestamp',
                         chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('transaction_set_approval_for_all', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE transaction_mint
(
    timestamp     INT8 PRIMARY KEY,

    to_address_id INT8           NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    token_id      NUMERIC(78, 0) NOT NULL
);

SELECT create_hypertable('transaction_mint', 'timestamp',
                         chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('transaction_mint', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE transaction_burn
(
    timestamp INT8 PRIMARY KEY,

    token_id  NUMERIC(78, 0) NOT NULL
);

SELECT create_hypertable('transaction_burn', 'timestamp',
                         chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('transaction_burn', 'unix_nano_now');

-- // ------------------------------------------------------------------------------------------------------------------

CREATE TABLE transaction_transfer_from
(
    timestamp       INT8 PRIMARY KEY,

    from_address_id INT8           NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    to_address_id   INT8           NOT NULL
        REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE,

    token_id        NUMERIC(78, 0) NOT NULL
);

SELECT create_hypertable('transaction_transfer_from', 'timestamp',
                         chunk_time_interval => unix_nano_interval('24 hour'));
SELECT set_integer_now_func('transaction_transfer_from', 'unix_nano_now');
