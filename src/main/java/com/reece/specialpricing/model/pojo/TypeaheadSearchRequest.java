package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.model.exception.ParameterException;
import com.reece.specialpricing.model.exception.TypedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeaheadSearchRequest {
    @NotBlank(message = "Invalid parameter: 'entity' is null or blank, which is not valid")
    private String entity;
    @NotBlank(message = "Invalid parameter: 'query' is null or blank, which is not valid")
    private String query;

    public List<TypedException> validate(){
        var errorList = new ArrayList<TypedException>();

        if(!entity.equals("customer") && !entity.equals("product")){
            errorList.add(
                    new ParameterException(
                            "Invalid parameter: 'entity' not in value array: ['customer', 'product']",
                            "entity"
                    )
            );
        }

        return errorList;
    }

    public void cleanUserInputData(){
        setQuery(getQuery().trim());
    }
}
