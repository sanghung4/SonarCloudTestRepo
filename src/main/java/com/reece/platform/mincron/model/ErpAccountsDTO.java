package com.reece.platform.mincron.model;

import lombok.Data;

import java.util.List;

@Data
public class ErpAccountsDTO {
    public ErpAccountsDTO() {}

    private ErpAccountInfoDTO billTo;
    private List<ErpAccountInfoDTO> shipTo;
}
