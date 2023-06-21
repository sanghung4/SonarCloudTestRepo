package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.enums.DivisionEnum;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.utilities.AccountDataFormatting;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ShipToAccountDTO {
    private UUID id;
    private String name;
    private UUID parentAccountId;
    private String erpAccountId;
    private ErpEnum erpSystemName;
    private DivisionEnum divisionEnum;
    private String branchId;
    private String branchAddress;

    public ShipToAccountDTO(Account account, ErpEnum erpEnum) {
        this.id = account.getId();
        this.name = AccountDataFormatting.formatAccountName(
                account.getErpAccountId(),
                account.getName(),
                account.getAddress(),
                erpEnum
        );
        if (account.getParentAccountId() != null) {
            this.parentAccountId = account.getParentAccountId();
        } else if (account.getParentAccount() != null && account.getParentAccount().getId() != null) {
            this.parentAccountId = account.getParentAccount().getId();
        }

        this.erpAccountId = account.getErpAccountId();
        this.erpSystemName = erpEnum;
        this.divisionEnum = erpEnum == ErpEnum.MINCRON ? DivisionEnum.WATERWORKS : DivisionEnum.PHVAC;
        this.branchId = account.getBranchId();
    }
}
