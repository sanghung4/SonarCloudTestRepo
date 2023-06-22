import { useContext, useMemo } from 'react';

import {
  Box,
  Divider,
  Grid,
  Link,
  Tooltip,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useHistory, useLocation } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';

import { switchAccountsStyles } from 'common/Header/util';
import { useUserAccountsQuery } from 'generated/graphql';
import { truncateText } from 'utils/strings';
import { WarningIcon } from 'icons';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { useCartContext } from 'providers/CartProvider';

/**
 * Styles
 */
const { ChangeButton, tooltipComponentProps } = switchAccountsStyles;

/**
 * Types
 */
type Props = {
  onChange?: () => void;
};

function SwitchAccounts(props: Props) {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const location = useLocation();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { authState, profile } = useContext(AuthContext);
  const { contract } = useCartContext();
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: userAccountsQuery } = useUserAccountsQuery({
    skip: !profile?.userId,
    variables: { userId: profile?.userId },
    onError: (error) => history.push('/error', { error })
  });

  /**
   * Memo
   */
  const userData = useMemo(userDataMemo, [
    contract,
    selectedAccounts,
    userAccountsQuery
  ]);

  /**
   * Callbacks
   */
  const onSwitchPressed = () => {
    history.push('/select-accounts');
    props.onChange?.();
  };

  /**
   * Render
   */
  if (
    !authState?.isAuthenticated ||
    location.pathname === '/select-accounts' ||
    !selectedAccounts.billTo ||
    (!userData.availableShipTos.length &&
      userData.availableBillTos.length === 1)
  ) {
    return null;
  }

  if (isSmallScreen) {
    return (
      <>
        <Box pt={2}>
          <Grid container>
            <Grid item xs={8}>
              <Box pb={1}>
                <Typography color="primary" variant="caption" fontWeight={500}>
                  {t('common.account')}:
                </Typography>
              </Box>
              <Box pb={1} data-testid="mobile-menu-bill-to">
                <Typography noWrap variant="body2">
                  {selectedAccounts.billTo?.name ?? ''}
                </Typography>
              </Box>
              <Box pb={1}>
                <Typography color="primary" variant="caption" fontWeight={500}>
                  {t('common.job')}:
                </Typography>
              </Box>
              <Box pb={3} data-testid="mobile-menu-ship-to">
                <Typography noWrap variant="body2">
                  {userData.shipToName}
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={4}>
              {userData.showButton && (
                <Box pl={7}>
                  <ChangeButton
                    sx={{ fontSize: '0.8rem' }}
                    color="primaryLight"
                    data-testid="change-button"
                    variant="inline"
                    fullWidth={false}
                    onClick={onSwitchPressed}
                  >
                    {t('common.change')}
                  </ChangeButton>
                </Box>
              )}
            </Grid>
          </Grid>
        </Box>
        <Divider />
      </>
    );
  }

  return (
    <Box display="flex" alignItems="center">
      {!!(
        selectedAccounts.billToErpAccount?.creditHold ||
        selectedAccounts.shipToErpAccount?.creditHold
      ) && (
        <Tooltip
          title={t('common.accountHold') as string}
          componentsProps={tooltipComponentProps}
        >
          <Box
            component={WarningIcon}
            mr={1}
            color="secondary.main"
            height={24}
            width={24}
          />
        </Tooltip>
      )}
      {!!userData.shipToName && (
        <Tooltip
          placement="top"
          componentsProps={tooltipComponentProps}
          title={
            <>
              <Typography
                variant="caption"
                component="p"
                fontSize={14}
                lineHeight="22px"
                data-testid="header-tooltip-bill-to"
              >
                {truncateText(selectedAccounts.billTo?.name ?? '', 45)}
              </Typography>
              {selectedAccounts.billTo?.name !== userData.shipToName && (
                <Typography
                  variant="caption"
                  component="p"
                  fontSize={14}
                  lineHeight="22px"
                  data-testid="header-tooltip-ship-to"
                >
                  {truncateText(userData.shipToName, 45)}
                </Typography>
              )}
            </>
          }
        >
          <Typography
            variant="caption"
            fontSize={14}
            lineHeight="22px"
            data-testid="header-ship-to"
          >
            <Box component="span" fontWeight={700}>
              {t('common.job')}
              {': '}
            </Box>
            {truncateText(userData.shipToName, 30)}
          </Typography>
        </Tooltip>
      )}
      {userData.showButton && (
        <Link
          component={RouterLink}
          data-testid="change-link"
          to="/select-accounts"
          underline="none"
          sx={{ p: 0, mr: 0, ml: 1 }}
        >
          <ChangeButton
            data-testid="switch-account-button"
            color="primaryLight"
            variant="text"
            size="small"
          >
            {t('common.switchAccount')}
          </ChangeButton>
        </Link>
      )}
    </Box>
  );

  function userDataMemo() {
    const availableBillTos = userAccountsQuery?.userAccounts ?? [];
    const selectedBillTo = availableBillTos.find(
      (item) => item?.id === selectedAccounts?.billTo?.id
    );
    const availableShipTos = selectedBillTo?.shipTos ?? [];

    const showButton =
      (availableBillTos.length > 1 || availableShipTos.length > 1) && !contract;

    const contractJobName = contract?.data?.jobName;
    const contractAddress =
      contract?.data?.accountInformation?.shipToAddress?.address1;

    const shipToName =
      contractJobName && contractAddress
        ? `${contractJobName} ${contractAddress}`
        : selectedAccounts.shipTo?.name ?? '';

    return {
      availableBillTos,
      availableShipTos,
      showButton,
      shipToName
    };
  }
}

export default SwitchAccounts;
