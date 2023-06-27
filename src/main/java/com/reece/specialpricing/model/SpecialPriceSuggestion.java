package com.reece.specialpricing.model;

import com.reece.specialpricing.model.exception.ParameterException;
import com.reece.specialpricing.model.exception.TypedException;
import com.reece.specialpricing.utilities.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.Instant;

import static com.reece.specialpricing.utilities.StringUtils.removePrefix;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialPriceSuggestion {
    @NotBlank(message = "Invalid parameter: 'customerId' is null or blank, which is not valid")
    private String customerId;
    @NotBlank(message = "Invalid parameter: 'productId' is null or blank, which is not valid")
    private String productId;

    private String territory;
    @NotBlank(message = "Invalid parameter: 'branch' is null or blank, which is not valid")
    private String branch;
    @NotNull(message = "Invalid parameter: 'cmpPrice' is null, which is not valid")
    @Min(value = 0L, message = "Invalid parameter: 'cmpPrice' is negative, which is not valid")
    private Double cmpPrice;
    @NotNull(message = "Invalid parameter: 'newPrice' is null, which is not valid")
    @Positive(message = "Invalid parameter: 'newPrice' is 0 or negative, which is not valid")
    private Double newPrice;
    @NotBlank(message = "Invalid parameter: 'priceCategory' is null or blank, which is not valid")
    private String priceCategory;
    @NotBlank(message = "Invalid parameter: 'changeWriterId' is null or blank, which is not valid")
    private String changeWriterId;
    @NotBlank(message = "Invalid parameter: 'changeWriterDisplayName' is null or blank, which is not valid")
    private String changeWriterDisplayName;

    public TypedException validate(String fieldPath){
        if(priceCategory.equals("User Defined")
                || priceCategory.equals("Rate Card")
                || priceCategory.equals("Typical Price")
                || priceCategory.equals("Recommended")){
            return null;
        }
        return new ParameterException(
                "Invalid parameter: 'priceCategory' value is not in the allowed value array: ['User Defined', 'Rate Card', 'Typical Price', 'Recommended']",
                String.format("%s.priceCategory", fieldPath)
            );
    }

    public static String[] getCsvHeaders(Boolean isCreate){
        if(!isCreate){
            return new String[]{
                "CUSTOMER ID (Bill To)",
                "WRITER NAME",
                "WRITER ID",
                "BRANCH",
                "TERRITORY",
                "PRODUCT ID",
                "CMP PRICE",
                "PRICE CATEGORY",
                "NEW PRICE",
                "DATE CREATED",
                "EXPIRATION DATE"
            };
        } else {
            return new String[]{
                "CUSTOMER ID (Bill To)",
                "WRITER NAME",
                "WRITER ID",
                "BRANCH",
                "TERRITORY",
                "PRODUCT ID",
                "CMP PRICE",
                "PRICE CATEGORY",
                "NEW PRICE",
                "DATE CREATED"
            };
        }
    }

    public String[] toCsvRow(Boolean isCreate){
        if(!isCreate) {
            return new String[]{
                    removePrefix(this.customerId, "MSC-"),
                    this.changeWriterDisplayName,
                    this.changeWriterId,
                    removePrefix(this.branch, "MSC-"),
                    this.territory = (this.territory == null || this.territory.isEmpty()) ? "DFLT" : this.territory,
                    removePrefix(this.productId, "MSC-"),
                    String.format("%.2f", this.cmpPrice),
                    this.priceCategory,
                    String.format("%.2f", this.newPrice),
                    DateTimeUtils.getCentralTimeZoneDateStringFromInstant(Instant.now()),
                    DateTimeUtils.getCentralTimeZoneDateStringFromInstant(Instant.now().minus(Duration.ofDays(1)))
            };
        } else {
            return new String[]{
                    removePrefix(this.customerId, "MSC-"),
                    this.changeWriterDisplayName,
                    this.changeWriterId,
                    removePrefix(this.branch, "MSC-"),
                    this.territory =  "DFLT",
                    removePrefix(this.productId, "MSC-"),
                    String.format("%.2f", this.cmpPrice),
                    this.priceCategory,
                    String.format("%.2f", this.newPrice),
                    DateTimeUtils.getCentralTimeZoneDateStringFromInstant(Instant.now()),
            };
        }
    }

    public void cleanUserInputData(){
        setBranch(getBranch().trim());
        setChangeWriterId(getChangeWriterId().trim());
        setChangeWriterDisplayName(getChangeWriterDisplayName().trim());
        setCustomerId(getCustomerId().trim());
        setProductId(getProductId().trim());
    }
}
