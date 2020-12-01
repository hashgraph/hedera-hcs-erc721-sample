package com.hedera.hashgraph.seven_twenty_one.contract;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class State {

    // used to lock write access to state during state snapshot serialization
    private final Lock lock = new ReentrantLock();

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

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public String getTokenName() {
        return this.tokenName;
    }

    public String getTokenSymbol() {
        return this.tokenSymbol;
    }

    public String getBaseURI() {
        return this.baseURI;
    }

    public void setTokenName(@Nonnull String tokenName) {
        this.tokenName = tokenName;
    }

    public void setTokenSymbol(@Nonnull String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public void setBaseURI(@Nonnull String baseURI) {
        this.baseURI = baseURI;
    }

    @Nullable
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@Nonnull Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Nullable
    public Address getOwner() {
        return owner;
    }

    public void setOwner(@Nonnull Address owner) {
        this.owner = owner;
    }

    @Nullable
    public Address getTokenOwner(Int tokenId) {
        return tokenOwners.get(tokenId);
    }

    public boolean isApproved(Address caller, Int tokenId) {
        var tokenApproval = tokenApprovals.get(tokenId);

        return tokenApproval != null && tokenApproval.equals(caller);
    }

    @Nullable
    public Address getApproved(Int tokenId) {
        return tokenApprovals.get(tokenId);
    }

    public boolean isOperatorApproved(Address caller, Int tokenId) {
        var operatorApproval = operatorApprovals.get(
            Objects.requireNonNull(tokenOwners.get(tokenId))
        );

        return (
            operatorApproval != null &&
            operatorApproval.getOrDefault(caller, false)
        );
    }

    public void setTokenApproval(Int tokenId, Address spender) {
        tokenApprovals.put(tokenId, spender);
    }

    public void clearTokenOwner(Int tokenId) {
        tokenOwners.remove(tokenId);
    }

    public void clearTokenApproval(Int tokenId) {
        tokenApprovals.remove(tokenId);
    }

    public void clearTokenURI(Int tokenId) {
        tokenURIs.remove(tokenId);
    }

    public void setOperatorApproval(
        Address caller,
        Address operator,
        boolean approved
    ) {
        operatorApprovals
            .computeIfAbsent(caller, v -> new HashMap<>())
            .put(operator, approved);
    }

    public void addToken(Int tokenId, Address to) {
        holderTokens.computeIfAbsent(to, v -> new HashSet<>()).add(tokenId);
    }

    public void setTokenOwner(Int tokenId, Address to) {
        tokenOwners.put(tokenId, to);
    }

    public void removeToken(Int tokenId, Address from) {
        var tokenHolder = holderTokens.get(from);

        if (tokenHolder == null) {
            // address holds no tokens
            return;
        }

        tokenHolder.remove(tokenId);
    }

    public int getNumberOfTokenOwners() {
        return tokenOwners.size();
    }

    public int balanceOf(Address address) {
        var tokens = holderTokens.get(address);

        return tokens == null ? 0 : tokens.size();
    }

    public Set<Int> getTokens(Address address) {
        return holderTokens.getOrDefault(address, Collections.emptySet());
    }
}
