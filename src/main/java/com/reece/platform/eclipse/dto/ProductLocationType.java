/*
 * Eclipse API Developer Documentation
 * This documentation provides a list of API endpoints provided in this release as well as examples for using the various API endpoints
 *
 * OpenAPI spec version: 9.1.1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.reece.platform.eclipse.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * List of valid Product Location Statuses  &#x27;S - Stock&#x27;  &#x27;F - DeFective&#x27;  &#x27;O - Over Shipment&#x27;  &#x27;R - Review&#x27;  &#x27;L - DispLay&#x27;  &#x27;T - Tagged&#x27;  &#x27;C - Customer Consignment&#x27;  &#x27;W - Warehouse&#x27;  &#x27;V - Vehicle&#x27;
 */
public enum ProductLocationType {
    STOCK("Stock"),
    DEFECTIVE("Defective"),
    OVERSHIPMENT("OverShipment"),
    REVIEW("Review"),
    DISPLAY("Display"),
    TAGGED("Tagged"),
    CONSIGNMENT("Consignment"),
    WAREHOUSE("Warehouse"),
    VEHICLE("Vehicle");

    private String value;

    ProductLocationType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ProductLocationType fromValue(String text) {
        for (ProductLocationType b : ProductLocationType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
