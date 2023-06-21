package com.reece.platform.products.pdw.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductSearchDocument {

    private String btu;
    private String capacity;

    @JsonProperty("category_1_name")
    private String category1Name;

    @JsonProperty("category_2_name")
    private String category2Name;

    @JsonProperty("category_3_name")
    private String category3Name;

    private Number cmp;

    @JsonProperty(value = "color/finish", access = JsonProperty.Access.WRITE_ONLY)
    private String colorSlashFinish;

    private String depth;

    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private Boolean energyStarFlag;

    private String erpProductId;
    private String featureBenefitListText;
    private String flowRate;
    private String fullImageUrlName;

    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private Boolean hazardousMaterialFlag;

    private String height;
    private String id;
    private String inletSize;
    private String internalItemNbr;
    private String lastUpdateDate;
    private String length;

    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private Boolean lowLeadCompliantFlag;

    private String material;
    private String mediumImageUrlName;

    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private Boolean mercuryFreeFlag;

    private String mfrCatalogDocFileName;
    private String mfrFullName;
    private String mfrInstallInstructionDocFileName;
    private String mfrItemDataDocFileName;
    private String mfrMsdsDocFileName;
    private String mfrSpecTechDocFileName;
    private String minimumIncrementQty;
    private Number packageHeightNbr;
    private Number packageLengthNbr;
    private Number packageVolumeNbr;
    private String packageVolumeUOMCode;
    private Number packageWeightNbr;
    private String packageWeightUOMCode;
    private Number packageWidthNbr;
    private String pressureRating;
    private String productLine;
    private String productOverviewDescription;

    @JsonProperty("product_search_boost_1_nbr")
    private String productSearchBoost1Nbr;

    @JsonProperty("product_search_boost_2_nbr")
    private String productSearchBoost2Nbr;

    @JsonProperty("product_search_boost_3_nbr")
    private String productSearchBoost3Nbr;

    @JsonProperty("product_search_boost_4_nbr")
    private String productSearchBoost4Nbr;

    private Number productSoldCount;
    private String searchKeywordText;
    private String size;
    private String temperatureRating;
    private String thumbnailImageUrlName;
    private String tonnage;
    private String unspcId;
    private String upcId;
    private String vendorPartNbr;
    private String cleanVendorPartNbr;
    private String voltage;

    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    private Boolean waterSenseCompliantFlag;

    private String wattage;
    private String webDescription;
    private String cleanWebDescription;
    private String cleanProductBrand;
    private String width;
    private List<Map<String, String>> technicalSpecifications;
    private List<String> customerNumber;
    private List<String> customerPartNumber;
    private List<Map<String, List<String>>> customerPartNumbers;
    private List<String> inStockLocation;
    private List<String> productBranchExclusion;
    private List<String> territoryExclusionList;

    @JsonProperty
    public String getColorfinish() {
        return colorSlashFinish;
    }
}
