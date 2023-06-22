package com.reece.platform.mincron.model.contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reece.platform.mincron.model.AddressDTO;
import lombok.Data;

@Data
public class SubmitOrderRequestDTO {
    private String shipBranchNumber;
    private String promiseDate;
    private String shipMethod;
    private String shipCode;
    private String shipDescription;
    private String phoneNumber;
    private String jobNumber;
    private String jobName;
    private String subTotal;
    private String taxAmount;
    private String shipHandleAmount;
    private String orderTotal;
    private String spInstructions;
    private String orderComments;
    private String poNumber;
    private AddressDTO shipToAddress;
}
