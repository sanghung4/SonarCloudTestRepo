package com.reece.platform.products.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.exceptions.EmptyProductIndexJobException;
import com.reece.platform.products.external.appsearch.AppSearchClient;
import com.reece.platform.products.external.appsearch.model.*;
import com.reece.platform.products.model.SearchEngineEnum;
import com.reece.platform.products.model.entity.SearchIndexMetadata;
import com.reece.platform.products.model.repository.SearchIndexMetadataDAO;
import com.reece.platform.products.pdw.model.ProductSearchDocument;
import com.reece.platform.products.pdw.repository.DataWarehouseRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

public class CreateIndexServiceTest {

    private static final String META_ENGINE_NAME = "ted-test";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TestLogger testLogger = TestLoggerFactory.getTestLogger(CreateIndexService.class);

    @Mock
    private DataWarehouseRepository dataWarehouseRepository;

    @Mock
    private SearchIndexMetadataDAO searchIndexMetadataDAO;

    @Mock
    private AppSearchClient appSearchClient;

    @Mock
    private SearchService searchService;

    private CreateIndexService createIndexService;

    @Captor
    private ArgumentCaptor<CreateEngineRequest> createEngineArgCaptor;

    @Captor
    private ArgumentCaptor<Map<String, FieldType>> schemaArgCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        createIndexService =
            new CreateIndexService(
                dataWarehouseRepository,
                appSearchClient,
                objectMapper,
                searchIndexMetadataDAO,
                searchService
            );
    }

    /**
     * This is a simple, successful run indexing 1,000 products.
     *
     * - a new engine is created
     * - the schema is applied
     * - the documents are added in chunks of 100 or less
     * - the new engine is added to the meta engine
     * - there is no old engine, so there's nothing to remove or delete
     * - the lastUpdateTime is updated and saved in the SearchIndexMetadata table
     */
    @Test
    public void buildAndPopulateNewEngine_simpleSuccess() {
        val testProducts = getTestProducts(1_000);
        val numberOfExpectedChunks = (int) Math.ceil(testProducts.size() / 100.0);

        when(dataWarehouseRepository.getAllProducts()).thenReturn(testProducts);

        val testMetaEngine = new Engine();
        testMetaEngine.setType(Engine.Type.META);
        testMetaEngine.setName(META_ENGINE_NAME);
        testMetaEngine.setSourceEngines(List.of());

        when(appSearchClient.getEngine(eq(META_ENGINE_NAME))).thenReturn(testMetaEngine);

        val metadata = new SearchIndexMetadata();
        val yesterday = LocalDateTime.now().minusDays(1);
        metadata.setId(UUID.randomUUID());
        metadata.setLastUpdateTime(yesterday);

        val searchEngine = SearchEngineEnum.plumbing_hvac.toString();
        val curationId = "cur_1234";
        val curationsResponse = new PaginatedResponse<Curation>();
        curationsResponse.setResults(List.of(getTestCuration(curationId)));
        curationsResponse.setMeta(getMetadata(1, 1, 1, 100));

        when(appSearchClient.listCurations(eq(META_ENGINE_NAME), any(PageRequest.class))).thenReturn(curationsResponse);

        when(searchIndexMetadataDAO.findAll()).thenReturn(List.of(metadata));
        testMetaEngine.setDocumentCount(125000L);
        PageRequest pageRequest = new PageRequest(10, 1);
        PaginatedResponse<Engine> enginesResponse = new PaginatedResponse<>();
        enginesResponse.setResults(getEngines());
        List<Engine> engines = enginesResponse.getResults();
        when(appSearchClient.listEngines(eq(pageRequest))).thenReturn(enginesResponse);
        assertDoesNotThrow(() -> createIndexService.buildAndPopulateNewEngine(META_ENGINE_NAME));

        verify(appSearchClient, times(1)).createEngine(createEngineArgCaptor.capture());
        val newEngineName = createEngineArgCaptor.getValue().getName();

        verify(appSearchClient, times(1)).updateSchema(anyString(), schemaArgCaptor.capture());
        assertEquals("text", schemaArgCaptor.getValue().get("minimum_increment_qty").toString());

        verify(appSearchClient, times(numberOfExpectedChunks)).indexDocuments(anyString(), anyList());

        verify(appSearchClient, times(1)).addMetaEngineSources(META_ENGINE_NAME, List.of(newEngineName));

        verify(appSearchClient, never()).deleteMetaEngineSources(anyString(), anyList());

        verify(searchService, times(1)).evictCategoryCache(searchEngine);
        verify(searchService, times(1)).getCategories(searchEngine);

        assertEquals(LocalDate.now(), metadata.getLastUpdateTime().toLocalDate());
    }

    /**
     * This is a simple, unsuccessful run of 0 products
     */
    @Test
    public void buildAndPopulateNewEngine_noProductsthrowsException() {
        val testProducts = getTestProducts(0);
        val numberOfExpectedChunks = (int) Math.ceil(testProducts.size() / 100.0);
        val searchEngine = SearchEngineEnum.plumbing_hvac.toString();

        when(dataWarehouseRepository.getAllProducts()).thenReturn(testProducts);

        val testMetaEngine = new Engine();
        testMetaEngine.setType(Engine.Type.META);
        testMetaEngine.setName(META_ENGINE_NAME);
        testMetaEngine.setSourceEngines(List.of());

        when(appSearchClient.getEngine(eq(META_ENGINE_NAME))).thenReturn(testMetaEngine);

        val metadata = new SearchIndexMetadata();
        val yesterday = LocalDateTime.now().minusDays(1);
        metadata.setId(UUID.randomUUID());
        metadata.setLastUpdateTime(yesterday);

        val curationId = "cur_1234";
        val curationsResponse = new PaginatedResponse<Curation>();
        curationsResponse.setResults(List.of(getTestCuration(curationId)));
        curationsResponse.setMeta(getMetadata(1, 1, 1, 100));

        when(appSearchClient.listCurations(eq(META_ENGINE_NAME), any(PageRequest.class))).thenReturn(curationsResponse);

        when(searchIndexMetadataDAO.findAll()).thenReturn(List.of(metadata));

        assertThrows(
            EmptyProductIndexJobException.class,
            () -> createIndexService.buildAndPopulateNewEngine(META_ENGINE_NAME)
        );

        verify(appSearchClient, times(0)).createEngine(createEngineArgCaptor.capture());

        verify(appSearchClient, times(0)).updateSchema(anyString(), schemaArgCaptor.capture());

        verify(appSearchClient, times(numberOfExpectedChunks)).indexDocuments(anyString(), anyList());

        verify(appSearchClient, never()).deleteMetaEngineSources(anyString(), anyList());
        verify(appSearchClient, never()).destroyEngine(anyString());

        verify(searchService, times(0)).evictCategoryCache(searchEngine);
        verify(searchService, times(0)).getCategories(searchEngine);

        assertNotEquals(LocalDate.now(), metadata.getLastUpdateTime().toLocalDate());
    }

    /**
     * This test validates that if more than two engines exist, the oldest engines are deleted.
     */
    @Test
    public void buildAndPopulateNewEngine_successRemoveOldEngines() {
        val testProducts = getTestProducts(100);

        when(dataWarehouseRepository.getAllProducts()).thenReturn(testProducts);

        val testOldEngineName = "random-old-engine-200";
        val testMetaEngine = new Engine();
        testMetaEngine.setType(Engine.Type.META);
        testMetaEngine.setName(META_ENGINE_NAME);

        val testOlderEngineName = "random-old-engine-100";

        testMetaEngine.setSourceEngines(List.of(testOldEngineName, testOlderEngineName));

        val curationId = "cur_1234";
        val curationsResponse = new PaginatedResponse<Curation>();
        curationsResponse.setResults(List.of(getTestCuration(curationId)));
        curationsResponse.setMeta(getMetadata(1, 1, 1, 100));

        when(appSearchClient.listCurations(eq(META_ENGINE_NAME), any(PageRequest.class))).thenReturn(curationsResponse);

        testMetaEngine.setDocumentCount(125000L);
        when(appSearchClient.getEngine(eq(META_ENGINE_NAME))).thenReturn(testMetaEngine);
        PageRequest pageRequest = new PageRequest(10, 1);
        PaginatedResponse<Engine> enginesResponse = new PaginatedResponse<>();
        enginesResponse.setResults(getEngines());
        when(appSearchClient.listEngines(eq(pageRequest))).thenReturn(enginesResponse);

        assertDoesNotThrow(() -> createIndexService.buildAndPopulateNewEngine(META_ENGINE_NAME));

        verify(appSearchClient, times(1)).destroyEngine(eq(getEngines().get(2).getName()));
    }

    /**
     * This test validates that if an engine is already attached to the meta engine, it is removed and destroyed.
     */
    @Test
    public void buildAndPopulateNewEngine_successRemoveOldSourceEngine() {
        val testProducts = getTestProducts(100);

        when(dataWarehouseRepository.getAllProducts()).thenReturn(testProducts);

        val testOldEngineName = "random-old-engine-200";
        val testMetaEngine = new Engine();
        testMetaEngine.setType(Engine.Type.META);
        testMetaEngine.setName(META_ENGINE_NAME);

        val testOlderEngineName = "random-old-engine-100";

        testMetaEngine.setSourceEngines(List.of(testOldEngineName, testOlderEngineName));

        val curationId = "cur_1234";
        val curationsResponse = new PaginatedResponse<Curation>();
        curationsResponse.setResults(List.of(getTestCuration(curationId)));
        curationsResponse.setMeta(getMetadata(1, 1, 1, 100));

        when(appSearchClient.listCurations(eq(META_ENGINE_NAME), any(PageRequest.class))).thenReturn(curationsResponse);

        when(appSearchClient.getEngine(eq(META_ENGINE_NAME))).thenReturn(testMetaEngine);
        testMetaEngine.setDocumentCount(125000L);
        PageRequest pageRequest = new PageRequest(10, 1);
        PaginatedResponse<Engine> enginesResponse = new PaginatedResponse<>();
        enginesResponse.setResults(getEngines());
        List<Engine> engines = enginesResponse.getResults();
        when(appSearchClient.listEngines(eq(pageRequest))).thenReturn(enginesResponse);
        assertDoesNotThrow(() -> createIndexService.buildAndPopulateNewEngine(META_ENGINE_NAME));

        verify(appSearchClient, times(1))
            .deleteMetaEngineSources(META_ENGINE_NAME, List.of(testOldEngineName, testOlderEngineName));
        verify(appSearchClient, times(1)).destroyEngine(testOlderEngineName);
        verify(appSearchClient, times(0)).destroyEngine(testOldEngineName);
    }

    @Test
    public void buildAndPopulateNewEngine_logErrors() {
        val testProducts = getTestProducts(1);

        when(dataWarehouseRepository.getAllProducts()).thenReturn(testProducts);

        val testMetaEngine = new Engine();
        testMetaEngine.setType(Engine.Type.META);
        testMetaEngine.setName(META_ENGINE_NAME);
        testMetaEngine.setSourceEngines(List.of());

        when(appSearchClient.getEngine(eq(META_ENGINE_NAME))).thenReturn(testMetaEngine);

        val curationId = "cur_1234";
        val curationsResponse = new PaginatedResponse<Curation>();
        curationsResponse.setResults(List.of(getTestCuration(curationId)));
        curationsResponse.setMeta(getMetadata(1, 1, 1, 100));

        when(appSearchClient.listCurations(eq(META_ENGINE_NAME), any(PageRequest.class))).thenReturn(curationsResponse);

        val errorMessage = "Error Happened";
        val errorResponse = new IndexDocumentsResponse();
        errorResponse.setErrors(List.of(errorMessage));
        when(appSearchClient.indexDocuments(anyString(), anyList())).thenReturn(List.of(errorResponse));
        testMetaEngine.setDocumentCount(125000L);
        PageRequest pageRequest = new PageRequest(10, 1);
        PaginatedResponse<Engine> enginesResponse = new PaginatedResponse<>();
        enginesResponse.setResults(getEngines());
        List<Engine> engines = enginesResponse.getResults();
        when(appSearchClient.listEngines(eq(pageRequest))).thenReturn(enginesResponse);

        assertDoesNotThrow(() -> createIndexService.buildAndPopulateNewEngine(META_ENGINE_NAME));

        testLogger
            .getLoggingEvents()
            .stream()
            .filter(ev -> ev.getLevel().equals(Level.ERROR))
            .findFirst()
            .ifPresentOrElse(
                ev ->
                    assertEquals(
                        LoggingEvent.error(
                            "Error indexing document {}\n\nERRORS: {}",
                            testProducts.get(0),
                            List.of(errorMessage)
                        ),
                        ev
                    ),
                Assertions::fail
            );
    }

    /**
     * This is a simple, successful run indexing 1,000 products.
     *
     * - a new engine is created
     * - the schema is applied
     * - the documents are added in chunks of 100 or less
     * - the new engine is added to the meta engine
     * - try to create curations. Exceptions thrown but continue
     * - test with paginated results of curations
     * - there is no old engine, so there's nothing to remove or delete
     * - the lastUpdateTime is updated and saved in the SearchIndexMetadata table
     */
    @Test
    public void buildAndPopulateNewEngine_withCurationException() {
        val testProducts = getTestProducts(1_000);
        when(dataWarehouseRepository.getAllProducts()).thenReturn(testProducts);

        val testMetaEngine = new Engine();
        testMetaEngine.setType(Engine.Type.META);
        testMetaEngine.setName(META_ENGINE_NAME);
        testMetaEngine.setSourceEngines(List.of());

        when(appSearchClient.getEngine(eq(META_ENGINE_NAME))).thenReturn(testMetaEngine);

        val metadata = new SearchIndexMetadata();
        val yesterday = LocalDateTime.now().minusDays(1);
        metadata.setId(UUID.randomUUID());
        metadata.setLastUpdateTime(yesterday);

        val curationId = "cur_1234";
        val curationsResponse = new PaginatedResponse<Curation>();
        curationsResponse.setResults(List.of(getTestCuration(curationId)));
        curationsResponse.setMeta(getMetadata(1, 2, 2, 1));

        when(appSearchClient.listCurations(eq(META_ENGINE_NAME), any(PageRequest.class))).thenReturn(curationsResponse);
        when(appSearchClient.createCuration(anyString(), any(Curation.class))).thenThrow(new NullPointerException());

        when(searchIndexMetadataDAO.findAll()).thenReturn(List.of(metadata));
        testMetaEngine.setDocumentCount(125000L);
        PageRequest pageRequest = new PageRequest(10, 1);
        PaginatedResponse<Engine> enginesResponse = new PaginatedResponse<>();
        enginesResponse.setResults(getEngines());
        List<Engine> engines = enginesResponse.getResults();
        when(appSearchClient.listEngines(eq(pageRequest))).thenReturn(enginesResponse);
        assertDoesNotThrow(() -> createIndexService.buildAndPopulateNewEngine(META_ENGINE_NAME));

        verify(appSearchClient, times(2)).deleteCuration(META_ENGINE_NAME, curationId);
    }

    @Test
    public void updateCurrentEngine_success() {
        val testEngineName = META_ENGINE_NAME + "-12345";
        val testMetaEngine = new Engine();
        testMetaEngine.setType(Engine.Type.META);
        testMetaEngine.setName(META_ENGINE_NAME);
        testMetaEngine.setSourceEngines(List.of(testEngineName));
        when(appSearchClient.getEngine(eq(META_ENGINE_NAME))).thenReturn(testMetaEngine);

        val testMetadata = new SearchIndexMetadata();
        testMetadata.setId(UUID.randomUUID());
        testMetadata.setLastUpdateTime(LocalDateTime.now().minusDays(1));

        when(searchIndexMetadataDAO.findAll()).thenReturn(List.of(testMetadata));

        val testProducts = getTestProducts(10);
        when(dataWarehouseRepository.getChangedProducts(eq(testMetadata.getLastUpdateTime()))).thenReturn(testProducts);

        createIndexService.updateCurrentEngine(META_ENGINE_NAME);

        verify(appSearchClient, times(1)).indexDocuments(eq(testEngineName), anyList());
    }

    private List<ProductSearchDocument> getTestProducts(int numProducts) {
        final List<ProductSearchDocument> testProducts = new ArrayList<>();

        for (var i = 0; i < numProducts; i++) {
            testProducts.add(getTestProduct(TestUtils.randomProductId()));
        }

        return testProducts;
    }

    private PaginatedResponse.Meta getMetadata(
        Integer current,
        Integer totalResults,
        Integer totalPages,
        Integer pageSize
    ) {
        val metadata = new PaginatedResponse.Meta();
        val page = new PaginatedResponse.Meta.Page();
        page.setCurrent(current);
        page.setSize(pageSize);
        page.setTotalPages(totalPages);
        page.setTotalResults(totalResults);
        metadata.setPage(page);
        return metadata;
    }

    private List<Engine> getEngines() {
        val newEngine = new Engine();
        val oldEngine = new Engine();
        val oldestEngine = new Engine();
        newEngine.setName("ted-test-1658275692001");
        newEngine.setDocumentCount(125000L);

        oldEngine.setName("ted-test-old-15125919");
        oldEngine.setDocumentCount(12500L);

        oldestEngine.setName("ted-test-oldest-19058127");
        oldestEngine.setDocumentCount(1250L);

        List<Engine> allEngines = new ArrayList<>();

        allEngines.add(newEngine);
        allEngines.add(oldEngine);
        allEngines.add(oldestEngine);

        return allEngines;
    }

    private Curation getTestCuration(String id) {
        val curation = new Curation();
        curation.setId(id);
        curation.setPromoted(List.of("testMetaEngine|MSC-1234", "testMetaEngine|MSC-1235"));
        curation.setHidden(Collections.emptyList());
        curation.setQueries(List.of("testQuery"));

        return curation;
    }

    private ProductSearchDocument getTestProduct(String id) {
        val document = new ProductSearchDocument();
        document.setCategory1Name("Plumbing Installation, Tools, Hardware & Safety");
        document.setCategory2Name("Plumbing Installation");
        document.setCategory3Name("TBC");
        document.setErpProductId(id);
        document.setFeatureBenefitListText("Lead-Free");
        document.setFullImageUrlName("http://images.tradeservice.com/ProductImages/DIR100157/EZFLOI_48250_LRG.jpg");
        document.setId(id);
        document.setInternalItemNbr("5550791");
        document.setLastUpdateDate("2022-02-08 16:40:05.407");
        document.setLowLeadCompliantFlag(true);
        document.setMediumImageUrlName("http://images.tradeservice.com/ProductImages/DIR100158/EZFLOI_48250_MED.jpg");
        document.setMfrFullName("Eastman");
        document.setMfrSpecTechDocFileName("http://images.tradeservice.com/ATTACHMENTS/DIR100212/EZFLOIE00087_1.pdf");
        document.setProductLine("STEEL-FLEX®");
        document.setProductOverviewDescription(
            "Water Heater Connector; Type Water Heater; End Connection 3/4 Inch Female Threaded x 3/4 Inch Female Threaded; Length 12 Inch; Tubing Material Braided Stainless Steel/Ferrule; End Fitting Material Nickel Plated Nut; Temperature Rating 221 Deg F; Pressure Rating 1200 PSI (Burst); Applicable Standard CSA, UPC; Lead-Free; Type Flexible"
        );
        document.setProductSoldCount(1);
        document.setSearchKeywordText(
            "EZFLO INTERNATIONAL INC 48250 12 SS WATER HEATER CONN FXF STAINLESS STEEL CONNECTORS"
        );
        document.setThumbnailImageUrlName(
            "http://images.tradeservice.com/ProductImages/DIR100158/EZFLOI_48250_SML.jpg"
        );
        document.setUnspcId("40141720");
        document.setUpcId("091712482505");
        document.setVendorPartNbr("48250");
        document.setCleanVendorPartNbr("48250");
        document.setWebDescription("12\" Stainless Steel Water Heater Connector");
        document.setCleanWebDescription("12  Stainless Steel Water Heater Connector");
        document.setCleanProductBrand("Eastman 12  Stainless Steel Water Heater Connector");
        document.setTechnicalSpecifications(
            List.of(
                Map.of(
                    "applicable_standard",
                    "CSA, UPC",
                    "end_connection",
                    "3/4 Inch Female Threaded x 3/4 Inch Female Threaded",
                    "end_fitting_material",
                    "Nickel Plated Nut",
                    "length",
                    "12 Inch",
                    "pressure_rating",
                    "1200 PSI (Burst)",
                    "temperature_rating",
                    "221 Deg F",
                    "tubing_material",
                    "Braided Stainless Steel/Ferrule",
                    "type",
                    "Water Heater"
                )
            )
        );
        document.setCustomerNumber(
            List.of(
                "302940",
                "335352",
                "302996",
                "302381",
                "302216",
                "303200",
                "306398",
                "302914",
                "301738",
                "300922",
                "302636",
                "301952",
                "302357",
                "302936",
                "301277",
                "301132",
                "305320",
                "302682"
            )
        );
        document.setCustomerPartNumber(
            List.of(
                "@505455",
                "@505455",
                "4450159",
                "4450159",
                "4450159",
                "@505455",
                "@505455",
                "4450159",
                "4450159",
                "@505455",
                "@505455",
                "4450159",
                "4450159",
                "4450159",
                "4450159",
                "@505455",
                "4450159",
                "4450159"
            )
        );
        document.setInStockLocation(List.of("1310"));

        return document;
    }
}
