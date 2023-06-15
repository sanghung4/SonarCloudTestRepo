package com.reece.platform.inventory.config;

import lombok.extern.slf4j.Slf4j;
import me.alidg.errors.HttpError;
import me.alidg.errors.WebErrorHandlerPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultExceptionLogger implements WebErrorHandlerPostProcessor {
    @Override
    public void process(HttpError error) {
        log.error("HttpError handled: status={}, fingerprint={}, errors={}", error.getHttpStatus(), error.getFingerprint(), error.getErrors());
        log.error("Original Exception:", error.getOriginalException());
    }
}
