package com.reece.platform.products.service;

import com.reece.platform.products.model.PermissionType;
import com.reece.platform.products.utilities.DecodedToken;
import java.util.UUID;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public Boolean userCanSubmitOrder(String authorization) {
        val token = DecodedToken.getDecodedHeader(authorization);
        return token.containsPermission(PermissionType.submit_cart_without_approval);
    }

    public Boolean userIsEmployee(String authorization) {
        if (authorization == null) return false;

        val token = DecodedToken.getDecodedHeader(authorization);
        return token.containsPermission(PermissionType.approve_all_users);
    }

    public Boolean userCanApproveOrder(String authorization) {
        if (authorization == null) return false;

        val token = DecodedToken.getDecodedHeader(authorization);
        return token.containsPermission(PermissionType.approve_cart);
    }

    public Boolean userCanManageQuotes(String authorization) {
        if (authorization == null) return false;

        val token = DecodedToken.getDecodedHeader(authorization);
        return token.containsPermission(PermissionType.submit_quote_order);
    }

    public Boolean userCanEditList(String authorization) {
        if (authorization == null) return false;
        val token = DecodedToken.getDecodedHeader(authorization);
        return token.containsPermission(PermissionType.edit_list);
    }

    public Boolean userCanManageBranches(String authorization) {
        if (authorization == null) return false;
        val token = DecodedToken.getDecodedHeader(authorization);
        return token.containsPermission(PermissionType.manage_branches);
    }

    public UUID getUserIdFromToken(String authorization) {
        if (authorization == null) return null;
        val token = DecodedToken.getDecoded(authorization);
        return UUID.fromString(token.getEcommUserId());
    }
}
