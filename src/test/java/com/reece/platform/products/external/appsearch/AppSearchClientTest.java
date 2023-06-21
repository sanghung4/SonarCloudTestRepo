package com.reece.platform.products.external.appsearch;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.reece.platform.products.TestUtils;
import com.reece.platform.products.external.appsearch.model.*;
import com.reece.platform.products.pdw.model.ProductSearchDocument;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(AppSearchClient.class)
public class AppSearchClientTest {

    @Autowired
    private AppSearchClient appSearchClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Value("${appsearch.phvac.enginename}")
    private String testEngine;

    @Test
    public void search_emptyRequest_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("empty-query-response.json");
        val expectedResponse = TestUtils.loadResponseJson("empty-query-response.json", SearchResponse.class);

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/search.json"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("{\"query\":\"\"}"))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new SearchRequest();

        val response = appSearchClient.search(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void search_queryOnly_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("query-only-response.json");
        val expectedResponse = TestUtils.loadResponseJson("query-only-response.json", SearchResponse.class);

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/search.json"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("{\"query\":\"copper\"}"))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new SearchRequest().query("copper");

        val response = appSearchClient.search(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void search_withPagination_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("query-with-pagination-response.json");
        val expectedResponse = TestUtils.loadResponseJson("query-with-pagination-response.json", SearchResponse.class);

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/search.json"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("{\"query\":\"copper\",\"page\":{\"current\":1,\"size\":10}}"))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new SearchRequest().query("copper").page(new PageRequest(10, 1));

        val response = appSearchClient.search(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void search_withFacets_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("query-with-facets-response.json");
        val expectedResponse = TestUtils.loadResponseJson("query-with-facets-response.json", SearchResponse.class);

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/search.json"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .json("{\"query\":\"copper\",\"facets\":{\"mfr_full_name\":[{\"type\":\"value\",\"size\":250}]}}")
            )
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new SearchRequest().query("copper").facet("mfr_full_name", new Facet().size(250));

        val response = appSearchClient.search(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void search_withFilters_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("query-with-filters-response.json");
        val expectedResponse = TestUtils.loadResponseJson("query-with-filters-response.json", SearchResponse.class);

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/search.json"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .json("{\"query\":\"copper\",\"filters\":{\"all\":[{\"mfr_full_name\":[\"Generic\",\"Viega\"]}]}}")
            )
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new SearchRequest()
            .query("copper")
            .filter(Filter.all(Stream.of(Filter.values("mfr_full_name", "Generic", "Viega"))));

        val response = appSearchClient.search(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void search_withSearchFields_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("query-with-search-fields-response.json");
        val expectedResponse = TestUtils.loadResponseJson(
            "query-with-search-fields-response.json",
            SearchResponse.class
        );

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/search.json"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("{\"query\":\"copper\",\"search_fields\":{\"mfr_full_name\":{}}}"))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new SearchRequest().query("copper").searchField("mfr_full_name", new SearchField());

        val response = appSearchClient.search(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void search_withResultFields_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("query-with-result-fields-response.json");
        val expectedResponse = TestUtils.loadResponseJson(
            "query-with-result-fields-response.json",
            SearchResponse.class
        );

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/search.json"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("{\"query\":\"copper\",\"result_fields\":{\"mfr_full_name\":{}}}"))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new SearchRequest().query("copper").resultField("mfr_full_name", new ResultField());

        val response = appSearchClient.search(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void querySuggestion_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("query-suggestion-response.json");
        val expectedResponse = TestUtils.loadResponseJson(
            "query-suggestion-response.json",
            QuerySuggestonResponse.class
        );

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/query_suggestion"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(
                content()
                    .json(
                        "{\"query\": \"pip\",\"types\": {\"documents\": {\"fields\": [\"vendor_part_nbr\",\"web_description\",\"customer_part_number\",\"mfr_full_name\"]}},\"size\": 7}"
                    )
            )
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val request = new QuerySuggestionRequest(
            "pip",
            7,
            List.of("vendor_part_nbr", "web_description", "customer_part_number", "mfr_full_name")
        );

        val response = appSearchClient.querySuggestion(testEngine, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void indexDocuments_success() {
        val testDocument = getTestDocument();
        val expectedResponseJson = TestUtils.loadResponseJsonString("index-documents-success-response.json");

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/documents"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json(TestUtils.jsonStringify(List.of(testDocument))))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.indexDocuments(testEngine, List.of(testDocument));
        assertEquals(testDocument.getId(), response.get(0).getId());
    }

    @Test
    public void updateDocuments_success() {
        val testDocument = getTestDocument();
        val expectedResponseJson = TestUtils.loadResponseJsonString("index-documents-success-response.json");

        this.mockRestServiceServer.expect(requestTo("/engines/" + testEngine + "/documents"))
            .andExpect(method(HttpMethod.PATCH))
            .andExpect(content().json(TestUtils.jsonStringify(List.of(testDocument))))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.updateDocuments(testEngine, List.of(testDocument));
        assertEquals(testDocument.getId(), response.get(0).getId());
    }

    @Test
    public void listEngines_success() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("list-engines-success-response.json");
        val exppectedTotalResults = 9;

        mockRestServiceServer
            .expect(requestTo("/engines"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.listEngines(null);

        assertEquals(exppectedTotalResults, response.getMeta().getPage().getTotalResults());
        assertEquals(exppectedTotalResults, response.getResults().size());
    }

    @Test
    public void listEngines_paginated() {
        val expectedResponseJson = TestUtils.loadResponseJsonString("list-engines-success-response.json");
        val expectedTotalResults = 9;
        val testCurrentPage = 2;
        val testPageSize = 10;

        mockRestServiceServer
            .expect(
                requestToUriTemplate(
                    "/engines?page[current]={current}&page[size]={size}",
                    testCurrentPage,
                    testPageSize
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(expectedResponseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.listEngines(new PageRequest(testPageSize, testCurrentPage));

        assertEquals(expectedTotalResults, response.getMeta().getPage().getTotalResults());
        assertEquals(expectedTotalResults, response.getResults().size());
    }

    @Test
    public void getEngine_meta() {
        val responseJson = TestUtils.loadResponseJsonString("get-engine-meta-response.json");

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.getEngine(testEngine);

        assertEquals(Engine.Type.META, response.getType());
        assertEquals(1, response.getSourceEngines().size());
        assertNull(response.getLanguage());
    }

    @Test
    public void getEngine_default() {
        val responseJson = TestUtils.loadResponseJsonString("get-engine-default-response.json");

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.getEngine(testEngine);

        assertEquals(Engine.Type.DEFAULT, response.getType());
        assertNull(response.getSourceEngines());
        assertEquals("en", response.getLanguage());
    }

    @Test
    public void createEngine_success() {
        val responseJson = TestUtils.loadResponseJsonString("get-engine-default-response.json");
        val testRequest = new CreateEngineRequest("products-1645092370124", "en");

        mockRestServiceServer
            .expect(requestTo("/engines"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json(TestUtils.jsonStringify(testRequest)))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.createEngine(testRequest);
        assertEquals(testRequest.getName(), response.getName());
    }

    @Test
    public void destroyEngine_success() {
        val responseJson = TestUtils.loadResponseJsonString("destroy-engine-success-response.json");

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.destroyEngine(testEngine);
        assertTrue(response.isDeleted());
    }

    @Test
    public void addMetaEngineSources_success() {
        val responseJson = TestUtils.loadResponseJsonString("get-engine-meta-response.json");
        val testSourceEngine = "test-engine-" + Instant.now().toEpochMilli();

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/source_engines"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json(TestUtils.jsonStringify(List.of(testSourceEngine))))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.addMetaEngineSources(testEngine, List.of(testSourceEngine));
        assertEquals(Engine.Type.META, response.getType());
    }

    @Test
    public void deleteMetaEngineSources_success() {
        val responseJson = TestUtils.loadResponseJsonString("get-engine-meta-response.json");
        val testSourceEngine = "test-engine-" + Instant.now().toEpochMilli();

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/source_engines"))
            .andExpect(method(HttpMethod.DELETE))
            .andExpect(content().json(TestUtils.jsonStringify(List.of(testSourceEngine))))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        val response = appSearchClient.deleteMetaEngineSources(testEngine, List.of(testSourceEngine));
        assertEquals(Engine.Type.META, response.getType());
    }

    @Test
    public void listCurations_success() {
        val responseJson = TestUtils.loadResponseJsonString("list-curations-success-response.json");

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/curations"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        appSearchClient.listCurations(testEngine, null);
    }

    @Test
    public void listCurations_paginated() {
        val responseJson = TestUtils.loadResponseJsonString("list-curations-success-response.json");
        val testCurrentPage = 2;
        val testPageSize = 10;

        mockRestServiceServer
            .expect(
                requestToUriTemplate(
                    "/engines/{engineName}/curations?page[current]={current}&page[size]={size}",
                    testEngine,
                    testCurrentPage,
                    testPageSize
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        appSearchClient.listCurations(testEngine, new PageRequest(testPageSize, testCurrentPage));
    }

    @Test
    public void getCuration_success() {
        val responseJson = TestUtils.loadResponseJsonString("get-curation-response.json");
        val curationId = "cur_123598423";

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/curations/" + curationId))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        appSearchClient.getCuration(testEngine, curationId);
    }

    @Test
    public void createCuration_success() {
        val responseJson = TestUtils.loadResponseJsonString("create-curation-response.json");

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/curations"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        appSearchClient.createCuration(testEngine, new Curation());
    }

    @Test
    public void updateCuration_success() {
        val responseJson = TestUtils.loadResponseJsonString("create-curation-response.json");
        val curationId = "cur_123598423";

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/curations/" + curationId))
            .andExpect(method(HttpMethod.PUT))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        appSearchClient.updateCuration(testEngine, curationId, new Curation());
    }

    @Test
    public void deleteCuration_success() {
        val responseJson = TestUtils.loadResponseJsonString("delete-curation-response.json");
        val curationId = "cur_123598423";

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/curations/" + curationId))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        appSearchClient.deleteCuration(testEngine, curationId);
    }

    @Test
    public void updateSchema_success() {
        val responseJson = TestUtils.loadResponseJsonString("update-schema-success-response.json");

        mockRestServiceServer
            .expect(requestTo("/engines/" + testEngine + "/schema"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("{ \"minimum_increment_qty\": \"text\", \"product_sold_count\": \"number\" }"))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        val schema = Map.of("minimum_increment_qty", FieldType.TEXT, "product_sold_count", FieldType.NUMBER);
        appSearchClient.updateSchema(testEngine, schema);
    }

    private ProductSearchDocument getTestDocument() {
        val document = new ProductSearchDocument();
        document.setCategory1Name("Plumbing Installation, Tools, Hardware & Safety");
        document.setCategory2Name("Plumbing Installation");
        document.setCategory3Name("TBC");
        document.setErpProductId("MSC-505455");
        document.setFeatureBenefitListText("Lead-Free");
        document.setFullImageUrlName("http://images.tradeservice.com/ProductImages/DIR100157/EZFLOI_48250_LRG.jpg");
        document.setId("MSC-505455");
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
        document.setWebDescription("12\" Stainless Steel Water Heater Connector");
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
