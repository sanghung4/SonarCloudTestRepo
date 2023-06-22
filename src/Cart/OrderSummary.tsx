import { ReactNode, useContext } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Divider,
  Tooltip,
  Typography,
  alpha,
  useScreenSize,
  Alert,
  Skeleton
} from '@dialexa/reece-component-library';
import { OnClick } from '@reece/global-types';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';

import { checkIfOver10Mil } from 'Cart/util';
import { useOrderSummary } from 'Cart/util/useOrderSummary';
import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import ConditionalWrapper from 'common/ConditionalWrapper';
import { DeliveryMethodEnum, OrderPreviewResponse } from 'generated/graphql';
import { WarningIcon } from 'icons';
import { format } from 'utils/currency';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { useCartContext } from 'providers/CartProvider';
import { standardUserRoles } from 'User';

export type OrderSummaryProps = {
  page: 'cart' | 'checkout' | 'confirmation' | 'approval';
  showTax?: boolean;
  hideButton?: boolean;
  disableButton?: boolean;
  buttonText?: ReactNode;
  onButtonClick?: OnClick;
  additionalCTAText?: ReactNode;
  onAdditionalCTAClick?: OnClick;
  approveData?: OrderPreviewResponse;
  orderPreviewLoading?: boolean;
  approveOrderLoading?: { approve: boolean; reject: boolean };
};

export default function OrderSummary(props: OrderSummaryProps) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { subTotal, tax, orderTotal } = useOrderSummary({
    page: props.page,
    showTax: props.showTax
  });

  /**
   * Context
   */
  const { cart, cartLoading, checkingOutWithQuote } = useCartContext();
  const { orderedCart } = useCheckoutContext();
  const { profile, ecommUser } = useContext(AuthContext);
  const {
    selectedAccounts: { shipToErpAccount, billToErpAccount }
  } = useSelectedAccountsContext();
  const isEmployee = profile?.isEmployee;
  const productCount = cart?.products?.length ?? 0;
  const disableForStandardCashCustomer = Boolean(
    // istanbul ignore next
    standardUserRoles.includes(ecommUser?.role?.name ?? '') &&
      billToErpAccount?.alwaysCod &&
      cart?.deliveryMethod === DeliveryMethodEnum.Delivery
  );

  /**
   * Render
   */
  return (
    <Box
      my={isSmallScreen ? 1 : undefined}
      position={isSmallScreen ? undefined : 'sticky'}
      top="0"
    >
      <Typography
        variant="h5"
        color="primary"
        py={3}
        px={isSmallScreen || props.page === 'approval' ? 0 : 3}
      >
        {t('cart.orderSummary')}
      </Typography>
      {Boolean(cart || orderedCart) && (
        <Box
          p={3}
          pb={2}
          width={1}
          borderRadius="10px"
          bgcolor={(theme) => alpha(theme.palette.primary02.main, 0.05)}
          data-testid="ordersummary-component"
        >
          {props.orderPreviewLoading ? (
            <Box data-testid="ordersummary-skeleton">
              <Typography variant="body1">
                <Skeleton width={150} />
              </Typography>
              <Typography variant="body1">
                <Skeleton width={200} />
              </Typography>
              <Typography variant="body1">
                <Skeleton width={200} />
              </Typography>
              <Box py={2}>
                <Divider />
              </Box>
              <Typography variant="body1">
                <Skeleton width={100} />
              </Typography>
            </Box>
          ) : (
            <>
              <Box
                pb={1}
                display="flex"
                justifyContent="space-between"
                alignItems="center"
              >
                <Typography color="primary" variant="body1" fontWeight={500}>
                  {t('cart.subtotal')}
                </Typography>
                <Typography
                  color="primary"
                  variant="body1"
                  data-testid="cart-subtotal"
                >
                  {props.approveData?.subTotal
                    ? format(parseFloat(props.approveData.subTotal))
                    : subTotal}
                </Typography>
              </Box>
              <Box
                display="flex"
                justifyContent="space-between"
                alignItems="center"
                pb={1}
              >
                <Typography color="primary" variant="caption">
                  {t('cart.shippingHandling')}
                </Typography>
                <Typography
                  color="primary"
                  variant="caption"
                  data-testid="cart-shipping-handling"
                >
                  {productCount || props.page === 'confirmation'
                    ? t('cart.free')
                    : '—'}
                </Typography>
              </Box>
              {props.showTax && (
                <Box
                  display="flex"
                  justifyContent="space-between"
                  alignItems="center"
                >
                  <Typography color="primary" variant="caption">
                    {t('common.tax')}
                  </Typography>
                  <Typography color="primary" variant="caption">
                    {props.approveData?.tax
                      ? format(parseFloat(props.approveData.tax))
                      : tax}
                  </Typography>
                </Box>
              )}
              <Box component={Divider} mx={-1} my={2} />
              <Box
                display="flex"
                justifyContent="space-between"
                alignItems="center"
              >
                <Typography color="primary" variant="body1" fontWeight={500}>
                  {!props.showTax
                    ? t('cart.totalBeforeTax')
                    : t('common.total')}
                </Typography>
                <Typography
                  color="primary"
                  variant="body1"
                  data-testid="cart-total-before-tax"
                >
                  {props.approveData?.orderTotal
                    ? format(parseFloat(props.approveData.orderTotal))
                    : orderTotal}
                </Typography>
              </Box>
            </>
          )}
          {!props.hideButton && (
            <Box pt={3} pb={2}>
              <ConditionalWrapper
                condition={Boolean(isEmployee)}
                wrapper={employeeTooltip}
              >
                <Button
                  style={{ width: '100%' }}
                  variant="primary"
                  data-testid="checkout-top-button"
                  disabled={
                    isEmployee ||
                    ((!productCount || cartLoading || !cart?.deliveryMethod) &&
                      props.page !== 'confirmation' &&
                      props.page !== 'approval' &&
                      !checkingOutWithQuote) ||
                    props.disableButton ||
                    shipToErpAccount?.creditHold ||
                    props.approveOrderLoading?.approve ||
                    checkIfOver10Mil(subTotal) ||
                    disableForStandardCashCustomer
                  }
                  onClick={props.onButtonClick}
                >
                  {cartLoading || props.approveOrderLoading?.approve ? (
                    <CircularProgress color="primary02.main" size={24} />
                  ) : (
                    props.buttonText
                  )}
                </Button>
              </ConditionalWrapper>
              {shipToErpAccount?.creditHold ? (
                <Alert
                  icon={<WarningIcon />}
                  sx={(theme) => ({
                    color: 'text.secondary',
                    bgcolor: 'inherit',
                    ...theme.typography.caption,
                    alignItems: 'center',
                    '& .MuiAlert-icon': {
                      color: 'secondary.main'
                    },
                    padding: 0
                  })}
                >
                  {t('common.accountHold')}
                </Alert>
              ) : null}
            </Box>
          )}
          {!!(props.additionalCTAText && props.onAdditionalCTAClick) && (
            <Button
              variant="text"
              color="primaryLight"
              fullWidth
              data-testid="additional-cta-button"
              onClick={props.onAdditionalCTAClick}
            >
              {cartLoading || props.approveOrderLoading?.reject ? (
                <CircularProgress color="primary02.main" size={24} />
              ) : (
                props.additionalCTAText
              )}
            </Button>
          )}
        </Box>
      )}
    </Box>
  );
}

// TODO: Remove this and revert back to using EmployeeTooltip component when standard access cash customers can checkout delivery with credit card
// Temporary solution to adding variable tooltips for the disabled button
// const CustomTooltip = (
//   title: string = 'Checkout is currently disabled for employees.'
// ) => {
//   return (children: JSX.Element) => (
//     <Tooltip title={title} placement="top">
//       <span>{children}</span>
//     </Tooltip>
//   );
// };

export const employeeTooltip = (children: JSX.Element) => (
  <Tooltip
    title="Checkout is currently disabled for employees."
    placement="top"
  >
    <span>{children}</span>
  </Tooltip>
);
