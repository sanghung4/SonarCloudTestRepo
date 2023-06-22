import { useContext } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import { BranchContext } from 'providers/BranchProvider';
import { styles } from 'Branches/util/styles';
import { BranchIcon, WarningIcon } from 'icons';

/**
 * Styles
 */
const { branchButtonSx, branchButtonStartIconLoadingSx } = styles.branchButton;

export default function BranchButton() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { authState } = useContext(AuthContext);
  const {
    homeBranchError,
    shippingBranch,
    shippingBranchLoading,
    homeBranchLoading,
    setBranchSelectOpen
  } = useContext(BranchContext);

  /**
   * Component
   */
  const startIcon = (
    <Box position="relative">
      {homeBranchError && !shippingBranch ? (
        <WarningIcon height={20} width={20} />
      ) : (
        <BranchIcon height={20} width={20} />
      )}
      {(shippingBranchLoading || homeBranchLoading) && (
        <Box sx={branchButtonStartIconLoadingSx}>
          <CircularProgress size="2rem" />
        </Box>
      )}
    </Box>
  );

  /**
   * Render
   */
  return (
    <Button
      data-testid="change-branch-button"
      color="gray"
      iconColor="primary"
      disabled={shippingBranchLoading || homeBranchLoading}
      startIcon={startIcon}
      variant="text"
      onClick={handleChangeBranch}
      sx={branchButtonSx}
    >
      {authState?.isAuthenticated ? (
        <Box maxWidth={isSmallScreen ? 'none' : 100}>
          <Box overflow="hidden">
            {shippingBranchLoading ? (
              <Skeleton width={100} />
            ) : (
              <Typography color="primary02.main" noWrap fontSize={15}>
                {shippingBranch?.name}
              </Typography>
            )}
          </Box>
          <Box overflow="hidden">
            {shippingBranchLoading ? (
              <Skeleton width={80} />
            ) : (
              <Typography color="primary02.main" noWrap fontSize={15}>
                {shippingBranch?.city}
                {shippingBranch?.city && shippingBranch?.state
                  ? `, ${shippingBranch?.state}`
                  : ''}
              </Typography>
            )}
          </Box>
        </Box>
      ) : (
        t('common.signInToShop')
      )}
    </Button>
  );
  /**
   * Handle functions
   */
  function handleChangeBranch() {
    if (authState?.isAuthenticated) {
      setBranchSelectOpen(true);
      return;
    }
    history.push('/login');
  }
}
