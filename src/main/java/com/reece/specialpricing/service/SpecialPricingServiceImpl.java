package com.reece.specialpricing.service;

import com.csvreader.CsvWriter;
import com.reece.specialpricing.model.*;
import com.reece.specialpricing.model.exception.CSVUploadException;
import com.reece.specialpricing.model.pojo.SpecialPriceChangeRequest;
import com.reece.specialpricing.postgres.SpecialPrice;
import com.reece.specialpricing.postgres.SpecialPricingDataService;
import com.reece.specialpricing.repository.FileUploadDataService;
import com.reece.specialpricing.utilities.ExcludedPriceLineEnum;
import com.reece.specialpricing.utilities.PagingUtils;
import com.reece.specialpricing.utilities.SortingUtils;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.reece.specialpricing.utilities.StringUtils.removePrefix;

@Slf4j
@Service
public class SpecialPricingServiceImpl implements SpecialPricingService {

    @Autowired
    @Qualifier("smbUploadService")
    private FileUploadDataService fileUploadService;

    @Autowired
    private SpecialPricingDataService specialPricingDataService;

    public PagedSearchResults getPrices(String customerId, String productId, String priceLine, PaginationContext pagingData){
        List<SpecialPrice> queryResults;
        List<SpecialPrice> filteredResults;

        if(customerId != null && !customerId.isBlank()) {
            if(productId != null && !productId.isBlank()) {
                queryResults = specialPricingDataService.findByCustomerIdAndProductId(customerId, productId);
            } else {
                queryResults = specialPricingDataService.findByCustomerId(customerId);
            }
        } else {
            queryResults = specialPricingDataService.findByProductId(productId);
        }

        var excludedPriceLineQueryResults=queryResults.stream().filter(specialPrice ->!specialPrice.getPriceLine().contains(ExcludedPriceLineEnum.ICPCORP.label)
                && !specialPrice.getPriceLine().contains(ExcludedPriceLineEnum.OWCORN.label)
                && !specialPrice.getPriceLine().contains(ExcludedPriceLineEnum.REFRIG.label))
                .collect(Collectors.toList());

        // If priceLine is present, filter down results that match the priceLine
        if (priceLine != null && !priceLine.isBlank()) {
            filteredResults = excludedPriceLineQueryResults.stream().filter(price -> Objects.equals(price.getPriceLine(), priceLine)).collect(Collectors.toList());
        } else {
            filteredResults = excludedPriceLineQueryResults;
        }

        var sortedResults = SortingUtils.dynamicSort(
                filteredResults
                        .stream()
                        .map(r -> (DynamicSortable) r).collect(Collectors.toList()),
                pagingData.getOrderDirection(),
                pagingData.getOrderBy()
        );

        return PagingUtils.getResultPage(pagingData, sortedResults);
    }

    /**
     * Fetch all Price Lines for a query to be used in a drop down to filter results
     * @param customerId
     * @param productId
     * @return
     */
    public Set<String> getPriceLines(String customerId, String productId) {
        List<SpecialPrice> queryResults;
        if(customerId != null && !customerId.isBlank()) {
            if(productId != null && !productId.isBlank()) {
                queryResults = specialPricingDataService.findByCustomerIdAndProductId(customerId, productId);
            } else {
                queryResults = specialPricingDataService.findByCustomerId(customerId);
            }
        } else {
            queryResults = specialPricingDataService.findByProductId(productId);
        }

        return getDistinctPriceLines(queryResults);
    }


    public UploadPriceChangesResult createAndUpdatePrices(@Valid SpecialPriceChangeRequest specialPriceChangeRequest) throws CSVUploadException {
        var failedUpdates = new ArrayList<SpecialPriceSuggestion>();
        var failedCreates = new ArrayList<SpecialPriceSuggestion>();
        List<SpecialPriceSuggestion> changes = specialPriceChangeRequest.getPriceChangeSuggestions();
        List<SpecialPriceSuggestion> createdPrices = specialPriceChangeRequest.getPriceCreateSuggestions();

        List<String> successfulPriceUpdateUploads = getPriceUpdateUploads(failedUpdates, changes);
        List<String> successfulPriceCreateUploads = getPriceCreateUploads(failedCreates, createdPrices);

        if((successfulPriceUpdateUploads.isEmpty() && !changes.isEmpty()) &&
                (successfulPriceCreateUploads.isEmpty() && !createdPrices.isEmpty())){
            throw new CSVUploadException("At least one CSV should have been generated, but it failed. Please refer to the logs. No CSVs were uploaded.");
        } else if (successfulPriceCreateUploads.isEmpty() && !createdPrices.isEmpty()){
            throw new CSVUploadException("At least one Create CSV should have been generated, but it failed. Please refer to the logs. No Create CSVs were uploaded.");
        } else if(successfulPriceUpdateUploads.isEmpty() && !changes.isEmpty()){
            throw new CSVUploadException("At least one Update CSV should have been generated, but it failed. Please refer to the logs. No Update CSVs were uploaded.");
        }
        return new UploadPriceChangesResult(successfulPriceUpdateUploads, successfulPriceCreateUploads,failedUpdates, failedCreates);
    }

    private List<String> getPriceUpdateUploads(ArrayList<SpecialPriceSuggestion> failedUpdates, List<SpecialPriceSuggestion> changes) {
        var successfulPriceUpdateUploads = changes
            .stream()
            .collect(Collectors.groupingBy(c -> Pair.with(c.getCustomerId(), c.getBranch())))
            .entrySet()
            .stream()
            .map(groupedChanges ->
                {
                    var customerId = groupedChanges.getKey().getValue0();
                    var branch = groupedChanges.getKey().getValue1();
                    var changeSetFileName =
                            String.format("Amend - %s - %s - %d.csv", removePrefix(customerId, "MSC-"), removePrefix(branch, "MSC-"), Instant.now().toEpochMilli());

                    try {
                        var csvBytes = generateChangeCsvBytes(groupedChanges.getValue(), false);

                        var uploadedFilePath = fileUploadService.uploadFile(csvBytes, changeSetFileName);

                        if(uploadedFilePath == null){
                            failedUpdates.addAll(groupedChanges.getValue());
                            return null;
                        }
                        return uploadedFilePath;
                    } catch (Exception e) {
                        log.error("Upload", e);
                        failedUpdates.addAll(groupedChanges.getValue());
                        return null;
                    }
                }
            )
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return successfulPriceUpdateUploads;
    }

    private List<String> getPriceCreateUploads(ArrayList<SpecialPriceSuggestion> failedCreatedPrices, List<SpecialPriceSuggestion> createdPrices) {
        var successfulPriceCreateUploads = createdPrices
                .stream()
                .collect(Collectors.groupingBy(c -> Pair.with(c.getCustomerId(), c.getBranch())))
                .entrySet()
                .stream()
                .map(groupedCreates ->
                        {
                            var customerId = groupedCreates.getKey().getValue0();
                            var branch = groupedCreates.getKey().getValue1();
                            var createSetFileName =
                                    String.format("Create - %s - %s - %d.csv", removePrefix(customerId, "MSC-"), removePrefix(branch, "MSC-"), Instant.now().toEpochMilli());

                            try {
                                var csvBytes = generateChangeCsvBytes(groupedCreates.getValue(), true);

                                var uploadedFilePath = fileUploadService.uploadFile(csvBytes, createSetFileName);

                                if(uploadedFilePath == null){
                                    failedCreatedPrices.addAll(groupedCreates.getValue());
                                    return null;
                                }
                                return uploadedFilePath;
                            } catch (Exception e) {
                                log.error("Create", e);
                                failedCreatedPrices.addAll(groupedCreates.getValue());
                                return null;
                            }
                        }
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return successfulPriceCreateUploads;
    }

    private byte[] generateChangeCsvBytes(List<SpecialPriceSuggestion> changes, Boolean isCreate) throws IOException {
        var outputByteStream = new ByteArrayOutputStream();
        CsvWriter changeWriter = null;
        try{
            changeWriter = new CsvWriter(new BufferedOutputStream(outputByteStream), ',', StandardCharsets.UTF_8);
            changeWriter.writeRecord(SpecialPriceSuggestion.getCsvHeaders(isCreate));

            for (SpecialPriceSuggestion change : changes) {
                changeWriter.writeRecord(change.toCsvRow(isCreate));
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(changeWriter!=null) {
                changeWriter.close();
            }if(outputByteStream!=null){
                outputByteStream.close();
            }
        }
        return outputByteStream.toByteArray();
    }

    public Set<String> getDistinctPriceLines(final List<SpecialPrice> specialPrices) {
        Set<String> priceLines = new HashSet<>();
        for(final SpecialPrice specialPrice: specialPrices) {
            priceLines.add(specialPrice.getPriceLine());
        }
        //Excluding Pricelines From Displaying to Front End
        priceLines.remove(ExcludedPriceLineEnum.ICPCORP.label);
        priceLines.remove(ExcludedPriceLineEnum.REFRIG.label);
        priceLines.remove(ExcludedPriceLineEnum.OWCORN.label);

        
        return priceLines.stream().sorted(String::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
