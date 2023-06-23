package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.models.daos.*;
import com.reece.punchoutcustomerbff.models.repositories.*;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import com.reece.punchoutcustomerbff.util.TestUtils;
import com.reece.punchoutcustomersync.dto.AuditInputDto;
import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceProductDto;
import com.reece.punchoutcustomersync.dto.max.ProductDocumentDto;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {
    @Mock
    private HikariDataSource hikariDataSource;

    @Mock
    private KourierService kourierService;

    @Mock
    private ElasticSearchService maxElasticSearchService;

    @Mock
    private CatalogProductRepository catalogProductRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private SyncLogRepository syncLogRepository;

    @Mock
    private SftpDataService sftpDataService;

    @Mock
    private AuditService auditService;

    @Mock
    private DateGenerator dateGenerator;

    @Mock
    private OutputCsvService outputCsvService;

    @InjectMocks
    private PricingService subject;

    @Captor
    private ArgumentCaptor<AuditInputDto> auditInputCaptor;

    @BeforeClass
    public void setup() {
        when(dateGenerator.generateTimestamp()).thenReturn(new Timestamp(new Date().getTime()));
    }

    @Test
    public void givenCustomerWithNoProductsWhenSyncCustomerProductCatalogsCalledThenNoProductsSaved() throws Exception {
        SyncLogDao syncLog = TestUtils.generateSyncLog();
        CustomerDao customer = TestUtils.generateCustomer();

        when(customerRepository.getOne(any(UUID.class))).thenReturn(customer);
        when(syncLogRepository.getOne(any(UUID.class))).thenReturn(syncLog);

        subject.syncCustomerProductCatalogs(1, customer.getId(), 1, syncLog.getId());

        verify(productRepository, times(0)).save(any(ProductDao.class));
        verify(catalogProductRepository, times(0)).save(any(CatalogProductDao.class));
    }

    @Test
    public void givenCustomerWithProductsAndCorrespondingMAXOrKourierRecordsWhenSyncCustomerProductCatalogsCalledThenProductsSaved() throws Exception {
        AuditDao audit = TestUtils.generateAudit();
        SyncLogDao syncLog = TestUtils.generateSyncLog();
        CustomerDao customer = TestUtils.generateCustomer();
        CatalogDao catalog = TestUtils.generateCatalogWithMappings();
        ProductDao product = TestUtils.generateProduct();
        catalog.getMappings().stream().findFirst().get().setProduct(product);
        customer.setCatalogs(Set.of(catalog));
        CustomersPriceProductDto productPrice = TestUtils.generateProductPrice();
        productPrice.setProductId(catalog.getMappings().stream().findFirst().get().getPartNumber());
        ProductDocumentDto productDocument = TestUtils.generateProductDocument();
        productDocument.setId("MSC-"+catalog.getMappings().stream().findFirst().get().getPartNumber());

        when(syncLogRepository.getOne(any(UUID.class))).thenReturn(syncLog);
        when(customerRepository.getOne(any(UUID.class))).thenReturn(customer);
        when(catalogProductRepository.findAllByCatalogIdWithLastPullDateBefore(any(UUID.class), any(Timestamp.class)))
                .thenReturn(catalog.getMappings().stream().collect(Collectors.toList()));
        when(maxElasticSearchService.getProductDocumentsForCustomer(any(CustomerDao.class), anyList())).thenReturn(Arrays.asList(productDocument));
        when(kourierService.getCustomerProductsPricing(any(CustomerDao.class), anyList())).thenReturn(Arrays.asList(productPrice));
        when(hikariDataSource.getMaximumPoolSize()).thenReturn(1);
        when(sftpDataService.uploadCsv(any(InputStream.class), any(String.class))).thenReturn("/test/file/path.csv");
        when(auditService.generateAudit(any(SyncLogDao.class), any(CustomerDao.class), any(AuditInputDto.class))).thenReturn(audit);
        when(outputCsvService.createCsv(anyList())).thenReturn(new ByteArrayInputStream(new byte[0]));

        subject.syncCustomerProductCatalogs(1, customer.getId(), 1, syncLog.getId());

        verify(hikariDataSource, times(4)).getConnection();
        verify(auditRepository, times(1)).save(audit);
    }

    @Test
    public void givenCustomerWithProductsAndNoCorrespondingMAXOrKourierRecordsWhenSyncCustomerProductCatalogsCalledThenProductsSaved() throws Exception {
        SyncLogDao syncLog = TestUtils.generateSyncLog();
        CustomerDao customer = TestUtils.generateCustomer();
        CatalogDao catalog = TestUtils.generateCatalogWithMappings();
        ProductDao product = TestUtils.generateProduct();
        catalog.getMappings().stream().findFirst().get().setProduct(product);
        customer.setCatalogs(Set.of(catalog));

        when(syncLogRepository.getOne(any(UUID.class))).thenReturn(syncLog);
        when(customerRepository.getOne(any(UUID.class))).thenReturn(customer);
        when(catalogProductRepository.findAllByCatalogIdWithLastPullDateBefore(any(UUID.class), any(Timestamp.class)))
                .thenReturn(catalog.getMappings().stream().collect(Collectors.toList()));
        when(maxElasticSearchService.getProductDocumentsForCustomer(any(CustomerDao.class), anyList())).thenReturn(new ArrayList<>());
        when(kourierService.getCustomerProductsPricing(any(CustomerDao.class), anyList())).thenReturn(new ArrayList<>());
        when(sftpDataService.uploadCsv(any(InputStream.class), any(String.class))).thenReturn("/test/file/path.csv");
        when(outputCsvService.createCsv(anyList())).thenReturn(new ByteArrayInputStream(new byte[0]));

        subject.syncCustomerProductCatalogs(1, customer.getId(), 1, syncLog.getId());

        verify(hikariDataSource, times(0)).getConnection();
        verify(auditRepository, times(0)).save(any(AuditDao.class));
        verify(auditService, times(1)).generateAudit(eq(syncLog), eq(customer), auditInputCaptor.capture());
        assertThat(auditInputCaptor.getValue().getErrors(), notNullValue());
        assertThat(auditInputCaptor.getValue().getErrors().size(), equalTo(2));
    }
}
