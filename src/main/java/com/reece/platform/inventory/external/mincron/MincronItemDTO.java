package com.reece.platform.inventory.external.mincron;

import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.CountLocationItemStatus;
import lombok.Data;
import lombok.val;

@Data
public class MincronItemDTO {

    private String itemNum;
    private String prodNum;
    private String prodDesc;
    private String catalogNum;
    private String tagNum;
    private String uom;

    public CountItem toEntity() {
        val countItem = new CountItem();
        countItem.setItemNum(getItemNum());
        countItem.setProductNum(getProdNum());
        countItem.setCatalogNum(getCatalogNum());
        countItem.setTagNum(getTagNum());
        countItem.setProductDesc(getProdDesc());
        countItem.setUom(getUom());
        countItem.setStatus(CountLocationItemStatus.UNCOUNTED);
        return countItem;
    }
}
