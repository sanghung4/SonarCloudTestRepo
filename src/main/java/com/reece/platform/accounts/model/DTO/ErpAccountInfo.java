package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Account information model for shipTo and billTo account
 */
@Data
@NoArgsConstructor
public class ErpAccountInfo implements Serializable {

    private UUID id;
    private ErpEnum erp;
    private String erpAccountId;
    private String companyName;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String branchId;
    private List<String> email;
    private List<ErpAccountInfo> shipToAccounts;
    private List<String> shipToAccountIds;
    private String erpName;
    private String poReleaseRequired;
    private boolean creditHold;
    private String territory;
    private boolean billToFlag;
    private boolean branchFlag;
    private boolean shipFlag;
    private String billToId;

    /**
     * Designates if a customer is a cash account (cash on delivery)
     */
    private Boolean alwaysCod;

    public ErpAccountInfo sanitize(boolean shouldSanitize) {
        if (shouldSanitize) {
            this.setCompanyName(null);
            this.setStreet1(null);
            this.setStreet2(null);
            this.setCity(null);
            this.setState(null);
            this.setZip(null);
            this.setPhoneNumber(null);
            this.setEmail(null);
            this.setShipToAccounts(null);
            this.setTerritory(null);
        }

        return this;
    }

    public ErpAccountInfo(CustomerDTO customerDTO) {
        this.erpAccountId = customerDTO.getId();
        this.companyName = customerDTO.getName();
        this.street1 = customerDTO.getAddressLine1();
        this.street2 = customerDTO.getAddressLine2();
        this.city = customerDTO.getCity();
        this.state = customerDTO.getState();
        this.zip = customerDTO.getPostalCode();
        this.territory = customerDTO.getHomeTerritory();
        this.erpName = ErpEnum.ECLIPSE.name();
        this.creditHold = customerDTO.getTotalCreditHoldFlag();
        this.branchId = customerDTO.getHomeBranch();
        this.billToFlag = customerDTO.getIsBillTo();
        this.branchFlag = customerDTO.getIsBranch();
        this.shipFlag = customerDTO.getIsShipTo();
        this.setBillToId(customerDTO.getBillToId());
        this.setAlwaysCod(customerDTO.getAlwaysCod());
    }
}
