package com.reece.punchoutcustomersync.dto.max;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDocumentDto {

    private String id;

    private String cmp;

    @SerializedName("erp_product_id")
    private String erpProductId;

    @SerializedName("full_image_url_name")
    private String fullImageUrlName;

    @SerializedName("internal_item_nbr")
    private String internalItemNbr;

    @SerializedName("last_update_date")
    private String lastUpdateDate;

    @SerializedName("medium_image_url_name")
    private String mediumImageUrlName;

    @SerializedName("mfr_catalog_doc_file_name")
    private String mfrCatalogDocFileName;

    @SerializedName("mfr_full_name")
    private String mfrFullName;

    @SerializedName("product_overview_description")
    private String productOverviewDescription;

    @SerializedName("product_sold_count")
    private String productSoldCount;

    @SerializedName("search_keyword_text")
    private String searchKeywordText;

    @SerializedName("thumbnail_image_url_name")
    private String thumbnailImageUrlName;

    @SerializedName("unspc_id")
    private String unspcId;

    @SerializedName("upc_id")
    private String upcId;

    @SerializedName("vendor_part_nbr")
    private String vendorPartNbr;

    @SerializedName("clean_vendor_part_nbr")
    private String cleanVendorPartNbr;

    @SerializedName("web_description")
    private String webDescription;

    @SerializedName("clean_web_description")
    private String cleanWebDescription;

    @SerializedName("clean_product_brand")
    private String cleanProductBrand;

    @SerializedName("technical_specifications")
    private List<String> technicalSpecifications;

    @SerializedName("customer_number")
    private List<String> customerNumbers;

    @SerializedName("customer_part_numbers")
    private List<String> customerPartNumbers;

    @SerializedName("in_stock_location")
    private List<String> inStockLocations;

    @SerializedName("territory_exclusion_list")
    private List<String> territoryExclusionList;

    @SerializedName("category_1_name")
    private String category1Name;

    @SerializedName("category_2_name")
    private String category2Name;

    @SerializedName("category_3_name")
    private String category3Name;

    public String getPartNumber() {
        return id.replaceFirst("MSC-", "");
    }
}
