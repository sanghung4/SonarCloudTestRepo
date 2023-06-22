import { Fragment, ReactNode, useContext } from 'react';

import {
  Box,
  DropdownButton,
  Link,
  MenuItem
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import {
  desktopSubheaderStyles,
  getNavData,
  NavData
} from 'common/Header/util';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

type Props = {
  item: keyof NavData;
  children: ReactNode;
};

export function DesktopSubheaderMenu(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isWaterworks } = useDomainInfo();

  /**
   * Context
   */
  const { activeFeatures, authState, ecommUser, profile } =
    useContext(AuthContext);
  const {
    selectedAccounts: { erpSystemName }
  } = useSelectedAccountsContext();

  /**
   * Data
   */
  const list = getNavData({
    activeFeatures,
    authState,
    ecommUser,
    erpSystemName,
    isWaterworks,
    profile,
    t
  })[props.item];
  /**
   * Render
   */
  return (
    <desktopSubheaderStyles.GridStyledLinks item>
      <DropdownButton
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'left'
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'left'
        }}
        content={(_, setOpen) => {
          const close = () => setOpen(false);
          return (
            <Box py={2}>
              {list.map((item, index) => (
                <Fragment key={`desktop-nav-${props.item}-${index}`}>
                  {item.condition && (
                    <Link
                      underline="none"
                      component={RouterLink}
                      onClick={close}
                      to={item.to}
                      data-testid={`desktop-nav-${item.id}`}
                    >
                      <MenuItem>
                        {item.name}
                        {item.comingSoon && ` - ${t('common.comingSoon')}`}
                      </MenuItem>
                    </Link>
                  )}
                </Fragment>
              ))}
            </Box>
          );
        }}
      >
        {props.children}
      </DropdownButton>
    </desktopSubheaderStyles.GridStyledLinks>
  );
}
