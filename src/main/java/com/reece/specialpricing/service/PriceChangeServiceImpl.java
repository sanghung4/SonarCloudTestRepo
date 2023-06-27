package com.reece.specialpricing.service;

import com.csvreader.CsvWriter;
import com.reece.specialpricing.model.SpecialPriceSuggestion;
import com.reece.specialpricing.model.UploadPriceChangesResult;
import com.reece.specialpricing.model.exception.CSVUploadException;
import com.reece.specialpricing.repository.FileUploadDataService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PriceChangeServiceImpl implements PriceChangeService {

    @Autowired
    @Qualifier("localUploadService")
    private FileUploadDataService fileUploadService;

    @Override
    public UploadPriceChangesResult uploadPriceChanges(List<SpecialPriceSuggestion> changes) throws CSVUploadException {
        var failedUpdates = new ArrayList<SpecialPriceSuggestion>();
        var successfulUploads = changes
            .stream()
            .collect(Collectors.groupingBy(c -> Pair.with(c.getCustomerId(), c.getBranch())))
            .entrySet()
            .stream()
            .map(groupedChanges ->
                {
                    var customerId = groupedChanges.getKey().getValue0();
                    var branch = groupedChanges.getKey().getValue1();
                    var changeSetFileName =
                            String.format("Amend - %s - %s - %d.csv", customerId, branch, Instant.now().toEpochMilli());

                    try {
                        var csvBytes = generateSpecialPriceCsvBytes(groupedChanges.getValue(), false);

                        var uploadedFilePath = fileUploadService.uploadFile(csvBytes, changeSetFileName);

                        if(uploadedFilePath == null){
                            failedUpdates.addAll(groupedChanges.getValue());
                            return null;
                        }
                        return uploadedFilePath;
                    } catch (Exception e) {
                        failedUpdates.addAll(groupedChanges.getValue());
                        return null;
                    }
                }
            )
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if(successfulUploads.isEmpty() && !changes.isEmpty()){
            throw new CSVUploadException("At least one CSV should have been generated, but it failed.  Please refer to the logs to identify why.  No CSVs were uploaded.");
        }
        return new UploadPriceChangesResult(successfulUploads, null,failedUpdates, null);

    }

    private byte[] generateSpecialPriceCsvBytes(List<SpecialPriceSuggestion> changes, boolean isCreate) throws IOException {
        var outputByteStream = new ByteArrayOutputStream();
        CsvWriter changeWriter = null;
        try{
            changeWriter = new CsvWriter(new BufferedOutputStream(outputByteStream), ',', StandardCharsets.UTF_8);
            changeWriter.writeRecord(SpecialPriceSuggestion.getCsvHeaders(isCreate));

            for (SpecialPriceSuggestion change : changes) {
                changeWriter.writeRecord(change.toCsvRow(isCreate));
            }
        } catch(IOException e){
            e.printStackTrace();
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
}
