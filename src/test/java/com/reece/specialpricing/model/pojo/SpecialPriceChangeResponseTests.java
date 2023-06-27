package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.model.UploadPriceChangesResult;
import com.reece.specialpricing.utilities.TestCommon;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class SpecialPriceChangeResponseTests extends TestCommon {
    @Test
    public void constructor_shouldPullFileNameFromPath(){
        var successfulUpload = "/file/a/b.csv";
        var results = new UploadPriceChangesResult(List.of(successfulUpload), Collections.emptyList(),List.of(validSuggestion), List.of(validSuggestion));
        var changeResponse = new SpecialPriceChangeResponse(results);
        assert changeResponse.getSuccessfulUploads().size() == 1;
        assert changeResponse.getSuccessfulUploads().get(0).getUploadedName().equals("b.csv");
        assert changeResponse.getSuccessfulUploads().get(0).getUploadedPath().equals(successfulUpload);
        assert changeResponse.getFailedUpdateSuggestions().size() == 1;
        assert changeResponse.getFailedUpdateSuggestions().get(0).equals(validSuggestion);
    }
}
