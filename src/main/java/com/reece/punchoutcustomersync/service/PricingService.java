package com.reece.punchoutcustomersync.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.reece.punchoutcustomerbff.mapper.GreenwingProductCatalogFileMapper;
import com.reece.punchoutcustomerbff.models.daos.AuditDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import com.reece.punchoutcustomerbff.models.repositories.AuditRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.models.repositories.SyncLogRepository;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomersync.dto.AuditErrorDto;
import com.reece.punchoutcustomersync.dto.AuditInputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;
import com.reece.punchoutcustomerbff.models.daos.*;
import com.reece.punchoutcustomerbff.models.repositories.*;
import com.reece.punchoutcustomerbff.util.ListUtil;
import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceProductDto;
import com.reece.punchoutcustomersync.dto.max.ProductDocumentDto;
import org.springframework.beans.factory.annotation.Value;
import com.zaxxer.hikari.HikariDataSource;

import javax.persistence.Table;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PricingService {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Value("${sync.uploadToS3:false}")
    private boolean uploadToS3;

    @Autowired
    HikariDataSource hikariDataSource;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SyncLogRepository syncLogRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private DateGenerator dateGenerator;

    @Autowired
    private SftpDataService sftpDataService;

    @Autowired
    private CatalogProductRepository catalogProductRepository;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private KourierService kourierService;

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Autowired
    private OutputCsvService outputCsvService;

    /**
     * Used to interact with Kourier/MAX ElasticSearch to get and save product information, generate product catalog CSV, upload to
     * Greenwing SFTP and to record auditing data.
     *
     * If any errors occur during this process, an AuditError record is created to log the issue details.
     *
     * @param syncTotalCustomers Number of total customers being synced in the current set
     * @param customerId The UUID associated to the given customer record.
     * @param customerOrderNumber The order in which the customer is being processed in relation to syncTotalCustomers
     * @param syncLogId The UUID associated to the given syncLog record.
     */
    public void syncCustomerProductCatalogs(Integer syncTotalCustomers, UUID customerId, Integer customerOrderNumber, UUID syncLogId)
            throws IOException, JSchException, SftpException {
        // get customer record with associated catalog/products
        Timestamp yesterday = new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        CustomerDao customer = customerRepository.getOne(customerId);
        SyncLogDao syncLog = syncLogRepository.getOne(syncLogId);
        AuditInputDto auditInput = AuditInputDto.builder()
                .s3Location("n/a")
                .build();

        if (customer.getCatalogs() == null || customer.getCatalogs().isEmpty()) {
            return;
        }

        CatalogDao customerCatalog = customer.getCatalogs().iterator().next();
        auditInput.setCatalogId(customerCatalog.getId().toString());
        List<CatalogProductDao> customerCatalogProducts = catalogProductRepository.findAllByCatalogIdWithLastPullDateBefore(customerCatalog.getId(), yesterday);
        log.info("{} catalog products for customer {} ready for sync process.", customerCatalogProducts.size(), customer.getName());

        List<AuditErrorDto> auditErrors = new ArrayList<>();
        // call the MAX endpoint to get product info:
        List<ProductDocumentDto> productDocuments = elasticSearchService.getProductDocumentsForCustomer(customer, customerCatalogProducts);
        log.info("Creating AuditErrors as needed from the results provided by ES.");
        auditErrors.addAll(createAuditErrorsFromElasticSearch(productDocuments, customerCatalogProducts));
        log.info("There are now {} audit errors.", auditErrors.size());

        // call the Kourier endpoint to get product pricing data:
        List<CustomersPriceProductDto> customersPriceProducts = kourierService.getCustomerProductsPricing(customer, customerCatalogProducts);
        log.info("Creating AuditErrors as needed from the results provided by Kourier.");
        auditErrors.addAll(createAuditErrorsFromKourier(customersPriceProducts, customerCatalogProducts));
        log.info("There are now {} audit errors.", auditErrors.size());

        log.info("Found product information from ES for {} products.", productDocuments.size());
        log.info("Found product information from Kourier for {} products.", customersPriceProducts.size());

        auditInput.setErrors(auditErrors);

        if (!customersPriceProducts.isEmpty()) {
            updateAllCatalogProductJdbcBatchCallable(customersPriceProducts);
        }
        if (!productDocuments.isEmpty()) {
            updateAllProductJdbcBatchCallable(productDocuments);
        }

        if (!customerCatalogProducts.isEmpty()) {
            log.info("Creating output CSV for Customer {} with {} products.", customer.getName(), customerCatalogProducts.size());
            Timestamp currentTimestamp = dateGenerator.generateTimestamp();
            String filename = String.format("%s_%s.csv", customer.getErpId(), DateUtil.formatDate(currentTimestamp, "YYYYMMDDhhmmss"));
            auditInput.setFileName(filename);

            List<String[]> csvData = GreenwingProductCatalogFileMapper.toCSVData(customerCatalogProducts, customer);
            InputStream csv = outputCsvService.createCsv(csvData);

            // Upload CSV to S3 Bucket:
            if (uploadToS3) {
                auditInput = uploadCSVToS3(csv, filename, auditInput);
            }

            // upload CSV to Greenwing via SFTP
            auditInput = uploadCSVToGreenwing(csv, filename, auditInput);

            AuditDao audit = auditService.generateAudit(syncLog, customer, auditInput);
            auditRepository.save(audit);
        }
    }

    private List<AuditErrorDto> createAuditErrorsFromElasticSearch(List<ProductDocumentDto> productDocuments, List<CatalogProductDao> customerCatalogProducts) {
        List<AuditErrorDto> auditErrors = new ArrayList<>();

        if (productDocuments.size() != customerCatalogProducts.size()) {
            List<String> productDocumentPartNumbers = productDocuments.stream().map(i -> i.getPartNumber()).collect(Collectors.toList());
            List<String> customerCatalogProductPartNumbers = customerCatalogProducts.stream().map(i -> i.getPartNumber()).collect(Collectors.toList());
            customerCatalogProductPartNumbers.removeAll(productDocumentPartNumbers);
            log.info("The amount of results retrieved from ES ({}) does not match the amount of catalog products in Punchout ({}).", productDocuments.size(), customerCatalogProducts.size());

            for (String customerCatalogProductPartNumber : customerCatalogProductPartNumbers) {
                AuditErrorDto auditError = AuditErrorDto.builder()
                        .error("Unable to find product information for product via MAX Elastic Search.")
                        .partNumber(customerCatalogProductPartNumber)
                        .errorDateTime(DateUtil.fromDate(dateGenerator.generateTimestamp()))
                        .build();

                auditErrors.add(auditError);
            }
        }

        return auditErrors;
    }

    private List<AuditErrorDto> createAuditErrorsFromKourier(List<CustomersPriceProductDto> customersPriceProducts, List<CatalogProductDao> customerCatalogProducts) {
        List<AuditErrorDto> auditErrors = new ArrayList<>();

        if (customersPriceProducts.size() != customerCatalogProducts.size()) {
            List<String> customersPriceProductPartNumbers = customersPriceProducts.stream().map(i -> i.getProductId()).collect(Collectors.toList());
            List<String> customerCatalogProductPartNumbers = customerCatalogProducts.stream().map(i -> i.getPartNumber()).collect(Collectors.toList());
            customerCatalogProductPartNumbers.removeAll(customersPriceProductPartNumbers);

            for (String customerCatalogProductPartNumber : customerCatalogProductPartNumbers) {
                AuditErrorDto auditError = AuditErrorDto.builder()
                        .error("Unable to find product information for product via Kourier.")
                        .partNumber(customerCatalogProductPartNumber)
                        .errorDateTime(DateUtil.fromDate(dateGenerator.generateTimestamp()))
                        .build();

                auditErrors.add(auditError);
            }
        }

        return auditErrors;
    }

    private AuditInputDto uploadCSVToS3(InputStream csv, String fileName, AuditInputDto auditInput) throws IOException {
        String filePath = amazonS3Service.uploadToS3(csv, fileName);
        auditInput.setS3DateTime(DateUtil.fromDate(dateGenerator.generateTimestamp()));
        auditInput.setS3Location(filePath);
        log.info("New file created at {}", filePath);
        return auditInput;
    }

    private AuditInputDto uploadCSVToGreenwing(InputStream csv, String fileName, AuditInputDto auditInput)
            throws JSchException, SftpException, IOException {
        try {
            String filePath = sftpDataService.uploadCsv(csv, fileName);
            auditInput.setFtpDateTime(DateUtil.fromDate(dateGenerator.generateTimestamp()));
            auditInput.setFtpLocation(filePath);
            log.info("New file created at {}", filePath);
        } catch(IOException | JSchException | SftpException exception) {
            log.error("Unable to create file {} at sftp location.", fileName);
            throw exception;
        }
        return auditInput;
    }

    private void updateAllProductJdbcBatch(List<ProductDocumentDto> productDocuments){
        log.info("update using jdbc batch to update products");
        String sql = String.format(
                "UPDATE %s SET name = ?, description = ?, image_full_size = ?, image_thumb = ?, manufacturer = ?, category_1_name = ?, category_2_name = ?, category_3_name = ?, unspsc = ?, max_sync_datetime = ?, manufacturer_part_number = ? WHERE part_number = ?",
                ProductDao.class.getAnnotation(Table.class).name()
        );
        try (Connection connection = hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            int counter = 0;
            for (ProductDocumentDto productDocument : productDocuments) {
                log.info("Updating Product "+productDocument.getPartNumber());
                statement.clearParameters();
                statement.setString(1, productDocument.getWebDescription());
                statement.setString(2, productDocument.getProductOverviewDescription());
                statement.setString(3, productDocument.getFullImageUrlName());
                statement.setString(4, productDocument.getThumbnailImageUrlName());
                statement.setString(5, productDocument.getMfrFullName());
                statement.setString(6, productDocument.getCategory1Name());
                statement.setString(7, productDocument.getCategory2Name());
                statement.setString(8, productDocument.getCategory3Name());
                statement.setString(9, productDocument.getUnspcId());
                statement.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
                statement.setString(11, productDocument.getVendorPartNbr());
                statement.setString(12, productDocument.getPartNumber());
                statement.addBatch();
                if ((counter + 1) % batchSize == 0 || (counter + 1) == productDocuments.size()) {
                    statement.executeBatch();
                    statement.clearBatch();
                }
                counter++;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void updateAllProductJdbcBatchCallable(List<ProductDocumentDto> productDocuments){
        log.info("update using jdbc batch, threading");
        ExecutorService executorService = Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
        List<List<ProductDocumentDto>> listOfBookSub = ListUtil.splitInto(productDocuments, batchSize, ProductDocumentDto.class);
        List<Callable<Void>> callables = listOfBookSub.stream().map(sublist ->
                (Callable<Void>) () -> {
                    updateAllProductJdbcBatch(sublist);
                    return null;
                }).collect(Collectors.toList());
        try {
            executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private void updateAllCatalogProductJdbcBatch(List<CustomersPriceProductDto> productPrices){
        log.info("update using jdbc batch to update catalog products");
        String sql = String.format(
                "UPDATE %s SET sell_price = ?, list_price = ?, uom = ?, last_pull_datetime = ?, sku_quantity = ? WHERE part_number = ?",
                CatalogProductDao.class.getAnnotation(Table.class).name()
        );
        try (Connection connection = hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ){
            int counter = 0;
            for (CustomersPriceProductDto productPrice : productPrices) {
                log.info("Updating Catalog Product "+productPrice.getProductId());
                statement.clearParameters();
                statement.setFloat(1, productPrice.getSellPrice());
                statement.setFloat(2, productPrice.getListPrice());
                statement.setString(3, productPrice.getUom());
                statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                statement.setInt(5, productPrice.getTotalAvailableQty());
                statement.setString(6, productPrice.getProductId());
                statement.addBatch();
                if ((counter + 1) % batchSize == 0 || (counter + 1) == productPrices.size()) {
                    statement.executeBatch();
                    statement.clearBatch();
                }
                counter++;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void updateAllCatalogProductJdbcBatchCallable(List<CustomersPriceProductDto> customerPrices){
        log.info("update using jdbc batch, threading");
        ExecutorService executorService = Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
        List<List<CustomersPriceProductDto>> listOfBookSub = ListUtil.splitInto(customerPrices, batchSize, CustomersPriceProductDto.class);
        List<Callable<Void>> callables = listOfBookSub.stream().map(sublist ->
                (Callable<Void>) () -> {
                    updateAllCatalogProductJdbcBatch(sublist);
                    return null;
                }).collect(Collectors.toList());
        try {
            executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}

