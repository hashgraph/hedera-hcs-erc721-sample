package com.hedera.hashgraph.seven_twenty_one.contract;

public final class StatusException extends Exception {

    public final Status status;

    public StatusException(Status status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return "function failed with status " + status;
    }
}
