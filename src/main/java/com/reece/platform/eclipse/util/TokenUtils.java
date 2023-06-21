package com.reece.platform.eclipse.util;

import com.auth0.jwt.JWT;
import com.reece.platform.eclipse.external.ec.EclipseCredentials;
import lombok.val;

import java.util.Base64;
import java.util.regex.Pattern;

public class TokenUtils {
    private static final Pattern BEARER_TOKEN_PATTERN = Pattern.compile("^Bearer (.+)$");

    private TokenUtils() {
    }

    /**
     * Accepts a authorization header value, extracts the bearer token, decodes the JWT, and returns the user id.
     *
     * @param authHeader A valid Bearer token authorization header
     * @return the user id or null if the token isn't valid
     */
    public static String extractUserId(String authHeader) {
        val matcher = BEARER_TOKEN_PATTERN.matcher(authHeader);
        if (matcher.matches()) {
            var encodedToken = matcher.group(1);
            var decodedToken = JWT.decode(encodedToken);
            return decodedToken.getClaim("uid").asString();
        }

        return null;
    }

    public static String extractSessionId(String authHeader, String sessionIdHeader) {
        if (sessionIdHeader != null) {
            return sessionIdHeader;
        }

        val matcher = BEARER_TOKEN_PATTERN.matcher(authHeader);
        if (matcher.matches()) {
            var encodedToken = matcher.group(1);
            var decodedToken = JWT.decode(encodedToken);
            return decodedToken.getId();
        }

        return null;
    }

    public static EclipseCredentials extractEclipseCredentials(String eclipseCredentialsHeader, String sessionIdHeader) {
        val decoded = Base64.getDecoder().decode(eclipseCredentialsHeader);
        val pair = new String(decoded);
        val parts = pair.split(":", 2);

        if (sessionIdHeader == null) {
            sessionIdHeader = eclipseCredentialsHeader;
        }

        return new EclipseCredentials(parts[0], parts[1], sessionIdHeader);
    }
}
