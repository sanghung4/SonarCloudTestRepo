package com.reece.punchoutcustomerbff.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.punchoutcustomerbff.dto.CatalogViewDto;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.UploadDao;
import com.reece.punchoutcustomerbff.rest.CatalogRest;
import com.reece.punchoutcustomerbff.util.BaseIntegrationTest;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@SpringBootTest
public class CatalogIntegrationTest extends BaseIntegrationTest {

    private static final String URL_CATALOG_RENAME = "/catalog/{catalogId}/rename";
    private static final String URL_CATALOG_VIEW = "/catalog/view/{catalogId}";
    private static final String URL_CATALOG_VIEW_WITH_PAGE = "/catalog/view/";
    private static final String URL_CATALOG_SAVE = "/catalog/new/{customerId}";

    @Test
    public void whenGivenCatalogIdRenameCatalogId() throws Exception {
        this.mockAdminLoggedIn();

        final String newCatalogName = "TEST CATALOG RENAMED.csv";

        CatalogDao catalog = TestUtils.generateCatalog();

        this.entityManager.persist(catalog);

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URL_CATALOG_RENAME, catalog.getId())
                    .header("Origin", "*")
                    .header("X-Auth-Token", SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .content(TestUtils.generateRenameCatalogRequest(newCatalogName))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        ResultDto catalogRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), ResultDto.class);

        CatalogDao updatedCatalogObject = this.entityManager.find(CatalogDao.class, catalog.getId());

        Assertions.assertEquals(
            updatedCatalogObject.getName(),
            newCatalogName,
            "Catalog name in the database matches the request name."
        );

        Assertions.assertTrue(catalogRs.isSuccess(), "Response payload equals '{\"success\":true}'");
    }

    @Test
    public void whenGivenCatalogWithSeveralAssociatedProducts() throws Exception {
        this.mockAdminLoggedIn();

        final Integer productListSize = 3;

        final String page = "1";

        final String perPage = "10";

        final UUID catalogUUID = UUID.fromString("d6925283-d174-4b7e-b9f2-3c7f5d60ca53");

        final CustomerDao customer = CustomerDao
            .builder()
            .uploads(new HashSet<>())
            .catalogs(new HashSet<>())
            .branchId("1233")
            .customerId("43234")
            .erpName("ECLIPSE")
            .contactName("peter")
            .contactPhone("12323543")
            .name("peter")
            .erpId("1")
            .isBillTo(Boolean.TRUE)
            .build();

        this.entityManager.persist(customer);

        CatalogDao catalog = TestUtils.generateCatalogWithGivenUUID(catalogUUID);

        catalog.setCustomer(customer);
        this.entityManager.persist(catalog);

        List<ProductDao> productDaoList = TestUtils.generateProductList(productListSize);

        productDaoList.forEach(productDao -> {
            entityManager.persist(productDao);
            CatalogProductDao catalogProduct = TestUtils.generateMappingRandomUUID();
            catalogProduct.setCatalog(catalog);
            catalogProduct.setProduct(productDao);
            entityManager.persist(catalogProduct);
        });

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(URL_CATALOG_VIEW, catalog.getId())
                    .queryParams(TestUtils.generatePaginatorQueryParams(page, perPage))
                    .header("Origin", "*")
                    .header("X-Auth-Token", SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        CatalogViewDto catalogRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CatalogViewDto.class);

        Assertions.assertNotNull(catalogRs.getResults(), "Results for catalogProduct is not null.");
        Assertions.assertEquals(
            String.valueOf(catalogRs.getPage()),
            page,
            "Payload page value is the same to query param page."
        );
        Assertions.assertEquals(
            String.valueOf(catalogRs.getTotalPages()),
            page,
            "Payload totalPages value is equal to 1."
        );
        Assertions.assertEquals(
            catalogRs.getCatalog().getId(),
            catalogUUID,
            "Payload catalogId value is equal to the catalogId used for creating catalog entity."
        );
        Assertions.assertEquals(
            catalogRs.getResults().size(),
            productDaoList.size(),
            "Payload results size matches the number of products associated"
        );
    }

    @Test
    public void whenGivenCatalogWithNoAssociatedProducts() throws Exception {
        this.mockAdminLoggedIn();

        final String page = "1";

        final String perPage = "10";

        final CustomerDao customer = CustomerDao
            .builder()
            .uploads(new HashSet<>())
            .catalogs(new HashSet<>())
            .branchId("1233")
            .customerId("43234")
            .erpName("ECLIPSE")
            .contactName("peter")
            .contactPhone("12323543")
            .name("peter")
            .erpId("1")
            .isBillTo(Boolean.TRUE)
            .build();

        this.entityManager.persist(customer);

        final UUID catalogUUID = UUID.fromString("d6925283-d174-4b7e-b9f2-3c7f5d60ca53");

        CatalogDao catalog = TestUtils.generateCatalogWithGivenUUID(catalogUUID);
        catalog.setCustomer(customer);
        this.entityManager.persist(catalog);

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(URL_CATALOG_VIEW, catalog.getId())
                    .queryParams(TestUtils.generatePaginatorQueryParams(page, perPage))
                    .header("Origin", "*")
                    .header("X-Auth-Token", SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        CatalogViewDto catalogRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CatalogViewDto.class);

        Assertions.assertEquals(
            page,
            String.valueOf(catalogRs.getPage()),
            "Payload page value is the same to query param page."
        );
        Assertions.assertEquals(
            "0",
            String.valueOf(catalogRs.getTotalPages()),
            "Payload totalPages value is equal to 0 due due to no products associated to the given catalog."
        );
        Assertions.assertEquals(
            catalogRs.getResults().size(),
            catalogRs.getTotalItems(),
            "Payload catalogId value is equal to the catalogId used for creating catalog entity."
        );
    }

    /**
     * Create a catalog with existed customer.
     * @throws Exception in case the test fails.
     */
    @Test
    public void whenGivenCatalogInfoWithCustomerExistsThenCreateCatalogOk() throws Exception {
        this.mockAdminLoggedIn();

        CustomerDao customer = CustomerDao
            .builder()
            .erpName("ECLIPSE")
            .isBillTo(Boolean.TRUE)
            .customerId("31222")
            .lastUpdate(Timestamp.from(Instant.now()))
            .erpId("1")
            .contactPhone("213123231")
            .contactName("peter")
            .branchId("1232")
            .uploads(new HashSet<>())
            .catalogs(new HashSet<>())
            .name("peter")
            .regions(new HashSet<>())
            .build();

        this.entityManager.persist(customer);

        ProductDao prod = ProductDao.builder().id(UUID.randomUUID()).partNumber("99996").build();
        String content = TestUtils.getContentToUpload(
            List.of(prod),
            customer.getBranchId(),
            customer.getId().toString()
        );

        this.entityManager.persist(prod);
        UploadDao upload = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .content(content)
            .fileName("file.csv")
            .uploadDatetime(Timestamp.from(Instant.now()))
            .build();

        this.entityManager.persist(upload);

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URL_CATALOG_SAVE, customer.getId().toString())
                    .header("Origin", "*")
                    .header("X-Auth-Token", SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.getCatalogRequest("Greenwing", List.of(upload.getId().toString())))
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        CatalogViewDto catalogRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CatalogViewDto.class);

        Assertions.assertEquals(customer.getId(), catalogRs.getCustomer().getId());
        Assertions.assertNotNull(catalogRs.getCatalogId());
        Assertions.assertEquals("DRAFT", catalogRs.getCatalog().getStatus());
        Assertions.assertEquals("Greenwing", catalogRs.getCatalog().getProcSystem());
        Assertions.assertFalse(catalogRs.getResults().isEmpty());
        Assertions.assertEquals("99996", catalogRs.getResults().get(0).getPartNumber());
    }

    /**
     * <h1> Test to get upload information from {@link CatalogProductDao} and  {@link UploadDao} </h1>
     * <p>
     *
     * The POST endpoint of {@link CatalogRest }{@code /catalog/view`}`
     * Get information to front about Catalog products.
     * the params to the endpoint are:
     * @param catalogId
     * @param page
     * @param perPage
     * @param uploadIds
     *
     * @author luis.bolivar
     * @throws Exception test throw exception.
     */
    @Test
    public void givenPaginationInfoWhenGetViewCatalogThenReturnInfoInPaginationOk() throws Exception {
        this.mockAdminLoggedIn();

        final CustomerDao customer = CustomerDao
            .builder()
            .erpName("ECLIPSE")
            .isBillTo(Boolean.TRUE)
            .customerId("31222")
            .lastUpdate(Timestamp.from(Instant.now()))
            .erpId("1")
            .contactPhone("213123231")
            .contactName("peter")
            .branchId("1232")
            .uploads(new HashSet<>())
            .catalogs(new HashSet<>())
            .name("peter")
            .regions(new HashSet<>())
            .build();

        this.entityManager.persist(customer);

        final CatalogDao catalogA = CatalogDao
            .builder()
            .id(UUID.randomUUID())
            .dateArchived(Timestamp.from(Instant.now()))
            .fileName("file-name.csv")
            .name("client-1")
            .status("DRAFT")
            .lastUpdate(Timestamp.from(Instant.now()))
            .customer(customer)
            .procSystem("32")
            .mappings(new HashSet<>())
            .build();

        this.entityManager.persist(catalogA);

        final ProductDao prodA = ProductDao.builder().id(UUID.randomUUID()).partNumber("91").build();
        final ProductDao prodB = ProductDao.builder().id(UUID.randomUUID()).partNumber("92").build();
        final ProductDao prodC = ProductDao.builder().id(UUID.randomUUID()).partNumber("93").build();
        final ProductDao prodD = ProductDao.builder().id(UUID.randomUUID()).partNumber("94").build();
        final ProductDao prodE = ProductDao.builder().id(UUID.randomUUID()).partNumber("95").build();
        final ProductDao prodF = ProductDao.builder().id(UUID.randomUUID()).partNumber("96").build();

        this.entityManager.persist(prodA);
        this.entityManager.persist(prodB);
        this.entityManager.persist(prodC);
        this.entityManager.persist(prodD);
        this.entityManager.persist(prodE);
        this.entityManager.persist(prodF);

        final CatalogProductDao cProdE = TestUtils.getCatalogProductDao(prodE, "prodE", catalogA);
        final CatalogProductDao cProdF = TestUtils.getCatalogProductDao(prodF, "prodF", catalogA);

        this.entityManager.persist(cProdE);
        this.entityManager.persist(cProdF);

        final UploadDao uploadA = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .fileName("file-A.csv")
            .customer(customer)
            .uploadDatetime(Timestamp.from(Instant.now()))
            .content(
                TestUtils.getContentToUpload(List.of(prodA, prodB), customer.getBranchId(), customer.getId().toString())
            )
            .build();

        final UploadDao uploadB = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .fileName("file-B.csv")
            .customer(customer)
            .uploadDatetime(Timestamp.from(Instant.now()))
            .content(
                TestUtils.getContentToUpload(List.of(prodC, prodD), customer.getBranchId(), customer.getId().toString())
            )
            .build();

        this.entityManager.persist(uploadA);
        this.entityManager.persist(uploadB);

        List<UUID> uuids = List.of(uploadA.getId(), uploadB.getId());

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URL_CATALOG_VIEW_WITH_PAGE)
                    .header("Origin", "*")
                    .header("X-Auth-Token", SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("catalogId", catalogA.getId().toString())
                    .param("page", "1")
                    .param("perPage", "3")
                    .content(TestUtils.getObject(uuids))
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        CatalogViewDto catalogRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CatalogViewDto.class);

        Assertions.assertEquals(200, rs.getResponse().getStatus());
        Assertions.assertNotNull(catalogRs);
        Assertions.assertEquals(catalogA.getId().toString(), catalogRs.getCatalogId());
        Assertions.assertEquals(1, catalogRs.getPage());
        Assertions.assertEquals(2, catalogRs.getTotalPages());
        Assertions.assertEquals(6, catalogRs.getTotalItems());
        Assertions.assertEquals(3, catalogRs.getResultsPerPage());
        Assertions.assertEquals(customer.getId(), catalogRs.getCustomer().getId());
        Assertions.assertEquals(3, catalogRs.getResults().size());
    }

    /**
     * <h1> Test to get upload information only from {@link UploadDao} </h1>
     * <p>
     *
     * The POST endpoint of {@link CatalogRest }{@code /catalog/view`}`
     * Get information to front about Catalog products.
     * the params to the endpoint are:
     * @param catalogId
     * @param page
     * @param perPage
     * @param uploadIds
     *
     * @author luis.bolivar
     * @throws Exception test throw exception.
     */
    @Test
    public void givenPaginationInfoWhenGetViewCatalogThenReturnInfoInPaginationOnlyFromUploadOk() throws Exception {
        this.mockAdminLoggedIn();

        final CustomerDao customer = CustomerDao
            .builder()
            .erpName("ECLIPSE")
            .isBillTo(Boolean.TRUE)
            .customerId("31222")
            .lastUpdate(Timestamp.from(Instant.now()))
            .erpId("1")
            .contactPhone("213123231")
            .contactName("peter")
            .branchId("1232")
            .uploads(new HashSet<>())
            .catalogs(new HashSet<>())
            .name("peter")
            .regions(new HashSet<>())
            .build();

        this.entityManager.persist(customer);

        final ProductDao prodA = ProductDao.builder().id(UUID.randomUUID()).partNumber("91").build();
        final ProductDao prodB = ProductDao.builder().id(UUID.randomUUID()).partNumber("92").build();
        final ProductDao prodC = ProductDao.builder().id(UUID.randomUUID()).partNumber("93").build();
        final ProductDao prodD = ProductDao.builder().id(UUID.randomUUID()).partNumber("94").build();

        this.entityManager.persist(prodA);
        this.entityManager.persist(prodB);
        this.entityManager.persist(prodC);
        this.entityManager.persist(prodD);

        final UploadDao uploadA = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .fileName("file-A.csv")
            .customer(customer)
            .uploadDatetime(Timestamp.from(Instant.now()))
            .content(
                TestUtils.getContentToUpload(List.of(prodA, prodB), customer.getBranchId(), customer.getId().toString())
            )
            .build();

        final UploadDao uploadB = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .fileName("file-B.csv")
            .customer(customer)
            .uploadDatetime(Timestamp.from(Instant.now()))
            .content(
                TestUtils.getContentToUpload(List.of(prodC, prodD), customer.getBranchId(), customer.getId().toString())
            )
            .build();

        this.entityManager.persist(uploadA);
        this.entityManager.persist(uploadB);

        List<UUID> uuids = List.of(uploadA.getId(), uploadB.getId());

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URL_CATALOG_VIEW_WITH_PAGE)
                    .header("Origin", "*")
                    .header("X-Auth-Token", SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("page", "1")
                    .param("perPage", "3")
                    .content(TestUtils.getObject(uuids))
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        CatalogViewDto catalogRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CatalogViewDto.class);

        Assertions.assertEquals(200, rs.getResponse().getStatus());
        Assertions.assertNotNull(catalogRs);
        Assertions.assertEquals(1, catalogRs.getPage());
        Assertions.assertEquals(2, catalogRs.getTotalPages());
        Assertions.assertEquals(4, catalogRs.getTotalItems());
        Assertions.assertEquals(3, catalogRs.getResultsPerPage());
        Assertions.assertEquals(3, catalogRs.getResults().size());
    }

    /**
     * <h1> Test to get upload information from {@link CatalogProductDao} and  {@link UploadDao} </h1>
     * <p>
     *
     * The POST endpoint of {@link CatalogRest }{@code /catalog/view`}`
     * Given a Catalog and Uploads Each With Products and Providing a page Parameter That is
     * Greater Than The Amount of Products Then Return Paginated Results with an Empty Result
     *
     * @param catalogId
     * @param page
     * @param perPage
     * @param uploadIds
     *
     * @author luis.bolivar
     * @throws Exception test throw exception.
     */
    @Test
    public void givenPaginationInfoWhenGetViewCatalogThenReturnInfoInPaginationMoreThanResultsOk() throws Exception {
        this.mockAdminLoggedIn();

        final CustomerDao customer = CustomerDao
            .builder()
            .erpName("ECLIPSE")
            .isBillTo(Boolean.TRUE)
            .customerId("31222")
            .lastUpdate(Timestamp.from(Instant.now()))
            .erpId("1")
            .contactPhone("213123231")
            .contactName("peter")
            .branchId("1232")
            .uploads(new HashSet<>())
            .catalogs(new HashSet<>())
            .name("peter")
            .regions(new HashSet<>())
            .build();

        this.entityManager.persist(customer);

        final CatalogDao catalogA = CatalogDao
            .builder()
            .id(UUID.randomUUID())
            .dateArchived(Timestamp.from(Instant.now()))
            .fileName("file-name.csv")
            .name("client-1")
            .status("DRAFT")
            .lastUpdate(Timestamp.from(Instant.now()))
            .customer(customer)
            .procSystem("32")
            .mappings(new HashSet<>())
            .build();

        this.entityManager.persist(catalogA);

        final ProductDao prodA = ProductDao.builder().id(UUID.randomUUID()).partNumber("91").build();
        final ProductDao prodB = ProductDao.builder().id(UUID.randomUUID()).partNumber("92").build();
        final ProductDao prodC = ProductDao.builder().id(UUID.randomUUID()).partNumber("93").build();
        final ProductDao prodD = ProductDao.builder().id(UUID.randomUUID()).partNumber("94").build();
        final ProductDao prodE = ProductDao.builder().id(UUID.randomUUID()).partNumber("95").build();
        final ProductDao prodF = ProductDao.builder().id(UUID.randomUUID()).partNumber("96").build();

        this.entityManager.persist(prodA);
        this.entityManager.persist(prodB);
        this.entityManager.persist(prodC);
        this.entityManager.persist(prodD);
        this.entityManager.persist(prodE);
        this.entityManager.persist(prodF);

        final CatalogProductDao cProdE = TestUtils.getCatalogProductDao(prodE, "prodE", catalogA);
        final CatalogProductDao cProdF = TestUtils.getCatalogProductDao(prodF, "prodF", catalogA);

        this.entityManager.persist(cProdE);
        this.entityManager.persist(cProdF);

        final UploadDao uploadA = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .fileName("file-A.csv")
            .customer(customer)
            .uploadDatetime(Timestamp.from(Instant.now()))
            .content(
                TestUtils.getContentToUpload(List.of(prodA, prodB), customer.getBranchId(), customer.getId().toString())
            )
            .build();

        final UploadDao uploadB = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .fileName("file-B.csv")
            .customer(customer)
            .uploadDatetime(Timestamp.from(Instant.now()))
            .content(
                TestUtils.getContentToUpload(List.of(prodC, prodD), customer.getBranchId(), customer.getId().toString())
            )
            .build();

        this.entityManager.persist(uploadA);
        this.entityManager.persist(uploadB);

        List<UUID> uuids = List.of(uploadA.getId(), uploadB.getId());

        MvcResult rs = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URL_CATALOG_VIEW_WITH_PAGE)
                    .header("Origin", "*")
                    .header("X-Auth-Token", SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("catalogId", catalogA.getId().toString())
                    .param("page", "9")
                    .param("perPage", "3")
                    .content(TestUtils.getObject(uuids))
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        CatalogViewDto catalogRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CatalogViewDto.class);

        Assertions.assertEquals(200, rs.getResponse().getStatus());
        Assertions.assertNotNull(catalogRs);
        Assertions.assertEquals(catalogA.getId().toString(), catalogRs.getCatalogId());
        Assertions.assertEquals(9, catalogRs.getPage());
        Assertions.assertEquals(2, catalogRs.getTotalPages());
        Assertions.assertEquals(6, catalogRs.getTotalItems());
        Assertions.assertEquals(3, catalogRs.getResultsPerPage());
        Assertions.assertEquals(customer.getId(), catalogRs.getCustomer().getId());
        Assertions.assertEquals(0, catalogRs.getResults().size());
    }
}
