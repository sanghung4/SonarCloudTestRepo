package com.reece.punchoutcustomerbff.service;

import com.reece.punchoutcustomerbff.dto.*;
import com.reece.punchoutcustomerbff.mapper.CatalogMapper;
import com.reece.punchoutcustomerbff.mapper.CatalogProductMapper;
import com.reece.punchoutcustomerbff.mapper.CustomerMapper;
import com.reece.punchoutcustomerbff.models.daos.*;
import com.reece.punchoutcustomerbff.models.repositories.*;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import com.reece.punchoutcustomerbff.util.ListUtil;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * Service used for general catalog interaction.
 * @author john.valentino
 */
@Service
@Slf4j
public class CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CatalogProductRepository catalogProductRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DateGenerator dateGenerator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private UploadService uploadService;

    protected CatalogService instance = this;

    private static final Integer MAX_PRODUCT_PER_PAGE = 200;

    public void renameCatalog(String catalogId, RenameCatalogDto input) {
        CatalogDao catalog = catalogRepository.findById(UUID.fromString(catalogId)).get();
        catalog.setName(input.getName());
        catalogRepository.save(catalog);
    }

    public CatalogViewDto listCatalogProducts(String catalogId, int page, int perPage) {
        perPage = sanitizePerPageAmount(perPage);

        CatalogViewDto result = CatalogViewDto
            .builder()
            .page(page)
            .resultsPerPage(perPage)
            .catalogId(catalogId)
            .build();

        // pull the catalog
        CatalogDao catalog = catalogRepository.findById(UUID.fromString(catalogId)).get();
        result.setCatalog(CatalogMapper.toDTO(catalog));

        // set the customer
        result.setCustomer(CustomerMapper.toDTO(catalog.getCustomer()));

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("partNumber"));
        Page<CatalogProductDao> paging = catalogProductRepository.findWithProducts(
            UUID.fromString(catalogId),
            pageable
        );
        result.setTotalPages(paging.getTotalPages());
        result.setTotalItems(paging.getTotalElements());

        List<CatalogProductDto> mappings = CatalogProductMapper.toDTOs(paging.getContent());
        result.setResults(mappings);

        return result;
    }

    private Integer sanitizePerPageAmount(Integer perPage) {
        if (perPage > MAX_PRODUCT_PER_PAGE) {
            perPage = MAX_PRODUCT_PER_PAGE;
        }
        return perPage;
    }

    public CatalogViewDto saveNewCatalog(String customerId, NewCatalogInputDto input) {
        // pull the customer
        CustomerDao customer = customerRepository.findById(UUID.fromString(customerId)).get();

        // create a new catalog
        Timestamp currentTimestamp = dateGenerator.generateTimestamp();
        CatalogDao catalog = CatalogDao
            .builder()
            .id(UUID.randomUUID())
            .name(input.getName())
            .customer(customer)
            .procSystem(input.getProcurementSystem())
            .status("DRAFT")
            .lastUpdate(currentTimestamp)
            .build();
        catalogRepository.save(catalog);

        // Make a map of all the mappings we need to create, starting with no product
        List<UUID> uploadIds = input.getUploadIds().stream().map(UUID::fromString).collect(Collectors.toList());
        List<UploadDao> uploads = uploadRepository.findAllById(uploadIds);
        for (UploadDao upload : uploads) {
            List<InputCsvRecord> uploadCsvRecords = uploadService.processInputCsv(upload.getContent());
            List<List<InputCsvRecord>> listOfUploadCsvRecords = ListUtil.splitInto(
                uploadCsvRecords,
                500,
                InputCsvRecord.class
            );
            for (List<InputCsvRecord> inputCsvRecords : listOfUploadCsvRecords) {
                List<String> partNumbers = inputCsvRecords
                    .stream()
                    .map(i -> i.getProductId())
                    .collect(Collectors.toList());
                List<ProductDao> products = productRepository.findByPartNumbers(partNumbers);
                log.info("Found " + products.size() + " products for the given part numbers.");
                List<CatalogProductDao> catalogProducts = new ArrayList<>();
                for (InputCsvRecord inputCsvRecord : inputCsvRecords) {
                    CatalogProductDao catalogProduct = CatalogProductDao
                        .builder()
                        .partNumber(inputCsvRecord.getProductId())
                        .catalog(catalog)
                        .id(UUID.randomUUID())
                        .build();

                    Optional<ProductDao> product = products
                        .stream()
                        .filter(i -> i.getPartNumber().equalsIgnoreCase(inputCsvRecord.getProductId()))
                        .findFirst();

                    if (product.isEmpty()) {
                        log.info("Product " + inputCsvRecord.getProductId() + " not found");
                        ProductDao newProduct = ProductDao
                            .builder()
                            .partNumber(inputCsvRecord.getProductId())
                            .id(UUID.randomUUID())
                            .build();
                        catalogProduct.setProduct(newProduct);
                    } else {
                        catalogProduct.setProduct(product.get());
                    }
                    catalogProducts.add(catalogProduct);
                }
                catalogProductRepository.saveAll(catalogProducts);
            }
        }

        // call the view for the first page on the catalog and return that as the result
        return instance.listCatalogProducts(catalog.getId().toString(), 1, 100);
    }

    public CatalogViewDto listCatalogAndUploadProducts(
        int page,
        int perPage,
        List<UUID> uploadIds,
        Optional<String> catalogId
    ) {
        perPage = sanitizePerPageAmount(perPage);

        CatalogViewDto catalogView = CatalogViewDto.builder().page(page).resultsPerPage(perPage).build();

        // get products from catalog if available:
        List<CatalogProductDto> catalogProducts = new ArrayList<>();
        if (catalogId.isPresent()) {
            if (uploadIds.isEmpty()) {
                return instance.listCatalogProducts(catalogId.get(), page, perPage);
            } else {
                CatalogDao catalog = catalogRepository.findById(UUID.fromString(catalogId.get())).get();
                catalogView.setCatalog(CatalogMapper.toDTO(catalog));
                catalogView.setCatalogId(catalog.getId().toString());
                catalogView.setCustomer(CustomerMapper.toDTO(catalog.getCustomer()));
                List<CatalogProductDao> catalogProductMappings = catalogProductRepository.findWithProducts(
                    UUID.fromString(catalogId.get())
                );
                List<CatalogProductDto> mappings = CatalogProductMapper.toDTOs(catalogProductMappings);
                catalogProducts.addAll(mappings);
            }
        }

        // get products from each upload:
        List<UploadDao> uploads = uploadRepository.findAllById(uploadIds);
        for (UploadDao upload : uploads) {
            List<InputCsvRecord> inputCsvs = uploadService.processInputCsv(upload.getContent());
            List<CatalogProductDto> productMapping = uploadService
                .generateProductMap(inputCsvs)
                .stream()
                .map(i -> i.getCatalogProduct())
                .collect(Collectors.toList());
            catalogProducts.addAll(productMapping);
        }

        // remove duplicates between catalog products and products in upload content:
        catalogProducts = catalogProducts.stream().distinct().collect(Collectors.toList());

        // create paging for the given catalog/upload products:
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("partNumber"));
        Integer start = (int) pageable.getOffset();
        Integer end = Math.min((start + pageable.getPageSize()), catalogProducts.size());
        List<CatalogProductDto> pageContent = new ArrayList<>();
        if (end >= start) {
            pageContent = catalogProducts.subList(start, end);
        }
        Page<CatalogProductDto> paging = new PageImpl<>(pageContent, pageable, catalogProducts.size());
        catalogView.setTotalPages(paging.getTotalPages());
        catalogView.setTotalItems(paging.getTotalElements());
        catalogView.setResults(paging.getContent());

        return catalogView;
    }
}
