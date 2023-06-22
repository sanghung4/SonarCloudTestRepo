package com.reece.punchoutcustomerbff.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.dto.LoginInputDto;
import com.reece.punchoutcustomerbff.dto.NewCatalogInputDto;
import com.reece.punchoutcustomerbff.dto.ProcurementSystemDto;
import com.reece.punchoutcustomerbff.dto.ProductDto;
import com.reece.punchoutcustomerbff.dto.RenameCatalogDto;
import com.reece.punchoutcustomerbff.dto.UploadInputDto;
import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import com.reece.punchoutcustomerbff.models.daos.ProcurementSystemDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class TestUtils {

    /**
     * Cors origin name for header.
     */
    public static final String H_CORS_NAME = "Origin";

    /**
     * Cors origin value for header.
     */
    public static final String H_CORS_VALUE = "*";

    /**
     * Reece authentication name for header.
     */
    public static final String H_AUTH_NAME = "X-Auth-Token";

    public static CustomerDao generateCustomerDao() {
        CustomerRegionDao region = CustomerRegionDao.builder().id(UUID.randomUUID()).name("hotel").build();
        CustomerDao input = CustomerDao
            .builder()
            .id(UUID.fromString("b32a915d-cd68-4d24-997c-3cff04fe0162"))
            .customerId("alpha")
            .erpId("bravo")
            .name("charlie")
            .branchId("delta")
            .branchName("echo")
            .contactName("foxtrot")
            .contactPhone("golf")
            .isBillTo(true)
            .lastUpdate(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
            .regions(Set.of(region))
            .build();
        region.setCustomer(input);
        return input;
    }

    public static CustomerDto generateCustomerDto() {
        UUID customerId = UUID.fromString("b32a915d-cd68-4d24-997c-3cff04fe0162");
        CustomerRegionDto region = CustomerRegionDto
            .builder()
            .id(UUID.randomUUID())
            .name("hotel")
            .customerId(customerId)
            .build();
        return CustomerDto
            .builder()
            .id(customerId)
            .customerId("alpha")
            .erpId("bravo")
            .name("charlie")
            .branchId("delta")
            .branchName("echo")
            .contactName("foxtrot")
            .contactPhone("golf")
            .isBillTo(true)
            .lastUpdate("2023-05-17T05:00:00.000+0000")
            .regions(List.of(region))
            .build();
    }

    public static void assertDefaultCustomerDto(CustomerDto output) {
        // and
        assertThat(output.getId(), equalTo(UUID.fromString("b32a915d-cd68-4d24-997c-3cff04fe0162")));
        assertThat(output.getCustomerId(), equalTo("alpha"));
        assertThat(output.getErpId(), equalTo("bravo"));
        assertThat(output.getName(), equalTo("charlie"));
        assertThat(output.getBranchId(), equalTo("delta"));
        assertThat(output.getBranchName(), equalTo("echo"));
        assertThat(output.getContactName(), equalTo("foxtrot"));
        assertThat(output.getContactPhone(), equalTo("golf"));
        assertThat(output.getIsBillTo(), equalTo(true));
        assertThat(output.getLastUpdate(), equalTo("2023-05-17T05:00:00.000+0000"));
        assertThat(output.getRegions().size(), equalTo(1));

        // and
        CustomerRegionDto regionOutput = output.getRegions().get(0);
        assertThat(regionOutput.getName(), equalTo("hotel"));
    }

    public static CatalogDao generateCatalog() {
        return CatalogDao
            .builder()
            .dateArchived(null)
            .fileName("customer-file-name-alpha")
            .status("DRAFT")
            .procSystem("Greenwing")
            .name("customer-name-alpha")
            .lastUpdate(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
            .id(UUID.fromString("d6925283-d174-4b7e-b9f2-3c7f5d60ca53"))
            .customer(CustomerDao.builder().build())
            .build();
    }

    public static void assertDefaultCatalogDto(CatalogDto input) {
        assertThat(input.getDateArchived(), equalTo(null));
        assertThat(input.getFileName(), equalTo("customer-file-name-alpha"));
        assertThat(input.getStatus(), equalTo("DRAFT"));
        assertThat(input.getProcSystem(), equalTo("Greenwing"));
        assertThat(input.getName(), equalTo("customer-name-alpha"));
        assertThat(input.getLastUpdate(), equalTo("2023-05-17T05:00:00.000+0000"));
        assertThat(input.getId().toString(), equalTo("d6925283-d174-4b7e-b9f2-3c7f5d60ca53"));
    }

    public static String getUserRequest(String email, String pass) throws JsonProcessingException {
        return getObject(LoginInputDto.builder().email(email).password(pass).build());
    }

    public static String getResponse(MvcResult rs) throws UnsupportedEncodingException, JSONException {
        return new JSONObject(rs.getResponse().getContentAsString()).toString();
    }

    public static ProductDao generateDefaultProduct() {
        return ProductDao
            .builder()
            .id(UUID.fromString("d019fcba-8adb-4c86-b896-2cc313ba86cd"))
            .categoryLevel1Name("Test Category 1")
            .categoryLevel2Name("Test Category 2")
            .categoryLevel3Name("Test Category 3")
            .deliveryInDays(7)
            .description("Test Product 04 Description")
            .imageFullSize("image-1")
            .name("Test Product 04")
            .partNumber("TEST-PART-04")
            .imageThumb("image-2")
            .manufacturer("Test Manufacturer")
            .manufacturerPartNumber("EST-MFN-04")
            .unspsc("TEST-unspsc")
            .maxSyncDatetime(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
            .build();
    }

    public static void assertDefaultProduct(ProductDto input) {
        assertThat(input.getId().toString(), equalTo("d019fcba-8adb-4c86-b896-2cc313ba86cd"));
        assertThat(input.getCategoryLevel1Name(), equalTo("Test Category 1"));
        assertThat(input.getCategoryLevel2Name(), equalTo("Test Category 2"));
        assertThat(input.getCategoryLevel3Name(), equalTo("Test Category 3"));
        assertThat(input.getDeliveryInDays(), equalTo(7));
        assertThat(input.getDescription(), equalTo("Test Product 04 Description"));
        assertThat(input.getImageFullSize(), equalTo("image-1"));
        assertThat(input.getName(), equalTo("Test Product 04"));
        assertThat(input.getPartNumber(), equalTo("TEST-PART-04"));
        assertThat(input.getImageThumb(), equalTo("image-2"));
        assertThat(input.getManufacturer(), equalTo("Test Manufacturer"));
        assertThat(input.getManufacturerPartNumber(), equalTo("EST-MFN-04"));
        assertThat(input.getUnspsc(), equalTo("TEST-unspsc"));
        assertThat(input.getMaxSyncDatetime(), equalTo("2023-05-17T05:00:00.000+0000"));
    }

    public static CatalogProductDao generateMapping() {
        return CatalogProductDao
            .builder()
            .uom("uom-alpha")
            .partNumber("part-number-alpha")
            .listPrice(new BigDecimal(0))
            .sellPrice(new BigDecimal(1))
            .lastPullDatetime(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
            .id(UUID.fromString("5e79df4c-56a8-47ed-93f5-125749c891f6"))
            .build();
    }

    public static CatalogProductDao generateMappingWithProduct() {
        CatalogProductDao output = generateMapping();
        output.setProduct(generateDefaultProduct());
        return output;
    }

    public static void assertDefaultMapping(CatalogProductDto input) {
        assertThat(input.getUom(), equalTo("uom-alpha"));
        assertThat(input.getPartNumber(), equalTo("part-number-alpha"));
        assertThat(input.getListPrice(), equalTo(new BigDecimal(0)));
        assertThat(input.getSellPrice(), equalTo(new BigDecimal((1))));
        assertThat(input.getLastPullDatetime(), equalTo("2023-05-17T05:00:00.000+0000"));
        assertThat(input.getId().toString(), equalTo("5e79df4c-56a8-47ed-93f5-125749c891f6"));
    }

    public static AuthorizedUserDao generateDefaultUser() {
        return AuthorizedUserDao
            .builder()
            .admin(true)
            .salt("$1$eUyPmeLf")
            .password("$1$eUyPmeLf$roagFNp6VhwGMMa39CCpK.")
            .email("alpha")
            .build();
    }

    public static String generateRenameCatalogRequest(String name) throws JsonProcessingException {
        return getObject(RenameCatalogDto.builder().name(name).build());
    }

    public static CatalogDao generateCatalogWithGivenUUID(UUID catalogId) {
        return CatalogDao
            .builder()
            .dateArchived(null)
            .fileName("customer-file-name-given-UUID")
            .status("DRAFT")
            .procSystem("Greenwing")
            .name("customer-name-given-UUID")
            .lastUpdate(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
            .id(catalogId)
            .build();
    }

    public static CatalogProductDao generateMappingRandomUUID() {
        return CatalogProductDao
            .builder()
            .uom("uom-alpha")
            .partNumber("part-number-alpha")
            .listPrice(new BigDecimal(0))
            .sellPrice(new BigDecimal(1))
            .lastPullDatetime(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
            .id(UUID.randomUUID())
            .build();
    }

    public static MultiValueMap<String, String> generatePaginatorQueryParams(String page, String perPage) {
        MultiValueMap<String, String> paginatorQueryParams = new LinkedMultiValueMap<>();
        paginatorQueryParams.add("page", page);
        paginatorQueryParams.add("perPage", perPage);
        return paginatorQueryParams;
    }

    public static List<ProductDao> generateProductList(Integer numberOfProducts) {
        List<ProductDao> productList = new ArrayList<>();

        for (int i = 1; i <= numberOfProducts; i++) {
            ProductDao product = ProductDao
                .builder()
                .id(UUID.randomUUID())
                .categoryLevel1Name("Test Category 1")
                .categoryLevel2Name("Test Category 2")
                .categoryLevel3Name("Test Category 3")
                .deliveryInDays(i)
                .description("Test Product " + i + " Description")
                .imageFullSize("image-" + i)
                .name("Test Product " + i)
                .partNumber("TEST-PART-" + i)
                .imageThumb("image-" + i)
                .manufacturer("Test Manufacturer")
                .manufacturerPartNumber("EST-MFN-" + i)
                .unspsc("TEST-unspsc")
                .maxSyncDatetime(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
                .build();

            productList.add(product);
        }

        return productList;
    }

    public static String getCatalogRequest(String procurementSystem, List<String> uploadIds)
        throws JsonProcessingException {
        return getObject(
            NewCatalogInputDto.builder().procurementSystem(procurementSystem).uploadIds(uploadIds).build()
        );
    }

    public static String getUploadInputDto(String filename, String encoded) throws JsonProcessingException {
        return getObject(UploadInputDto.builder().fileName(filename).encoded(encoded).build());
    }

    public static <T> String getObject(T object) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        map.configure(SerializationFeature.WRAP_ROOT_VALUE, Boolean.FALSE);
        ObjectWriter ow = map.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    public static List<ProcurementSystemDao> getListProcurementSystemDao() {
        return List.of(
            ProcurementSystemDao.builder().name("procurement-1").build(),
            ProcurementSystemDao.builder().name("procurement-2").build(),
            ProcurementSystemDao.builder().name("procurement-3").build()
        );
    }

    public static void assertProcurementToDtos(List<ProcurementSystemDao> inputs, List<ProcurementSystemDto> result) {
        Assertions.assertEquals(inputs.get(0).getName(), result.get(0).getName());
        Assertions.assertEquals(inputs.get(1).getName(), result.get(1).getName());
        Assertions.assertEquals(inputs.get(1).getName(), result.get(1).getName());
    }

    public static Set<CustomerRegionDao> getListCustomerRegionDao() {
        return Set.of(
            CustomerRegionDao
                .builder()
                .name("customer-region-1")
                .id(UUID.randomUUID())
                .customer(CustomerDao.builder().build())
                .build(),
            CustomerRegionDao
                .builder()
                .name("customer-region-2")
                .id(UUID.randomUUID())
                .customer(CustomerDao.builder().build())
                .build(),
            CustomerRegionDao
                .builder()
                .name("customer-region-3")
                .id(UUID.randomUUID())
                .customer(CustomerDao.builder().build())
                .build()
        );
    }

    public static void assertCustomerRegion(Set<CustomerRegionDao> inputs, List<CustomerRegionDto> result) {
        result
            .stream()
            .filter(Objects::nonNull)
            .forEach(dto -> {
                CustomerRegionDao dao = inputs
                    .stream()
                    .filter(d -> Objects.equals(d.getId().toString(), dto.getId().toString()))
                    .findFirst()
                    .orElse(CustomerRegionDao.builder().build());

                Assertions.assertEquals(dto.getId(), dao.getId());
                Assertions.assertEquals(dto.getName(), dao.getName());
            });

        Assertions.assertEquals(inputs.size(), result.size());
    }

    public static void assertCustomerRegionNotOk(Set<CustomerRegionDao> inputs, List<CustomerRegionDto> result) {
        int sizeInput = inputs != null ? inputs.size() : 0;
        int sizeResult = result != null ? result.size() : 0;
        Assertions.assertNotNull(result);
        Assertions.assertNull(inputs);
        Assertions.assertEquals(sizeInput, sizeResult);
    }

    public static ProductDao getProductDao() {
        return ProductDao
            .builder()
            .id(UUID.randomUUID())
            .unspsc("123213")
            .name("product-1")
            .description("product-1")
            .partNumber("123323")
            .manufacturerPartNumber("1232312")
            .build();
    }

    public static void assertProductToDto(ProductDao input, ProductDto result) {
        Assertions.assertEquals(input.getId(), result.getId());
        Assertions.assertEquals(input.getUnspsc(), result.getUnspsc());
        Assertions.assertEquals(input.getName(), result.getName());
        Assertions.assertEquals(input.getDescription(), result.getDescription());
        Assertions.assertEquals(input.getPartNumber(), result.getPartNumber());
        Assertions.assertEquals(input.getManufacturerPartNumber(), result.getManufacturerPartNumber());
    }

    public static CatalogProductDao getCatalogProductDao(ProductDao product, String uom, CatalogDao catalog) {
        return CatalogProductDao
            .builder()
            .product(product)
            .catalog(catalog)
            .id(UUID.randomUUID())
            .sellPrice(BigDecimal.TEN)
            .listPrice(BigDecimal.TEN)
            .uom(uom)
            .lastPullDatetime(Timestamp.from(Instant.now()))
            .skuQuantity(4)
            .partNumber(product.getPartNumber())
            .build();
    }

    public static String getContentToUpload(List<ProductDao> products, String branchId, String customerId) {
        StringBuilder result = new StringBuilder("Product ID,Branch,Customer ID\n");
        products.forEach(prod ->
            result.append(prod.getPartNumber()).append(",").append(branchId).append(",").append(customerId).append("\n")
        );
        return result.toString();
    }
}
