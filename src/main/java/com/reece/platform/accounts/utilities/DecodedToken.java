package com.reece.platform.accounts.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reece.platform.accounts.model.seed.PermissionType;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

// https://www.lenar.io/how-to-decode-jwt-authentication-token/
public class DecodedToken {

    public String sub;
    public String ecommUserId;
    public String[] ecommPermissions;

    public static DecodedToken getDecoded(String encodedToken) {
        String[] pieces = encodedToken.split("\\.");
        String b64payload = pieces[1];
        String jsonString = new String(Base64.getDecoder().decode(b64payload), StandardCharsets.UTF_8);

        return new Gson().fromJson(jsonString, DecodedToken.class);
    }

    public static DecodedToken getDecodedHeader(String token) {
        String[] headerPieces = token.split(" ");
        String encodedToken = headerPieces[1];
        return DecodedToken.getDecoded(encodedToken);
    }

    public boolean containsPermission(PermissionType permission) {
        PermissionType[] permissions = { permission };
        return containsPermissions(permissions);
    }

    public boolean containsPermissions(PermissionType[] permissions) {
        List<String> ecommPermissions = Arrays.asList(this.ecommPermissions);
        return Arrays
            .stream(permissions)
            .map(p -> ecommPermissions.contains(p.toString()))
            .reduce(true, Boolean::logicalAnd);
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
