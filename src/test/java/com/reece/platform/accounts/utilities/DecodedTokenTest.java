package com.reece.platform.accounts.utilities;

import static org.junit.jupiter.api.Assertions.*;

import com.reece.platform.accounts.model.seed.PermissionType;
import org.junit.jupiter.api.Test;

class DecodedTokenTest {

    @Test
    void containsPermission_singlePermission_true() {
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.ecommPermissions = new String[] { PermissionType.approve_account_user.toString() };
        assertTrue(decodedToken.containsPermission(PermissionType.approve_account_user));
    }

    @Test
    void containsPermission_singlePermission_false() {
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.ecommPermissions = new String[] { PermissionType.approve_account_user.toString() };
        assertFalse(decodedToken.containsPermission(PermissionType.approve_all_users));
    }

    @Test
    void containsPermission_multiple_permission_token_true() {
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.ecommPermissions =
            new String[] {
                PermissionType.approve_account_user.toString(),
                PermissionType.approve_all_users.toString(),
            };
        assertTrue(decodedToken.containsPermission(PermissionType.approve_all_users));
    }

    @Test
    void containsPermission_multiple_permission_token_false() {
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.ecommPermissions =
            new String[] {
                PermissionType.approve_account_user.toString(),
                PermissionType.approve_all_users.toString(),
            };
        assertFalse(decodedToken.containsPermission(PermissionType.approve_cart));
    }

    @Test
    void containsPermissions_multiple_permission_token_true() {
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.ecommPermissions =
            new String[] {
                PermissionType.approve_account_user.toString(),
                PermissionType.approve_all_users.toString(),
            };
        assertTrue(
            decodedToken.containsPermissions(
                new PermissionType[] { PermissionType.approve_account_user, PermissionType.approve_all_users }
            )
        );
    }

    @Test
    void containsPermissions_multiple_permission_token_false() {
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.ecommPermissions =
            new String[] { PermissionType.approve_cart.toString(), PermissionType.approve_all_users.toString() };
        assertFalse(
            decodedToken.containsPermissions(
                new PermissionType[] { PermissionType.approve_account_user, PermissionType.approve_all_users }
            )
        );
    }

    @Test
    void containsPermissions_single_permission_token_false() {
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.ecommPermissions = new String[] { PermissionType.approve_account_user.toString() };
        assertFalse(
            decodedToken.containsPermissions(
                new PermissionType[] { PermissionType.approve_account_user, PermissionType.approve_all_users }
            )
        );
    }
}
