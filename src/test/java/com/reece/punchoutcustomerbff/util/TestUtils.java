package com.reece.punchoutcustomerbff.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.models.daos.*;
import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceProductDto;
import com.reece.punchoutcustomersync.dto.max.ProductDocumentDto;
import org.mockito.Mockito;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class TestUtils {

  public static final CustomerDao generateCustomer() {
    CustomerRegionDao region = CustomerRegionDao.builder()
        .id(UUID.randomUUID())
        .name("hotel")
        .build();

    CustomerDao input = CustomerDao.builder()
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

    return input;
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
    CatalogDao output = CatalogDao.builder()
        .dateArchived(null)
        .fileName("customer-file-name-alpha")
        .status(CatalogStatusUtil.DRAFT)
        .procSystem(ProcurementSystemUtil.GREENWING)
        .name("customer-name-alpha")
        .lastUpdate(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
        .id(UUID.fromString("d6925283-d174-4b7e-b9f2-3c7f5d60ca53"))
        .build();

    return output;
  }

  public static void assertDefaultCatalogDto(CatalogDto input) {
    assertThat(input.getDateArchived(), equalTo(null));
    assertThat(input.getFileName(), equalTo("customer-file-name-alpha"));
    assertThat(input.getStatus(), equalTo(CatalogStatusUtil.DRAFT));
    assertThat(input.getProcSystem(), equalTo(ProcurementSystemUtil.GREENWING));
    assertThat(input.getName(), equalTo("customer-name-alpha"));
    assertThat(input.getLastUpdate(), equalTo("2023-05-17T05:00:00.000+0000"));
    assertThat(input.getId().toString(), equalTo("d6925283-d174-4b7e-b9f2-3c7f5d60ca53"));
  }

  public static CatalogDao generateCatalogWithMappings() {
    CatalogDao catalog = TestUtils.generateCatalog();
    catalog.setMappings(Set.of(TestUtils.generateMapping()));
    return catalog;
  }

  public static CatalogProductDao generateMapping() {
    CatalogProductDao output = CatalogProductDao.builder()
        .uom("uom-alpha")
        .partNumber("part-number-alpha")
        .listPrice(new BigDecimal(0))
        .sellPrice(new BigDecimal(1))
        .lastPullDatetime(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
        .id(UUID.fromString("5e79df4c-56a8-47ed-93f5-125749c891f6"))
        .build();
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

  public static SyncLogDao generateSyncLog() {
    SyncLogDao syncLog = SyncLogDao.builder()
            .id(UUID.randomUUID())
            .startDatetime(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
            .status("STARTED")
            .build();
    return syncLog;
  }

  public static ProductDocumentDto generateProductDocument() {
    ProductDocumentDto productDocument = ProductDocumentDto.builder()
            .productOverviewDescription("Product Name")
            .build();
    return productDocument;
  }

  public static CustomersPriceProductDto generateProductPrice() {
    CustomersPriceProductDto productPrice = CustomersPriceProductDto.builder()
            .sellPrice(10.1f)
            .build();
    return productPrice;
  }

  public static ProductDao generateProduct() {
    ProductDao productPrice = ProductDao.builder()
            .build();
    return productPrice;
  }

  public static AuditDao generateAudit() {
    AuditDao audit = AuditDao.builder()
            .build();
    return audit;
  }
}
