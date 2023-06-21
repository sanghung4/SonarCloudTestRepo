package com.reece.platform.products.external.appsearch;

import com.reece.platform.products.external.appsearch.model.*;
import java.util.List;
import java.util.Map;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AppSearchClient {

    private final RestTemplate restTemplate;

    public AppSearchClient(
        RestTemplateBuilder restTemplateBuilder,
        @Value("${appsearch.endpointbase}") String endpointBase,
        @Value("${appsearch.privatekey}") String searchKey
    ) {
        this.restTemplate =
            restTemplateBuilder
                .rootUri(endpointBase + "/api/as/v1")
                .defaultHeader("Authorization", "Bearer " + searchKey)
                .build();
    }

    public SearchResponse search(String engineName, SearchRequest searchRequest) {
        val response = restTemplate.postForEntity(
            "/engines/{engineName}/search.json",
            searchRequest,
            SearchResponse.class,
            engineName
        );
        return response.getBody();
    }

    public QuerySuggestonResponse querySuggestion(String engineName, QuerySuggestionRequest request) {
        val response = restTemplate.postForEntity(
            "/engines/{engineName}/query_suggestion",
            request,
            QuerySuggestonResponse.class,
            engineName
        );
        return response.getBody();
    }

    /**
     * Index a batch of documents.
     * @param engineName unique Engine name
     * @param documents List of document objects to be indexed
     * @param <T> The document type being indexed
     * @return A list of response objects containing either the document id or any errors
     */
    public <T> List<IndexDocumentsResponse> indexDocuments(String engineName, List<T> documents) {
        val responseType = new ParameterizedTypeReference<List<IndexDocumentsResponse>>() {};
        val response = restTemplate.exchange(
            "/engines/{engineName}/documents",
            HttpMethod.POST,
            new HttpEntity<>(documents),
            responseType,
            engineName
        );
        return response.getBody();
    }

    /**
     * Partial update a batch of documents.
     * @param engineName unique Engine name
     * @param documents List of document objects to be updated.
     * @param <T> The document type being indexed
     * @return A list of response objects containing either the document id or any errors
     */
    public <T> List<IndexDocumentsResponse> updateDocuments(String engineName, List<T> documents) {
        val responseType = new ParameterizedTypeReference<List<IndexDocumentsResponse>>() {};
        val response = restTemplate.exchange(
            "/engines/{engineName}/documents",
            HttpMethod.PATCH,
            new HttpEntity<>(documents),
            responseType,
            engineName
        );
        return response.getBody();
    }

    // Engine

    public PaginatedResponse<Engine> listEngines(PageRequest page) {
        var url = "/engines";
        if (page != null) {
            url =
                UriComponentsBuilder
                    .fromPath(url)
                    .query("page[current]={current}&page[size]={size}")
                    .buildAndExpand(page.getCurrent(), page.getSize())
                    .toUriString();
        }

        val responseType = new ParameterizedTypeReference<PaginatedResponse<Engine>>() {};
        val response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return response.getBody();
    }

    public Engine getEngine(String engineName) {
        val response = restTemplate.getForEntity("/engines/{engineName}", Engine.class, engineName);
        return response.getBody();
    }

    public Engine createEngine(CreateEngineRequest createEngineRequest) {
        val response = restTemplate.postForEntity("/engines", createEngineRequest, Engine.class);
        return response.getBody();
    }

    public DeleteResponse destroyEngine(String engineName) {
        val response = restTemplate.exchange(
            "/engines/{engineName}",
            HttpMethod.DELETE,
            null,
            DeleteResponse.class,
            engineName
        );
        return response.getBody();
    }

    // Source Engine

    public Engine addMetaEngineSources(String engineName, List<String> sourceEngines) {
        val response = restTemplate.postForEntity(
            "/engines/{engineName}/source_engines",
            sourceEngines,
            Engine.class,
            engineName
        );
        return response.getBody();
    }

    public Engine deleteMetaEngineSources(String engineName, List<String> sourceEngines) {
        val requestEntity = new HttpEntity<>(sourceEngines);
        val response = restTemplate.exchange(
            "/engines/{engineName}/source_engines",
            HttpMethod.DELETE,
            requestEntity,
            Engine.class,
            engineName
        );
        return response.getBody();
    }

    // Curations
    public PaginatedResponse<Curation> listCurations(String engineName, PageRequest page) {
        var url = UriComponentsBuilder
            .fromPath("/engines/{engineName}/curations")
            .buildAndExpand(engineName)
            .toUriString();

        if (page != null) {
            url =
                UriComponentsBuilder
                    .fromPath(url)
                    .query("page[current]={current}&page[size]={size}")
                    .buildAndExpand(page.getCurrent(), page.getSize())
                    .toUriString();
        }

        val responseType = new ParameterizedTypeReference<PaginatedResponse<Curation>>() {};
        val response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return response.getBody();
    }

    public Curation getCuration(String engineName, String curationId) {
        val response = restTemplate.getForEntity(
            "/engines/{engineName}/curations/{curationId}",
            Curation.class,
            engineName,
            curationId
        );
        return response.getBody();
    }

    public CreateCurationResponse createCuration(String engineName, Curation curation) {
        val response = restTemplate.postForEntity(
            "/engines/{engineName}/curations",
            curation,
            CreateCurationResponse.class,
            engineName
        );
        return response.getBody();
    }

    public CreateCurationResponse updateCuration(String engineName, String curationId, Curation curation) {
        curation.setId(null); // Don't send the id property in the request body
        val response = restTemplate.exchange(
            "/engines/{engineName}/curations/{curationId}",
            HttpMethod.PUT,
            new HttpEntity<>(curation),
            CreateCurationResponse.class,
            engineName,
            curationId
        );
        return response.getBody();
    }

    public DeleteResponse deleteCuration(String engineName, String curationId) {
        val response = restTemplate.exchange(
            "/engines/{engineName}/curations/{curationId}",
            HttpMethod.DELETE,
            null,
            DeleteResponse.class,
            engineName,
            curationId
        );
        return response.getBody();
    }

    public Map<String, FieldType> updateSchema(String engineName, Map<String, FieldType> schema) {
        val responseType = new ParameterizedTypeReference<Map<String, FieldType>>() {};
        val response = restTemplate.exchange(
            "/engines/{engineName}/schema",
            HttpMethod.POST,
            new HttpEntity<>(schema),
            responseType,
            engineName
        );
        return response.getBody();
    }
}
