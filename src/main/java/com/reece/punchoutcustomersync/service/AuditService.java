package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.mapper.AuditMapper;
import com.reece.punchoutcustomerbff.mapper.CatalogProductMapper;
import com.reece.punchoutcustomerbff.mapper.SyncLogMapper;
import com.reece.punchoutcustomerbff.models.daos.AuditDao;
import com.reece.punchoutcustomerbff.models.daos.AuditErrorDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import com.reece.punchoutcustomerbff.models.repositories.AuditErrorRepository;
import com.reece.punchoutcustomerbff.models.repositories.AuditRepository;
import com.reece.punchoutcustomerbff.models.repositories.CatalogProductRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.models.repositories.ProductRepository;
import com.reece.punchoutcustomerbff.models.repositories.SyncLogRepository;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomerbff.util.ListUtil;
import com.reece.punchoutcustomerbff.util.SyncLogStatusUtil;
import com.reece.punchoutcustomersync.dto.AuditErrorDto;
import com.reece.punchoutcustomersync.dto.AuditInputDto;
import com.reece.punchoutcustomersync.dto.AuditOutputDto;
import com.reece.punchoutcustomersync.dto.OutputCsvRecord;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Used for general auditing related activities.
 * @author john.valentino
 */
@Service
@Slf4j
public class AuditService {

  @Autowired
  private SyncLogRepository syncLogRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private AuditRepository auditRepository;

  @Autowired
  private DateGenerator dateGenerator;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CatalogProductRepository catalogProductRepository;

  @Autowired
  protected OutputCsvService outputCsvService;

  public AuditOutputDto audit(AuditInputDto input) {
    AuditOutputDto result = new AuditOutputDto();

    Timestamp currentTimestamp = dateGenerator.generateTimestamp();

    // attempt to find the sync record, and create or update it
    Optional<SyncLogDao> optional = syncLogRepository.findById(UUID.fromString(input.getSyncId()));
    SyncLogDao sync = this.generateSync(optional, currentTimestamp, input);
    syncLogRepository.save(sync);
    result.setSync(SyncLogMapper.toDTO(sync));

    // lookup the customer, and create a new audit record for it with errors if needed
    CustomerDao customer = customerRepository.findById(UUID.fromString(input.getCustomerId())).get();

    // create a new audit record
    AuditDao audit = this.generateAudit(sync, customer, input);
    auditRepository.save(audit);
    result.setAudit(AuditMapper.toDTO(audit));

    // now handle making updates based on the given CSV records
    List<OutputCsvRecord> outputCsvRecords = outputCsvService.parse(input.getEncodedOutputCSV());
    result.setOutputCsvRecords(outputCsvRecords);

    // now we need to process all those products by part number, and for the current customer mappings
    List<CatalogProductDao> mappings = this.processProductUpdates(outputCsvRecords,
        currentTimestamp, input.getCatalogId());
    result.setMappings(CatalogProductMapper.toDTOs(mappings));

    return result;
  }

  protected SyncLogDao generateSync(Optional<SyncLogDao> optional, Timestamp currentTimestamp,
                                    AuditInputDto input) {
    SyncLogDao sync = null;
    if (optional.isEmpty()) {
      sync = SyncLogDao.builder()
          .id(UUID.fromString(input.getSyncId()))
          .startDatetime(currentTimestamp)
          .endDatetime(currentTimestamp)
          .status(SyncLogStatusUtil.COMPLETED)
          .build();
    } else {
      sync = optional.get();
      sync.setEndDatetime(currentTimestamp);
    }
    return sync;
  }

  public AuditDao generateAudit(SyncLogDao sync, CustomerDao customer, AuditInputDto input) {
    // create a new audit record
    AuditDao audit = AuditDao.builder()
        .id(UUID.randomUUID())
        .sync(sync)
        .customer(customer)
        .s3Location(input.getS3Location())
        .ftpLocation(input.getFtpLocation())
        .errors(new HashSet<>())
        .fileName(input.getFileName()).build();

    if (input.getFtpDateTime() != null) {
      audit.setFtpDateTime(DateUtil.toTimestamp(input.getFtpDateTime()));
      audit.setUploadDatetime(DateUtil.toTimestamp(input.getFtpDateTime()));
    }

    if (input.getS3DateTime() != null) {
      audit.setS3DateTime(DateUtil.toTimestamp(input.getS3DateTime()));
    }

    // create records for any errors...
    for (AuditErrorDto inputError : input.getErrors()) {
      AuditErrorDao error = AuditErrorDao.builder()
          .audit(audit)
          .errorDateTime(DateUtil.toTimestamp(inputError.getErrorDateTime()))
          .partNumber(inputError.getPartNumber())
          .id(UUID.randomUUID())
          .error(inputError.getError())
          .build();
      audit.getErrors().add(error);
    }
    return audit;
  }

  protected List<CatalogProductDao> processProductUpdates(List<OutputCsvRecord> outputCsvRecords,
                                       Timestamp currentTimestamp, String catalogId) {
    // pull out all the part numbers, and have a map to output records
    List<String> partNumbers = new ArrayList<>();
    Map<String, OutputCsvRecord> partNumberToRecord = new LinkedHashMap<>();

    for (OutputCsvRecord record : outputCsvRecords) {
      partNumbers.add(record.getPartNumber());
      partNumberToRecord.put(record.getPartNumber(), record);
    }

    // split the part numbers into groups of 100
    List<List<String>> listsOfPartNumbers = ListUtil.splitInto(partNumbers, 100, String.class);

    // find all the matching products by part number
    Map<String, ProductDao> partNumberToProduct = new LinkedHashMap<>();
    for (List<String> subset : listsOfPartNumbers) {
      List<ProductDao> products = productRepository.findByPartNumbers(subset);
      for (ProductDao product : products) {
        partNumberToProduct.put(product.getPartNumber(), product);
      }
    }

    // create or update all the products by part number
    for (String partNumber : partNumbers) {
      OutputCsvRecord record = partNumberToRecord.get(partNumber);
      ProductDao product = partNumberToProduct.get(partNumber);

      if (product == null) {
        product = ProductDao.builder()
            .id(UUID.randomUUID())
            .partNumber(partNumber)
            .build();
        partNumberToProduct.put(partNumber, product);
      }

      product.setCategoryLevel1Name(record.getCategoryLevel1Name());
      product.setCategoryLevel2Name(record.getCategoryLevel2Name());
      product.setCategoryLevel3Name(record.getCategoryLevel3Name());
      product.setDescription(record.getProductDescription());
      product.setDeliveryInDays(record.getDeliveryInDays());
      product.setImageFullSize(record.getImageFullsize());
      product.setImageThumb(record.getImageThumb());
      product.setManufacturer(record.getManufacturer());
      product.setManufacturerPartNumber(record.getManufacturerPartNumber());
      product.setName(record.getProductName());
      product.setUnspsc(record.getUnspsc());
      product.setMaxSyncDatetime(currentTimestamp);

      productRepository.save(product);
    }

    // now we have to pull catalog mappings and update them as well as associate products
    List<CatalogProductDao> mappings = catalogProductRepository.findAllByCatalogId(
        UUID.fromString(catalogId));
    for (CatalogProductDao mapping : mappings) {
      String partNumber = mapping.getPartNumber();
      OutputCsvRecord record = partNumberToRecord.get(partNumber);

      if (record == null) {
        log.warn("mapping " + mapping.getId().toString() + " uses part number "
            + partNumber + ", but that part number was not in the Output CSV");
        continue;
      }

      ProductDao product = partNumberToProduct.get(partNumber);

      if (product == null) {
        log.warn("mapping " + mapping.getId().toString() + " uses part number "
            + partNumber + ", but that part number does not have a Product");
        continue;
      }

      mapping.setProduct(product);
      mapping.setUom(record.getUnitOfMeasure());
      mapping.setListPrice(record.getListPrice());
      mapping.setLastPullDatetime(currentTimestamp);
      mapping.setSellPrice(record.getProductPrice());
      // mapping.setSkuQuantity(); does not exist yet
      catalogProductRepository.save(mapping);
    }

    return mappings;
  }

}
