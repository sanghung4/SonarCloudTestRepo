package com.reece.platform.inventory.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.inventory.external.eclipse.EclipseErrorDTO;
import lombok.val;
import me.alidg.errors.Argument;
import me.alidg.errors.HandledException;
import me.alidg.errors.WebErrorHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MincronErrorHandler implements WebErrorHandler {
    @Override
    public boolean canHandle(Throwable throwable) {
        return throwable instanceof HttpClientErrorException;
    }

    @Override
    public HandledException handle(Throwable throwable) {
        val e = (HttpClientErrorException) throwable;
        val objectMapper = new ObjectMapper();
        EclipseErrorDTO statusError = null;
        try {
            statusError = objectMapper.readValue(e.getResponseBodyAsByteArray(), EclipseErrorDTO.class);
            return new HandledException("MINCRON_EXCEPTION", e.getStatusCode(), Map.of("MINCRON_EXCEPTION", List.of(Argument.arg("MINCRON_EXCEPTION", statusError.getMessage()))));
        } catch (IOException ioException) {
            return new HandledException("MINCRON_EXCEPTION", HttpStatus.BAD_REQUEST, Map.of("MINCRON_EXCEPTION", List.of(Argument.arg("MINCRON_EXCEPTION", e.getMessage()))));
        }
    }
}
