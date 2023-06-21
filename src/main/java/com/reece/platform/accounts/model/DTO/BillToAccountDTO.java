package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.enums.*;
import com.reece.platform.accounts.utilities.AccountDataFormatting;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BillToAccountDTO {
    private UUID id;
    private String name;
    private String erpAccountId;
    private ErpEnum erpSystemName;
    private List<ShipToAccountDTO> shipTos;
    private DivisionEnum divisionEnum;
    private String branchId;
    private String branchAddress;

    public BillToAccountDTO() {};

    public BillToAccountDTO(Account billToAccount, List<ShipToAccountDTO> shipToAccounts, ErpEnum erpSystemName) {
        this.id = billToAccount.getId();

        this.name = AccountDataFormatting.formatAccountName(
            billToAccount.getErpAccountId(),
            billToAccount.getName(),
            billToAccount.getAddress(),
            erpSystemName
        );

        this.erpAccountId = billToAccount.getErpAccountId();
        this.shipTos = shipToAccounts;
        this.erpSystemName = erpSystemName;
        this.divisionEnum = erpSystemName == ErpEnum.MINCRON ? DivisionEnum.WATERWORKS : DivisionEnum.PHVAC;
        this.branchId = billToAccount.getBranchId();
    }
}
