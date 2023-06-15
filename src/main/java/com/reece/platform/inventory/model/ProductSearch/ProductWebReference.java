package com.reece.platform.inventory.model.ProductSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Object representing an Eclipse ProductWebReference.
 */

@Data
public class ProductWebReference {
    private String webReferenceId = null;
    private String webReferenceDescription = null;
    private String webReferenceParameters = null;

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
