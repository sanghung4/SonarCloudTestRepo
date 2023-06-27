package com.reece.specialpricing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestDynamicSortable implements DynamicSortable {
    private String field1;
    private String field2;

    public String getComparisonField(String fieldName){
        if(fieldName.equals("field1")){
            return getField1();
        } else {
            return getField2();
        }
    }
}
