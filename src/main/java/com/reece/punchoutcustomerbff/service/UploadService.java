package com.reece.punchoutcustomerbff.service;

import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import com.reece.punchoutcustomerbff.dto.InputCsvRecord;
import com.reece.punchoutcustomerbff.dto.ProductDto;
import com.reece.punchoutcustomerbff.dto.UploadInputDto;
import com.reece.punchoutcustomerbff.dto.UploadOutputDto;
import com.reece.punchoutcustomerbff.mapper.ProductMapper;
import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.UploadDao;
import com.reece.punchoutcustomerbff.models.dtos.ProductToCsvRecordDto;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.models.repositories.ProductRepository;
import com.reece.punchoutcustomerbff.models.repositories.UploadRepository;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * General service used for dealing with uploads.
 * @author john.valentino
 */
@Service
@Slf4j
public class UploadService {

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private DateGenerator dateGenerator;

    /**
     * Deletes the upload record of the given ID.
     * @param uploadId The Upload ID in the database.
     */
    public void deleteUpload(String uploadId) {
        uploadRepository.deleteById(UUID.fromString(uploadId));
    }

    /**
     * Used for uploading a CSV file, which results in a single record in the upload table,
     * but that also parses the CSV file and returns it in Product Format.
     * @param input The input payload.
     * @param customerId The ID of the customer in the database.
     * @return UploadOutputDto A product-representation of the CSV file content.
     */
    public UploadOutputDto upload(UploadInputDto input, String customerId) {
        UploadOutputDto output = new UploadOutputDto();

        // process the CSV
        byte[] decodedBytes = Base64.getDecoder().decode(input.getEncoded());
        String decodedString = new String(decodedBytes);
        List<InputCsvRecord> records = this.processInputCsv(decodedString);

        // TODO: assert that there is only one customer

        // Lookup the customer
        CustomerDao customer = customerRepository.findById(UUID.fromString(customerId)).get();

        // figure out all the products
        List<ProductToCsvRecordDto> productToRecord = this.generateProductMap(records);

        // split then up into lists of 100 each
        List<List<ProductToCsvRecordDto>> productsToLookup = this.splitInto(productToRecord, 100);

        List<String> invalidPartNumberLines = new ArrayList<>();

        // lookup each product in groups of 100
        for (List<ProductToCsvRecordDto> productToCsvRecords : productsToLookup) {
            List<String> batchInvalidPartNumbers = findInvalidPartNumberLines(productToCsvRecords);
            List<String> list = productToCsvRecords
                .stream()
                .map(i -> i.getCatalogProduct().getPartNumber())
                .collect(Collectors.toList());
            invalidPartNumberLines.addAll(batchInvalidPartNumbers);
            list.removeAll(batchInvalidPartNumbers);

            List<ProductDao> products = productRepository.findByPartNumbers(list);

            // for each found product, add it to the current mapping
            for (ProductDao product : products) {
                ProductDto outputProduct = ProductMapper.toDTO(product);
                Optional<CatalogProductDto> mapping = productToCsvRecords
                    .stream()
                    .filter(i -> i.getCatalogProduct().getPartNumber() == product.getPartNumber())
                    .map(i -> i.getCatalogProduct())
                    .findFirst();
                if (!mapping.isEmpty()) {
                    mapping.get().setProduct(outputProduct);
                }
            }

            for (String partNumber : list) {
                Optional<CatalogProductDto> mapping = productToCsvRecords
                    .stream()
                    .filter(i -> i.getCatalogProduct().getPartNumber() == partNumber)
                    .map(i -> i.getCatalogProduct())
                    .findFirst();
                if (!mapping.isEmpty()) {
                    output.getProducts().add(mapping.get());
                }
            }
        }

        // create an upload record
        AuthorizedUserDao user = securityService.currentLoggedInUser();
        UploadDao upload = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .uploadDatetime(dateGenerator.generateTimestamp())
            .customer(customer)
            .fileName(input.getFileName())
            .content(decodedString)
            .user(user)
            .build();
        uploadRepository.save(upload);

        output.setUploadId(upload.getId().toString());

        if (!invalidPartNumberLines.isEmpty()) {
            output.setError(
                String.format("Line(s) %s failed to upload, invalid Part #", String.join(",", invalidPartNumberLines))
            );
        }

        return output;
    }

    private List<String> findInvalidPartNumberLines(List<ProductToCsvRecordDto> list) {
        // TODO: Add real validation logic here.
        // Right now we are only validating that each partNumber is < 200 characters long.
        return list
            .stream()
            .filter(i -> i.getInputCsvRecord().getProductId().length() > 200)
            .map(i -> String.valueOf(i.getInputCsvRecord().getLineNumber()))
            .collect(Collectors.toList());
    }

    public List<InputCsvRecord> processInputCsv(String content) {
        List<InputCsvRecord> results = new ArrayList<>();

        String[] headers = { "Product ID", "Branch", "Customer ID" };
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(headers).setSkipHeaderRecord(true).build();

        Reader in = new StringReader(content);

        Iterable<CSVRecord> records = new ArrayList<>();

        try {
            records = csvFormat.parse(in);
        } catch (IOException e) {
            log.error("Unable to process the following CSV:\n" + content, e);
        }

        for (CSVRecord record : records) {
            InputCsvRecord output = InputCsvRecord
                .builder()
                .productId(record.get(headers[0]))
                .branch(record.get(headers[1]))
                .customerId(record.get(headers[2]))
                .lineNumber(record.getRecordNumber())
                .build();
            results.add(output);
        }

        return results;
    }

    public List<ProductToCsvRecordDto> generateProductMap(List<InputCsvRecord> inputs) {
        List<ProductToCsvRecordDto> outputs = new ArrayList<>();

        for (int i = 0; i < inputs.size(); i++) {
            InputCsvRecord input = inputs.get(i);
            CatalogProductDto output = CatalogProductDto.builder().partNumber(input.getProductId()).build();
            ProductToCsvRecordDto productToCsvRecord = ProductToCsvRecordDto
                .builder()
                .inputCsvRecord(input)
                .catalogProduct(output)
                .build();
            outputs.add(productToCsvRecord);
        }

        return outputs;
    }

    public List<List<ProductToCsvRecordDto>> splitInto(List<ProductToCsvRecordDto> inputs, int size) {
        List<List<ProductToCsvRecordDto>> outputs = new ArrayList<>();

        List<ProductToCsvRecordDto> currentList = new ArrayList<>();
        outputs.add(currentList);

        for (ProductToCsvRecordDto input : inputs) {
            if (currentList.size() == size) {
                currentList = new ArrayList<>();
                outputs.add(currentList);
            }

            currentList.add(input);
        }

        return outputs;
    }
}
