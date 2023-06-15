package com.reece.platform.inventory.model;

import lombok.Data;

@Data
public class Quantity {

    private String uom;

    private String umqt;

    private Integer quantity;
}