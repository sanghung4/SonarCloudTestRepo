import {
  Box,
  Divider,
  Link,
  useScreenSize,
  useTheme
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useLocation } from 'react-router-dom';

import useFooterStyles from 'common/Footer/util/useFooterStyles';
import { testIds } from 'test-utils/testIds';

const year = new Date().getFullYear();

export default function FooterLegal() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { typography } = useTheme();
  const styles = useFooterStyles();
  const { pathname } = useLocation();

  /**
   * Render
   */
  return (
    <Box
      display="flex"
      justifyContent={isSmallScreen ? 'center' : 'space-between'}
      alignItems="center"
      flexDirection={isSmallScreen ? 'column' : 'row'}
      color="mediumGray.main"
      fontSize={12}
      lineHeight={typography.pxToRem(18)}
      sx={styles.bottomBarSx}
    >
      {!pathname.includes('select-accounts') && (
        <Box
          display="flex"
          flex="1"
          justifyContent={isSmallScreen ? 'space-evenly' : 'flex-start'}
          alignItems="center"
          component="nav"
          flexWrap="nowrap"
          sx={styles.bottomBarUrlWrapperSx}
          data-testid={testIds.Footer.legalLinks}
        >
          <Link
            to="/terms-of-access"
            component={RouterLink}
            color="inherit"
            underline="none"
            data-testid="terms-of-access-footer"
          >
            {t('legal.termsOfAccess')}
          </Link>
          <Divider orientation="vertical" />
          <Link
            to="/privacy-policy"
            component={RouterLink}
            color="inherit"
            underline="none"
            data-testid="privacy-policy-footer"
          >
            {t('legal.privacyPolicy')}
          </Link>
          <Divider orientation="vertical" />
          <Link
            to="/terms-of-sale"
            component={RouterLink}
            color="inherit"
            underline="none"
            data-testid="terms-of-sale-footer"
          >
            {t('legal.termsOfSale')}
          </Link>
          <Divider orientation="vertical" />
          <Link
            to="/do-not-sell-my-info"
            component={RouterLink}
            color="inherit"
            underline="none"
            data-testid="do-not-sell-my-info-footer"
          >
            {t('legal.doNotSellMyInfo')}
          </Link>
        </Box>
      )}
      <Box flexShrink={0} marginLeft="auto">
        &copy;&nbsp;
        {t('common.copyright', { year })}
      </Box>
    </Box>
  );
}
