package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.DTO.ErpAccountInfo;

import java.io.Serializable;
import java.util.List;

public class ErpAccounts implements Serializable {
    private ErpAccountInfo billTo;
    private List<ErpAccountInfo> shipTo;

    public ErpAccountInfo getBillTo() {
        return billTo;
    }

    public void setBillTo(ErpAccountInfo billTo) {
        this.billTo = billTo;
    }

    public List<ErpAccountInfo> getShipTo() {
        return shipTo;
    }

    public void setShipTo(List<ErpAccountInfo> shipTo) {
        this.shipTo = shipTo;
    }
}
