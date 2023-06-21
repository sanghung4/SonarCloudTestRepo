package com.reece.platform.products.helpers;

import com.reece.platform.products.model.TechDoc;
import com.reece.platform.products.pdw.model.ProductSearchDocument;

public class Util {

    public static boolean isValidUrl(String url) {
        return url.contains("http");
    }

    public static boolean isValidUrl(TechDoc doc) {
        return isValidUrl(doc.getUrl());
    }

    public static ProductSearchDocument productMapping(ProductSearchDocument product) {
        if (product.getWebDescription() != null && product.getProductOverviewDescription() != null) {
            product.setCleanWebDescription(
                product
                    .getWebDescription()
                    .replaceAll("[^0-9a-zA-Z:,]+", " ")
                    .replaceAll("(?!\\d)(| x |x| X |X| \\* |)(?<!\\d)", "") + ", " +
                product
                    .getProductOverviewDescription()
                    .replaceAll("[^0-9a-zA-Z:,]+", " ")
                    .replaceAll("(?!\\d)(| x |x| X |X| \\* |)(?<!\\d)", ""));
        } else {
            if (product.getWebDescription() != null) {
                product.setCleanWebDescription(
                    product
                        .getWebDescription()
                        .replaceAll("[^0-9a-zA-Z:,]+", " ")
                        .replaceAll("(?!\\d)(| x |x| X |X| \\* |)(?<!\\d)", ""));
            }
            if (product.getProductOverviewDescription() != null) {
                product.setCleanWebDescription(
                    product
                        .getProductOverviewDescription()
                        .replaceAll("[^0-9a-zA-Z:,]+", " ")
                        .replaceAll("(?!\\d)(| x |x| X |X| \\* |)(?<!\\d)", ""));
            }
        }
        if (product.getVendorPartNbr() != null) {
            product.setCleanVendorPartNbr(product.getVendorPartNbr().replaceAll("[^0-9a-zA-Z:,]+", " "));
        }
        if (product.getMfrFullName() != null) {
            product.setCleanProductBrand(product.getMfrFullName() + " " + product.getCleanWebDescription());
        }
        return product;
    }
}
