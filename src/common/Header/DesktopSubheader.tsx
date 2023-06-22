import { useContext } from 'react';

import {
  Container,
  Grid,
  Link,
  Toolbar
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import CategoriesButtonWrapper from 'Categories/CategoriesButtonWrapper';
import SwitchAccounts from 'common/Header/SwitchAccounts';
import {
  desktopSubheaderStyles,
  DesktopSubheaderMenu
} from 'common/Header/util';
import { MaxIcon } from 'icons';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { ErpSystemEnum } from 'generated/graphql';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export default function DesktopSubheader() {
  /**
   * Hooks
   */
  const { t } = useTranslation();
  const { profile } = useContext(AuthContext);
  const { isWaterworks } = useDomainInfo();

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();
  const isMincron = selectedAccounts.erpSystemName === ErpSystemEnum.Mincron;

  /**
   * Render
   */
  return (
    <Toolbar disableGutters sx={desktopSubheaderStyles.toolbarSx}>
      <Container maxWidth="lg">
        <Grid container alignItems="center">
          {/* ===== Browse Products ===== */}
          {!isWaterworks && !isMincron && (
            <Grid item>
              <CategoriesButtonWrapper />
            </Grid>
          )}
          {/* ===== Brands ===== */}
          {!isWaterworks && !isMincron && (
            <desktopSubheaderStyles.GridStyledLinks item>
              <Link
                component={RouterLink}
                underline="none"
                to="/brands"
                data-testid="nav-brands"
              >
                {t('common.brands')}
              </Link>
            </desktopSubheaderStyles.GridStyledLinks>
          )}
          {/* ===== MAX ===== */}
          <DesktopSubheaderMenu item="max">
            <MaxIcon />
          </DesktopSubheaderMenu>
          {/* ===== Resources ===== */}
          <DesktopSubheaderMenu item="resources">
            {t('common.resources')}
          </DesktopSubheaderMenu>
          {!!profile?.userId && (
            <Grid item ml="auto" mr={-1}>
              <SwitchAccounts />
            </Grid>
          )}
        </Grid>
      </Container>
    </Toolbar>
  );
}
