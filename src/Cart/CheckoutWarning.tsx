import { Box, useScreenSize } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { WarningIcon } from 'icons';
import {
  CheckoutWarningContainer,
  CheckoutWarningTypography,
  CheckoutWarningMobileTypography
} from 'Cart/util/styled';

export default function Warning() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  return (
    <CheckoutWarningContainer data-testid="checkout-warning-message">
      <Box component={WarningIcon} ml={1} color="secondary.main" />
      <Box flex={1} ml={1}>
        {isSmallScreen ? (
          <CheckoutWarningMobileTypography>
            {t('cart.checkoutWarning')}
          </CheckoutWarningMobileTypography>
        ) : (
          <CheckoutWarningTypography>
            {t('cart.checkoutWarning')}
          </CheckoutWarningTypography>
        )}
      </Box>
    </CheckoutWarningContainer>
  );
}
