package com.reece.platform.products.model.eclipse.common;

import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.model.DTO.AddressDTO;
import com.reece.platform.products.model.DTO.BranchOrderInfoDTO;
import com.reece.platform.products.model.entity.Address;
import com.reece.platform.products.utilities.StringUtilities;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EclipseAddressResponseDTO {

    public EclipseAddressResponseDTO(Address address) {
        this.streetLineOne = address.getStreet1();
        this.streetLineTwo = address.getStreet2();
        this.city = address.getCity();
        this.state = address.getState();
        this.postalCode = address.getZip();
        this.country = address.getCountry();
    }

    public EclipseAddressResponseDTO(com.reece.platform.products.external.mincron.model.Address address) {
        this.streetLineOne = address.getAddress1();
        this.streetLineTwo = address.getAddress2();
        this.city = address.getCity();
        this.state = address.getState();
        this.postalCode = address.getZip();
        this.country = address.getCountry();
    }

    public EclipseAddressResponseDTO(BranchOrderInfoDTO branchInfo) {
        this.streetLineOne = branchInfo.getStreetLineOne();
        this.streetLineTwo = branchInfo.getStreetLineTwo();
        this.streetLineThree = branchInfo.getStreetLineThree();
        this.city = branchInfo.getCity();
        this.state = branchInfo.getState();
        this.postalCode = branchInfo.getPostalCode();
        this.country = branchInfo.getCountry();
    }

    public EclipseAddressResponseDTO(BranchResponseDTO branchResponseDTO) {
        this.streetLineOne = StringUtilities.trimString(branchResponseDTO.getAddress1());
        this.streetLineTwo = StringUtilities.trimString(branchResponseDTO.getAddress2());
        this.city = StringUtilities.trimString(branchResponseDTO.getCity());
        this.state = StringUtilities.trimString(branchResponseDTO.getState());
        this.postalCode = StringUtilities.trimString(branchResponseDTO.getZip());
        this.country = null;
    }

    public EclipseAddressResponseDTO(AddressDTO shipToAddress) {
        this.streetLineOne = StringUtilities.trimString(shipToAddress.getAddress1());
        this.streetLineTwo = StringUtilities.trimString(shipToAddress.getAddress2());
        this.streetLineThree = StringUtilities.trimString(shipToAddress.getAddress3());
        this.city = StringUtilities.trimString(shipToAddress.getCity());
        this.state = StringUtilities.trimString(shipToAddress.getState());
        this.postalCode = StringUtilities.trimString(shipToAddress.getZip());
        this.country = null;
    }

    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
