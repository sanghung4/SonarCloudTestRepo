package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.ErpUserInformation;
import lombok.Data;

@Data
public class SubmitOrderDTO {

    private ErpUserInformation erpUserInformation;
    private String billToAccountId;
}
