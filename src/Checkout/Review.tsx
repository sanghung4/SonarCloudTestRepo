import {
  Box,
  Grid,
  Hidden,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { kebabCase, startCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import CartList from 'Cart/CartList';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import Heading from 'Checkout/Heading';
import Location from 'Checkout/Location';
import { CustomAddressInput, Step } from 'Checkout/util/types';
import CreditCardListItem from 'CreditCard/CreditCardListItem';
import Loader from 'common/Loader';
import {
  Branch,
  Delivery,
  PaymentMethodTypeEnum,
  PreferredTimeEnum,
  WillCall
} from 'generated/graphql';
import { EditIcon } from 'icons';
import { formatDate } from 'utils/dates';
import { useCartContext } from 'providers/CartProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export default function Review() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { cart, contract, contractBranch } = useCartContext();
  const {
    paymentData,
    setStep,
    shipToBranch,
    deliveryMethodObject,
    isDelivery
  } = useCheckoutContext();
  const {
    selectedAccounts: { billToErpAccount }
  } = useSelectedAccountsContext();

  const isCC =
    paymentData.paymentMethodType === PaymentMethodTypeEnum.Creditcard;
  const location = isDelivery
    ? contract
      ? contract?.data
      : ((deliveryMethodObject as Delivery)?.address as CustomAddressInput)
    : contract
    ? (contractBranch as Branch)
    : shipToBranch;

  /**
   * Render
   */
  return !cart ? (
    <Loader />
  ) : (
    <>
      <Heading
        title={t('common.paymentInformation')}
        actionIcon={<EditIcon />}
        actionText={t('common.edit')}
        actionCb={() => setStep(Step.PAYMENT)}
        dataTestId="edit-payment-info"
      />
      <Grid container mb={isSmallScreen ? 2 : 8}>
        <Grid item xs={isCC ? 12 : 6} md={3}>
          <Typography
            component="span"
            variant="body1"
            color="textSecondary"
            fontWeight={isSmallScreen ? 500 : 400}
          >
            {t('cart.paymentMethod')}
          </Typography>
        </Grid>
        <Grid item xs={isCC ? 12 : 6} md={6}>
          {cart.creditCard && isCC ? (
            <CreditCardListItem creditCard={cart.creditCard} expired={false} />
          ) : (
            !!paymentData.paymentMethodType && (
              <Typography
                variant="body1"
                data-testid={kebabCase(
                  `${paymentData.paymentMethodType}-payment-method`
                )}
              >
                {t(`cart.${paymentData.paymentMethodType?.toLowerCase()}`)}
              </Typography>
            )
          )}
        </Grid>
      </Grid>
      <Grid container mb={isSmallScreen ? 2 : 8}>
        <Grid item xs={isCC ? 12 : 6} md={3}>
          <Typography
            component="span"
            variant="body1"
            color="textSecondary"
            fontWeight={isSmallScreen ? 500 : 400}
          >
            <Box
              component="span"
              fontWeight={isSmallScreen ? 500 : 400}
              data-testid="cart-poNumber"
            >
              {t('cart.poNumber')}
            </Box>
          </Typography>
        </Grid>
        <Grid item xs={isCC ? 12 : 6} md={6}>
          <Typography variant="body1" pt={isCC ? 1 : 0}>
            {paymentData.poNumber}
          </Typography>
        </Grid>
      </Grid>
      <Grid container mb={isSmallScreen ? 2 : 8}>
        <Grid item xs={12} md={6}>
          <Box mr={isSmallScreen ? 0 : 0.75}>
            <Heading title={t('cart.billingInformation')} />
            <Location location={billToErpAccount} includePhone={!!contract} />
          </Box>
        </Grid>
        <Grid item xs={12} md={6}>
          <Box ml={isSmallScreen ? 0 : 4.75}>
            <Heading
              title={t('cart.deliveryInformation')}
              actionIcon={<EditIcon />}
              actionText={t('common.edit')}
              actionCb={() => setStep(Step.INFO)}
              dataTestId="edit-delivery-info"
            />
            <Grid container>
              <Grid item xs={12} md={8}>
                <Typography variant="body1" fontWeight={700}>
                  {t('common.shipTo')}
                </Typography>
                <Location location={location} includePhone={!!contract} />
              </Grid>
              <Grid item xs={12} md={4}>
                {!!(
                  deliveryMethodObject?.preferredDate ||
                  deliveryMethodObject?.preferredTime
                ) && (
                  <Box mt={isSmallScreen ? 6 : 0}>
                    <Typography variant="body1" fontWeight={700}>
                      {isDelivery
                        ? t('cart.deliveryDate')
                        : t('cart.willCallDetails')}
                    </Typography>
                    {!!deliveryMethodObject.preferredDate && (
                      <Typography variant="body1">
                        {formatDate(deliveryMethodObject.preferredDate)}
                      </Typography>
                    )}
                    {!!deliveryMethodObject.preferredTime && (
                      <Typography variant="body1">
                        {deliveryMethodObject.preferredTime ===
                        PreferredTimeEnum.Asap
                          ? PreferredTimeEnum.Asap
                          : startCase(
                              deliveryMethodObject.preferredTime.toLowerCase()
                            )}
                      </Typography>
                    )}
                  </Box>
                )}
              </Grid>
            </Grid>
          </Box>
        </Grid>
      </Grid>
      <Box mb={isSmallScreen ? 2 : 8}>
        <Heading
          title={
            <Box
              component="span"
              maxWidth={isSmallScreen ? '80%' : `${200 / 3}%`}
              display="block"
            >
              {t(
                isDelivery
                  ? `cart.deliveryInstructions`
                  : 'cart.willCallInstructions'
              )}
            </Box>
          }
          actionIcon={<EditIcon />}
          actionText={t('common.edit')}
          actionCb={() => setStep(Step.INFO)}
          dataTestId="edit-special-instructions"
        />
        <Typography variant="body1" data-testid="spl-instructions">
          {((deliveryMethodObject as Delivery)?.deliveryInstructions
            ? (deliveryMethodObject as Delivery).deliveryInstructions
            : (deliveryMethodObject as WillCall)?.pickupInstructions) ||
            t('common.na')}
        </Typography>
      </Box>
      <Box mb={2}>
        <Heading
          title={t('cart.orderDetails')}
          actionIcon={<EditIcon />}
          actionText={t('cart.editCart')}
          actionCb={() => history.push('/cart')}
          dataTestId="edit-cart"
        />
        <Hidden mdDown>
          <Typography
            variant="body1"
            color="primary02.main"
            mt={-4}
            mb={3}
            data-testid="cart-item-count"
          >
            {cart.products?.length ?? 0}{' '}
            {t('cart.itemInCart', {
              count: cart.products?.length ?? 0
            })}
          </Typography>
          <CartList cart={cart} readOnly />
        </Hidden>
        <Hidden mdUp>
          <Box mx={-2} mt={-5}>
            <CartList cart={cart} readOnly />
          </Box>
        </Hidden>
      </Box>
    </>
  );
}
