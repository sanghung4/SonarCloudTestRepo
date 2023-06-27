package com.reece.specialpricing.model;

import com.reece.specialpricing.model.exception.ParameterException;
import com.reece.specialpricing.model.exception.TypedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationContext {
    @NotNull(message = "Invalid parameter: 'page' is null, which is not valid")
    @Positive(message = "Invalid parameter: 'page' is 0 or negative, which is not valid")
    private int page = 1;
    @NotNull(message = "Invalid parameter: 'pageSize' is null, which is not valid")
    @Positive(message = "Invalid parameter: 'pageSize' is 0 or negative, which is not valid")
    private int pageSize = 5;
    @NotBlank(message = "Invalid parameter: 'orderBy' is null or blank, which is not valid")
    private String orderBy = "displayName";
    @NotBlank(message = "Invalid parameter: 'orderDirection' is null or blank, which is not valid")
    private String orderDirection = "asc";

    public List<TypedException> validate(boolean isSpecialPricePagination){
        var errorList = new ArrayList<TypedException>();
        if(isSpecialPricePagination
                && !orderBy.equals("displayName") && !orderBy.equals("manufacturer") && !orderBy.equals("branch")
                && !orderBy.equals("customerDisplayName") && !orderBy.equals("manufacturerReferenceNumber") && !orderBy.equals("priceLine")
                && !orderBy.equals("currentPrice") && !orderBy.equals("standardCost") && !orderBy.equals("typicalPrice")
                && !orderBy.equals("rateCardPrice") && !orderBy.equals("recommendedPrice")
        ){
            errorList.add(
                    new ParameterException(
                            "Invalid parameter: 'orderBy' not in value array: ['displayName', 'manufacturer', 'branch', 'customerDisplayName', 'manufacturerReferenceNumber', 'priceLine','currentPrice','standardCost'," +
                                    "'typicalPrice','rateCardPrice','recommendedPrice']",
                            "orderBy"
                    )
            );
        } else if(!isSpecialPricePagination && !orderBy.equals("id") && !orderBy.equals("displayName")){
            errorList.add(
                    new ParameterException(
                            "Invalid parameter: 'orderBy' not in value array: ['id', 'displayName']",
                            "orderBy"
                    )
            );
        }

        if(!orderDirection.equals("asc") && !orderDirection.equals("desc")){
            errorList.add(
                    new ParameterException(
                            "Invalid parameter: 'orderDirection' not in value array: ['asc', 'desc']",
                            "orderDirection"
                    )
            );
        }

        return errorList;
    }
}