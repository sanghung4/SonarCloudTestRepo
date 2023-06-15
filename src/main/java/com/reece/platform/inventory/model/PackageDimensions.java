package com.reece.platform.inventory.model;

import lombok.Data;

@Data
public class PackageDimensions {

    private double height;
    private double length;
    private double volume;
    private String volumeUnitOfMeasure;
    private double width;
    private double weight;
    private String weightUnitOfMeasure;

    public boolean isEmpty() {
        return (
            this.height == 0 &&
            this.length == 0 &&
            this.volume == 0 &&
            this.volumeUnitOfMeasure == null &&
            this.width == 0 &&
            this.weight == 0 &&
            this.weightUnitOfMeasure == null
        );
    }
}