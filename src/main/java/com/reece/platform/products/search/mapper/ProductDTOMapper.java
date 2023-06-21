package com.reece.platform.products.search.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.external.appsearch.model.SearchResponse;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.ProductDTO;
import com.reece.platform.products.search.model.EnvironmentalOption;
import com.reece.platform.products.search.model.SearchBoostProperty;
import com.reece.platform.products.search.model.TechDocType;
import com.reece.platform.products.search.model.TechnicalSpecificationProperty;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDTOMapper {

    private static final String ERP_PRODUCT_ID = "erp_product_id";
    private static final String ECLIPSE_PRODUCT_ID_PREFIX = "MSC-";
    private static final String PRODUCT_OVERVIEW_DESCRIPTION = "product_overview_description";
    private static final String CATEGORY_1_NAME = "category_1_name";
    private static final String CATEGORY_2_NAME = "category_2_name";
    private static final String CATEGORY_3_NAME = "category_3_name";
    private static final String MFR_FULL_NAME = "mfr_full_name";
    private static final String UPC_ID = "upc_id";
    private static final String FEATURE_BENEFIT_LIST_TEXT = "feature_benefit_list_text";
    private static final String FULL_IMAGE_URL_NAME = "full_image_url_name";
    private static final String THUMBNAIL_IMAGE_URL_NAME = "thumbnail_image_url_name";
    private static final String MEDIUM_IMAGE_URL_NAME = "medium_image_url_name";
    private static final String PACKAGE_HEIGHT_NBR = "package_height_nbr";
    private static final String PACKAGE_LENGTH_NBR = "package_length_nbr";
    private static final String PACKAGE_VOLUME_NBR = "package_volume_nbr";
    private static final String PACKAGE_VOLUME_UOM_CODE = "package_volume_uom_code";
    private static final String PACKAGE_WEIGHT_NBR = "package_weight_nbr";
    private static final String PACKAGE_WEIGHT_UOM_CODE = "package_weight_uom_code";
    private static final String PACKAGE_WIDTH_NBR = "package_width_nbr";
    private static final String MINIMUM_INCREMENT_QTY = "minimum_increment_qty";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private static final String PRODUCT_SOLD_COUNT = "product_sold_count";
    private static final String UNSPC_ID = "unspc_id";
    private static final String VENDOR_PART_NBR = "vendor_part_nbr";
    private static final String WEB_DESCRIPTION = "web_description";
    private static final String CUSTOMER_NUMBER = "customer_number";
    private static final String CUSTOMER_PART_NUMBER = "customer_part_number";
    private static final String CUSTOMER_PART_NUMBERS = "customer_part_numbers";
    private static final String IN_STOCK_LOCATION = "in_stock_location";
    private static final String PRODUCT_BRANCH_EXCLUSION = "product_branch_exclusion";
    private static final String SEARCH_KEYWORD_TEXT = "search_keyword_text";
    private static final String TECHNICAL_SPECIFICATIONS = "technical_specifications";
    private static final String ECLIPSE = "ECLIPSE";
    private static final String TERRITORY_EXCLUSION_LIST = "territory_exclusion_list";

    private static final String CMP = "cmp";
    private static final String NUMERICAL_AND_PERIOD_REGEX = "[^\\d.]";

    private final ObjectMapper objectMapper;

    public ProductDTO searchResultToProductDTO(SearchResponse.Result result) {
        val productDTO = new ProductDTO();

        result
            .getStringValue(ERP_PRODUCT_ID)
            .filter(productId -> productId.startsWith(ECLIPSE_PRODUCT_ID_PREFIX))
            .map(productId -> productId.replace(ECLIPSE_PRODUCT_ID_PREFIX, ""))
            .ifPresent(productDTO::setPartNumber);

        result.getStringValue(PRODUCT_OVERVIEW_DESCRIPTION).ifPresent(productDTO::setProductOverview);

        val categories = new ArrayList<String>();
        result.getStringValue(CATEGORY_1_NAME).ifPresent(categories::add);
        result.getStringValue(CATEGORY_2_NAME).ifPresent(categories::add);
        result.getStringValue(CATEGORY_3_NAME).ifPresent(categories::add);
        productDTO.setCategories(categories);

        result.getStringValue(MFR_FULL_NAME).ifPresent(productDTO::setManufacturerName);

        val techDocs = new ArrayList<TechDoc>();
        for (val techDoc : TechDocType.values()) {
            result
                .getStringValue(techDoc.getFileNameKey())
                .map(fileName -> new com.reece.platform.products.model.TechDoc(techDoc.getDisplayName(), fileName))
                .ifPresent(techDocs::add);
        }

        productDTO.setTechnicalDocuments(techDocs);

        val environmentalOptions = new ArrayList<String>();
        for (val option : EnvironmentalOption.values()) {
            result
                .getStringValue(option.getFlagKey())
                .ifPresent(value -> {
                    if (Boolean.parseBoolean(value)) {
                        environmentalOptions.add(option.getDisplayName());
                    }
                });
        }

        productDTO.setEnvironmentalOptions(environmentalOptions);
        result.getStringValue(UPC_ID).ifPresent(productDTO::setUpc);
        result.getStringValue(FEATURE_BENEFIT_LIST_TEXT).ifPresent(productDTO::setFeaturesAndBenefits);
        result.getStringValue(CMP).ifPresent(productDTO::setCmp);

        val imageUrls = new ImageUrls();
        result.getStringValue(FULL_IMAGE_URL_NAME).ifPresent(imageUrls::setLarge);
        result
            .getStringValue(THUMBNAIL_IMAGE_URL_NAME)
            .ifPresent(imageUrl -> {
                imageUrls.setSmall(imageUrl);
                imageUrls.setThumb(imageUrl);
            });
        result.getStringValue(MEDIUM_IMAGE_URL_NAME).ifPresent(imageUrls::setMedium);
        productDTO.setImageUrls(imageUrls);

        val packageDimensions = new PackageDimensions();
        result
            .getStringValue(PACKAGE_HEIGHT_NBR)
            .map(value -> value.replaceAll(NUMERICAL_AND_PERIOD_REGEX, ""))
            .map(Double::parseDouble)
            .ifPresent(packageDimensions::setHeight);
        result
            .getStringValue(PACKAGE_LENGTH_NBR)
            .map(value -> value.replaceAll(NUMERICAL_AND_PERIOD_REGEX, ""))
            .map(Double::parseDouble)
            .ifPresent(packageDimensions::setLength);
        result
            .getStringValue(PACKAGE_VOLUME_NBR)
            .map(value -> value.replaceAll(NUMERICAL_AND_PERIOD_REGEX, ""))
            .map(Double::parseDouble)
            .ifPresent(packageDimensions::setVolume);
        result.getStringValue(PACKAGE_VOLUME_UOM_CODE).ifPresent(packageDimensions::setVolumeUnitOfMeasure);
        result
            .getStringValue(PACKAGE_WEIGHT_NBR)
            .map(value -> value.replaceAll(NUMERICAL_AND_PERIOD_REGEX, ""))
            .map(Double::parseDouble)
            .ifPresent(packageDimensions::setWeight);
        result.getStringValue(PACKAGE_WEIGHT_UOM_CODE).ifPresent(packageDimensions::setWeightUnitOfMeasure);
        result
            .getStringValue(PACKAGE_WIDTH_NBR)
            .map(value -> value.replaceAll(NUMERICAL_AND_PERIOD_REGEX, ""))
            .map(Double::parseDouble)
            .ifPresent(packageDimensions::setWidth);

        productDTO.setPackageDimensions(packageDimensions);

        result.getStringValue(MINIMUM_INCREMENT_QTY).map(Integer::parseInt).ifPresent(productDTO::setMinIncrementQty);

        result.getStringValue(LAST_UPDATE_DATE).ifPresent(productDTO::setLastUpdateDate);
        result.getStringValue(ERP_PRODUCT_ID).ifPresent(productDTO::setId);
        result.getStringValue(PRODUCT_SOLD_COUNT).ifPresent(productDTO::setProductSoldCount);
        result.getStringValue(UNSPC_ID).ifPresent(productDTO::setUnspsc);
        result.getStringValue(VENDOR_PART_NBR).ifPresent(productDTO::setManufacturerNumber);
        result.getStringValue(WEB_DESCRIPTION).ifPresent(productDTO::setName);
        result.getListValue(CUSTOMER_NUMBER).ifPresent(productDTO::setCustomerNumber);
        result
            .getListValue(CUSTOMER_PART_NUMBER)
            .ifPresentOrElse(
                productDTO::setCustomerPartNumber,
                () -> {
                    result
                        .getStringValue(CUSTOMER_PART_NUMBER)
                        .ifPresent(customerPartNumber -> {
                            productDTO.setCustomerPartNumber(List.of(customerPartNumber));
                        });
                }
            );

        result.getListValue(IN_STOCK_LOCATION).ifPresent(productDTO::setInStockLocation);
        result.getListValue(TERRITORY_EXCLUSION_LIST).ifPresent(productDTO::setTerritoryExclusionList);
        result.getListValue(PRODUCT_BRANCH_EXCLUSION).ifPresent(productDTO::setProductBranchExclusion);
        result.getStringValue(SEARCH_KEYWORD_TEXT).ifPresent(productDTO::setSearchKeywordText);

        val productSearchBoost = new HashMap<String, String>();
        for (val searchBoostProperty : SearchBoostProperty.values()) {
            result
                .getStringValue(searchBoostProperty.toString())
                .ifPresent(value -> productSearchBoost.put(searchBoostProperty.toString(), value));
        }
        productDTO.setProductSearchBoost(productSearchBoost);

        val techSpecs = new ArrayList<TechSpec>();

        for (val techSpecProperty : TechnicalSpecificationProperty.values()) {
            result
                .getStringValue(techSpecProperty.toString())
                .ifPresent(value -> {
                    val techSpec = new TechSpec();
                    techSpec.setName(techSpecProperty.toString());
                    techSpec.setValue(value);
                    techSpecs.add(techSpec);
                });
        }

        result
            .getListValue(TECHNICAL_SPECIFICATIONS)
            .ifPresent(technicalSpecifications -> {
                try {
                    val techSpecJson = technicalSpecifications.stream().findFirst().orElse("{}");
                    final Map<?, ?> parsedJson = objectMapper.readValue(techSpecJson, Map.class);
                    for (val entry : parsedJson.entrySet()) {
                        val propertyName = (String) entry.getKey();
                        val formattedName = WordUtils.capitalize(propertyName.replace("_", " "), ' ', '/');
                        val techSpec = new TechSpec();
                        techSpec.setName(formattedName);
                        techSpec.setValue((String) entry.getValue());
                        techSpecs.add(techSpec);
                    }
                } catch (JsonProcessingException e) {
                    // TODO: create custom exception type to wrap
                    throw new RuntimeException(e);
                }
            });

        if (!techSpecs.isEmpty()) {
            productDTO.setTechSpecifications(techSpecs);
        }

        val customerPartNumbersList = new ArrayList<CustomerPartNumber>();

        result
            .getListValue(CUSTOMER_PART_NUMBERS)
            .ifPresent(customerPartNumbers -> {
                try {
                    var customerPartNumbersRaw = customerPartNumbers.toString();
                    final List<Map<String, List<String>>> parsedList = objectMapper.readValue(customerPartNumbersRaw, List.class);
                    parsedList.forEach(customerPartMap -> {
                        List<CustomerPartNumber> parsedCustomerPartNumbers = customerPartMap.entrySet()
                                .stream()
                                .map(entry -> {
                                    CustomerPartNumber customerPartNumber = new CustomerPartNumber();
                                    customerPartNumber.setCustomer(entry.getKey());
                                    customerPartNumber.setPartNumbers(entry.getValue());
                                    return customerPartNumber;
                                }).collect(Collectors.toList());
                        customerPartNumbersList.addAll(parsedCustomerPartNumbers);
                    });
                } catch (JsonProcessingException e) {}
            });

        if (!customerPartNumbersList.isEmpty()) {
            productDTO.setCustomerPartNumbers(customerPartNumbersList);
        }

        // TODO: remove/update this line when Mincron products are used
        productDTO.setErp(ECLIPSE);

        return productDTO;
    }
}
