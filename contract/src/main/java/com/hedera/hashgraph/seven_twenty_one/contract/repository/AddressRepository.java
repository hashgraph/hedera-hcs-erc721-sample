package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.ADDRESS;

import com.google.errorprone.annotations.Var;
import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import java.util.HashMap;
import java.util.Map;
import org.jooq.DSLContext;

public final class AddressRepository {

    private final DSLContext context;

    private final Map<Address, Long> addressMap = new HashMap<>();

    public AddressRepository(DSLContext context) {
        this.context = context;
    }

    public long toAddressId(Address address) {
        @Var
        var addressId = addressMap.get(address);

        if (addressId != null) {
            return addressId;
        }

        var addressBytes = address.toByteString().toByteArray();

        addressId =
            context
                .insertInto(ADDRESS, ADDRESS.ADDRESS_)
                .values(addressBytes)
                .onConflict(ADDRESS.ADDRESS_)
                .doUpdate()
                .set(ADDRESS.ADDRESS_, addressBytes)
                .returning(ADDRESS.ID)
                .fetchOne()
                .get(ADDRESS.ID);

        addressMap.put(address, addressId);

        return addressId;
    }
}
