package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.model.ImageUrls;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.val;

@Data
public class ProductSearchResultDTO {

    private PaginationResponseDTO pagination;
    private List<ProductDTO> products;

    public ProductSearchResultDTO(ProductSearchResponseDTO response) {
        this.pagination = new PaginationResponseDTO();
        this.products = new ArrayList<>();

        val metadata = response.getMetadata();
        this.pagination.setPageSize(metadata.getPageSize());
        this.pagination.setCurrentPage(
                (int) Math.ceil((double) metadata.getStartIndex() / this.pagination.getPageSize())
            );
        this.pagination.setTotalItemCount(metadata.getTotalItems());

        val products = response.getResults();
        if (Objects.nonNull(products)) {
            for (val product : products) {
                val newProduct = new ProductDTO();
                newProduct.setId(String.valueOf(product.getId()));
                newProduct.setPartNumber(product.getCatalogNumber());
                newProduct.setName(product.getDescription());
                newProduct.setUpc(product.getUpc());
                ImageUrls imageUrls = new ImageUrls();
                for (val webReference : product.getWebReferences()) {
                    val imageUrl = webReference.getWebReferenceParameters();
                    switch (webReference.getWebReferenceId()) {
                        case "THUMB":
                            imageUrls.setThumb(imageUrl);
                            break;
                        case "FULLIMAGE":
                            imageUrls.setSmall(imageUrl);
                            imageUrls.setMedium(imageUrl);
                            imageUrls.setLarge(imageUrl);
                            break;
                        default:
                            break;
                    }
                }
                newProduct.setImageUrls(imageUrls);
                this.products.add(newProduct);
            }
        }
    }
}
