package com.hedera.hashgraph.seven_twenty_one.contract;

public enum Event {
    CONSTRUCTED(1),
    APPROVAL(2),
    APPROVAL_FOR_ALL(3),
    TRANSFER(4);

    public final int value;

    private Event(int value) {
        this.value = value;
    }
}
