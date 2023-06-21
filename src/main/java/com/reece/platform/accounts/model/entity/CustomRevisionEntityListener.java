package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.utilities.DecodedToken;
import lombok.SneakyThrows;
import org.hibernate.envers.RevisionListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomRevisionEntityListener implements RevisionListener {

    @SneakyThrows
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
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

        customRevisionEntity.setEmail(editor);
    }
}
