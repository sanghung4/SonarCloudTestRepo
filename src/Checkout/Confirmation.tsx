import { useEffect, useMemo, useState } from 'react';
import {
  Box,
  Divider,
  Grid,
  Hidden,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { startCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import ContractCartComponent from 'Cart/CartList';
import OrderSummary from 'Cart/OrderSummary';
import CheckoutCart from 'Checkout/CheckoutCart';
import Location from 'Checkout/Location';
import CreditCardListItem from 'CreditCard/CreditCardListItem';
import { PreferredTimeEnum } from 'generated/graphql';
import { formatDate } from 'utils/dates';
import { checkStatus } from 'utils/statusMapping';
import { Step } from './util/types';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { useCheckoutContext } from './CheckoutProvider';
import { WarningIcon } from 'icons';
import { BackOrderWarningBox } from './util/styles';

export default function Confirmation() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const [locationKeys, setLocationKeys] = useState<(string | undefined)[]>([]);

  /**
   * Context
   */
  const {
    orderData,
    deliveryMethodObject,
    shipToBranch,
    orderedCart,
    orderedContract,
    step
  } = useCheckoutContext();
  const isDelivery =
    !orderData.deliveryMethod ||
    checkStatus(orderData.deliveryMethod) === 'delivery';
  const {
    selectedAccounts: { billToErpAccount }
  } = useSelectedAccountsContext();
  const isBackOrdered = useMemo(backOrderMemo, [orderData]);

  useEffect(() => {
    const unlisten = history.listen((location) => {
      if (history.action === 'PUSH' && location.key) {
        setLocationKeys([location.key]);
        return;
      }
      if (history.action === 'POP') {
        if (locationKeys[1] === location.key) {
          // Forward event
          const output = locationKeys.slice(1);
          setLocationKeys(output);
          return;
        }
        // Back event
        const output = [location.key, ...locationKeys];
        setLocationKeys(output);
        if (step === Step.CONFIRMATION) {
          history.replace('/');
        }
      }
    });
    return unlisten;
  }, [history, locationKeys, setLocationKeys, step]);

  /**
   * Components
   */
  const OrderSummaryComponent = () => (
    <OrderSummary
      page="confirmation"
      showTax
      buttonText={t('cart.viewAllOrders')}
      onButtonClick={() => history.push('/orders')}
      additionalCTAText={t('cart.continueShopping')}
      onAdditionalCTAClick={() => history.push('/')}
    />
  );

  const DividerComponent = () => (
    <Box
      ml={isSmallScreen ? -2 : -4}
      mt={isSmallScreen ? 1 : 3}
      mb={isSmallScreen ? 5 : 7}
      width={(theme) => `calc(100% + ${theme.spacing(4)})`}
    >
      <Divider />
    </Box>
  );

  const BackOrderWarningComponent = () => (
    <BackOrderWarningBox
      bgcolor="secondary06.main"
      mb={4}
      border={1}
      borderRadius={1}
      borderColor="secondary.main"
      data-testid="back-order-warning-box"
    >
      <Grid container spacing={2}>
        <Grid item xs={1.5} md={0.5}>
          <Box
            component={WarningIcon}
            marginLeft={3}
            width={32}
            color="secondary.main"
            marginTop={2.25}
          />
        </Grid>
        <Grid item xs={10.5} md={11.5}>
          <Typography
            px={2}
            py={2}
            pt={2}
            pb={1}
            fontSize="20px"
            fontWeight={500}
          >
            <b>{t('cart.backOrderWarning')}</b>
          </Typography>
          <Box px={2} pb={2} fontWeight={400}>
            <Typography>
              <b>{t('cart.backOrderWarningBoldText')}</b>
              {t('cart.backOrderWarningAddText')}
            </Typography>
          </Box>
        </Grid>
      </Grid>
    </BackOrderWarningBox>
  );

  /**
   * Render
   */
  return (
    <Box mt={isSmallScreen ? 4 : 5}>
      <Hidden mdUp>
        <Box mx={-2} mb={3}>
          <OrderSummaryComponent />
          {orderedContract && isBackOrdered && BackOrderWarningComponent()}
        </Box>
      </Hidden>
      <Grid
        className="printGridBlock"
        container
        spacing={isSmallScreen ? 2 : 0}
      >
        <Grid item xs={12} md={5} className="printGridWidth_33_inline">
          <Box component="span">
            <Typography
              variant="h5"
              fontWeight={700}
              color="textSecondary"
              display="inline"
            >
              {t('common.orderNumber')}
              {': '}
            </Typography>
            <Typography
              variant="h5"
              fontWeight={400}
              color="textSecondary"
              data-testid="confirmation-order-number"
              display="inline"
            >
              {orderData.orderNumber}
              {orderData.invoiceNumber ? `.${orderData.invoiceNumber}` : ''}
            </Typography>
          </Box>
        </Grid>
        <Grid item xs={12} md={6} className="printGridWidth_66_inline">
          <Box component="span">
            <Typography
              variant="h5"
              fontWeight={700}
              color="textSecondary"
              display="inline"
            >
              {t('common.status')}
              {': '}
            </Typography>
            <Typography
              variant="h5"
              fontWeight={400}
              color="purple.main"
              mt={isSmallScreen ? 2 : 0}
              data-testid="confirmation-order-status"
              display="inline"
            >
              {startCase(orderData.webStatus?.toLowerCase() ?? '')}
            </Typography>
          </Box>
        </Grid>
      </Grid>
      <Hidden mdDown>
        {DividerComponent()}
        {orderedContract && isBackOrdered && BackOrderWarningComponent()}
      </Hidden>

      <Box
        px={isSmallScreen ? 0 : 1}
        pb={isSmallScreen ? 0 : 2}
        py={0}
        mb={isSmallScreen ? 0 : 3}
      >
        <Grid
          className="printGridBlock"
          container
          spacing={isSmallScreen ? 3 : 6}
        >
          <Grid item xs={12} md={4} className="printGridWidth_33_inline">
            <Box mt={isSmallScreen ? 8 : 0}>
              <Box mb={3}>
                <Typography variant="h5" color="primary">
                  {t('common.orderInformation')}
                </Typography>
              </Box>
              {!!orderedContract && (
                <>
                  <Grid container spacing={4}>
                    <Grid item xs={5}>
                      <Typography noWrap variant="body1" fontWeight={500}>
                        {t('contracts.contractNumber')}
                      </Typography>
                    </Grid>
                    <Grid item xs={7}>
                      <Typography
                        noWrap
                        data-testid="confirmation-contract-number"
                      >
                        {orderedContract?.data?.contractNumber ||
                          t('common.na')}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid container spacing={4}>
                    <Grid item xs={5}>
                      <Typography noWrap variant="body1" fontWeight={500}>
                        {t('contracts.contractName')}
                      </Typography>
                    </Grid>
                    <Grid item xs={7}>
                      <Typography
                        noWrap
                        data-testid="confirmation-contract-name"
                      >
                        {orderedContract?.data?.contractDescription ||
                          t('common.na')}
                      </Typography>
                    </Grid>
                  </Grid>
                </>
              )}
              <Grid container spacing={4}>
                <Grid item xs={5}>
                  <Typography variant="body1" fontWeight={500}>
                    {t('common.jobName')}
                  </Typography>
                </Grid>
                <Grid item xs={7}>
                  <Typography noWrap data-testid="confirmation-job-name">
                    {orderData.shipToName}
                  </Typography>
                </Grid>
              </Grid>
              <Grid container spacing={4}>
                <Grid item xs={5}>
                  <Typography variant="body1" fontWeight={500}>
                    {t('common.orderDate')}
                  </Typography>
                </Grid>
                <Grid item xs={7}>
                  <Typography
                    variant="body1"
                    data-testid="confirmation-order-date"
                  >
                    {orderData.orderDate}
                  </Typography>
                </Grid>
              </Grid>
              <Grid container spacing={4}>
                <Grid item xs={5}>
                  <Typography variant="body1" fontWeight={500}>
                    {t('common.orderedBy')}
                  </Typography>
                </Grid>
                <Grid item xs={7}>
                  <Typography
                    variant="body1"
                    data-testid="confirmation-ordered-by"
                  >
                    {orderData.orderedBy}
                  </Typography>
                </Grid>
              </Grid>
              <Grid container spacing={4}>
                <Grid item xs={5}>
                  <Typography variant="body1" fontWeight={500}>
                    {t('common.poNumber')}
                  </Typography>
                </Grid>
                <Grid item xs={7}>
                  <Typography
                    variant="body1"
                    data-testid="confirmation-order-po-number"
                  >
                    {orderData.customerPO}
                  </Typography>
                </Grid>
              </Grid>
              {!!orderData.creditCard && (
                <Grid container spacing={4}>
                  <Grid item xs={5}>
                    <Typography variant="body1" fontWeight={500}>
                      {t('common.payment')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7}>
                    <CreditCardListItem
                      creditCard={orderData.creditCard}
                      expired={false}
                      hideType
                      hideExpired
                      noEmphasis
                    />
                  </Grid>
                </Grid>
              )}
            </Box>
          </Grid>
          <Grid item xs={12} md={4} className="printGridWidth_66_inline">
            <Box mt={isSmallScreen ? 8 : 0}>
              <Box mb={3}>
                <Typography variant="h5" color="primary">
                  {t(
                    isDelivery
                      ? `cart.deliveryInformation`
                      : 'cart.willCallInformation'
                  )}
                </Typography>
              </Box>
              <Grid container spacing={4}>
                <Grid item xs={5}>
                  <Typography variant="body1" fontWeight={500}>
                    {t('common.shipTo')}
                  </Typography>
                </Grid>
                <Grid item xs={7} data-testid="confirmation-ship-to">
                  <Typography variant="body1">
                    {orderData.deliveryMethod === 'OUR_TRUCK' ||
                    orderData.deliveryMethod === 'Our Truck'
                      ? orderData.shipAddress?.streetLineOne
                      : orderData.branchInfo?.streetLineOne}
                  </Typography>
                  <Typography variant="body1">
                    {orderData.deliveryMethod === 'OUR_TRUCK' ||
                    orderData.deliveryMethod === 'Our Truck'
                      ? orderData.shipAddress?.streetLineTwo
                      : orderData.branchInfo?.streetLineTwo}
                  </Typography>
                  <Typography variant="body1">
                    {orderData.deliveryMethod === 'OUR_TRUCK' ||
                    orderData.deliveryMethod === 'Our Truck'
                      ? orderData.shipAddress?.streetLineThree
                      : orderData.branchInfo?.streetLineThree}
                  </Typography>
                  <Typography variant="body1">
                    {`${
                      orderData.deliveryMethod === 'OUR_TRUCK' ||
                      orderData.deliveryMethod === 'Our Truck'
                        ? orderData.shipAddress?.city
                        : orderData.branchInfo?.city
                    }, ${
                      orderData.deliveryMethod === 'OUR_TRUCK' ||
                      orderData.deliveryMethod === 'Our Truck'
                        ? orderData.shipAddress?.state
                        : orderData.branchInfo?.state
                    } ${
                      orderData.deliveryMethod === 'OUR_TRUCK' ||
                      orderData.deliveryMethod === 'Our Truck'
                        ? orderData.shipAddress?.postalCode
                        : orderData.branchInfo?.postalCode
                    }`}
                  </Typography>
                  <Typography variant="body1">
                    {orderedContract && orderData.deliveryMethod === 'Our Truck'
                      ? billToErpAccount?.phoneNumber ?? ''
                      : shipToBranch?.phone ?? ''}
                  </Typography>
                </Grid>
              </Grid>
              <Grid container spacing={4}>
                <Grid item xs={5}>
                  <Typography variant="body1" fontWeight={500}>
                    {t('common.shipDate')}
                  </Typography>
                </Grid>
                <Grid item xs={7}>
                  <Typography
                    variant="body1"
                    data-testid="confirmation-ship-date"
                  >
                    {orderedContract
                      ? deliveryMethodObject?.preferredDate
                        ? formatDate(deliveryMethodObject.preferredDate)
                        : ''
                      : orderData.shipDate}
                    {!!deliveryMethodObject?.preferredTime && (
                      <Box ml={1} component="span" color="primary02.main">
                        {deliveryMethodObject.preferredTime ===
                        PreferredTimeEnum.Asap
                          ? deliveryMethodObject.preferredTime
                          : startCase(
                              deliveryMethodObject.preferredTime.toLowerCase()
                            )}
                      </Box>
                    )}
                  </Typography>
                </Grid>
              </Grid>
              <Grid container spacing={4}>
                <Grid item xs={5}>
                  <Typography width={150} variant="body1" fontWeight={500}>
                    {t('common.deliveryMethod')}
                  </Typography>
                </Grid>
                <Grid item xs={7}>
                  <Typography
                    variant="body1"
                    data-testid="confirmation-delivery-method"
                  >
                    {orderData.deliveryMethod
                      ? checkStatus(orderData.deliveryMethod)
                        ? t(`common.${checkStatus(orderData.deliveryMethod)}`)
                        : t('common.delivery')
                      : '-'}
                  </Typography>
                </Grid>
              </Grid>
            </Box>
          </Grid>
          <Grid
            item
            xs={12}
            md={4}
            className="printGridWidth_100 printBreakPage"
          >
            <Box mt={isSmallScreen ? 8 : 0}>
              <Box mb={3}>
                <Typography variant="h5" color="primary">
                  {t(`cart.billingInformation`)}
                </Typography>
              </Box>
              <Grid item xs={12} md={10}>
                <Location
                  location={billToErpAccount}
                  includePhone={!!orderedContract}
                />
              </Grid>
            </Box>
            <Box mt={isSmallScreen ? 8 : 4}>
              <Box mb={3}>
                <Typography variant="h5" color="primary">
                  {t(
                    isDelivery
                      ? 'cart.deliveryInstructions'
                      : 'cart.willCallInstructions'
                  )}
                </Typography>
              </Box>
              <Typography
                variant="body1"
                data-testid="confirmation-special-instructions"
              >
                {orderData.specialInstructions ?? t('common.na')}
              </Typography>
            </Box>
          </Grid>
        </Grid>
      </Box>
      <Hidden mdDown>{DividerComponent()}</Hidden>
      {!(orderedContract && !isSmallScreen) && (
        <Box mt={0} mb={2} className="printBreakPage">
          <Typography variant="h5" color="primary">
            {t('cart.orderDetails')}
          </Typography>
        </Box>
      )}
      <Hidden mdDown>
        <Grid container className="printGridBlock">
          <Grid item xs={12} md={9} pr={3} className="printGridWidth_100">
            {orderedContract && orderedCart ? (
              <ContractCartComponent cart={orderedCart} readOnly />
            ) : (
              <CheckoutCart />
            )}
          </Grid>
          <Grid
            item
            xs={12}
            md={3}
            pl={3}
            className="printGridWidth_50 printBreakPage"
          >
            <OrderSummaryComponent />
          </Grid>
        </Grid>
      </Hidden>
      <Hidden mdUp>
        {DividerComponent()}
        <Box mx={-2} mt={-5}>
          {orderedContract && orderedCart ? (
            <ContractCartComponent cart={orderedCart} readOnly />
          ) : (
            <CheckoutCart />
          )}
        </Box>
      </Hidden>
    </Box>
  );

  /**
   * Memo Definitions
   */
  function backOrderMemo() {
    return !!orderData?.lineItems?.filter((i) => {
      return !!i.backOrderedQuantity;
    })?.length;
  }
}
