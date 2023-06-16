package com.reece.platform.eclipse.dto.inventory;

import com.reece.platform.eclipse.util.StringUtil;
import lombok.Data;

@Data
public class CountInfoDTO {

    private String branchNum;
    private String countId;
    private Integer numItems;
    private String countDate;

    public CountInfoDTO(KourierCountInfoDTO kourierCountInfoDTO) {
        branchNum = kourierCountInfoDTO.getBrid();
        countId = StringUtil.formatCountId(kourierCountInfoDTO.getCycle());
        numItems = kourierCountInfoDTO.getNumItems();
        countDate = kourierCountInfoDTO.getReadyDateTimeStamp();
    }
}
