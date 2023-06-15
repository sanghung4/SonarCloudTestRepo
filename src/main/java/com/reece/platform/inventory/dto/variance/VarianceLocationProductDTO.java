package com.reece.platform.inventory.dto.variance;

import com.reece.platform.inventory.dto.LocationProductDTO;
import com.reece.platform.inventory.external.eclipse.EclipseLocationItemDTO;
import com.reece.platform.inventory.external.mincron.MincronItemDTO;
import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.CountLocationItemStatus;
import com.reece.platform.inventory.model.VarianceCountItemStatus;
import lombok.Value;
import lombok.val;

@Value
public class VarianceLocationProductDTO {

    String id;
    String prodNum;
    String prodDesc;
    String catalogNum;
    String tagNum;
    String uom;
    Integer quantity;
    CountLocationItemStatus status;
    Integer sequence;
    Double varianceCost;
    String productImageUrl;
    VarianceCountItemStatus varianceStatus;

    public static VarianceLocationProductDTO fromEntity(CountItem countItem) {
        return new VarianceLocationProductDTO(
            countItem.getItemNum(),
            countItem.getProductNum(),
            countItem.getProductDesc(),
            countItem.getCatalogNum(),
            countItem.getTagNum(),
            countItem.getUom(),
            countItem.getMostRecentQuantity(),
            countItem.getStatus(),
            countItem.getSequence(),
            countItem.getVarianceCost(),
            countItem.getImageUrl(),
            countItem.getVarianceStatus()
        );
    }

    public CountItem toEntity() {
        val entity = new CountItem();

        entity.setItemNum(getId());
        entity.setProductNum(getProdNum());
        entity.setProductDesc(getProdDesc());
        entity.setCatalogNum(getCatalogNum());
        entity.setTagNum(getTagNum());
        entity.setUom(getUom());
        entity.setMostRecentQuantity(getQuantity());
        entity.setStatus(getStatus());
        entity.setSequence(getSequence());
        entity.setImageUrl(getProductImageUrl());
        entity.setVarianceCost(getVarianceCost());
        entity.setVarianceStatus(getVarianceStatus());
        return entity;
    }
}
