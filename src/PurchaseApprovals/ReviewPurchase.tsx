import { useContext, useState, useEffect } from 'react';

import {
  Box,
  Grid,
  Hidden,
  Typography,
  useScreenSize,
  Container,
  Button,
  Tooltip,
  Skeleton,
  useSnackbar
} from '@dialexa/reece-component-library';
import { startCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import CartList from 'Cart/CartList';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import Heading from 'Checkout/Heading';
import Location from 'Checkout/Location';
import Loader from 'common/Loader';
import { formatDate } from 'utils/dates';
import OrderSummary from 'Cart/OrderSummary';
import RejectOrderDialog from 'PurchaseApprovals/RejectOrderDialog';
import {
  Cart,
  useApproveOrderMutation,
  useCartQuery,
  useOrderPendingApprovalQuery,
  useRejectOrderMutation,
  useSubmitOrderPreviewMutation
} from 'generated/graphql';
import { AuthContext } from 'AuthProvider';
import { useHistory } from 'react-router-dom';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { useCartContext } from 'providers/CartProvider';

function ReviewPurchase() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const history: any = useHistory();

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { cart } = useCartContext();
  const { isDelivery } = useCheckoutContext();
  const { selectedAccounts } = useSelectedAccountsContext();
  /**
   * State
   */
  const [rejectDialogOpen, setRejectDialogOpen] = useState(false);

  /**
   * Data
   */
  const { data: orderData, loading: orderLoading } =
    useOrderPendingApprovalQuery({
      variables: {
        orderId: history.location.state.selectedOrder.orderId
      }
    });

  const [
    submitOrderPreview,
    { data: orderPreviewData, loading: orderPreviewLoading }
  ] = useSubmitOrderPreviewMutation({
    variables: {
      cartId: orderData?.orderPendingApproval?.cartId ?? '',
      userId: profile?.userId ?? '',
      shipToAccountId: selectedAccounts.shipTo?.id ?? '',
      billToAccountId: selectedAccounts.billTo?.erpAccountId ?? ''
    },
    onCompleted: (data) => {
      if (
        data?.submitOrderPreview.orderTotal &&
        parseFloat(data?.submitOrderPreview.orderTotal!) * 100 !==
          cartData?.cart.total
      ) {
        pushAlert(t('purchaseApprovals.orderTotalChanged'), {
          variant: 'info'
        });
      }
    }
  });

  const [approveOrder, { loading: approveOrderLoading }] =
    useApproveOrderMutation({
      variables: {
        cartId: orderData?.orderPendingApproval?.cartId ?? '',
        userId: profile?.userId ?? '',
        shipToAccountId: selectedAccounts.shipTo?.id ?? '',
        billToAccountId: selectedAccounts.billTo?.erpAccountId ?? ''
      },
      refetchQueries: ['GetOrdersPendingApproval'],
      onCompleted: () => {
        history.push('/purchase-approvals');
        pushAlert(t('purchaseApprovals.orderApproved'), {
          variant: 'success'
        });
      }
    });

  const [rejectOrder, { loading: rejectOrderLoading }] = useRejectOrderMutation(
    {
      refetchQueries: ['GetOrdersPendingApproval'],
      onCompleted: () => {
        setRejectDialogOpen(false);
        history.push('/purchase-approvals');
        pushAlert(t('purchaseApprovals.orderRejected'), {
          variant: 'info'
        });
      }
    }
  );

  const { data: cartData, loading: cartLoading } = useCartQuery({
    skip: orderLoading,
    variables: {
      id: orderData?.orderPendingApproval?.cartId!,
      userId: profile!.userId,
      shipToAccountId: cart?.shipToId!,
      includeProducts: true
    }
  });

  /**
   * Page Title
   */
  useDocumentTitle(
    t('dynamicPageTitles.purchaseApproval', {
      orderNumber: orderData?.orderPendingApproval.orderId ?? ''
    })
  );

  /**
   * Effects
   */
  useEffect(() => {
    if (!orderLoading && !cartLoading) {
      submitOrderPreview();
    }
  }, [submitOrderPreview, orderLoading, cartLoading]);

  /**
   * Callbacks
   */
  const handleReject = (reason: string) => {
    rejectOrder({
      variables: {
        cartId: orderData?.orderPendingApproval?.cartId!,
        userId: profile!.userId,
        shipToAccountId: selectedAccounts!.shipTo?.id!,
        rejectOrderInfo: { rejectionReason: reason }
      }
    });
  };

  return !cart ? (
    <Loader />
  ) : (
    <>
      <RejectOrderDialog
        open={rejectDialogOpen}
        onClose={() => setRejectDialogOpen(false)}
        onSubmit={handleReject}
        loading={rejectOrderLoading}
      />
      <Box bgcolor="common.white" flex="1" height="100%">
        <Container maxWidth="lg" sx={{ pb: 12 }}>
          <Box py={3.75} position="relative">
            <Box px={isSmallScreen ? 2 : 0}>
              <Grid container spacing={isSmallScreen ? 0 : 6}>
                <Grid item xs={12} md={6}>
                  <Box fontWeight={700} px={isSmallScreen ? 1 : 0}>
                    <Tooltip title={`${t('common.shipTo')}`}>
                      <>
                        {orderLoading ? (
                          <Skeleton width={300} height={35} />
                        ) : (
                          <Typography noWrap variant="h4">
                            {`${t('common.orderNumber')}: ${
                              orderData?.orderPendingApproval.orderId
                            }`}
                          </Typography>
                        )}
                      </>
                    </Tooltip>
                  </Box>
                </Grid>
                <Hidden mdUp>
                  <Grid item xs={12}>
                    <OrderSummary
                      page="approval"
                      showTax={true}
                      buttonText={t('purchaseApprovals.approveOrder')}
                      onButtonClick={() => approveOrder()}
                      additionalCTAText={t('purchaseApprovals.rejectOrder')}
                      onAdditionalCTAClick={() => setRejectDialogOpen(true)}
                      approveData={orderPreviewData?.submitOrderPreview}
                      orderPreviewLoading={orderPreviewLoading}
                      approveOrderLoading={{
                        approve: approveOrderLoading,
                        reject: rejectOrderLoading
                      }}
                    />
                  </Grid>
                </Hidden>
                <Grid item xs={12} md={9}>
                  <Heading title={t('common.paymentInformation')} />
                  <Grid container sx={{ mb: isSmallScreen ? 2 : 8 }}>
                    <Grid item xs={6} md={3}>
                      <Typography
                        component="span"
                        variant="body1"
                        color="textSecondary"
                        sx={{
                          wordWrap: 'break-word',
                          fontWeight: isSmallScreen ? 500 : 400
                        }}
                      >
                        {t('cart.paymentMethod')}
                      </Typography>
                    </Grid>
                    <Grid item xs={6} md={6}>
                      <Typography
                        variant="body1"
                        sx={{ wordWrap: 'break-word' }}
                      >
                        {orderPreviewLoading || orderLoading ? (
                          <Skeleton width={200} />
                        ) : (
                          t(
                            `cart.${cartData?.cart.paymentMethodType?.toLowerCase()}`
                          )
                        )}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid container>
                    <Grid item md={3} xs={6} sx={{ mb: isSmallScreen ? 2 : 8 }}>
                      <Typography
                        component="span"
                        variant="body1"
                        color="textSecondary"
                        sx={{
                          wordWrap: 'break-word',
                          fontWeight: isSmallScreen ? 500 : 400
                        }}
                      >
                        {t('cart.poNumber')}
                      </Typography>
                    </Grid>
                    <Grid item xs={6} md={6}>
                      <Typography
                        variant="body1"
                        sx={{ wordWrap: 'break-word' }}
                      >
                        {orderPreviewLoading ? (
                          <Skeleton width={100} />
                        ) : (
                          cartData?.cart.poNumber
                        )}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid container sx={{ mb: isSmallScreen ? 2 : 8 }}>
                    <Grid item md={6} xs={12}>
                      <Box mr={isSmallScreen ? 0 : 0.75}>
                        <Heading title={t('cart.billingInformation')} />
                        <Location
                          location={selectedAccounts.billToErpAccount}
                        />
                      </Box>
                    </Grid>
                    <Grid item xs={12} md={6}>
                      <Box ml={isSmallScreen ? 0 : 4.75}>
                        <Heading title={t('cart.deliveryInformation')} />
                        <Grid container>
                          <Grid item xs={12} md={8}>
                            <Typography
                              variant="body1"
                              sx={{ fontWeight: 700 }}
                            >
                              {t('common.shipTo')}
                            </Typography>
                            <Location
                              boldName={false}
                              //   @ts-ignore
                              location={cartData?.cart.delivery?.address!}
                            />
                          </Grid>
                          <Grid item xs={12} md={4}>
                            {cartData?.cart.delivery.preferredDate ||
                            cartData?.cart.delivery.preferredTime ? (
                              <Box mt={isSmallScreen ? 6 : 0}>
                                <Typography
                                  variant="body1"
                                  sx={{ fontWeight: 700 }}
                                >
                                  {t(
                                    `cart.${
                                      isDelivery
                                        ? 'deliveryDate'
                                        : 'willCallDetails'
                                    }`
                                  )}
                                </Typography>
                                {cartData?.cart.delivery.preferredDate ? (
                                  <Typography variant="body1">
                                    {formatDate(
                                      cartData?.cart.delivery.preferredDate
                                    )}
                                  </Typography>
                                ) : null}
                                {cartData?.cart.delivery.preferredTime ? (
                                  <Typography variant="body1">
                                    {cartData?.cart.delivery.preferredTime ===
                                    'ASAP'
                                      ? 'ASAP'
                                      : startCase(
                                          cartData?.cart.delivery.preferredTime.toLowerCase()
                                        )}
                                  </Typography>
                                ) : null}
                              </Box>
                            ) : null}
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
                    />
                    <Typography variant="body1">
                      {}
                      {cartData?.cart.delivery.deliveryInstructions ??
                        t('common.na')}
                    </Typography>
                  </Box>
                  <Box mb={2}>
                    <Heading title={t('cart.orderDetails')} />
                    <Hidden mdDown>
                      <Box mt={isSmallScreen ? -2 : -4} mb={3}>
                        <Typography variant="body1" color="primary02.main">
                          {cart.products?.length ?? 0}&nbsp;
                          {t('cart.itemInCart', {
                            count: cart.products?.length ?? 0
                          })}
                        </Typography>
                      </Box>
                      {cartData ? (
                        <CartList cart={cartData?.cart as Cart} readOnly />
                      ) : null}
                    </Hidden>
                    <Hidden mdUp>
                      <Box mx={-2} mt={-5}>
                        {cartData ? (
                          <CartList cart={cartData?.cart as Cart} readOnly />
                        ) : null}
                      </Box>
                    </Hidden>
                  </Box>
                </Grid>
                <Hidden mdDown>
                  <Grid item md={3}>
                    <OrderSummary
                      page="approval"
                      showTax={true}
                      buttonText={t('purchaseApprovals.approveOrder')}
                      onButtonClick={() => approveOrder()}
                      additionalCTAText={t('purchaseApprovals.rejectOrder')}
                      onAdditionalCTAClick={() => setRejectDialogOpen(true)}
                      approveData={orderPreviewData?.submitOrderPreview}
                      orderPreviewLoading={orderPreviewLoading}
                      approveOrderLoading={{
                        approve: approveOrderLoading,
                        reject: rejectOrderLoading
                      }}
                    />
                  </Grid>
                </Hidden>
                <Grid item xs={12} md={9}>
                  <Button
                    sx={{ minWidth: isSmallScreen ? undefined : 300 }}
                    fullWidth={isSmallScreen}
                    onClick={() => approveOrder()}
                    data-testid="approve-order-button"
                  >
                    {t('purchaseApprovals.approveOrder')}
                  </Button>
                  <Box
                    ml={isSmallScreen ? 0 : 3}
                    mt={isSmallScreen ? 2 : 0}
                    display={isSmallScreen ? 'flex' : 'inline-flex'}
                    justifyContent={isSmallScreen ? 'center' : 'flex-start'}
                  >
                    <Button
                      variant="text"
                      onClick={() => setRejectDialogOpen(true)}
                      data-testid="reject-order-button"
                    >
                      {t('purchaseApprovals.rejectOrder')}
                    </Button>
                  </Box>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Container>
      </Box>
    </>
  );
}

export default ReviewPurchase;
