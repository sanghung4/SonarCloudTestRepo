package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.model.*;
import static com.reece.platform.inventory.util.Util.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.join;

@Data
@NoArgsConstructor
public class ProductDTO {

    private String id;
    private String partNumber;
    private String name;
    private List<String> taxonomy;
    private List<String> categories;
    private String manufacturerName;
    private String manufacturerNumber;
    private String productType;
    private float price;
    private Stock stock;
    private List<TechDoc> technicalDocuments;
    private List<String> environmentalOptions;
    private String upc;
    private String unspsc;
    private String seriesModelFigureNumber;
    private String productOverview;
    private String featuresAndBenefits;
    private List<TechSpec> techSpecifications;
    private ImageUrls imageUrls;
    private PackageDimensions packageDimensions;
    private String erp;
    private int minIncrementQty;
    private Map<String, String> productSearchBoost;
    private String lastUpdateDate;
    private String productSoldCount;
    private String searchKeywordText;
    private List<String> customerNumber;
    private List<String> customerPartNumber;
    private List<String> inStockLocation;
    private List<String> productBranchExclusion;
    private String status;

    public void setTechnicalDocuments(List<TechDoc> techDocs) {
        // Some technical documents could come back as just file names.
        // For example, see PDW ID 3933690.
        // In this case, the UI would try to append the file name to the current URL, resulting in an error.
        // Instead, see if other docs have full URLs associated. If so, use that base URL for this file name.

        if (techDocs == null || techDocs.size() == 0) {
            this.technicalDocuments = techDocs;
            return;
        }

        boolean containsInvalidDoc = techDocs.stream().filter(doc -> !isValidUrl(doc)).count() > 0;

        if (containsInvalidDoc) {
            TechDoc validDoc = techDocs.stream().filter(doc -> isValidUrl(doc)).findFirst().orElse(null);

            if (validDoc == null) {
                this.technicalDocuments = techDocs;
                return;
            }

            List<String> urlTokens = Arrays.asList(validDoc.getUrl().split("/"));
            final String baseUrl = join("/", urlTokens.subList(0, urlTokens.size() - 1));

            this.technicalDocuments =
                    techDocs
                            .stream()
                            .map(doc -> {
                                if (!isValidUrl(doc)) {
                                    doc.setUrl(String.format("%s/%s", baseUrl, doc.getUrl()));
                                }
                                return doc;
                            })
                            .collect(Collectors.toList());
        } else {
            this.technicalDocuments = techDocs;
        }
    }

    public void setPackageDimensions(PackageDimensions packageDimensions) {
        if (packageDimensions != null && !packageDimensions.isEmpty()) {
            this.packageDimensions = packageDimensions;
        }
    }
}