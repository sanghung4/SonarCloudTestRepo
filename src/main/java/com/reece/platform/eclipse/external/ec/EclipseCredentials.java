package com.reece.platform.eclipse.external.ec;

import lombok.ToString;
import lombok.Value;
import lombok.val;

@Value
public class EclipseCredentials {
    String username;
    @ToString.Exclude
    String password;
    String sessionId;

    public static EclipseCredentials fromStorageFormat(String storageFormat, String sessionId) {
        val parts = storageFormat.split(":");
        return new EclipseCredentials(parts[0], parts[1], sessionId);
    }

    public String toStorageFormat() {
        return getUsername() + ":" + getPassword();
    }
}
