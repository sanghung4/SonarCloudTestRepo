import { useCallback, useContext, useEffect, useMemo, useState } from 'react';

import {
  Box,
  Button as MaterialButton,
  Container,
  Grid,
  Hidden,
  Step as StepperStep,
  StepLabel,
  Stepper,
  Tooltip,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import Button from 'components/Button';
import { useTranslation } from 'react-i18next';
import { Redirect, useHistory, useLocation } from 'react-router-dom';
import { isUndefined, omit } from 'lodash-es';
import { Location, Action } from 'history';

import { AuthContext } from 'AuthProvider';

import OrderSummary from 'Cart/OrderSummary';
import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import Heading from 'Checkout/Heading';
import { Step } from 'Checkout/util/types';
import BackToTop from 'common/BackToTop';
import Loader from 'common/Loader';
import { ChevronLeftIcon } from 'icons';
import NavigationAlert from 'common/Alerts/NavigationAlert';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { useCartContext } from 'providers/CartProvider';
import {
  Cart,
  ContractAddress,
  Delivery,
  DeliveryMethodEnum,
  PaymentMethodTypeEnum,
  useApproveQuoteMutation,
  useDeleteContractCartMutation,
  useGetOrderLazyQuery,
  useSubmitContractOrderFromCartLazyQuery,
  useSubmitContractOrderReviewLazyQuery,
  useSubmitOrderMutation,
  useSubmitOrderPreviewMutation,
  WillCall
} from 'generated/graphql';
import { Permission } from 'common/PermissionRequired';
import { BranchContext } from 'providers/BranchProvider';
import { CreditCardContext } from 'CreditCard/CreditCardProvider';
import { stateCode } from 'utils/states';
import { contractInfo, defaultOrderData } from './util';
import { trackSubmitOrder } from 'utils/analytics';
import { formatDate } from 'utils/dates';
import Info from './Info';
import Payment from './Payment';
import Review from './Review';
import Confirmation from './Confirmation';
import CheckoutWarning from 'Cart/CheckoutWarning';

export default function Checkout() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const history = useHistory();
  const { pathname } = useLocation();
  useDocumentTitle(t('common.checkout'));

  /**
   * State
   */
  const [loading, setLoading] = useState(false);

  /**
   * Context
   */
  const { profile, user } = useContext(AuthContext);
  const { shippingBranch } = useContext(BranchContext);
  const { creditCard, setSelectedCreditCard } = useContext(CreditCardContext);
  const {
    cart,
    cartLoading,
    checkingOutWithQuote,
    contract,
    clearContract,
    clearQuote,
    getUserCart,
    lineNotes,
    quoteId,
    quoteShipToId,
    itemCount,
    updateCart: cartContextUpdateCart
  } = useCartContext();
  const {
    disableContinue,
    orderedCart,
    orderedContract,
    tempCartItems,
    orderData,
    setOrderPreviewData,
    setMnCronShoppingCartId,
    setShipToBranch,
    deliveryData,
    deliveryMethodObject,
    mnCronShoppingCartId,
    orderPreviewData,
    paymentData,
    setDisableContinue,
    setOrderData,
    setOrderedCart,
    setOrderedContract,
    setOrderedLineNotes,
    setPaymentData,
    setPoNumberError,
    setStep,
    setTempCartItems,
    step,
    updateCart,
    updateDelivery,
    updateWillCall,
    willCallData,
    isWillCall,
    deliveryMethod,
    isDelivery
  } = useCheckoutContext();
  const {
    selectedAccounts: { billTo, shipTo, billToErpAccount }
  } = useSelectedAccountsContext();
  const companyName = billToErpAccount?.companyName;

  /**
   * Data
   */
  const [getOrderDataQuery] = useGetOrderLazyQuery({
    onCompleted: (data) => {
      const omittedData = omit(
        data.order,
        '__typename',
        'contractNumber',
        'jobNumber',
        'shipAddress',
        'specialInstructions',
        'orderedBy'
      );

      setOrderData({
        ...orderData,
        ...omittedData,
        lineItems: omittedData.lineItems ?? [],
        orderNumber: orderData.orderNumber ?? ''
      });
    },
    onError: (error) => {
      pushAlert(error.message, { variant: 'error' });
      setLoading(false);
    }
  });

  const [contractOrderReviewQuery] = useSubmitContractOrderReviewLazyQuery({
    fetchPolicy: 'no-cache',
    onCompleted: ({ submitContractOrderReview }) => {
      setOrderPreviewData({
        subTotal: submitContractOrderReview.subTotal ?? '',
        tax: submitContractOrderReview.taxAmount ?? '',
        orderTotal: submitContractOrderReview.totalAmount ?? ''
      });
      setMnCronShoppingCartId(submitContractOrderReview?.shoppingCartId ?? '');
      setStep(step + 1);
      setLoading(false);
      setOrderData({
        amount: submitContractOrderReview?.totalAmount,
        billToName: companyName,
        branchInfo: {
          branchName: contract?.data?.accountInformation?.branch?.branchName,
          city: contract?.data?.accountInformation?.branch?.city,
          country: contract?.data?.accountInformation?.branch?.country,
          postalCode:
            contract?.data?.accountInformation?.branch?.zip?.trim() ?? '',
          state: contract?.data?.accountInformation?.branch?.state,
          streetLineOne: contract?.data?.accountInformation?.branch?.address1,
          streetLineThree: contract?.data?.accountInformation?.branch?.address2,
          streetLineTwo: contract?.data?.accountInformation?.branch?.address3
        },
        deliveryMethod: submitContractOrderReview?.shipmentMethod,
        invoiceNumber: submitContractOrderReview?.invoiceNumber,
        lineItems:
          cart?.products?.map((lineItem) => ({
            erpPartNumber: lineItem?.erpPartNumber,
            imageUrls: lineItem?.product?.imageUrls,
            manufacturerName: lineItem?.product?.manufacturerName,
            manufacturerNumber: lineItem?.product?.manufacturerNumber,
            orderQuantity: lineItem?.quantity ?? 0,
            productId: lineItem?.product?.id,
            productName: lineItem?.product?.name,
            productOrderTotal:
              (lineItem?.quantity ?? 0) * (lineItem?.pricePerUnit ?? 0),
            shipQuantity: lineItem?.quantity ?? 0,
            unitPrice: lineItem?.pricePerUnit || 0
          })) ?? [],
        orderDate: submitContractOrderReview.orderDate,
        orderTotal: Number(submitContractOrderReview?.totalAmount) || 0,
        orderedBy: submitContractOrderReview?.orderBy,
        shipAddress: {
          city: submitContractOrderReview?.shipToAddress?.city,
          country: submitContractOrderReview?.shipToAddress?.country,
          postalCode:
            submitContractOrderReview?.shipToAddress?.zip?.trim() ?? '',
          state: submitContractOrderReview?.shipToAddress?.state,
          streetLineOne: submitContractOrderReview?.shipToAddress?.address1,
          streetLineThree: submitContractOrderReview?.shipToAddress?.address2,
          streetLineTwo: submitContractOrderReview?.shipToAddress?.address3
        },
        shipDate: submitContractOrderReview?.shipDate,
        shipToName: companyName,
        specialInstructions: isWillCall
          ? willCallData.pickupInstructions
          : deliveryData.deliveryInstructions,
        subTotal: Number(submitContractOrderReview?.subTotal) || 0,
        tax: Number(submitContractOrderReview?.taxAmount) || 0
      });
    },
    onError: (error) => {
      pushAlert(error.message, { variant: 'error' });
      setLoading(false);
    }
  });

  const [submitOrderPreviewMutation] = useSubmitOrderPreviewMutation({
    onCompleted: (data) => {
      setOrderPreviewData(data.submitOrderPreview);
      setStep(step + 1);
      setLoading(false);
    },
    onError: (error) => {
      pushAlert(error.message, { variant: 'error' });
      setLoading(false);
    }
  });

  const [submitContractOrderQuery] = useSubmitContractOrderFromCartLazyQuery({
    onCompleted: (data) => {
      setOrderData({
        ...orderData,
        orderNumber: data.submitContractOrderFromCart,
        orderStatus: t('orders.submitted'),
        customerPO: paymentData.poNumber,
        deliveryMethod: deliveryMethod,
        orderedBy: user?.name
      });
    },
    onError: (error) => {
      pushAlert(error.message, { variant: 'error' });
      setLoading(false);
    }
  });

  const [approveQuote] = useApproveQuoteMutation();
  const [submitOrderMutation] = useSubmitOrderMutation();
  const [deleteCart] = useDeleteContractCartMutation();

  /**
   * Callback
   */
  const handlePrevStep = useCallback(handlePrevStepCallback, [
    history,
    setDisableContinue,
    setOrderPreviewData,
    setPaymentData,
    setPoNumberError,
    setStep,
    step
  ]);

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const handleNextStep = useCallback(handleNextStepCallback, [
    step,
    setStep,
    paymentData,
    deliveryData,
    willCallData
  ]);

  /**
   * Memo
   */
  const stepInfo = useMemo(stepInfoMemo, [
    checkingOutWithQuote,
    contract,
    handleNextStep,
    history,
    step,
    profile,
    t
  ]);

  /**
   * Effect
   */
  useEffect(shippingBranchEffect, [setShipToBranch, shippingBranch]);
  useEffect(scrollToEffect, [step]);
  useEffect(() => {
    const fetchOrderPreviewData = async () => {
      await handleInfoNext();
    };
    if (pathname === '/checkout/payment' && !orderPreviewData.orderTotal) {
      setLoading(true);
      fetchOrderPreviewData().catch(console.error);
    } else if (pathname === '/checkout/payment' && step === Step.REVIEW) {
      setStep(Step.PAYMENT);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pathname]);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(unmountEffect, []);
  useEffect(() => {
    if (
      cart?.id &&
      shippingBranch?.isPricingOnly &&
      cart.deliveryMethod === DeliveryMethodEnum.Willcall
    ) {
      cartContextUpdateCart?.(cart.id, {
        deliveryMethod: DeliveryMethodEnum.Delivery
      });
    }
  }, [shippingBranch, cart, cartContextUpdateCart]);

  /**
   * Components
   */
  // Renders the correct step based on the reducer
  const OrderSummaryComponent = () =>
    step !== Step.CONFIRMATION ? (
      <OrderSummary
        page="checkout"
        showTax={stepInfo.showTax}
        hideButton={!stepInfo.buttonLabel}
        disableButton={disableContinue}
        buttonText={stepInfo.buttonLabel}
        onButtonClick={stepInfo.buttonAction}
      />
    ) : null;

  const { contractNumber, contractDescription, job } = contractInfo({
    contract,
    orderedContract
  });

  function getStepComponent({
    cart,
    orderedCart,
    step
  }: {
    cart?: Cart;
    orderedCart?: Cart;
    step: Step;
  }) {
    if ((cart || orderedCart) && !isUndefined(step)) {
      switch (step) {
        case Step.INFO:
          return <Info />;
        case Step.PAYMENT:
          return <Payment />;
        case Step.REVIEW:
          return <Review />;
        case Step.CONFIRMATION:
          return <Confirmation />;
      }
    }
  }

  /**
   * Render
   */
  return (!checkingOutWithQuote && !cart && !orderedCart) ||
    (billToErpAccount?.creditHold && step === 0) ? (
    <Redirect to="/cart" />
  ) : (
    <Box bgcolor="common.white" flex="1" height="100%">
      {!cart && (!orderedCart || !tempCartItems.length) ? (
        <Loader size="page" />
      ) : (
        <>
          <Container maxWidth="lg" sx={{ pb: 12 }}>
            <NavigationAlert
              when={showNavPrompt(step)}
              onConfirm={handleNavConfirm}
            />
            <Box py={3.75} position="relative">
              {(loading || cartLoading) && <Loader backdrop size="page" />}
              <Grid container spacing={isSmallScreen ? 0 : 3}>
                <Grid item xs={12}>
                  {contract || orderedContract ? (
                    <>
                      <Grid
                        display="flex"
                        flexDirection={isSmallScreen ? 'column' : 'row'}
                        flexWrap={isSmallScreen ? 'wrap' : 'nowrap'}
                        container
                      >
                        <Grid item xs="auto">
                          <Typography
                            noWrap
                            fontWeight={700}
                            variant="h4"
                            data-testid="checkout-contract-header"
                          >
                            {t('contract.contractNum')}
                            {contractNumber}
                          </Typography>
                        </Grid>
                        <Hidden mdDown>
                          <Grid item xs="auto">
                            <Box width={64} />
                          </Grid>
                        </Hidden>
                        <Grid
                          item
                          xs
                          overflow={isSmallScreen ? undefined : 'hidden'}
                          textOverflow="ellipsis"
                        >
                          <Typography
                            noWrap
                            fontWeight={700}
                            variant="h4"
                            data-testid="checkout-contract-desc"
                          >
                            {contractDescription}
                          </Typography>
                        </Grid>
                      </Grid>
                      <Box mt={4}>
                        <Tooltip title={job}>
                          <Typography
                            noWrap
                            variant="h5"
                            fontWeight={400}
                            fontSize={isSmallScreen ? 16 : 20}
                            data-testid="checkout-job-name"
                          >
                            {t('common.jobName')}: {job}
                          </Typography>
                        </Tooltip>
                      </Box>
                    </>
                  ) : (
                    <Box fontWeight={700} px={isSmallScreen ? 1 : 0}>
                      <Tooltip title={shipTo?.name ?? `${t('common.na')}`}>
                        <Typography noWrap variant="h4" data-testid="job-name">
                          {t('common.jobName')}:{' '}
                          {shipTo?.name ?? t('common.na')}
                        </Typography>
                      </Tooltip>
                    </Box>
                  )}
                </Grid>
                <Hidden mdDown>
                  <Grid item md={6} />
                </Hidden>
              </Grid>
              <Grid container spacing={isSmallScreen ? 0 : 3}>
                <Grid
                  item
                  container
                  xs={12}
                  md={6}
                  spacing={isSmallScreen ? 0 : 3}
                  pt={isSmallScreen ? 4 : 0}
                >
                  <Hidden mdUp>
                    <Grid item xs={12}>
                      <Typography
                        variant="body1"
                        align="center"
                        color="primary02.main"
                      >
                        {stepInfo.number === 1
                          ? t(
                              isDelivery
                                ? `cart.step${stepInfo.number}Delivery`
                                : `cart.step${stepInfo.number}WillCall`
                            )
                          : t(`cart.step${stepInfo.number}`)}
                      </Typography>
                    </Grid>
                  </Hidden>
                  {step !== Step.CONFIRMATION && (
                    <Grid item xs={12} md={6}>
                      <Stepper
                        activeStep={stepInfo.number - 1}
                        background="gray"
                        highlightCompleted={false}
                      >
                        <StepperStep>
                          <StepLabel>{t('cart.info')}</StepLabel>
                        </StepperStep>
                        <StepperStep>
                          <StepLabel>{t('cart.payment')}</StepLabel>
                        </StepperStep>
                        <StepperStep>
                          <StepLabel>{t('cart.review')}</StepLabel>
                        </StepperStep>
                      </Stepper>
                    </Grid>
                  )}
                  <Hidden mdDown>
                    <Grid item md={6}>
                      <Typography
                        variant="body1"
                        color="primary02.main"
                        pt={1.5}
                        display="flex"
                        data-testid="step-tracker-label"
                      >
                        {stepInfo.number === 1
                          ? t(
                              isDelivery
                                ? `cart.step${stepInfo.number}Delivery`
                                : `cart.step${stepInfo.number}WillCall`
                            )
                          : t(`cart.step${stepInfo.number}`)}
                      </Typography>
                    </Grid>
                  </Hidden>
                </Grid>
                <Hidden mdUp>
                  <Grid item xs={12}>
                    {OrderSummaryComponent()}
                    {itemCount > 199 && <CheckoutWarning />}
                  </Grid>
                </Hidden>
                <Hidden mdDown>
                  <Grid item md={6} />
                </Hidden>
              </Grid>
              <Box px={isSmallScreen ? 2 : 0}>
                <Grid container spacing={isSmallScreen ? 0 : 6}>
                  <Grid item xs={12} md={step === Step.CONFIRMATION ? 12 : 9}>
                    {getStepComponent({
                      cart,
                      orderedCart,
                      step
                    })}
                    <Box mt={8}>
                      {step !== Step.CONFIRMATION && (
                        <>
                          <Heading title="" />
                          <Grid container>
                            {stepInfo.alignActions === 'column' && (
                              <Grid item xs={12} md={3} />
                            )}
                            <Grid
                              item
                              xs={12}
                              md={stepInfo.alignActions === 'column' ? 9 : 12}
                            >
                              {stepInfo.showPrev && !!contract && (
                                <Hidden mdDown>
                                  <Box
                                    ml={3}
                                    mt={0}
                                    display="inline-flex"
                                    justifyContent="flex-start"
                                  >
                                    <MaterialButton
                                      variant="text"
                                      onClick={handlePrevStep}
                                      data-testid="back-step-button"
                                      startIcon={<ChevronLeftIcon />}
                                    >
                                      {step === Step.INFO
                                        ? t('cart.backtocontract')
                                        : t('cart.previousStep')}
                                    </MaterialButton>
                                  </Box>
                                </Hidden>
                              )}
                              {stepInfo.showNext && (
                                <Button
                                  style={{
                                    minWidth: 300,
                                    display: 'inline-flex',
                                    ...(isSmallScreen ? { width: '100%' } : {})
                                  }}
                                  onClick={handleNextStep}
                                  testId="checkout-bottom-button"
                                  disabled={disableContinue}
                                  label={stepInfo.buttonLabel}
                                />
                              )}
                              {stepInfo.showPrev && !contract ? (
                                <Box
                                  ml={isSmallScreen ? 0 : 3}
                                  mt={isSmallScreen ? 2 : 0}
                                  display={
                                    isSmallScreen ? 'flex' : 'inline-flex'
                                  }
                                  justifyContent={
                                    isSmallScreen ? 'center' : 'flex-start'
                                  }
                                >
                                  <MaterialButton
                                    variant="text"
                                    onClick={handlePrevStep}
                                    data-testid="previous-step-button"
                                    startIcon={
                                      isSmallScreen ? (
                                        <ChevronLeftIcon />
                                      ) : undefined
                                    }
                                  >
                                    {t('cart.previousStep')}
                                  </MaterialButton>
                                </Box>
                              ) : (
                                <Hidden mdUp>
                                  <Box
                                    ml={0}
                                    mt={2}
                                    display="flex"
                                    justifyContent="center"
                                  >
                                    <MaterialButton
                                      variant="text"
                                      onClick={handlePrevStep}
                                      data-testid="back-step-button"
                                      startIcon={<ChevronLeftIcon />}
                                    >
                                      {step === Step.INFO
                                        ? t('cart.backtocontract')
                                        : t('cart.previousStep')}
                                    </MaterialButton>
                                  </Box>
                                </Hidden>
                              )}
                            </Grid>
                          </Grid>
                        </>
                      )}
                    </Box>
                  </Grid>
                  <Hidden mdDown>
                    {step !== Step.CONFIRMATION && (
                      <Grid item md={3}>
                        {OrderSummaryComponent()}
                        {itemCount > 199 && <CheckoutWarning />}
                      </Grid>
                    )}
                  </Hidden>
                </Grid>
              </Box>
            </Box>
          </Container>
          <Hidden mdUp>
            <BackToTop />
          </Hidden>
        </>
      )}
    </Box>
  );

  /**
   * Effect Definitions
   */
  function shippingBranchEffect() {
    if (shippingBranch) {
      setShipToBranch(shippingBranch);
    }
  }
  function scrollToEffect() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
  function unmountEffect() {
    return () => {
      clearQuote();
    };
  }

  /**
   * Memo Definitions
   */
  function stepInfoMemo() {
    const noApproval = profile?.permissions.includes(
      Permission.SUBMIT_CART_WITHOUT_APPROVAL
    );

    switch (step) {
      case Step.INFO:
        return {
          number: 1,
          showNext: true,
          showPrev: true,
          showTax: checkingOutWithQuote,
          buttonLabel: t('cart.continueCheckout'),
          buttonAction: handleNextStep,
          alignActions: 'column'
        };
      case Step.PAYMENT:
        return {
          number: 2,
          showNext: true,
          showPrev: true,
          showTax: true,
          buttonLabel: t('cart.continueCheckout'),
          buttonAction: handleNextStep,
          alignActions: 'column'
        };
      case Step.REVIEW:
        return {
          number: 3,
          showNext: true,
          showPrev: true,
          showTax: true,
          buttonLabel: t(
            noApproval ? 'cart.submitOrder' : 'cart.submitOrderForApproval'
          ),
          buttonAction: handleNextStep,
          alignActions: contract ? 'column' : 'left'
        };
      case Step.CONFIRMATION:
        return {
          number: 4,
          showNext: false,
          showPrev: false,
          showTax: false,
          buttonLabel: t('cart.viewAllOrders'),
          buttonAction: () => history.push('/orders'),
          alignActions: 'column'
        };
      default:
        return {
          number: 0,
          showNext: true,
          showPrev: true,
          showTax: false
        };
    }
  }

  /**
   * Callback Definitions
   */
  function handlePrevStepCallback() {
    setDisableContinue(false);
    switch (step) {
      case Step.INFO:
        history.push('/cart', { canShowNavAlert: true });
        scrollToEffect();
        break;
      case Step.PAYMENT:
        setStep(step - 1);
        setPaymentData({
          paymentMethodType: PaymentMethodTypeEnum.Billtoaccount
        });
        setOrderPreviewData({
          subTotal: '',
          tax: '',
          orderTotal: ''
        });
        setPoNumberError(false);
        break;
      default:
        setStep(step - 1);
    }
  }

  async function handleInfoNext() {
    setDisableContinue(false);
    let isWillCall = cart?.deliveryMethod === DeliveryMethodEnum.Willcall;
    let isDelivery = cart?.deliveryMethod === DeliveryMethodEnum.Delivery;
    if (shippingBranch?.isPricingOnly) {
      isWillCall = false;
      isDelivery = true;
    }

    if (isWillCall) {
      await updateWillCall(willCallData);
    } else {
      await updateDelivery(deliveryData);
    }

    if (checkingOutWithQuote) {
      setStep(step + 1);
      setLoading(false);
      return;
    }

    if (checkingOutWithQuote) {
      setStep(step + 1);
      setLoading(false);
      return;
    }

    if (contract) {
      const cartLineItems = cart?.products
        ? cart?.products?.map((lineItem) => ({
            branch: {
              branchNumber: contract?.data?.accountInformation?.branch?.entityId
            },
            description: '', //lineItem?.product?.name,
            lineComment: lineNotes?.[lineItem?.id ?? ''],
            lineNumber: parseInt(lineItem?.id ?? ''),
            productNumber: lineItem?.customerPartNumber,
            quantityOrdered: `${lineItem?.quantity ?? 0}`,
            taxable: 'Y',
            netPrice: `${lineItem?.pricePerUnit || 0}`,
            unitPrice: `${lineItem?.pricePerUnit || 0}`
          }))
        : [];
      const shipAddress = isDelivery
        ? contract?.data?.accountInformation?.shipToAddress
        : contract?.data?.accountInformation?.branch;

      const variables = {
        orderReview: {
          addItemsToCart: { items: cartLineItems },
          createCartRequest: {
            accountId: billTo?.erpAccountId,
            application: 'B2B',
            branchNumber: contract?.data?.accountInformation?.branch?.entityId,
            contractNumber: contract?.data?.contractNumber,
            jobName: contract?.data?.jobName,
            jobNumber: contract?.data?.customerInfo?.jobNumber,
            rePrice: 'Y',
            shipmentDetail: {
              shipMethod: isDelivery ? 'O' : 'P',
              shippingAddress1: shipAddress?.address1,
              shippingAddress2: shipAddress?.address2,
              shippingAddress3:
                (shipAddress as ContractAddress)?.address3 ?? '',
              shippingCity: shipAddress?.city,
              shippingState: !!shipAddress?.state
                ? stateCode(shipAddress?.state) ?? shipAddress?.state
                : '',
              shippingZip: shipAddress?.zip ? shipAddress?.zip.trim() : ''
            },
            shoppingCartId: cart?.id,
            userId: profile?.userId ?? ''
          }
        }
      };

      setSelectedCreditCard('');
      setPaymentData({
        ...paymentData,
        creditCard: null,
        paymentMethodType: PaymentMethodTypeEnum.Billtoaccount
      });
      await contractOrderReviewQuery({ variables });
      return;
    }
    await submitOrderPreviewMutation({
      variables: {
        cartId: cart?.id ?? '',
        userId: profile?.userId ?? '',
        shipToAccountId: cart?.shipToId ?? '',
        billToAccountId: billTo?.erpAccountId ?? ''
      }
    });

    setLoading(false);
    return;
  }

  async function handlePaymentNext() {
    setDisableContinue(false);
    if (paymentData.poNumber) {
      setPoNumberError(false);
      await updateCart(paymentData);
      setStep(step + 1);
      setLoading(false);
      return;
    }
    setPoNumberError(true);
    setLoading(false);
    return;
  }

  async function handleReviewNext() {
    setDisableContinue(false);
    let isWillCall = cart?.deliveryMethod === DeliveryMethodEnum.Willcall;
    let isDelivery = cart?.deliveryMethod === DeliveryMethodEnum.Delivery;
    if (shippingBranch?.isPricingOnly) {
      isWillCall = false;
      isDelivery = true;
    }
    setLoading(true);
    setTempCartItems(cart?.products ?? []);

    // quote
    if (checkingOutWithQuote) {
      const shipToEntityId =
        billTo?.shipTos?.find((a) => a?.erpAccountId === quoteShipToId)?.id ??
        '';

      const creditCardFormatted =
        cart?.paymentMethodType === PaymentMethodTypeEnum.Creditcard
          ? {
              ...omit(creditCard, '__typename'),
              expirationDate: {
                date: creditCard?.expirationDate.date ?? ''
              }
            }
          : null;

      const variables = {
        approveQuoteInput: {
          billToEntityId: billTo?.erpAccountId ?? '',
          shipToEntityId,
          instructions: isWillCall
            ? cart?.willCall?.pickupInstructions
            : cart?.delivery?.deliveryInstructions,
          address: {
            city: cart?.delivery?.address?.city ?? '',
            streetLineOne: cart?.delivery?.address?.street1 ?? '',
            streetLineTwo: cart?.delivery?.address?.street2,
            state: cart?.delivery?.address?.state ?? '',
            postalCode: cart?.delivery?.address?.zip ?? ''
          },
          creditCard: creditCardFormatted,
          orderedBy: user?.name ?? 'ecomm',
          preferredDate: isWillCall
            ? cart?.willCall?.preferredDate
            : cart?.delivery?.preferredDate,
          preferredTime: isWillCall
            ? cart?.willCall?.preferredTime
            : cart?.delivery?.preferredTime,
          isDelivery: !isWillCall,
          shouldShipFullOrder:
            !isWillCall && !!cart?.delivery?.shouldShipFullOrder,
          poNumber: cart?.poNumber ?? '',
          branchId: cart?.shippingBranchId ?? '',
          userId: profile?.userId ?? '',
          quoteId: quoteId ?? ''
        }
      };

      await approveQuote({ variables }).then((orderData) => {
        setOrderData(orderData?.data?.approveQuote ?? defaultOrderData);
        setStep(step + 1);
        setLoading(false);
        trackSubmitOrder({
          billTo: billTo?.erpAccountId,
          shipTo: shipTo?.erpAccountId,
          orderNumber: orderData?.data?.approveQuote.orderNumber,
          shippingBranch: shippingBranch?.branchId,
          products:
            cart?.products?.map((lineItem) => ({
              name: lineItem.product?.name,
              qty: lineItem.quantity ?? 0,
              mfr: lineItem.product?.manufacturerNumber ?? '',
              erp: lineItem.product?.partNumber
            })) ?? [],
          netTotal: orderPreviewData.orderTotal
        });
      });

      return;
    }

    // contract
    if (contract) {
      const shippingAddress = isDelivery
        ? contract?.data?.accountInformation?.shipToAddress
        : contract?.data?.accountInformation?.branch;

      const shipCode = isDelivery
        ? (deliveryMethodObject as Delivery)?.shouldShipFullOrder
          ? 'O'
          : 'M'
        : ''; // Sending Delivery type one or multiple - O or M
      const preferredTime = deliveryMethodObject?.preferredTime ?? '';

      const variables = {
        contractOrderSubmit: {
          contractName: contract?.data?.contractDescription ?? '',
          contractNumber: contract?.data?.contractNumber ?? '',
          deliveryMethod: deliveryMethod,
          jobName: contract?.data?.jobName,
          jobNumber: contract?.data?.customerInfo?.jobNumber,
          lineItems:
            cart?.products?.map((lineItem) => ({
              orderQuantity: lineItem?.quantity ?? 0,
              productName: lineItem?.product?.name,
              unitPrice: lineItem?.pricePerUnit || 0
            })) ?? [],
          orderDate: orderData?.orderDate ?? '',
          orderTotal: orderPreviewData.orderTotal,
          poNumber: paymentData.poNumber,
          promiseDate: deliveryMethodObject?.preferredDate
            ? formatDate(deliveryMethodObject?.preferredDate, 'MMddyyyy')
            : '',
          shipBranchNumber:
            contract?.data?.accountInformation?.branch?.entityId,
          shipCode: shipCode,
          shipDescription: deliveryMethodObject?.preferredDate
            ? formatDate(deliveryMethodObject?.preferredDate) +
              ' ' +
              preferredTime
            : '',
          shipHandleAmount: '0',
          shipMethod: isDelivery ? 'O' : 'P',
          shipToAddress: {
            address1: shippingAddress?.address1,
            address2: shippingAddress?.address2,
            address3: shippingAddress?.address3,
            city: shippingAddress?.city,
            country: shippingAddress?.country,
            taxJurisdiction: shippingAddress?.taxJurisdiction,
            state: !!shippingAddress?.state
              ? stateCode(shippingAddress?.state) ?? shippingAddress?.state
              : '',
            zip: shippingAddress?.zip ? shippingAddress?.zip.trim() : ''
          },
          shipToId: shipTo?.id ?? '',
          spInstructions: (deliveryMethodObject as Delivery)
            ?.deliveryInstructions
            ? (deliveryMethodObject as Delivery).deliveryInstructions
            : (deliveryMethodObject as WillCall)?.pickupInstructions,
          subTotal: orderPreviewData.subTotal,
          taxAmount: orderPreviewData.tax
        },
        application: 'B2B',
        accountId: billTo?.erpAccountId ?? '',
        userId: profile?.userId ?? '',
        shoppingCartId: mnCronShoppingCartId ?? ''
      };

      await submitContractOrderQuery({ variables })
        .then(async (contractOrderData) => {
          if (contractOrderData?.data) {
            await getOrderDataQuery({
              variables: {
                accountId: billTo?.id ?? '',
                orderId: contractOrderData.data.submitContractOrderFromCart,
                userId: profile?.userId ?? '',
                orderStatus: t('orders.pending')
              }
            });
            setOrderedCart(cart);
            setOrderedContract(contract);
            setOrderedLineNotes(lineNotes);
            clearContract();
            setStep(step + 1);
            trackSubmitOrder({
              billTo: billTo?.erpAccountId,
              shipTo: shipTo?.erpAccountId,
              orderNumber: contractOrderData.data.submitContractOrderFromCart,
              shippingBranch: shippingBranch?.branchId,
              products: cart?.products
                ? cart?.products.map((lineItem) => ({
                    name: lineItem?.product?.name,
                    qty: lineItem?.quantity ?? 0,
                    mfr: lineItem?.product?.manufacturerNumber ?? '',
                    erp: lineItem?.product?.partNumber
                  }))
                : [],
              netTotal: orderPreviewData.orderTotal
            });
          }
        })
        .finally(() => {
          setLoading(false);
        });

      return;
    }

    // default
    await submitOrderMutation({
      variables: {
        cartId: cart?.id ?? '',
        userId: profile?.userId ?? '',
        shipToAccountId: cart?.shipToId ?? '',
        billToAccountId: billTo?.erpAccountId ?? ''
      }
    })
      .then(async (orderData) => {
        if (orderData?.data && shipTo) {
          setOrderData(omit(orderData.data.submitOrder, '__typename'));
          await getUserCart(shipTo);
          setStep(step + 1);
          trackSubmitOrder({
            billTo: billTo?.erpAccountId,
            shipTo: shipTo?.erpAccountId,
            orderNumber: orderData.data.submitOrder.orderNumber,
            shippingBranch: shippingBranch?.branchId,
            products:
              cart?.products?.map((lineItem) => ({
                name: lineItem.product?.name,
                qty: lineItem.quantity ?? 0,
                mfr: lineItem.product?.manufacturerNumber ?? '',
                erp: lineItem.product?.partNumber
              })) ?? [],
            netTotal: orderPreviewData.orderTotal
          });
        }
      })
      .finally(() => {
        setLoading(false);
      });
    return;
  }

  async function handleNextStepCallback() {
    setLoading(true);
    if (step === Step.INFO) {
      handleInfoNext();
    } else if (step === Step.PAYMENT) {
      handlePaymentNext();
    } else if (step === Step.REVIEW) {
      handleReviewNext();
    } else {
      setStep(step + 1);
    }
  }

  async function handleNavConfirm() {
    clearContract();
    if (!mnCronShoppingCartId) return;
    await deleteCart({
      variables: {
        shoppingCartId: mnCronShoppingCartId,
        userId: profile?.userId ?? '',
        accountId: billTo?.erpAccountId ?? '',
        branchNumber:
          contract?.data?.accountInformation?.branch?.entityId ?? '',
        application: 'B2B'
      }
    });
  }

  function showNavPrompt(step: Step) {
    return (
      pLocation: Location<any>,
      nLocation: Location<any> | undefined,
      action?: Action
    ) => {
      const falseParameters =
        pLocation.pathname === nLocation?.pathname ||
        action === 'POP' ||
        nLocation?.pathname?.includes('/checkout') ||
        nLocation?.pathname?.includes('/cart') ||
        step === Step.CONFIRMATION;
      return falseParameters
        ? false
        : !!pLocation?.state?.canShowCustomNavAlert;
    };
  }
}
