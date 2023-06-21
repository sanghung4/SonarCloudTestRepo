package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.ErpUserInformation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsInfoDTO {

    private List<ItemInfoDTO> itemInfoList;
    private ErpUserInformation erpUserInformation;
}
