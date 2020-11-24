-- Define NOW in "nanoseconds since the epoch"
-- https://docs.timescale.com/latest/api#set_integer_now_func
CREATE OR REPLACE FUNCTION unix_nano_now() returns BIGINT
    LANGUAGE SQL
    STABLE as
$$
SELECT extract(epoch from now())::BIGINT * 1000000000::BIGINT
$$;

-- Define an interval conversion function
CREATE OR REPLACE FUNCTION unix_nano_interval(ival text) returns BIGINT
    LANGUAGE SQL
    STABLE AS
$$
SELECT extract(epoch from $1::interval)::BIGINT * 1000000000::BIGINT
$$;

-- Define timestamp conversion function from nanos to timestamp
CREATE OR REPLACE FUNCTION unix_nano_timestamp(un BIGINT) returns TIMESTAMPTZ
    LANGUAGE SQL
    STABLE AS
$$
SELECT to_timestamp($1 / 1000000000::BIGINT)
$$;

-- Define timestamp conversion function from timestamp to nanos
CREATE OR REPLACE FUNCTION to_unix_nano(ts TIMESTAMP) returns BIGINT
    LANGUAGE SQL
    STABLE AS
$$
SELECT extract(epoch from $1)::BIGINT * 1000000000::BIGINT
$$;
