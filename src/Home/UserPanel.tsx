import React, { useContext } from 'react';

import {
  Box,
  Button,
  Grid,
  Link,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import { ErpSystemEnum } from 'generated/graphql';
import { MaxIcon } from 'icons/';
import decodeHTMLEntities from 'utils/decodeHTMLEntities';
import { Permission } from 'common/PermissionRequired';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

function UserPanel() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { activeFeatures, authState, ecommUser, firstName, profile } =
    useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();
  const isMincron = selectedAccounts.erpSystemName === ErpSystemEnum.Mincron;

  return (
    <Box sx={{ bgcolor: 'background.paper' }}>
      {/* Welcome section */}
      <Box px={4} py={3}>
        <Typography
          variant="h5"
          color="primary"
          sx={{ display: 'block', fontWeight: 700, mb: 2 }}
          data-testid="user-panel-title"
        >
          {decodeHTMLEntities(t('common.hi', { name: firstName }))}
        </Typography>
        <Typography variant="body1" component="span" color="primary">
          <Box display="block" data-testid="user-panel-subtitle">
            {t('common.homeUserPanelSub')}
          </Box>
          <Box display="block" position="relative" top={8}>
            <MaxIcon
              width={108}
              height={38}
              data-testid="user-panel-max-icon"
            />
          </Box>
        </Typography>
      </Box>
      {/* Buttons */}
      <Box mx={8} mt={2} mb={6}>
        <Grid container direction="column" spacing={2}>
          {/* Contracts */}
          {ecommUser?.role?.name !== t('roles.invoiceOnly') && isMincron && (
            <Grid item>
              <Link to="/contracts" component={RouterLink}>
                <Button
                  variant="secondary"
                  data-testid="contracts-button"
                  fullWidth
                >
                  {t('common.contracts')}
                </Button>
              </Link>
            </Grid>
          )}
          {/* Orders */}
          <Grid item>
            <Link to="/orders" component={RouterLink}>
              <Button variant="secondary" data-testid="orders-button" fullWidth>
                {t('common.orders')}
              </Button>
            </Link>
          </Grid>
          {/* Quotes */}
          {!isMincron && (
            <Grid item>
              <Link to="/quotes" component={RouterLink}>
                <Button
                  variant="secondary"
                  data-testid="quotes-button"
                  fullWidth
                >
                  {t('common.quotes')}
                </Button>
              </Link>
            </Grid>
          )}
          {/* Invoices */}
          {!!(
            authState?.isAuthenticated &&
            profile?.permissions.includes(Permission.VIEW_INVOICE)
          ) && (
            <Grid item>
              <Link to="/invoices" component={RouterLink}>
                <Button
                  variant="secondary"
                  data-testid="invoices-button"
                  fullWidth
                >
                  {t('common.invoices')}
                </Button>
              </Link>
            </Grid>
          )}
          {/* Lists */}
          {!!activeFeatures?.includes('LISTS') && !isMincron && (
            <Grid item>
              <Link to="/lists" component={RouterLink}>
                <Button
                  variant="secondary"
                  data-testid="lists-button"
                  fullWidth
                >
                  {t('common.lists')}
                </Button>
              </Link>
            </Grid>
          )}
          {/* Purchase Approvals */}
          {!!(
            !authState?.isAuthenticated ||
            profile?.permissions.includes(Permission.APPROVE_CART)
          ) &&
            !isMincron && (
              <Grid item>
                <Link to="/purchase-approvals" component={RouterLink}>
                  <Button
                    variant="secondary"
                    data-testid="purchase-approvals-button"
                    fullWidth
                  >
                    {t('common.purchaseApprovals')}
                  </Button>
                </Link>
              </Grid>
            )}
          {/* Previously Purchased Product */}
          {!!authState?.isAuthenticated && !isMincron && (
            <Grid item>
              <Link to="/previously-purchased-products" component={RouterLink}>
                <Button
                  variant="secondary"
                  data-testid="previously-purchased-button"
                  fullWidth
                >
                  {t('common.previouslyPurchased')}
                </Button>
              </Link>
            </Grid>
          )}
        </Grid>
      </Box>
    </Box>
  );
}

export default UserPanel;
