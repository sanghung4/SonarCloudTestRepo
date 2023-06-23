package com.reece.punchoutcustomersync.interfaces;

import com.reece.punchoutcustomersync.dto.max.EngineDto;
import com.reece.punchoutcustomersync.dto.max.ProductDocumentDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ElasticSearchClient {

    @Headers({
            "Accept: application/json"
    })
    @HTTP(method = "get", path = "/api/as/v1/engines/{engine}/documents", hasBody = true)
    Call<List<ProductDocumentDto>> getProductAttributes(@Path("engine") String engine, @Body List<String> erpProductIds);

    @Headers({
            "Accept: application/json"
    })
    @GET("/api/as/v1/engines/ecomm-products")
    Call<EngineDto> getEngine();
}
