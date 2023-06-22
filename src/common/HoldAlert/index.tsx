import React, { useContext, useMemo, useState } from 'react';

import {
  Alert,
  Box,
  Collapse,
  IconButton,
  lighten,
  useScreenSize
} from '@dialexa/reece-component-library';

import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import { WarningIcon, CloseIcon } from 'icons';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

function HoldAlert() {
  /**
   * Custom Hooks
   */
  const location = useLocation();
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * State
   */
  const [open, setOpen] = useState(true);

  /**
   * Context
   */
  const { authState } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Memo
   */
  const showBanner = useMemo(() => {
    return (
      location.pathname !== '/select-accounts' &&
      (selectedAccounts.billToErpAccount?.creditHold ||
        selectedAccounts.shipToErpAccount?.creditHold) &&
      authState?.isAuthenticated
    );
  }, [
    authState?.isAuthenticated,
    location.pathname,
    selectedAccounts.billToErpAccount?.creditHold,
    selectedAccounts.shipToErpAccount?.creditHold
  ]);

  /**
   * Data
   */
  if (!showBanner) {
    return null;
  }

  return (
    <Box
      zIndex={2}
      top={0}
      position="relative"
      left={isSmallScreen ? undefined : 0}
      right={isSmallScreen ? undefined : 0}
    >
      <Box
        justifyContent="center"
        position={isSmallScreen ? undefined : 'sticky'}
        top={isSmallScreen ? undefined : 0}
        left={isSmallScreen ? undefined : 0}
        right={isSmallScreen ? undefined : 0}
      >
        <Collapse in={open}>
          <Alert
            icon={<WarningIcon />}
            sx={(theme) => ({
              color: 'text.secondary',
              bgcolor: `${lighten(theme.palette.secondary.light, 0.88)}`,
              ...theme.typography.body2,
              alignItems: 'center',
              '& .MuiAlert-icon': {
                color: 'secondary.main'
              },
              border: 1,
              borderColor: 'secondary.main'
            })}
            data-testid="hold-alert"
            action={
              <IconButton
                color="inherit"
                size="small"
                data-testid="close-hold-alert"
                onClick={() => {
                  setOpen(false);
                }}
              >
                <CloseIcon />
              </IconButton>
            }
          >
            {t('common.accountHold')}
          </Alert>
        </Collapse>
      </Box>
    </Box>
  );
}

export default HoldAlert;
