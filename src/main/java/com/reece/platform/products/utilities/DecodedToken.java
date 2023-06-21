package com.reece.platform.products.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reece.platform.products.model.PermissionType;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.apache.commons.codec.binary.Base64;

// https://www.lenar.io/how-to-decode-jwt-authentication-token/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecodedToken {

    private String sub;
    private String ecommUserId;
    private String[] ecommPermissions;

    public static DecodedToken getDecoded(String encodedToken) {
        String[] pieces = encodedToken.split("\\.");
        String b64payload = pieces[1];
        String jsonString = new String(Base64.decodeBase64(b64payload), StandardCharsets.UTF_8);

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
        if (this.ecommPermissions == null) return false;

        List<String> ecommPermissions = Arrays.asList(this.ecommPermissions);
        return Arrays
            .stream(permissions)
            .map(p -> ecommPermissions.contains(p.toString()))
            .reduce(true, Boolean::logicalAnd);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
