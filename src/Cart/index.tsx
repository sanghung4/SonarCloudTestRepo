import { useCallback, useEffect, useState } from 'react';

import {
  Box,
  Button,
  Container,
  Grid,
  Hidden,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { Location, Action } from 'history';
import { kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import CartList from 'Cart/CartList';
import DeliveryMethod from 'Cart/DeliveryMethod';
import ItemsRemovedDialog from 'Cart/ItemsRemovedDialog';
import OrderSummary from 'Cart/OrderSummary';
import RemoveCartItemsDialog from 'Cart/RemoveCartItemsDialog';
import Shipments from 'Cart/Shipments';
import { checkIfOver10Mil } from 'Cart/util';
import { useOrderSummary } from 'Cart/util/useOrderSummary';
import Warning from 'Cart/Warning';
import NavigationAlert from 'common/Alerts/NavigationAlert';
import BackToTop from 'common/BackToTop';
import HoldAlertDialog from 'common/HoldAlert/HoldAlertDialog';
import Loader from 'common/Loader';
import useDocumentTitle from 'hooks/useDocumentTitle';
import AddToListbutton from 'Product/AddToListButton';
import { useCartContext } from 'providers/CartProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import CheckoutWarning from 'Cart/CheckoutWarning';
import { useListsContext } from 'providers/ListsProvider';

function Cart() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('common.cart'));

  /**
   * Context
   */
  const {
    cart,
    cartLoading,
    clearContract,
    contract,
    deleteCartItems,
    itemAdded,
    itemCount,
    setItemAdded,
    setQuoteId,
    updateItemQuantity,
    refreshCart
  } = useCartContext();
  const { setAddAlltoListId, setAvailableListIds } = useListsContext();
  const { isMincron, selectedAccounts } = useSelectedAccountsContext();

  const productCount = cart?.products?.length ?? 0;
  const { subTotal } = useOrderSummary({ page: 'cart' });

  /**
   * State
   */
  const [availableInLists, setAvailableInLists] = useState<string[]>([]);
  const [shouldShipFullOrder, setShouldShipFullOrder] = useState('one');
  const [stockAlertOpen, setStockAlertOpen] = useState(false);
  const [accountHoldDialogOpen, setAccountHoldDialogOpen] = useState(false);
  const [itemsRemovedDialogOpen, setItemsRemovedDialogOpen] = useState(false);

  /**
   * Effect
   */
  useEffect(cartQtyAlertEffect, [cart, contract]);
  useEffect(() => {
    setQuoteId(undefined);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(cartItemAddedEffect, [itemAdded]);

  const orderSummaryButtonClick = useCallback(orderSummaryButtonClickCb, [
    history,
    selectedAccounts.billToErpAccount?.creditHold,
    contract,
    refreshCart
  ]);

  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const updatedAddedAllToLists = (value: string[]) => {
    setAvailableInLists(value);
  };

  /**
   * Render
   */
  return (
    <Box
      bgcolor="common.white"
      flex="1"
      height="100%"
      display="flex"
      flexDirection="column"
      overflow="hidden"
      position="relative"
    >
      {!cart ? (
        <Loader />
      ) : (
        <>
          <NavigationAlert when={showNavPrompt} onConfirm={handleNavConfirm} />
          <HoldAlertDialog
            open={accountHoldDialogOpen}
            onClose={() => setAccountHoldDialogOpen(false)}
          />
          <ItemsRemovedDialog
            open={itemsRemovedDialogOpen}
            onClose={() => setItemsRemovedDialogOpen(false)}
          />
          <Container maxWidth="lg" sx={{ flex: 1 }}>
            <Box py={3.75}>
              <Box
                display="flex"
                justifyContent="space-between"
                alignItems="center"
              >
                <Grid xs={12}>
                  <Typography
                    fontWeight={700}
                    variant="h4"
                    component="h1"
                    data-testid={
                      contract
                        ? kebabCase(`${t('cart.releaseFromContract')}-title`)
                        : kebabCase(`${t('common.shoppingCart')}-title`)
                    }
                  >
                    {contract
                      ? t('cart.releaseFromContract')
                      : t('common.shoppingCart')}
                  </Typography>
                  <Box pt={0.625}>
                    {contract ? (
                      <Box mt={4}>
                        <Typography
                          variant="h5"
                          fontWeight={isSmallScreen ? 500 : 400}
                          fontSize={isSmallScreen ? 16 : 20}
                          component={isSmallScreen ? 'div' : 'span'}
                          data-testid="cart-contract-number"
                        >
                          {t('contract.contractNum')}
                          {contract.data?.contractNumber || t('common.na')}
                        </Typography>
                        <Typography
                          variant="h5"
                          fontWeight={isSmallScreen ? 500 : 400}
                          fontSize={isSmallScreen ? 16 : 20}
                          component={isSmallScreen ? 'div' : 'span'}
                          ml={isSmallScreen ? 0 : 3}
                          data-testid="cart-contract-desc"
                        >
                          {contract.data?.contractDescription || t('common.na')}
                        </Typography>
                      </Box>
                    ) : (
                      <Grid container xs={12} md={8.5}>
                        <Grid xs={6}>
                          <Typography
                            variant="body1"
                            data-testid="items-in-cart"
                          >
                            {cartLoading ? (
                              <Skeleton variant="rectangular" width={100} />
                            ) : (
                              `${productCount} ${t('cart.itemInCart', {
                                count: productCount
                              })}`
                            )}
                          </Typography>
                        </Grid>
                        <Hidden mdDown>
                          {itemCount > 1 && !contract && !isMincron && (
                            <Grid
                              item
                              justifyContent="right"
                              display="flex"
                              xs={6}
                            >
                              <>
                                <Box pr={5}>
                                  <AddToListbutton
                                    availableInList={availableInLists}
                                    updatedAddedToLists={updatedAddedAllToLists}
                                    index={-1}
                                    isAddAlltoList={true}
                                    cartId={cart.id}
                                  />
                                </Box>
                                <Button
                                  variant="inline"
                                  color="primaryLight"
                                  data-testid="remove-all-from-cart"
                                  onClick={handleOpen}
                                  sx={{ ml: 2 }}
                                >
                                  {t('cart.removeAllFromCart')}
                                </Button>
                                <RemoveCartItemsDialog
                                  open={open}
                                  onClose={handleClose}
                                />
                              </>
                            </Grid>
                          )}
                        </Hidden>
                      </Grid>
                    )}
                  </Box>
                </Grid>
              </Box>
              <Hidden mdUp>
                <OrderSummary
                  page="cart"
                  buttonText={t('cart.proceedToCheckout')}
                  onButtonClick={orderSummaryButtonClick}
                />
                {itemCount > 199 && <CheckoutWarning />}
              </Hidden>
              <Box pt={contract ? 2 : isSmallScreen ? 3 : 6} pb={6}>
                <Grid container spacing={6}>
                  <Grid item xs={12} md={9}>
                    <Box position="relative" mb={isSmallScreen ? 4 : 2.5}>
                      {cartLoading ? (
                        <Loader backdrop size="parent" loaderSize={60} />
                      ) : null}
                      <DeliveryMethod
                        isDisabled={!productCount}
                        deliveryMethod={cart.deliveryMethod}
                      />
                      <Shipments
                        isDisabled={cartLoading}
                        deliveryMethod={cart.deliveryMethod}
                        shouldShipFullOrder={shouldShipFullOrder}
                        setShouldShipFullOrder={setShouldShipFullOrder}
                        stockAlertOpen={stockAlertOpen}
                        setStockAlertOpen={setStockAlertOpen}
                      />
                      {checkIfOver10Mil(subTotal) && (
                        <Box mt={3} data-testid="over-10mil-warning">
                          <Warning />
                        </Box>
                      )}
                    </Box>
                    <Hidden mdUp>
                      {itemCount > 1 && !contract && !isMincron && (
                        <Grid
                          item
                          justifyContent="left"
                          display="flex"
                          xs={12}
                          mt={3}
                        >
                          <>
                            <Box pr={5} pl={2}>
                              <AddToListbutton
                                availableInList={availableInLists}
                                updatedAddedToLists={updatedAddedAllToLists}
                                index={-1}
                                isAddAlltoList={true}
                                cartId={cart.id}
                              />
                            </Box>
                            <Button
                              variant="inline"
                              color="primaryLight"
                              data-testid="remove-all-from-cart"
                              onClick={handleOpen}
                              sx={{ ml: 0 }}
                            >
                              {t('cart.removeAllFromCart')}
                            </Button>
                            <RemoveCartItemsDialog
                              open={open}
                              onClose={handleClose}
                            />
                          </>
                        </Grid>
                      )}
                    </Hidden>
                    <CartList
                      cart={cart}
                      updateItemQuantity={updateItemQuantity}
                    />
                  </Grid>
                  <Hidden mdDown>
                    <Grid item xs={12} md={3}>
                      <OrderSummary
                        page="cart"
                        buttonText={t('cart.proceedToCheckout')}
                        onButtonClick={orderSummaryButtonClick}
                      />
                      {itemCount > 199 && <CheckoutWarning />}
                    </Grid>
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

  function orderSummaryButtonClickCb() {
    refreshCart?.();
    selectedAccounts.billToErpAccount?.creditHold
      ? setAccountHoldDialogOpen(true)
      : history.push('/checkout', { canShowCustomNavAlert: !!contract });
  }

  async function handleNavConfirm() {
    clearContract();
    cart?.id && deleteCartItems();
  }

  function cartQtyAlertEffect() {
    setStockAlertOpen(false);
    if (!cart) {
      return;
    }
    const { products, removedProducts, delivery } = cart;
    const { shouldShipFullOrder } = delivery;
    setShouldShipFullOrder(shouldShipFullOrder ? 'one' : 'multiple');

    setItemsRemovedDialogOpen(!!removedProducts?.length);

    if (!products || !products?.length) {
      setStockAlertOpen(false);
      return;
    }
    for (const product of products!) {
      const availableQty = product?.qtyAvailable ?? 0;
      const qty = product?.quantity ?? 0;
      const notAvailable = (!availableQty || availableQty < qty) && !contract;
      if (notAvailable) {
        setStockAlertOpen(true);
        break;
      }
    }
  }

  function cartItemAddedEffect() {
    if (itemAdded) {
      setAvailableInLists([]);
      setAvailableListIds([]);
      setAddAlltoListId('');
      setItemAdded?.(false);
    }
  }

  function showNavPrompt(
    pLocation: Location<any>,
    nLocation?: Location,
    action?: Action
  ) {
    return pLocation.pathname === nLocation?.pathname ||
      action === 'POP' ||
      nLocation?.pathname?.includes('/checkout')
      ? false
      : !!pLocation?.state?.canShowNavAlert;
  }
}

export default Cart;
