package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;

import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class GreenwingProductCatalogFileMapper {
    public static final String[] HEADERS = {
            "Product Name",
            "Product Description",
            "Image Fullsize",
            "Image Thumb",
            "Product Price",
            "List Price",
            "Part Number",
            "Unit of Measure",
            "Manufacturer",
            "Manufacturer Part Number",
            "Category Level 1 Name",
            "Category Level 2 Name",
            "Category Level 3 Name",
            "UNSPSC",
            "Delivery In Days",
            "Buyer ID",
    };

    public static List<String[]> toCSVData(Collection<CatalogProductDao> catalogProducts, CustomerDao customer) {
        ArrayList<String[]> csvData = new ArrayList<>();
        csvData.add(HEADERS);
        if (catalogProducts == null || !Persistence.getPersistenceUtil().isLoaded(catalogProducts)) {
            return csvData;
        }
        for (CatalogProductDao catalogProduct : catalogProducts) {
            csvData.add(GreenwingProductCatalogFileMapper.toCsvLine(catalogProduct, customer));
        }
        return csvData;
    }

    public static String[] toCsvLine(CatalogProductDao catalogProduct, CustomerDao customer) {
        String[] csvLine = {
                catalogProduct.getProduct().getName(),
                catalogProduct.getProduct().getDescription() != null ? catalogProduct.getProduct().getDescription() : catalogProduct.getProduct().getName(),
                catalogProduct.getProduct().getImageFullSize(),
                catalogProduct.getProduct().getImageThumb(),
                catalogProduct.getSellPrice().toString(),
                catalogProduct.getListPrice().toString(),
                catalogProduct.getPartNumber(),
                catalogProduct.getUom() != null ? catalogProduct.getUom() : "each",
                catalogProduct.getProduct().getManufacturer() != null ? catalogProduct.getProduct().getManufacturer() : "generic",
                catalogProduct.getProduct().getManufacturerPartNumber() != null ? catalogProduct.getProduct().getManufacturerPartNumber() : "generic",
                catalogProduct.getProduct().getCategoryLevel1Name() != null ? catalogProduct.getProduct().getCategoryLevel1Name() : "Other",
                catalogProduct.getProduct().getCategoryLevel2Name(),
                catalogProduct.getProduct().getCategoryLevel3Name(),
                catalogProduct.getProduct().getUnspsc() != null ? catalogProduct.getProduct().getUnspsc() : "30190000",
                catalogProduct.getProduct().getDeliveryInDays() != null ? catalogProduct.getProduct().getDeliveryInDays().toString() : "7",
                customer.getCustomerId()
        };
        return csvLine;
    }
}
