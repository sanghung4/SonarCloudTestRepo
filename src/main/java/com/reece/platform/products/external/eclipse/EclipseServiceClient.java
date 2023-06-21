package com.reece.platform.products.external.eclipse;

import com.reece.platform.products.exceptions.EclipseException;
import com.reece.platform.products.model.DTO.GetOrderResponseDTO;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EclipseServiceClient {

    private final RestTemplate restTemplate;

    public EclipseServiceClient(
        RestTemplateBuilder restTemplateBuilder,
        @Value("${eclipse_service_url}") String eclipseServiceUrl
    ) {
        restTemplate = restTemplateBuilder.rootUri(eclipseServiceUrl).build();
    }

    public Optional<GetOrderResponseDTO> getOrder(String accountId, String orderId, String invoiceNumber) {
        try {
            val response = restTemplate.getForEntity(
                "/accounts/{accountId}/orders/{orderId}?invoiceNumber={invoiceNumber}",
                GetOrderResponseDTO.class,
                accountId,
                orderId,
                invoiceNumber
            );
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error returned from EclipseService#getOrder", e);

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }

            throw new EclipseException(e.getMessage(), e.getStatusCode());
        }
    }
}
