package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.ByteString;
import java.math.BigInteger;

public final class Int {

    public static final Int MAX = new Int(new BigInteger("2").pow(256));

    public static final Int ZERO = new Int(BigInteger.ZERO);

    public final BigInteger value;

    public Int(BigInteger value) {
        if (value.compareTo(MAX.value) > 0) {
            // this is *greater than* Int.MAX
            throw new IllegalArgumentException(
                "integers must not exceed 2^256 or " + MAX
            );
        }

        if (value.compareTo(ZERO.value) < 0) {
            // this is *less than* zero
            throw new IllegalArgumentException(
                "integers must not be less than zero"
            );
        }

        this.value = value;
    }

    public static Int fromByteString(ByteString data) {
        return new Int(new BigInteger(data.toByteArray()));
    }

    public ByteString toByteString() {
        return ByteString.copyFrom(value.toByteArray());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var otherInt = (Int) o;
        return value.equals(otherInt.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
