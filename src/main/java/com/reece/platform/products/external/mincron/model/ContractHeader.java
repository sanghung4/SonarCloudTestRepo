package com.reece.platform.products.external.mincron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractHeader {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Branch {

        private String branchNumber;
        private String branchName;
        private Address addressDTO;
    }

    private String jobName;
    private Address shipToAddress;
    private String gstHstUseCode;
    private LocalDate contractDate;
    private LocalDate promisedDate;
    private String otherCharges;
    private String contractNumber;
    private String subTotal;
    private String gstHstAmount;
    private String enteredBy;
    private Branch branch;
    private LocalDate firstReleaseDate;
    private LocalDate firstShipmentDate;
    private LocalDate lastReleaseDate;
    private String totalAmount;
    private List<String> specialInstructions;
    private String purchaseOrderNumber;
    private LocalDate lastShipmentDate;
    private String taxAmount;
    private String jobNumber;
    private String contractDescription;
    private String invoicedToDateAmount;
}
