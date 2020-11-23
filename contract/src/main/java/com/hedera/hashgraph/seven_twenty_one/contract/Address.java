package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.PublicKey;

public final class Address {

    public final PublicKey publicKey;

    public Address(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public static Address fromString(String text) {
        return new Address(PublicKey.fromString(text));
    }

    public static Address fromByteString(ByteString data) {
        return new Address(PublicKey.fromBytes(data.toByteArray()));
    }

    public ByteString toByteString() {
        return ByteString.copyFrom(publicKey.toBytes());
    }

    @Override
    public String toString() {
        return publicKey.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address otherAddress = (Address) o;
        return publicKey.equals(otherAddress.publicKey);
    }

    @Override
    public int hashCode() {
        return publicKey.hashCode();
    }
}
