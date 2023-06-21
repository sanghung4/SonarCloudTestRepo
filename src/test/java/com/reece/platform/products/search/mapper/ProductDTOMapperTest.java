package com.reece.platform.products.search.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.external.appsearch.model.SearchResponse;
import com.reece.platform.products.model.DTO.ProductDTO;
import com.reece.platform.products.model.TechSpec;
import com.reece.platform.products.search.model.EnvironmentalOption;
import com.reece.platform.products.search.model.SearchBoostProperty;
import com.reece.platform.products.search.model.TechDocType;
import com.reece.platform.products.search.model.TechnicalSpecificationProperty;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.val;
import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductDTOMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SearchResponse.Result testSearchResult = TestUtils.loadResponseJson(
        "search-result-product.json",
        SearchResponse.Result.class
    );

    private ProductDTOMapper productDTOMapper;

    @BeforeEach
    public void setup() {
        productDTOMapper = new ProductDTOMapper(objectMapper);
    }

    @Test
    public void testMapErpProductIdToPartNumber() {
        val testPartNumber = testSearchResult
            .getStringValue("erp_product_id")
            .map(erpProductId -> erpProductId.replace("MSC-", ""))
            .orElse(null);

        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        assertEquals(testPartNumber, productDTO.getPartNumber());
    }

    @Test
    public void testMapCategories() {
        val testCategory1 = testSearchResult.getStringValue("category_1_name").orElse(null);

        val testCategory2 = testSearchResult.getStringValue("category_2_name").orElse(null);

        val testCategory3 = testSearchResult.getStringValue("category_3_name").orElse(null);

        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);
        val categories = productDTO.getCategories();

        assertEquals(3, categories.size());
        assertEquals(testCategory1, categories.get(0));
        assertEquals(testCategory2, categories.get(1));
        assertEquals(testCategory3, categories.get(2));
    }

    @Test
    public void testMapMfrName() {
        val testMfrName = testSearchResult.getStringValue("mfr_full_name").orElse(null);

        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        assertEquals(testMfrName, productDTO.getManufacturerName());
    }

    @Test
    public void testMapTechDocs() {
        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        for (val techDoc : TechDocType.values()) {
            testSearchResult
                .getStringValue(techDoc.getFileNameKey())
                .ifPresent(testFileName -> {
                    val td = productDTO
                        .getTechnicalDocuments()
                        .stream()
                        .map(com.reece.platform.products.model.TechDoc::getUrl)
                        .filter(url -> url.equals(testFileName))
                        .findFirst()
                        .orElse(null);

                    assertEquals(testFileName, td);
                });
        }
    }

    @Test
    public void testEnvironmentalOptions() {
        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        for (val option : EnvironmentalOption.values()) {
            val shouldHaveOption = testSearchResult
                .getStringValue(option.getFlagKey())
                .map(Boolean::parseBoolean)
                .orElse(false);
            assertEquals(shouldHaveOption, productDTO.getEnvironmentalOptions().contains(option.getDisplayName()));
        }
    }

    @Test
    public void testMapUPC() {
        val testUPCId = testSearchResult.getStringValue("upc_id").orElse(null);

        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        assertEquals(testUPCId, productDTO.getUpc());
    }

    @Test
    public void testMapFeaturesAndBenefits() {
        testStringPropertyMapping("feature_benefit_list_text", "featuresAndBenefits");
    }

    @Test
    public void testMapImageUrls() {
        val testFullImageUrl = testSearchResult.getStringValue("full_image_url_name").orElse(null);
        val testThumbnailImageUrl = testSearchResult.getStringValue("thumbnail_image_url_name").orElse(null);
        val testMediumImageUrl = testSearchResult.getStringValue("medium_image_url_name").orElse(null);

        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);
        val imageUrls = productDTO.getImageUrls();

        assertEquals(testFullImageUrl, imageUrls.getLarge());
        assertEquals(testThumbnailImageUrl, imageUrls.getSmall());
        assertEquals(testThumbnailImageUrl, imageUrls.getThumb());
        assertEquals(testMediumImageUrl, imageUrls.getMedium());
    }

    // TODO: need test data that includes packageDimensions

    //    @Test
    //    public void testMapPkgDimensions() {
    //        val testHeight = testSearchResult.getStringValue("package_height_nbr").map(Double::parseDouble).orElse(0.0);
    //        val testLength = testSearchResult.getStringValue("package_length_nbr").map(Double::parseDouble).orElse(0.0);
    //        val testVolume = testSearchResult.getStringValue("package_volume_nbr").map(Double::parseDouble).orElse(0.0);
    //        val testVolumeUom = testSearchResult.getStringValue("package_volume_uom").orElse(null);
    //        val testWeight = testSearchResult.getStringValue("package_weight_nbr").map(Double::parseDouble).orElse(0.0);
    //        val testWeightUom = testSearchResult.getStringValue("package_weight_uom").orElse(null);
    //        val testWidth = testSearchResult.getStringValue("package_width_nbr").map(Double::parseDouble).orElse(0.0);
    //
    //        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);
    //        val pkgDimensions = productDTO.getPackageDimensions();
    //
    //        assertEquals(testHeight, pkgDimensions.getHeight());
    //        assertEquals(testLength, pkgDimensions.getLength());
    //        assertEquals(testVolume, pkgDimensions.getVolume());
    //        assertEquals(testVolumeUom, pkgDimensions.getVolumeUnitOfMeasure());
    //        assertEquals(testWeight, pkgDimensions.getWeight());
    //        assertEquals(testWeightUom, pkgDimensions.getWeightUnitOfMeasure());
    //        assertEquals(testWidth, pkgDimensions.getWidth());
    //    }

    @Test
    public void testMapMinimumIncrementQty() {
        val sourceResult = testSearchResult.getStringValue("minimum_increment_qty").map(Integer::parseInt).orElse(null);
        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        assertEquals(sourceResult, productDTO.getMinIncrementQty());
    }

    @Test
    public void testMapLastUpdateDate() {
        testStringPropertyMapping("last_update_date", "lastUpdateDate");
    }

    @Test
    public void testMapId() {
        testStringPropertyMapping("erp_product_id", "id");
    }

    @Test
    public void testMapProductSoldCount() {
        testStringPropertyMapping("product_sold_count", "productSoldCount");
    }

    @Test
    public void testMapUnspsc() {
        testStringPropertyMapping("unspc_id", "unspsc");
    }

    @Test
    public void testMapManufacturerNumber() {
        testStringPropertyMapping("vendor_part_nbr", "manufacturerNumber");
    }

    @Test
    public void testMapName() {
        testStringPropertyMapping("web_description", "name");
    }

    @Test
    public void testMapCustomerNumber() {
        testListPropertyMapping("customer_number", "customerNumber");
    }

    @Test
    public void testMapCustomerPartNumber() {
        testListPropertyMapping("customer_part_number", "customerPartNumber");
    }

    @Test
    public void testMapInStockLocation() {
        testListPropertyMapping("in_stock_location", "inStockLocation");
    }

    @Test
    public void testMapProductBranchExclusion() {
        testListPropertyMapping("in_stock_location", "inStockLocation");
    }

    @Test
    public void testMapSearchKeywordText() {
        testStringPropertyMapping("search_keyword_text", "searchKeywordText");
    }

    @Test
    public void testMapCmp(){testStringPropertyMapping("cmp", "cmp");}

    @Test
    public void testMapProductSearchBoost() {
        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);
        val productSearchBoost = productDTO.getProductSearchBoost();

        for (val searchBoostProperty : SearchBoostProperty.values()) {
            val value = testSearchResult.getStringValue(searchBoostProperty.toString()).orElse(null);
            assertEquals(value, productSearchBoost.get(searchBoostProperty.toString()));
        }
    }

    @Test
    public void testMapTechSpecs() throws JsonProcessingException {
        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);
        val techSpecs = productDTO
            .getTechSpecifications()
            .stream()
            .collect(Collectors.toMap(TechSpec::getName, Function.identity()));

        // top-level properties that are mapped to TechSpecs
        for (val property : TechnicalSpecificationProperty.values()) {
            testSearchResult
                .getStringValue(property.toString())
                .ifPresent(expected -> {
                    val actual = techSpecs.get(property.toString());
                    assertEquals(expected, actual.getValue());
                });
        }

        // properties in the "technical_specifications" JSON blob are all mapped to TechSpecs
        val techSpecJson = testSearchResult
            .getListValue("technical_specifications")
            .map(l -> {
                if (l.size() > 0) {
                    return l.get(0);
                }

                return "{}";
            })
            .orElse("{}");

        final Map<?, ?> parsedJson = objectMapper.readValue(techSpecJson, Map.class);
        for (val entry : parsedJson.entrySet()) {
            val name = WordUtils.capitalize(((String) entry.getKey()).replace("_", " "), ' ', '/');
            val expected = (String) entry.getValue();

            assertEquals(expected, techSpecs.get(name).getValue());
        }
    }

    @Test
    public void testMapErp() {
        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);
        assertEquals("ECLIPSE", productDTO.getErp());
    }

    private void testStringPropertyMapping(String sourceName, String targetName) {
        val sourceResult = testSearchResult.getStringValue(sourceName).orElse(null);

        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        val targetResult = ReflectionTestUtils.getField(productDTO, ProductDTO.class, targetName);

        assertEquals(sourceResult, targetResult);
    }

    private void testListPropertyMapping(String sourceName, String targetName) {
        val sourceResult = testSearchResult.getListValue(sourceName).orElse(null);

        val productDTO = productDTOMapper.searchResultToProductDTO(testSearchResult);

        val targetResult = ReflectionTestUtils.getField(productDTO, ProductDTO.class, targetName);

        assertEquals(sourceResult, targetResult);
    }
}
