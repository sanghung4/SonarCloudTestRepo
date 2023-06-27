package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.model.exception.ParameterException;
import com.reece.specialpricing.model.exception.TypedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialPriceSearchRequest {
    private String customerId;
    private String productId;
    private String priceLine;

    public List<TypedException> validate(){
        var errorList = new ArrayList<TypedException>();
        if((customerId == null || customerId.isBlank()) && (productId == null || productId.isBlank())){
            errorList.add(
                    new ParameterException(
                            "Invalid parameter: 'customerId' and 'productId' are both null or empty, which is not valid",
                            "customerId or productId"
                    )
            );
        }
        return errorList;
    }

    public void cleanUserInputData(){
        if(customerId != null) {
            setCustomerId(getCustomerId().trim());
        }
        if(productId != null) {
            setProductId(getProductId().trim());
        }
        if (priceLine != null)
        {
            setPriceLine(getPriceLine().trim());
        }
    }
}
