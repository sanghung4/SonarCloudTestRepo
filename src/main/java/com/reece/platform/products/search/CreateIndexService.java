package com.reece.platform.products.search;

import static com.reece.platform.products.helpers.Util.productMapping;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.exceptions.EmptyProductIndexJobException;
import com.reece.platform.products.external.appsearch.AppSearchClient;
import com.reece.platform.products.external.appsearch.model.*;
import com.reece.platform.products.helpers.Util;
import com.reece.platform.products.model.SearchEngineEnum;
import com.reece.platform.products.model.entity.SearchIndexMetadata;
import com.reece.platform.products.model.repository.SearchIndexMetadataDAO;
import com.reece.platform.products.pdw.model.ProductSearchDocument;
import com.reece.platform.products.pdw.repository.DataWarehouseRepository;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateIndexService {

    private static final String ENGINE_LANGUAGE = "en";
    private static final int MAX_DOC_COUNT_PER_CALL = 100;
    private static final int CURATIONS_PAGE_SIZE = 25;

    private final DataWarehouseRepository dataWarehouseRepository;
    private final AppSearchClient appSearchClient;
    private final ObjectMapper objectMapper;
    private final SearchIndexMetadataDAO searchIndexMetadataDAO;
    private final SearchService searchService;

    public void buildAndPopulateNewEngine(String metaEngineName) throws EmptyProductIndexJobException {
        log.info("ENTERING buildAndPopulateNewEngine()");
        val start = Instant.now();

        val products = loadProducts();

        if (products.isEmpty()) {
            throw new EmptyProductIndexJobException();
        }

        val engineName = buildEngineName(metaEngineName);

        createEngine(engineName);

        // Save the updateTime right before we query snowflake
        val updateTime = LocalDateTime.now();

        addProductsToEngine(engineName, products);

        updateMetaEngine(metaEngineName, engineName);

        findLastKnownGoodEngine(metaEngineName, engineName);

        val metadata = searchIndexMetadataDAO.findAll().stream().findFirst().orElseGet(SearchIndexMetadata::new);
        metadata.setLastUpdateTime(updateTime);
        searchIndexMetadataDAO.save(metadata);

        // Wipe category cache and refresh
        searchService.evictCategoryCache(String.valueOf(SearchEngineEnum.plumbing_hvac));
        searchService.evictCategoryCache(String.valueOf(SearchEngineEnum.waterworks));
        searchService.evictCategoryCache(String.valueOf(SearchEngineEnum.bath_kitchen));

        searchService.getCategories(String.valueOf(SearchEngineEnum.plumbing_hvac));
        searchService.getCategories(String.valueOf(SearchEngineEnum.waterworks));
        searchService.getCategories(String.valueOf(SearchEngineEnum.bath_kitchen));

        log.info(
            "EXITING buildAndPopulateNewEngine(), duration {}ms",
            Duration.between(start, Instant.now()).toMillis()
        );
    }

    public void updateCurrentEngine(String metaEngineName) {
        log.info("ENTERING updateCurrentEngine()");
        val start = Instant.now();

        val engineName = getCurrentEngine(metaEngineName)
            .orElseThrow(() -> new IllegalStateException("Tried to update currentEngine, currentEngine not found."));

        // Save the lastUpdateTime right before we query snowflake
        val metadata = searchIndexMetadataDAO
            .findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Tried to update currentEngine, but no index metadata found"));
        val updateTime = LocalDateTime.now();

        val products = loadChangedProducts(metadata.getLastUpdateTime());

        addProductsToEngine(engineName, products);

        metadata.setLastUpdateTime(updateTime);
        searchIndexMetadataDAO.save(metadata);

        log.info("EXITING updateCurrentEngine(), duration {}ms", Duration.between(start, Instant.now()).toMillis());
    }

    private void createEngine(String engineName) {
        log.info("ENTERING createEngine(\"{}\")", engineName);
        appSearchClient.createEngine(new CreateEngineRequest(engineName, ENGINE_LANGUAGE));

        val schema = loadSchema();
        appSearchClient.updateSchema(engineName, schema);

        log.info("EXITING createEngine()");
    }

    private Optional<String> getCurrentEngine(String metaEngineName) {
        val metaEngine = appSearchClient.getEngine(metaEngineName);
        return metaEngine.getSourceEngines().stream().filter(name -> name.contains(metaEngineName)).findFirst();
    }

    private Map<String, FieldType> loadSchema() {
        val resource = new ClassPathResource("ecomm-products-schema.json", getClass());
        try (val is = resource.getInputStream()) {
            val responseType = new TypeReference<Map<String, FieldType>>() {};
            return objectMapper.readValue(is, responseType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ProductSearchDocument> loadProducts() {
        log.info("ENTERING loadProducts()");
        val start = Instant.now();
        val products = dataWarehouseRepository.getAllProducts();
        log.info("EXITING loadProducts(), duration {}ms", Duration.between(start, Instant.now()).toMillis());
        return products;
    }

    private List<ProductSearchDocument> loadChangedProducts(LocalDateTime lastUpdateTime) {
        log.info("ENTERING loadChangedProducts()");
        val start = Instant.now();
        val changedProducts = dataWarehouseRepository.getChangedProducts(lastUpdateTime);
        log.info("EXITING loadChangedProducts(), duration {}ms", Duration.between(start, Instant.now()).toMillis());
        return changedProducts;
    }

    private void addProductsToEngine(String engineName, List<ProductSearchDocument> products) {
        log.info("ENTERING addProductsToEngine(), {} items", products.size());
        /*
            (?!\d)(| x |x| X |X| \* |)(?<!\d) this regex removes 'x' between two digits of measurement value.
            [^0-9a-zA-Z:,]+ this regex removes special characters from measurement field and MFR number field
        */
        products = products.stream().map(Util::productMapping).collect(Collectors.toList());

        for (int i = 0, cnt = products.size(); i < cnt; i += MAX_DOC_COUNT_PER_CALL) {
            val chunk = subList(products, i, i + MAX_DOC_COUNT_PER_CALL);
            log.info("Indexing items {} through {}", i, i + chunk.size() - 1);
            val startTime = Instant.now();
            val response = appSearchClient.indexDocuments(engineName, chunk);
            logErrors(chunk, response);

            log.info(
                "Indexed items {} through {} in {} seconds",
                i,
                i + chunk.size() - 1,
                (Duration.between(startTime, Instant.now()).toMillis()) / 1000.0
            );
        }

        log.info("EXITING addProductsToEngine()");
    }

    private void updateMetaEngine(String metaEngineName, String engineName) {
        log.info("ENTERING updateMetaEngine(\"{}\", \"{}\")", metaEngineName, engineName);
        val oldEngines = getOldEngines(metaEngineName, engineName);
        log.info("Retrieved old engines: {}", oldEngines);

        val addEngineResponse = appSearchClient.addMetaEngineSources(metaEngineName, List.of(engineName));

        log.info(
            "Added source engine '{}' to meta engine '{}' with response {}",
            engineName,
            metaEngineName,
            addEngineResponse
        );

        updateCurations(metaEngineName, engineName);

        if (!oldEngines.isEmpty()) {
            // Get all but the latest engine, so we can keep it as a backup
            var oldestEngines = oldEngines
                .stream()
                .sorted()
                .collect(Collectors.toList())
                .subList(0, oldEngines.size() - 1);
            log.info("oldEngines.size() > 0, preparing to delete old engines: {}", oldEngines);
            val deleteResponse = appSearchClient.deleteMetaEngineSources(metaEngineName, oldEngines);
            log.info("metaEngineSources deleted with response: {}", deleteResponse);

            for (val oldEngine : oldestEngines) {
                log.info("Deleting old source engine: {}", oldEngine);
                val destoryEngineResponse = appSearchClient.destroyEngine(oldEngine);
                log.info("Engine '{}' destroyed with response: {}", oldEngine, destoryEngineResponse);
            }
        }

        log.info("EXITING updateMetaEngine()");
    }

    private void findLastKnownGoodEngine(String metaEngineName, String latestEngineName) {
        // Get the document count for the main engine, so we can track if a new engine lacks the full product count
        val documentCount = appSearchClient.getEngine(metaEngineName).getDocumentCount();
        val oldEngines = appSearchClient
            .listEngines(new PageRequest(10, 1)) // Capping size at 10 since we should never get to that number on engines.
            .getResults()
            .stream()
            .filter(engine ->
                !engine.getName().equals(latestEngineName) && engine.getName().startsWith(metaEngineName + "-")
            )
            .toList();
        val lastKnownGoodEngineName = oldEngines
            .stream()
            .filter(engine -> engine.getDocumentCount().equals(documentCount))
            .findFirst()
            .map(engine -> {
                log.info("Found last known good engine: {}", engine.getName());
                return engine.getName();
            })
            .orElseGet(() ->
                oldEngines
                    .stream()
                    .filter(engine -> engine.getDocumentCount() >= documentCount - 5000)
                    .findFirst()
                    .map(engine -> {
                        log.warn(
                            "No engine with matching document count found, persisting engine that is within 5000 products of metaEngine: {}, ({} products)",
                            engine.getName(),
                            engine.getDocumentCount()
                        );
                        return engine.getName();
                    })
                    .orElse("")
            );
        if (lastKnownGoodEngineName.isEmpty()) {
            log.warn("No engine matching criteria for \"last known good engine\" found");
        } else {
            log.info("Preserving last good engine: {}", lastKnownGoodEngineName);
        }
        val enginesToDestroy = oldEngines
            .stream()
            .map(Engine::getName)
            .filter(name -> !name.equals(lastKnownGoodEngineName))
            .toList();
        for (val engine : enginesToDestroy) {
            log.info("Deleting engine {}", engine);
            appSearchClient.destroyEngine(engine);
        }
    }

    private void updateCurations(String metaEngineName, String engineName) {
        log.info("ENTERING updateCurations()");

        var curationsResponse = appSearchClient.listCurations(metaEngineName, new PageRequest(CURATIONS_PAGE_SIZE, 1));

        val totalNumberOfPages = curationsResponse.getMeta().getPage().getTotalPages();
        val totalNumberOfCurations = curationsResponse.getMeta().getPage().getTotalResults();

        log.info("Preparing to move over {} curations", totalNumberOfCurations);

        // Iterate through all pages of curations
        for (int i = 0; i < totalNumberOfPages; i++) {
            log.info(
                "Copying curations {} through {}",
                i * CURATIONS_PAGE_SIZE,
                i * CURATIONS_PAGE_SIZE + CURATIONS_PAGE_SIZE - 1
            );

            // Skip first iteration since already fetched result above
            if (i != 0) {
                curationsResponse =
                    appSearchClient.listCurations(metaEngineName, new PageRequest(CURATIONS_PAGE_SIZE, i + 1));
            }
            for (val curation : curationsResponse.getResults()) {
                updateCuration(engineName, metaEngineName, curation);
            }
        }

        log.info("EXITING updateCurations()");
    }

    private void updateCuration(String engineName, String metaEngineName, Curation curation) {
        try {
            appSearchClient.deleteCuration(metaEngineName, curation.getId());
            val promoted = updateCurationDocuments(engineName, curation.getPromoted());
            val hidden = updateCurationDocuments(engineName, curation.getHidden());
            curation.setPromoted(promoted);
            curation.setHidden(hidden);

            appSearchClient.createCuration(metaEngineName, curation);
        } catch (Exception e) {
            log.info("Could not create curation {}", curation.getId());
        }
    }

    private List<String> getOldEngines(String metaEngineName, String engineName) {
        val metaEngine = appSearchClient.getEngine(metaEngineName);
        val oldEngines = new ArrayList<>(metaEngine.getSourceEngines());
        oldEngines.remove(engineName);
        return oldEngines;
    }

    private List<String> updateCurationDocuments(String metaEngineName, List<String> documents) {
        return documents
            .stream()
            .sequential()
            .map(documentId -> buildCurationDocumentId(metaEngineName, documentId))
            .collect(Collectors.toList());
    }

    private static String buildCurationDocumentId(String engineName, String curationDocumentId) {
        String documentId = curationDocumentId.split("\\|")[1];
        return String.format("%s|%s", engineName, documentId);
    }

    private static String buildEngineName(String prefix) {
        return String.format("%s-%d", prefix, Instant.now().toEpochMilli());
    }

    private static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        val safeToIndex = Math.min(toIndex, list.size());
        return list.subList(fromIndex, safeToIndex);
    }

    private static void logErrors(
        List<ProductSearchDocument> chunk,
        List<IndexDocumentsResponse> indexDocumentsResponses
    ) {
        for (int i = 0, cnt = indexDocumentsResponses.size(); i < cnt; i++) {
            val response = indexDocumentsResponses.get(i);
            if (!response.getErrors().isEmpty()) {
                log.error("Error indexing document {}\n\nERRORS: {}", chunk.get(i), response.getErrors());
            }
        }
    }
}
