package com.reece.punchoutcustomersync.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.models.daos.AuditDao;
import com.reece.punchoutcustomerbff.models.daos.AuditErrorDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import com.reece.punchoutcustomerbff.models.repositories.AuditRepository;
import com.reece.punchoutcustomerbff.models.repositories.CatalogProductRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.models.repositories.ProductRepository;
import com.reece.punchoutcustomerbff.models.repositories.SyncLogRepository;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomerbff.util.SyncLogStatusUtil;
import com.reece.punchoutcustomerbff.util.TestUtils;
import com.reece.punchoutcustomersync.dto.AuditErrorDto;
import com.reece.punchoutcustomersync.dto.AuditInputDto;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {

  @Mock
  private SyncLogRepository syncLogRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private AuditRepository auditRepository;

  @Mock
  private DateGenerator dateGenerator;

  private OutputCsvService outputCsvService = new OutputCsvService();

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CatalogProductRepository catalogProductRepository;

  @Mock
  private Optional<SyncLogDao> optionalSync;

  @Mock
  private Optional<CustomerDao> optionalCustomer;

  @Captor
  private ArgumentCaptor<SyncLogDao> syncCaptor;

  @Captor
  private ArgumentCaptor<AuditDao> auditCaptor;

  @Captor
  private ArgumentCaptor<ProductDao> productCaptor;

  @Captor
  private ArgumentCaptor<CatalogProductDao> mappingCaptor;

  @InjectMocks
  private AuditService subject;


  @Test
  public void testAuditWhenNewSync() throws Exception {
    // given
    subject.outputCsvService = new OutputCsvService();
    AuditInputDto input = this.generateAuditInput();

    // and: mock the current time
    when(dateGenerator.generateTimestamp()).thenReturn(DateUtil.toTimestamp("2023-05-11T05:00:00.000+0000"));

    // and: there is no exiting sync record
    when(syncLogRepository.findById(UUID.fromString(input.getSyncId()))).thenReturn(optionalSync);
    when(optionalSync.isEmpty()).thenReturn(true);

    // and: there is a customer
    when(customerRepository.findById(UUID.fromString(input.getCustomerId()))).thenReturn(optionalCustomer);
    CustomerDao customer = TestUtils.generateCustomer();
    when(optionalCustomer.get()).thenReturn(customer);

    // and: we have no matching products
    when(productRepository.findByPartNumbers(List.of("TEST-PART-05"))).thenReturn(List.of());

    // and: mock a mapping that exists for this catalog
    CatalogProductDao m = CatalogProductDao.builder()
        .partNumber("TEST-PART-05")
        .build();
    List<CatalogProductDao> mappings = List.of(m);
    when(catalogProductRepository.findAllByCatalogId(
        UUID.fromString(input.getCatalogId()))).thenReturn(mappings);

    // when
    subject.audit(input);

    // then: verify the content of the sync log
    verify(syncLogRepository).save(syncCaptor.capture());
    SyncLogDao sync = syncCaptor.getValue();

    assertThat(DateUtil.fromDate(sync.getStartDatetime()), equalTo("2023-05-11T05:00:00.000+0000"));
    assertThat(DateUtil.fromDate(sync.getEndDatetime()), equalTo("2023-05-11T05:00:00.000+0000"));
    assertThat(sync.getStatus(), equalTo(SyncLogStatusUtil.COMPLETED));

    // and: verify audit record
    verify(auditRepository).save(auditCaptor.capture());
    AuditDao audit = auditCaptor.getValue();

    assertThat(audit.getSync(), equalTo(sync));
    assertThat(audit.getErrors().size(), equalTo(1));
    assertThat(audit.getCustomer(), equalTo(customer));
    assertThat(audit.getFtpLocation(), equalTo("/alpha/bravo.csv"));
    assertThat(DateUtil.fromDate(audit.getS3DateTime()), equalTo("2023-05-17T05:00:00.000+0000"));
    assertThat(audit.getS3Location(), equalTo("https://charlie.csv"));
    assertThat(audit.getFileName(), equalTo("delta.csv"));
    assertThat(DateUtil.fromDate(audit.getFtpDateTime()), equalTo("2023-05-18T05:00:00.000+0000"));

    AuditErrorDao error = (AuditErrorDao) audit.getErrors().toArray()[0];
    assertThat(DateUtil.fromDate(error.getErrorDateTime()), equalTo("2023-05-19T05:00:00.000+0000"));
    assertThat(error.getPartNumber(), equalTo("echo"));
    assertThat(error.getError(), equalTo("foxtrot"));

    // and: verify a new product was created
    verify(productRepository).save(productCaptor.capture());
    ProductDao product = productCaptor.getValue();
    assertThat(product.getPartNumber(), equalTo("TEST-PART-05"));
    assertThat(product.getDescription(), equalTo("Test Product 05 Description"));
    assertThat(product.getManufacturer(), equalTo("Test Manufacturer"));
    assertThat(product.getCategoryLevel1Name(), equalTo("Test Category 1"));
    assertThat(product.getCategoryLevel2Name(), equalTo("Test Category 2"));
    assertThat(product.getCategoryLevel3Name(), equalTo("Test Category 3"));
    assertThat(product.getDeliveryInDays(), equalTo(7));
    assertThat(product.getImageFullSize(), equalTo("https://www.reece.com/static/media/logo.18178d36.svg"));
    assertThat(product.getImageThumb(), equalTo("https://www.reece.com/static/media/logo.18178d36.svg"));
    assertThat(product.getManufacturerPartNumber(), equalTo("TEST-MFN-05"));
    assertThat(DateUtil.fromDate(product.getMaxSyncDatetime()), equalTo("2023-05-11T05:00:00.000+0000"));
    assertThat(product.getName(), equalTo("Test Product 05"));
    assertThat(product.getUnspsc(), equalTo("TEST-unspsc-05"));

    // and: verify the mapping was created
    verify(catalogProductRepository).save(mappingCaptor.capture());
    CatalogProductDao mapping = mappingCaptor.getValue();
    assertThat(mapping.getProduct().getPartNumber(), equalTo("TEST-PART-05"));
    assertThat(mapping.getPartNumber(), equalTo("TEST-PART-05"));
    assertThat(DateUtil.fromDate(mapping.getLastPullDatetime()), equalTo("2023-05-11T05:00:00.000+0000"));
    assertThat(mapping.getUom(), equalTo("FT"));
    assertThat(mapping.getListPrice().doubleValue(), equalTo(0.02D));
    assertThat(mapping.getSellPrice().doubleValue(), equalTo(0.01D));
  }

  private AuditInputDto generateAuditInput() throws Exception {
    String content = Files.readString(Paths.get("./src/test/resources/test-output-csv.csv"));
    String encoded = Base64.getEncoder().encodeToString(content.getBytes());

    AuditErrorDto error = AuditErrorDto.builder()
        .errorDateTime("2023-05-19T05:00:00.000+0000")
        .partNumber("echo")
        .error("foxtrot")
        .build();

    AuditInputDto input = AuditInputDto.builder()
        .ftpLocation("/alpha/bravo.csv")
        .s3DateTime("2023-05-17T05:00:00.000+0000")
        .s3Location("https://charlie.csv")
        .fileName("delta.csv")
        .customerId("b32a915d-cd68-4d24-997c-3cff04fe0162")
        .catalogId("21efa79c-c001-44db-b038-be9ccd449301")
        .ftpDateTime("2023-05-18T05:00:00.000+0000")
        .syncId("6bb71653-19ae-4424-978b-de9484683a68")
        .errors(List.of(error))
        .encodedOutputCSV(encoded)
        .build();
    return input;
  }

}
