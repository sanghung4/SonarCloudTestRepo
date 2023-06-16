package com.reece.platform.eclipse.dto.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class KourierFullCountDTO {

    private Integer erpcountID;
    private String countDesc;
    private String erpBranchNum;
    private String createDate;

    @JsonProperty("Products")
    private List<KourierCountProductDTO> products;
}
