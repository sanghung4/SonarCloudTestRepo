package com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import lombok.Data;
import lombok.val;

@Data
@JsonDeserialize(using = Quantity.Deserializer.class)
public class Quantity {

    private String uom;

    private String umqt;

    private Integer quantity;

    public static class Deserializer extends JsonDeserializer<Quantity> {

        @Override
        public Quantity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
            Quantity quantity = new Quantity();
            TreeNode root = jsonParser.getCodec().readTree(jsonParser);
            double tempQty = 0.0;
            int tempUmqt = 0;
            for (var it = root.fieldNames(); it.hasNext();) {
                val fieldName = it.next();
                val resultField = (JsonNode) root.get(fieldName);
                switch (fieldName) {
                    case "uom":
                        quantity.setUom(resultField.asText());
                        break;
                    case "quantity":
                        tempQty = resultField.asDouble();
                        break;
                    case "umqt":
                        tempUmqt = resultField.asInt();
                        quantity.setUmqt(resultField.asText());
                        break;
                    default:
                        break;
                }
            }
            quantity.setQuantity((int) (tempUmqt * tempQty));
            return quantity;
        }
    }
}
