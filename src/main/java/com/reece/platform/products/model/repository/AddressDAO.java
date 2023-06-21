package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDAO extends JpaRepository<Address, UUID> {
    Address findByStreet1AndCityAndStateAndZipAndCountry(
        String street1,
        String city,
        String state,
        String zip,
        String country
    );
}
