import { useContext } from 'react';

import {
  Badge,
  Box,
  Button,
  IconButton,
  useScreenSize
} from '@dialexa/reece-component-library';
import { CircularProgress } from '@mui/material';
import { useLocation, useHistory } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';

import { cartButtonStyles } from 'common/Header/util';
import { CartIcon } from 'icons';
import { useCartContext } from 'providers/CartProvider';

export default function CartButton() {
  /**
   * Custom hooks
   */
  const location = useLocation();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { cartLoading, itemLoading } = useCartContext();

  /**
   * Callbacks
   */
  const goToCart = useGoToCart();

  /**
   * Render
   */
  if (location.pathname !== '/select-accounts') {
    return isSmallScreen ? (
      <Box position="relative" ml={3}>
        {!!(cartLoading || itemLoading) && (
          <cartButtonStyles.CartLoaderComponent>
            <CircularProgress size="2rem" />
          </cartButtonStyles.CartLoaderComponent>
        )}
        <IconButton
          onClick={goToCart}
          size="large"
          data-testid="go-to-cart-button-mobile"
        >
          <CartButtonContent />
        </IconButton>
      </Box>
    ) : (
      <Button
        data-testid="go-to-cart-button-desktop"
        color="gray"
        iconColor="primary"
        startIcon={<CartButtonContent />}
        variant="text"
        onClick={goToCart}
        sx={cartButtonStyles.cartButtonSx}
      >
        {t('common.cart')}
      </Button>
    );
  }
  return null;
}

export function CartButtonContent() {
  /**
   * Hooks / Consts
   */
  const { isSmallScreen } = useScreenSize();
  const { cartLoading, itemCount, itemLoading } = useCartContext();
  const invisible = !!(cartLoading || itemLoading || !itemCount);
  const isLoading = !!(!isSmallScreen && (cartLoading || itemLoading));

  /**
   * Render
   */
  return (
    <Badge
      showZero={false}
      badgeContent={itemCount}
      invisible={invisible}
      max={999}
    >
      <CartIcon height={20} width={20} />
      {isLoading && (
        <cartButtonStyles.CartLoaderComponent>
          <CircularProgress size="2rem" />
        </cartButtonStyles.CartLoaderComponent>
      )}
    </Badge>
  );
}

export function useGoToCart() {
  /**
   * Hooks
   */
  const history = useHistory();
  const { authState } = useContext(AuthContext);
  const { checkingOutWithQuote, clearQuote, contract } = useCartContext();

  /**
   * Output
   */
  return () => {
    if (checkingOutWithQuote) {
      clearQuote();
    }
    const showNavAlert = authState?.isAuthenticated && contract;
    history.push(
      authState?.isAuthenticated ? '/cart' : '/login',
      showNavAlert ? { canShowNavAlert: true } : {}
    );
  };
}
