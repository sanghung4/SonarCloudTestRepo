package com.reece.platform.inventory.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EclipseLoadCountDto {
    @CsvBindByName(column = "BatchNumber")
    private String batchNumber;
    @CsvBindByName(column = "BatchDescription")
    private String batchDescription;
    @CsvBindByName(column = "BatchDate")
    private String batchDate;
    @CsvBindByName(column = "BatchLockDate")
    private String batchLockDate;
    @CsvBindByName(column = "CreatedById")
    private String createdById;
    @CsvBindByName(column = "CreatedByName")
    private String createdByName;
    @CsvBindByName(column = "RecordId")
    private String recordId;
    @CsvBindByName(column = "CountId")
    private String countId;
    @CsvBindByName(column = "Branch")
    private String branch;
    @CsvBindByName(column = "Location")
    private String location;
    @CsvBindByName(column = "ProductId")
    private String productId;
    @CsvBindByName(column = "ProductDesc1")
    private String productDescription1;
    @CsvBindByName(column = "ProductDesc2")
    private String productDescription2;
    @CsvBindByName(column = "UnitOfMeasure")
    private String unitOfMeasure;
    @CsvBindByName(column = "CurrentCount")
    private String currentCount;
    @CsvBindByName(column = "CountDate")
    private String countDate;
    @CsvBindByName(column = "Recount1")
    private String recount1;
    @CsvBindByName(column = "Recount2")
    private String recount2;
    @CsvBindByName(column = "Recount3")
    private String recount3;
    @CsvBindByName(column = "OnHand")
    private String onHand;
    @CsvBindByName(column = "AverageCost")
    private String averageCost;
    @CsvBindByName(column = "CatalogNumber")
    private String catalogNumber;
}
