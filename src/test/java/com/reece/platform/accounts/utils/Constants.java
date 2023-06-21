package com.reece.platform.accounts.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Constants {

    public static String authToken(String userId) {
        String body =
            "{\n" +
            "  \"ver\": 1,\n" +
            "  \"jti\": \"AT.DrRmPgn76LY_NX20SU3dz9dW2-I57effjqEceZ6F4qQ\",\n" +
            "  \"iss\": \"https://dev-432546.okta.com/oauth2/default\",\n" +
            "  \"aud\": \"api://default\",\n" +
            "  \"iat\": 1610128976,\n" +
            "  \"exp\": 1610132576,\n" +
            "  \"cid\": \"0oa13b0b5bMKXZfJU4x7\",\n" +
            "  \"uid\": \"00u1pitum0DEW9Ju84x7\",\n" +
            "  \"scp\": [\n" +
            "    \"profile\",\n" +
            "    \"openid\",\n" +
            "    \"email\"\n" +
            "  ],\n" +
            "  \"sub\": \"brady+local@dialexa.com\",\n" +
            "  \"ecommUserId\": \"0f0a9c0b-f513-4a50-8673-e5e1ac68856a\",\n" +
            "  \"ecommPermissions\": [\n" +
            "    \"view_user\",\n" +
            "    \"edit_company_info\",\n" +
            "    \"edit_invoice\",\n" +
            "    \"approve_users_accounts\",\n" +
            "    \"edit_role\",\n" +
            "    \"edit_users_accounts\",\n" +
            "    \"edit_list\",\n" +
            "    \"edit_profile\",\n" +
            "    \"edit_order\",\n" +
            "    \"edit_quote\",\n" +
            "    \"invite_user\"\n" +
            "  ]\n" +
            "}";

        return ("." + Base64.getEncoder().encodeToString(body.getBytes(StandardCharsets.UTF_8)) + ".");
    }
}
