package com.reece.platform.mincron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AddressDTO {
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String state;
    private String zip;
    private String county;
    private String country;
    private String taxJurisdiction;

    public AddressDTO(String address1, String address2, String address3,
                      String city, String state, String country, String zip) {
        if (address1 != null){
            this.setAddress1(address1.trim().isBlank() ? null : address1.trim());
        }
        if (address2 != null){
            this.setAddress2(address2.trim().isBlank() ? null : address2.trim());
        }
        if (address3 != null){
            this.setAddress3(address3.trim().isBlank() ? null : address3.trim());
        }
        if (city != null){
            this.setCity(city.trim().isBlank() ? null : city.trim());
        }
        if (state != null){
            this.setState(state.trim().isBlank() ? null : state.trim());
        }
        if (country != null){
            this.setCountry(country.trim().isBlank() ? null : country.trim());
        }
        if (zip != null){
            this.setZip(zip.trim().isBlank() ? null : zip.trim());
        }
    }
}
