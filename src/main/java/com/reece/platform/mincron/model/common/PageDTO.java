package com.reece.platform.mincron.model.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO<T> {
    Integer rowsReturned;
    Integer totalRows;
    Integer startRow;
    public List<T> results = new ArrayList<>();
}
