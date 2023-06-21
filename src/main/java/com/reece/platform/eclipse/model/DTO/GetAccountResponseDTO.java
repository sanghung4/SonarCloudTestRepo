package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.EntityResponse.EntityResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetAccountResponseDTO implements Serializable {
    private String erpAccountId;
    private String companyName;
    private String phoneNumber;
    private List<String> email;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private String branchId;
    private String poReleaseRequired;
    private boolean creditHold;
    private List<GetAccountResponseDTO> shipToAccounts;
    private List<String> shipToAccountIds;

    public GetAccountResponseDTO() {}

    public GetAccountResponseDTO(EntityResponse res, List<GetAccountResponseDTO> shipToAccounts) {
        if (res.getEntityInquiryResponse() != null) {
            String companyName = res.getEntityInquiryResponse().getEntity().getEntityName();
            if(companyName != null) { this.setCompanyName(companyName); }

            String id = res.getEntityInquiryResponse().getEntity().getEntityID();
            this.setErpAccountId(id);

            if (res.getEntityInquiryResponse().getEntity().getEmailAddressList() != null) {
                this.setEmail(res.getEntityInquiryResponse().getEntity().getEmailAddressList().getEmailAddresses().stream().map(ea -> ea.getContent()).collect(Collectors.toList()));
            }

            if (res.getEntityInquiryResponse().getEntity().getContactShortList() != null) {
                String phoneNumber = res.getEntityInquiryResponse().getEntity().getContactShortList().getContactShort().getTelephone().getNumber();
                this.setPhoneNumber(phoneNumber);
            }

            if (res.getEntityInquiryResponse().getEntity().getAddress() != null) {
                String street1 = res.getEntityInquiryResponse().getEntity().getAddress().getStreetLineOne();
                this.setStreet1(street1);
                String street2 = res.getEntityInquiryResponse().getEntity().getAddress().getStreetLineTwo();
                this.setStreet2(street2);
                String city = res.getEntityInquiryResponse().getEntity().getAddress().getCity();
                this.setCity(city);
                String state = res.getEntityInquiryResponse().getEntity().getAddress().getState();
                this.setState(state);
                String zip = res.getEntityInquiryResponse().getEntity().getAddress().getPostalCode();
                this.setZip(zip);
            }

            if (res.getEntityInquiryResponse().getEntity().getBranch() != null) {
                this.setBranchId(res.getEntityInquiryResponse().getEntity().getBranch().getBranchId());
            }

            if(shipToAccounts != null) {
                this.shipToAccounts = shipToAccounts;
            }

            this.setPoReleaseRequired(res.getEntityInquiryResponse().getEntity().getPOReleaseRequired());

            if (res.getEntityInquiryResponse().getEntity().getCredit() != null) {
                this.creditHold = res.getEntityInquiryResponse().getEntity().getCredit().getOrderEntryOK().equals("No");
            }
        }
    }
}
