package com.reece.platform.mincron.model.contracts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reece.platform.mincron.model.AddressDTO;
import com.reece.platform.mincron.model.BranchDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDTO {

    private String jobName;
    private AddressDTO shipToAddress;
    private String gstHstUseCode;
    private BigDecimal otherCharges;
    private String contractNumber;
    private BigDecimal subTotal;
    private BigDecimal gstHstAmount;
    private String enteredBy;
    private BranchDTO branch;
    private LocalDate contractDate;
    private LocalDate promisedDate;
    private LocalDate firstReleaseDate;
    private LocalDate firstShipmentDate;
    private LocalDate lastReleaseDate;
    private LocalDate lastShipmentDate;
    private BigDecimal totalAmount;
    private String purchaseOrderNumber;
    private BigDecimal taxAmount;
    private BigDecimal otherTaxAmount;
    private String jobNumber;
    private String contractDescription;
    private BigDecimal invoicedToDateAmount;
}
