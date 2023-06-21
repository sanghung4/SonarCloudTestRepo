package com.reece.platform.products.service;

import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.PriceAndAvailability;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerProductInfoService {

    private static int LARGE_PARTITION_SIZE = 20;
    private static int SMALL_PARTITION_SIZE = 5;
    private static int PARTITION_THRESHOLD = 50;

    private final ErpService erpService;
    private final ProductService productService;

    /**
     * Get Pricing and availability for part numbers
     *    - Will execute on multiple threads
     * @param partNumbers
     * @param erpUserInformation
     * @param employee
     * @param shipToId
     * @param userId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<PriceAndAvailability> getPriceAndAvailability(
        List<String> partNumbers,
        ErpUserInformation erpUserInformation,
        boolean employee,
        UUID shipToId,
        UUID userId
    ) throws ExecutionException, InterruptedException {
        int partitionSize = partNumbers.size() > PARTITION_THRESHOLD ? LARGE_PARTITION_SIZE : SMALL_PARTITION_SIZE;
        List<CompletableFuture<ProductResponse>> futures = new ArrayList<>();

        // Partition Eclipse Pricing and availability calls in parallel
        for (int i = 0; i < partNumbers.size(); i += partitionSize) {
            var partitionedList = partNumbers.subList(i, Math.min(i + partitionSize, partNumbers.size()));
            CompletableFuture<ProductResponse> partitionedProducts = erpService.getEclipseProductDataAsync(
                partitionedList,
                erpUserInformation,
                employee
            );
            futures.add(partitionedProducts);
        }

        futures.forEach(CompletableFuture::join);

        val products = new ArrayList<Product>();
        futures.forEach(future -> {
            try {
                var subproducts = future.get().getMassProductInquiryResponse().getProductList().getProducts();
                products.addAll(subproducts);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        val selectedBranch = productService.getSelectedCartBranch(userId, shipToId).orElse(null);
        val erp = ErpEnum.valueOf(erpUserInformation.getErpSystemName());
        val listIdsPerPartNumber = productService.getAllListIdsByPartNumbersAndErpAccountId(
            partNumbers,
            erpUserInformation.getErpAccountId(),
            erp
        );
        val priceAndAvailability = new ArrayList<PriceAndAvailability>();
        for (val partNumber : partNumbers) {
            products
                .stream()
                .filter(product -> product.getPartIdentifiers().getEclipsePartNumber().equals(partNumber))
                .findFirst()
                .ifPresent(erpProduct -> {
                    //Get listIds for matched partNumber
                    var listIds = new ArrayList<String>();
                    listIdsPerPartNumber
                        .stream()
                        .filter(p -> p[0].equals(partNumber))
                        .forEach(p -> {
                            listIds.addAll(Arrays.asList(p[1].split(",")));
                        });
                    priceAndAvailability.add(new PriceAndAvailability(erpProduct, selectedBranch, listIds));
                });
        }
        return priceAndAvailability;
    }
}
