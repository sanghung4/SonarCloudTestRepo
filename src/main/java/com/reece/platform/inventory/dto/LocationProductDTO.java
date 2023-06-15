package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.external.eclipse.EclipseLocationItemDTO;
import com.reece.platform.inventory.external.eclipse.EclipseProductDTO;
import com.reece.platform.inventory.external.mincron.MincronItemDTO;
import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.CountLocationItemStatus;
import lombok.Value;
import lombok.val;

@Value
public class LocationProductDTO {

    String id;
    String prodNum;
    String prodDesc;
    String catalogNum;
    String tagNum;
    String uom;
    String productImageUrl;
    Integer quantity;
    CountLocationItemStatus status;
    Integer sequence;
    String controlNum;

    public static LocationProductDTO fromEntity(CountItem countItem) {
        return new LocationProductDTO(
            countItem.getItemNum(),
            countItem.getProductNum(),
            countItem.getProductDesc(),
            countItem.getCatalogNum(),
            countItem.getTagNum(),
            countItem.getUom(),
            countItem.getImageUrl(),
            countItem.getMostRecentQuantity(),
            countItem.getStatus(),
            countItem.getSequence(),
            countItem.getControlNum()
        );
    }

    public static LocationProductDTO fromMincronItemDTO(MincronItemDTO mincronItemDTO) {
        return new LocationProductDTO(
            mincronItemDTO.getItemNum(),
            mincronItemDTO.getProdNum(),
            mincronItemDTO.getProdDesc(),
            mincronItemDTO.getCatalogNum(),
            mincronItemDTO.getTagNum(),
            mincronItemDTO.getUom(),
            null,
            null,
            CountLocationItemStatus.UNCOUNTED,
            null,
            null
        );
    }

    public static LocationProductDTO fromEclipseProductDTO(EclipseProductDTO eclipseProductDTO) {
        return new LocationProductDTO(
            eclipseProductDTO.getProductId(),
            eclipseProductDTO.getProductId(),
            eclipseProductDTO.getProductDescription(),
            eclipseProductDTO.getCatalogNumber(),
            eclipseProductDTO.getProductId(),
            eclipseProductDTO.getUom(),
            eclipseProductDTO.getImageUrl(),
            null,
            CountLocationItemStatus.UNCOUNTED,
            null,
            eclipseProductDTO.getControlNum()
        );
    }

    public static LocationProductDTO fromEclipseLocationItemDTO(EclipseLocationItemDTO eclipseLocationItemDTO) {
        return new LocationProductDTO(
            eclipseLocationItemDTO.getProductId(),
            eclipseLocationItemDTO.getProductId(),
            eclipseLocationItemDTO.getDescription(),
            eclipseLocationItemDTO.getCatalogNumber(),
            eclipseLocationItemDTO.getProductId(),
            eclipseLocationItemDTO.getUnitOfMeasureName(),
            eclipseLocationItemDTO.getImageUrl(),
            null,
            CountLocationItemStatus.UNCOUNTED,
            null,
            eclipseLocationItemDTO.getControlNum()
        );
    }

    public CountItem toEntity() {
        val entity = new CountItem();

        entity.setItemNum(getId());
        entity.setProductNum(getProdNum());
        // Column size is only 255 characters in length
        entity.setProductDesc(getProdDesc().substring(0, Math.min(getProdDesc().length(), 255)));
        entity.setCatalogNum(getCatalogNum());
        entity.setTagNum(getTagNum());
        entity.setUom(getUom());
        entity.setImageUrl(getProductImageUrl());
        entity.setMostRecentQuantity(getQuantity());
        entity.setStatus(getStatus());
        entity.setSequence(getSequence());
        entity.setControlNum(getControlNum());

        return entity;
    }
}
