package com.reece.platform.products.branches.model.enums;

public enum MileRadiusEnum {
    MILES_25(25f),
    MILES_50(50f),
    MILES_100(100f),
    MILES_200(200f),
    MILES_400(400f);

    private final float miles;

    MileRadiusEnum(float miles) {
        this.miles = miles;
    }

    public float getMiles() {
        return miles;
    }
}
