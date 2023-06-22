import { useContext } from 'react';

import {
  Box,
  Button,
  DropdownButton,
  Grid,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import { userButtonStyles, UserItemButton } from 'common/Header/util';
import PermissionRequired, { Permission } from 'common/PermissionRequired';
import { AccountSettingsIcon } from 'icons';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

/**
 * Styles
 */
const { dropDownButtonSx, signInButtonSx } = userButtonStyles;

export default function UserButton() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { activeFeatures, authState, firstName, profile } =
    useContext(AuthContext);
  const isAuthenticated = !!authState?.isAuthenticated;
  const hasPermissions = !!profile?.permissions?.length;
  const canManagePayments = !!activeFeatures?.includes('MANAGE_PAYMENTS');
  const { error: erpAccountError, isMincron } = useSelectedAccountsContext();

  /**
   * Render
   */
  return !isAuthenticated ? (
    <Button
      color="gray"
      iconColor="primary"
      onClick={() => history.push('/login')}
      startIcon={<AccountSettingsIcon color="primary" height={36} />}
      variant="text"
      sx={signInButtonSx}
      data-testid="sign-in-header-button"
    >
      {t('common.signInRegister')}
    </Button>
  ) : (
    <DropdownButton
      sx={dropDownButtonSx}
      content={(_, setOpen) => (
        <Grid container direction="column" px={3} py={1}>
          <Grid container item alignItems="center">
            <Typography variant="h5" display="block" py={2} fontWeight={700}>
              {t('common.hello', { name: firstName })}
            </Typography>
          </Grid>
          <Grid container item direction="column" alignItems="flex-start">
            <PermissionRequired permissions={[Permission.APPROVE_ALL_USERS]}>
              <UserItemButton
                label={t('common.customerApproval')}
                path="/customer-approval"
                setOpen={setOpen}
                push={history.push}
              />
            </PermissionRequired>
            <PermissionRequired permissions={[Permission.MANAGE_ROLES]}>
              <UserItemButton
                label={t('common.userManagement')}
                path="/user-management"
                setOpen={setOpen}
                push={history.push}
              />
            </PermissionRequired>
            {isAuthenticated && !erpAccountError && hasPermissions && (
              <UserItemButton
                label={t('common.accountInformation')}
                path="/account"
                setOpen={setOpen}
                push={history.push}
              />
            )}
            {canManagePayments && !isMincron && (
              <PermissionRequired
                permissions={[Permission.MANAGE_PAYMENT_METHODS]}
              >
                <UserItemButton
                  label={t('common.paymentInformation')}
                  path="/payment-information"
                  setOpen={setOpen}
                  push={history.push}
                />
              </PermissionRequired>
            )}
            <PermissionRequired permissions={[Permission.MANAGE_BRANCHES]}>
              <UserItemButton
                label={t('common.branchManagement')}
                path="/branch-management"
                setOpen={setOpen}
                push={history.push}
              />
            </PermissionRequired>

            <Box mt={1.25}>
              <UserItemButton
                isSignOut
                label={t('common.signOut')}
                path="/logout"
                push={history.push}
                setOpen={setOpen}
              />
            </Box>
          </Grid>
        </Grid>
      )}
      startIcon={<AccountSettingsIcon color="primary" />}
    >
      <Typography
        variant="body1"
        fontSize={12}
        lineHeight="18px"
        fontWeight={400}
      >
        {t('common.myAccount')}
      </Typography>
    </DropdownButton>
  );
}
