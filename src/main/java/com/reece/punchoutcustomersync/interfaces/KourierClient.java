package com.reece.punchoutcustomersync.interfaces;

import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import java.util.List;

public interface KourierClient {

    @Headers({
            "X-CONNECTION: ECLIPSE"
    })
    @GET("/KAPI/PRODUCTS/PRICE")
    Call<CustomersPriceDto> getCustomerProductsPricing(@Query("CustomerID") String customerId, @Query("Branch") String branchId, @Query("ProductID") String productIds);
}
