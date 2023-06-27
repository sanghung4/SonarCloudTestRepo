package com.reece.specialpricing.model;

import com.reece.specialpricing.utilities.DateTimeUtils;
import com.reece.specialpricing.utilities.TestCommon;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

public class SpecialPriceChangeSuggestionTests extends TestCommon {
    @Test
    public void toCsvRow_shouldOrderValuesAccordingToContract(){
        var csvRow = validSuggestion.toCsvRow(false);
        assert csvRow[0].equals(validSuggestion.getCustomerId());
        assert csvRow[1].equals(validSuggestion.getChangeWriterDisplayName());
        assert csvRow[2].equals(validSuggestion.getChangeWriterId());
        assert csvRow[3].equals(validSuggestion.getBranch());
        assert csvRow[4].equals(validSuggestion.getTerritory());
        assert csvRow[5].equals(validSuggestion.getProductId());
        assert csvRow[6].equals("1000.00");
        assert csvRow[7].equals(validSuggestion.getPriceCategory());
        assert csvRow[8].equals("100.00");
        assert csvRow[9].equals(DateTimeUtils.getCentralTimeZoneDateStringFromInstant(Instant.now()));
        assert csvRow[10].equals(DateTimeUtils.getCentralTimeZoneDateStringFromInstant(Instant.now().minus(Duration.ofDays(1))));
    }

    @Test
    public void getCsvHeaders_shouldOrderHeadersAccordingToUpdateContract(){
        var csvHeaders = SpecialPriceSuggestion.getCsvHeaders(false);
        assert csvHeaders[0].equals("CUSTOMER ID (Bill To)");
        assert csvHeaders[1].equals("WRITER NAME");
        assert csvHeaders[2].equals("WRITER ID");
        assert csvHeaders[3].equals("BRANCH");
        assert csvHeaders[4].equals("TERRITORY");
        assert csvHeaders[5].equals("PRODUCT ID");
        assert csvHeaders[6].equals("CMP PRICE");
        assert csvHeaders[7].equals("PRICE CATEGORY");
        assert csvHeaders[8].equals("NEW PRICE");
        assert csvHeaders[9].equals("DATE CREATED");
        assert csvHeaders[10].equals("EXPIRATION DATE");
    }

    @Test
    public void getCsvHeaders_shouldOrderHeadersAccordingToCreateContract(){
        var csvHeaders = SpecialPriceSuggestion.getCsvHeaders(true);
        assert csvHeaders[0].equals("CUSTOMER ID (Bill To)");
        assert csvHeaders[1].equals("WRITER NAME");
        assert csvHeaders[2].equals("WRITER ID");
        assert csvHeaders[3].equals("BRANCH");
        assert csvHeaders[4].equals("TERRITORY");
        assert csvHeaders[5].equals("PRODUCT ID");
        assert csvHeaders[6].equals("CMP PRICE");
        assert csvHeaders[7].equals("PRICE CATEGORY");
        assert csvHeaders[8].equals("NEW PRICE");
        assert csvHeaders[9].equals("DATE CREATED");
    }

    @Test
    public void validate_shouldReturnNullIfValid(){
        var suggestion = new SpecialPriceSuggestion(
                "change",
                "change",
                "change",
                "change",
                1000.00,
                100.00,
                "Rate Card",
                "change",
                "change"

        );
        var result = suggestion.validate("somePath");
        assert result == null;

        suggestion = new SpecialPriceSuggestion(
                "change",
                "change",
                "change",
                "change",
                1000.00,
                100.00,
                "User Defined",
                "change",
                "change"
        );

        result = suggestion.validate("somePath");
        assert result == null;

        suggestion = new SpecialPriceSuggestion(
                "change",
                "change",
                "change",
                "change",
                1000.00,
                100.00,
                "Typical Price",
                "change",
                "change"
        );

        result = suggestion.validate("somePath");
        assert result == null;

        suggestion = new SpecialPriceSuggestion(
                "change",
                "change",
                "change",
                "change",
                1000.00,
                100.00,
                "Recommended",
                "change",
                "change"
        );

        result = suggestion.validate("somePath");
        assert result == null;
    }

    @Test
    public void validate_shouldReturnExceptionIfInvalid(){
        var suggestion = new SpecialPriceSuggestion(
                "change",
                "change",
                "change",
                "change",
                1000.00,
                100.00,
                "Rate card",
                "change",
                "change"
        );
        var result = suggestion.validate("somePath");
        assert result != null;
        assert result.getMessage().equals("Invalid parameter: 'priceCategory' value is not in the allowed value array: ['User Defined', 'Rate Card', 'Typical Price', 'Recommended']");
        assert result.getExceptionType().equals("InvalidParameter");
        assert result.getErrorField().equals("somePath.priceCategory");
    }

    @Test
    public void cleanUserInputData_shouldTrimAllFields(){
        var suggestion = new SpecialPriceSuggestion();
        suggestion.setBranch("         1          ");
        suggestion.setChangeWriterId("         2          ");
        suggestion.setChangeWriterDisplayName("         3          ");
        suggestion.setCustomerId("         4          ");
        suggestion.setProductId("         5          ");

        suggestion.cleanUserInputData();

        assert suggestion.getBranch().equals("1");
        assert suggestion.getChangeWriterId().equals("2");
        assert suggestion.getChangeWriterDisplayName().equals("3");
        assert suggestion.getCustomerId().equals("4");
        assert suggestion.getProductId().equals("5");
    }
}
