package com.reece.platform.products.model.DTO;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PageDTO<T> {

    Integer rowsReturned;
    Integer totalRows;
    Integer startRow;
    public List<T> results = new ArrayList<>();
}
