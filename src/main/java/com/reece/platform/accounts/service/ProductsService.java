package com.reece.platform.accounts.service;

import com.reece.platform.accounts.model.DTO.CartDeleteResponseDTO;
import com.reece.platform.accounts.model.DTO.DeleteCartRequestDTO;
import com.reece.platform.accounts.model.DTO.DeliveriesDeleteResponseDTO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductsService {
    @Value("${product_service_url}")
    private String productServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<CartDeleteResponseDTO> deleteCartsByShipToId(UUID shipToAccountId) {
        val url = productServiceUrl + "/cart";
        val request = new HttpEntity<>(new DeleteCartRequestDTO(shipToAccountId));
        return Optional.ofNullable(
                restTemplate.exchange(url, HttpMethod.DELETE, request, CartDeleteResponseDTO.class).getBody()
        );
    }

    public Optional<DeliveriesDeleteResponseDTO> deleteDeliveries(UUID shipToId) {
        val url = productServiceUrl + "/deliveries/" + shipToId;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity request = new HttpEntity(headers);
        return Optional.ofNullable(
                restTemplate.exchange(url, HttpMethod.DELETE, request, DeliveriesDeleteResponseDTO.class).getBody());
    }
}
