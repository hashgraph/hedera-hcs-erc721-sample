package com.hedera.hashgraph.seven_twenty_one.contract;

public enum Status {
    OK(0),

    /** Any transaction with a declared transaction ID that does not match the Hedera wrapper. */
    TRANSACTION_ID_MISMATCH(1),

    /** Any transaction called without a caller set in the function body. */
    CALLER_NOT_SET(2),

    /** Any transaction called with an invalid function signature. */
    INVALID_SIGNATURE(3),

    /** Any function except for the constructor called before the constructor is called. */
    CONSTRUCTOR_NOT_CALLED(4),

    /** Any function that references a token by ID where the ID does not exist. */
    TOKEN_NOT_FOUND(5),

    /** Any function asking to operate that is rejected because the caller is not the token owner or does not have some approval. */
    UNAUTHORIZED(6),

    /** Approve function where #spender is the token owner. */
    APPROVE_SPENDER_IS_OWNER(7),

    /** SetApprovalForAll function where #operator is the caller. */
    SET_APPROVAL_FOR_ALL_CALLER_IS_OPERATOR(8),

    /** Mint function where #to is not set.  */
    MINT_TO_NOT_SET(9),

    /** Mint function where the token already exists. */
    MINT_TOKEN_EXISTS(10);

    public final int value;

    private Status(int value) {
        this.value = value;
    }
}
