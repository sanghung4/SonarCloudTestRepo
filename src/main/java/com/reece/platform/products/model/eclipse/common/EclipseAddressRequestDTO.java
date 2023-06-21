package com.reece.platform.products.model.eclipse.common;

import com.reece.platform.products.model.entity.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EclipseAddressRequestDTO {

    public EclipseAddressRequestDTO(Address address) {
        if (address.getCompanyName() != null) {
            this.streetLineOne = address.getCompanyName();
            this.streetLineTwo = address.getStreet1();
            this.streetLineThree = address.getStreet2();
        } else {
            this.streetLineOne = address.getStreet1();
            this.streetLineTwo = address.getStreet2();
        }
        this.city = address.getCity();
        this.state = address.getState();
        this.postalCode = address.getZip();
        this.country = address.getCountry();
    }

    private String streetLineOne;

    private String streetLineTwo;

    private String streetLineThree;

    private String city;

    private String state;

    private String postalCode;

    private String country;
}
