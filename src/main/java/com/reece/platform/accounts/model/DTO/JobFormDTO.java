package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class JobFormDTO {
    private JobDTO job;
    private ProjectDTO project;
    private GeneralContractorDTO generalContractor;
    private BondingDTO bonding;
    private OwnerDTO owner;
    private LenderDTO lender;
    private String file;
    private String accountId;
    private String fileMimeType;
}
