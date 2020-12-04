package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.common.base.MoreObjects;
import java.util.Objects;

public final class Tuple2<T, U> {

    public final T first;

    public final U second;

    public Tuple2(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Tuple2 tuple3 = (Tuple2) o;

        return first.equals(tuple3.first) && second.equals(tuple3.second);
    }

    @Override
    public String toString() {
        return MoreObjects
            .toStringHelper(this)
            .addValue(first)
            .addValue(second)
            .toString();
    }
}
