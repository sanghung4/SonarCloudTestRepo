package com.reece.platform.products.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.snowflake.client.jdbc.internal.org.checkerframework.common.value.qual.IntRangeFromGTENegativeOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadListData {

    @CsvBindByName(required = true, column = "Part #")
    private String partNumber;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "MFR Name")
    private String mfrName;

    @CsvBindByName(column = "QTY")
    private Integer quantity;

    @CsvBindByName(column = "Price")
    @CsvIgnore
    private Integer price;

    @CsvBindByName(column = "MFR #")
    @CsvIgnore
    private String mfrNumber;

    public UploadListData(String partNum, String description, String mfrName, int qty) {
        this.partNumber = partNum;
        this.description = description;
        this.mfrName = mfrName;
        this.quantity = qty;
    }
}
