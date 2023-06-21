package com.reece.platform.products.model;

import java.io.Serializable;

/**
 * Data model for package dimensions
 */
public class PackageDimensions implements Serializable {

    private double height;
    private double length;
    private double volume;
    private String volumeUnitOfMeasure;
    private double width;
    private double weight;
    private String weightUnitOfMeasure;

    public PackageDimensions() {}

    public PackageDimensions(
        double height,
        double length,
        double volume,
        String volumeUnitOfMeasure,
        double width,
        double weight,
        String weightUnitOfMeasure
    ) {
        this.height = height;
        this.length = length;
        this.volume = volume;
        this.volumeUnitOfMeasure = volumeUnitOfMeasure;
        this.width = width;
        this.weight = weight;
        this.weightUnitOfMeasure = weightUnitOfMeasure;
    }

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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getVolumeUnitOfMeasure() {
        return volumeUnitOfMeasure;
    }

    public void setVolumeUnitOfMeasure(String volumeUnitOfMeasure) {
        this.volumeUnitOfMeasure = volumeUnitOfMeasure;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightUnitOfMeasure() {
        return weightUnitOfMeasure;
    }

    public void setWeightUnitOfMeasure(String weightUnitOfMeasure) {
        this.weightUnitOfMeasure = weightUnitOfMeasure;
    }
}
