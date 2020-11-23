package com.hedera.hashgraph.seven_twenty_one.contract;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public final class State {

    // timestamp that this state was last updated
    // null = never updated
    @Nullable
    private Instant timestamp;

    // mapping from holder addresses to their tokens
    private final Map<Address, Set<Int>> holderTokens;

    // mapping from token ID to the token owner address
    private final Map<Int, Address> tokenOwners;

    // mapping from token ID to approved addresses
    private final Map<Int, Address> tokenApprovals;

    private final Map<Address, Map<Address, Boolean>> operatorApprovals;

    private final Map<Int, String> tokenURIs;

    // each Hedera contract has an owner
    @Nullable
    private Address owner;

    @Nullable
    private String tokenName;

    @Nullable
    private String tokenSymbol;

    @Nullable
    private String baseURI;

    public State() {
        holderTokens = new HashMap<>();
        tokenOwners = new HashMap<>();
        tokenApprovals = new HashMap<>();
        operatorApprovals = new HashMap<>();
        tokenURIs = new HashMap<>();
    }

    @Nullable
    public Instant getTimestamp() {
        return timestamp;
    }
}
