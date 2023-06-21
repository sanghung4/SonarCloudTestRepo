package com.reece.platform.products.model.DTO;

import static com.reece.platform.products.constants.ElasticsearchFieldNames.*;

import com.reece.platform.products.model.ErpUserInformation;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequestDTO {

    private String searchTerm;
    private Integer currentPage;
    private Integer pageSize;
    private String erpSystem;
    private List<ProductSearchFilterDTO> selectedAttributes;
    private List<ProductSearchFilterDTO> selectedCategories;
    // Expect 0 - 3
    private Integer categoryLevel;
    private UUID shipToId;
    private String selectedBranchId;
    private List<String> resultFields;
    private String state;
    private String engine;

    public ProductSearchRequestDTO(ProductSearchRequestDTO originalRequest, String filterToRemove) {
        this(originalRequest, filterToRemove, false);
    }

    /**
     * This is a special constructor. It constructs a new search request DTO with all requested filters except the one
     * specified in filterToRemove. The name specified in filterToRemove should correspond to the filter name
     * used in the elasticsearch service. It will be mapped to the proper DTO attribute type to remove.
     *
     * @param originalRequest original search request with all fitlers
     * @param filterToRemove elasicsearch service name for the attribute to remove
     */
    public ProductSearchRequestDTO(
        ProductSearchRequestDTO originalRequest,
        String filterToRemove,
        boolean preserveFilterName
    ) {
        this.searchTerm = originalRequest.searchTerm;
        this.currentPage = originalRequest.currentPage;
        this.pageSize = 0;
        this.erpSystem = originalRequest.erpSystem;
        this.categoryLevel = originalRequest.categoryLevel;
        this.selectedCategories = originalRequest.selectedCategories;
        this.selectedBranchId = originalRequest.selectedBranchId;

        if (originalRequest.engine != null && !originalRequest.engine.isEmpty()) {
            this.engine = originalRequest.engine;
        }

        if (originalRequest.selectedAttributes != null && !originalRequest.selectedAttributes.isEmpty()) {
            this.selectedAttributes =
                originalRequest.selectedAttributes
                    .stream()
                    .filter(attribute ->
                        !attribute
                            .getAttributeType()
                            .equals(preserveFilterName ? filterToRemove : esAttributeToDtoAttribute(filterToRemove))
                    )
                    .collect(Collectors.toList());
        } else {
            this.selectedAttributes = new ArrayList<>();
        }
    }

    public static String esAttributeToDtoAttribute(String esAttributeName) {
        switch (esAttributeName) {
            case MFR_FULL_NAME:
                return BRAND;
            case PRODUCT_LINE:
                return "line";
            case CATEGORY_1_NAME:
                return "category1";
            case CATEGORY_2_NAME:
                return "category2";
            case CATEGORY_3_NAME:
                return "category3";
            default:
                return esAttributeName;
        }
    }
}
