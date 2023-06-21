package com.reece.platform.accounts.config;

import com.reece.platform.accounts.utilities.DecodedToken;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditAwareImpl();
    }
}

class AuditAwareImpl implements AuditorAware<String> {

    @SneakyThrows
    @Override
    public Optional<String> getCurrentAuditor() {
        String editor = "SYSTEM";

        // TODO: tw - this is a workaround until we can integrate Okta with spring security.
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            String authorizationHeader = requestAttributes.getRequest().getHeader("authorization");

            if (authorizationHeader != null && !authorizationHeader.isBlank()) {
                DecodedToken token = DecodedToken.getDecodedHeader(authorizationHeader);
                editor = token.sub;
            }
        }

        return Optional.of(editor);
    }
}
