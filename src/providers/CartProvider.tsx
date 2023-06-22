import { useState, useContext, useEffect, createContext, useMemo } from 'react';

import { MutationFetchPolicy } from '@apollo/client/core/watchQueryOptions';
import { GraphQLErrors } from '@apollo/client/errors';
import { isUndefined, uniqBy, pick, noop } from 'lodash-es';
import { useHistory, useLocation } from 'react-router-dom';
import { useSnackbar } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  Branch,
  Cart,
  CartInput,
  ContractDetails,
  Delivery,
  DeliveryInput,
  EcommAccount,
  ErpSystemEnum,
  GetBranchQuery,
  ItemInfo,
  Quote,
  useAddItemsToCartMutation,
  useAddAllListItemsToCartMutation,
  useCartFromQuoteQuery,
  useCartLazyQuery,
  useCartUserIdAccountIdLazyQuery,
  useCreateCartMutation,
  useDeleteCartItemsMutation,
  useDeleteItemMutation,
  useGetBranchQuery,
  useRefreshCartMutation,
  useUpdateCartMutation,
  useUpdateDeliveryMutation,
  useUpdateItemQuantityMutation,
  useUpdateWillCallMutation,
  WillCall,
  WillCallInput,
  DeliveryMethodEnum,
  ProductPricing,
  useDeleteCreditCardFromCartMutation
} from 'generated/graphql';

import { AuthContext } from 'AuthProvider';
import { AddedItemInfo } from 'Order/utils/addItemsToCartCb';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import {
  findCartProductIndex,
  pullAllContractProducts,
  pullContractProductFromMap
} from 'providers/utils/CartProviderUtils';

/**
 * CONSTS
 */
const noCache: { fetchPolicy: MutationFetchPolicy } = {
  fetchPolicy: 'network-only'
};
export const MAX_CART_ITEMS = 600;

/**
 * Types
 */
export type CartContractType = {
  id: string;
  shipToId: string;
  data?: ContractDetails;
};

export type CartContextType = {
  cart?: Cart;
  quoteId?: string | null;
  contract?: CartContractType;
  itemCount: number;
  itemAdded?: boolean;
  lineNotes: Record<string, string>;
  quoteData?: Quote | null;
  cartLoading: boolean;
  disableAddToCart: boolean;
  itemLoading?: string;
  previousCart?: Cart;
  quoteShipToId?: string | null;
  contractBranch?: GetBranchQuery['branch'];
  updateCartLoading?: boolean;
  checkingOutWithQuote: boolean;
  isWillCall: boolean;
  clearQuote: () => void;
  updateCart?: (cartId: string, cart: CartInput) => Promise<Cart | undefined>;
  deleteItem: (item: string) => void;
  setQuoteId: (quoteId?: string) => void;
  getUserCart: (shipTo: EcommAccount) => Promise<Cart | undefined>;
  setContract: (type?: CartContractType) => void;
  refreshCart?: () => Promise<Cart | undefined>;
  setItemAdded?: (val: boolean) => void;
  setLineNotes: (notes: Record<string, string>) => void;
  setQuoteData: (quote: Quote) => void;
  clearContract: () => void;
  addItemsToCart: (info: AddedItemInfo[]) => void;
  addAllListItemsToCart: (listId: string, listItemsInfo: ItemInfo[]) => void;
  deleteCartItems: () => Promise<Cart | undefined>;
  setPreviousCart: (cart?: Cart) => void;
  setQuoteShipToId: (item: string) => void;
  setSelectedBranch: (branch: Branch) => void;
  updateCartFromQuote?: (cartInput: CartInput) => void;
  updateWillCallBranch?: (cart: Cart) => void;
  addItemToCart: (
    productId: string,
    quantity: number,
    minIncrementQty: number,
    pricingData?: ProductPricing,
    customerNumber?: string
  ) => void;
  updateItemQuantity: (
    itemId: string,
    quantity: number,
    minIncrementQty: number
  ) => void;
  releaseContractToCart: (
    contract: ContractDetails | undefined,
    qtyMap?: Record<string, string>
  ) => void;
  updateDelivery?: (
    deliveryInfo: DeliveryInput
  ) => Promise<Delivery | undefined>;
  updateWillCall?: (
    willCallInfo: WillCallInput
  ) => Promise<WillCall | undefined>;
  deleteCreditCardFromCart: (cardId: string) => Promise<Cart | undefined>;
};

/**
 * Context
 */
// istanbul ignore next
const promiseNoop = <T,>() => new Promise<T>(noop);
export const CartContext = createContext<CartContextType>({
  addItemToCart: noop,
  addItemsToCart: noop,
  addAllListItemsToCart: noop,
  cartLoading: false,
  checkingOutWithQuote: false,
  contractBranch: undefined,
  clearContract: noop,
  clearQuote: noop,
  deleteCartItems: promiseNoop,
  deleteItem: noop,
  disableAddToCart: false,
  getUserCart: promiseNoop,
  itemAdded: false,
  itemCount: 0,
  itemLoading: undefined,
  lineNotes: {},
  releaseContractToCart: noop,
  setContract: noop,
  setItemAdded: noop,
  setLineNotes: noop,
  setQuoteId: noop,
  setQuoteData: noop,
  setQuoteShipToId: noop,
  setPreviousCart: noop,
  setSelectedBranch: noop,
  updateItemQuantity: noop,
  isWillCall: false,
  deleteCreditCardFromCart: promiseNoop
});

/**
 * Component
 */
function CartProvider(props: { children: React.ReactNode }) {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const location = useLocation();
  const { t } = useTranslation();
  const { pushAlert } = useSnackbar();

  /**
   * States
   */
  const [selectedBranch, setSelectedBranch] = useState<Branch>();
  const [cart, setCart] = useState<Cart>();
  const [previousCart, setPreviousCart] = useState<Cart>();
  const [quoteId, setQuoteId] = useState<string>();
  const [quoteShipToId, setQuoteShipToId] = useState<string>();
  const [quoteData, setQuoteData] = useState<Quote>();
  const [itemLoading, setItemLoading] = useState<string>();
  const [contract, setContract] = useState<CartContractType>();
  const [lineNotes, setLineNotes] = useState<Record<string, string>>({});
  const [itemAdded, setItemAdded] = useState(false);

  /**
   * Contexts
   */
  const { selectedAccounts, isEclipse, isMincron } =
    useSelectedAccountsContext();
  const { authState, profile } = useContext(AuthContext);
  const shipToId = selectedAccounts.shipTo?.id ?? '';
  const shippingBranchId = selectedAccounts.shippingBranchId;
  const userId = profile?.userId ?? '';

  /**
   * Data
   */
  // 🟣 Query - Get cart by id
  const [getCart, { loading: cartByIdLoading }] = useCartLazyQuery({
    onCompleted: ({ cart }) => setCart(cart)
  });

  // 🟣 Query - Get cart if quoteId exists
  const { loading: cartFromQuoteLoading } = useCartFromQuoteQuery({
    skip: !userId || !quoteId || !shipToId || !selectedBranch,
    variables: {
      userId,
      shipToAccountId: shipToId,
      quoteId: quoteId!,
      branchId: selectedBranch?.branchId ?? ''
    },
    onCompleted: ({ cartFromQuote }) => {
      setPreviousCart(cart);
      setCart(cartFromQuote);
    }
  });

  // 🟣 Query - Get Cart by User and Ship To Account IDs
  const [
    getCartUserIdAccountId,
    {
      loading: cartUserIdAccountIdLoading,
      called: cartUserIdAccountIdCalled,
      refetch: cartUserIdAccountIdRefetch
    }
  ] = useCartUserIdAccountIdLazyQuery({
    refetchWritePolicy: 'overwrite',
    onCompleted: ({ cartUserIdAccountId }) =>
      cartUserIdAccountId && setCart(cartUserIdAccountId),
    onError: (error) => handleEmptyCartError(error.graphQLErrors)
  });

  // 🟣 Query - Contact branch
  const { data: getBranchData } = useGetBranchQuery({
    skip: !contract?.data?.accountInformation?.branch?.branchNumber,
    variables: {
      branchId: contract?.data?.accountInformation?.branch?.branchNumber ?? ''
    }
  });

  // 🟣 Mutation - Contact branch
  const [createCartMutation] = useCreateCartMutation({
    onCompleted: ({ createCart: id }) => {
      if (id && userId && shipToId) {
        const includeProducts = true;
        const shipToAccountId = shipToId;
        getCart({
          variables: { id, userId, shipToAccountId, includeProducts }
        });
      } else handleCartError('getCart');
    }
  });
  //
  // 🟣 Mutation - MISC
  const [addItemsToCartMutation] = useAddItemsToCartMutation(noCache);
  const [addAllListItemsToCartMutation] =
    useAddAllListItemsToCartMutation(noCache);
  const [deleteItemMutation] = useDeleteItemMutation(noCache);
  const [deleteCartItemsMutation] = useDeleteCartItemsMutation(noCache);
  const [refreshCartMutation] = useRefreshCartMutation(noCache);
  const [updateCartMutation, { loading: updateCartLoading }] =
    useUpdateCartMutation(noCache);
  const [updateDeliveryMutation] = useUpdateDeliveryMutation(noCache);
  const [updateItemQuantityMutation] = useUpdateItemQuantityMutation(noCache);
  const [updateWillCallMutation] = useUpdateWillCallMutation(noCache);
  const [deleteCreditCardFromCartMutation] =
    useDeleteCreditCardFromCartMutation(noCache);

  /**
   * Constants
   */
  const checkingOutWithQuote = Boolean(quoteId);
  const invalidCartState = !cart || !userId || !shipToId;

  /**
   * Memos
   */
  // 🔵 Memo - Item count
  const itemCount = useMemo(() => {
    const selectCart = checkingOutWithQuote
      ? previousCart?.products
      : cart?.products;
    const count = contract
      ? selectCart?.length || 0
      : uniqBy(selectCart, 'erpPartNumber').length;
    return count;
  }, [checkingOutWithQuote, previousCart, cart, contract]);
  const disableAddToCart = itemCount >= MAX_CART_ITEMS;

  /**
   * Effects
   */
  // 🟡 - Init Cart as Eclipse
  useEffect(() => {
    // If the user is logged in and we have the Account info
    // then try to get the Cart for the User and
    // selected Ship To Account
    const isInValidAddress =
      ['/select-accounts', '/login/callback'].indexOf(location.pathname) === -1;
    if (
      isInValidAddress &&
      authState?.isAuthenticated &&
      userId &&
      shipToId &&
      !cartUserIdAccountIdCalled &&
      shippingBranchId
    ) {
      const variables = { userId, accountId: shipToId, includeProducts: true };
      getCartUserIdAccountId({ variables });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    authState?.isAuthenticated,
    cartUserIdAccountIdCalled,
    getCartUserIdAccountId,
    location.pathname,
    shipToId,
    shippingBranchId,
    userId
  ]);

  // 🟡 Effect - Init Cart as Mincron
  useEffect(() => {
    if (isMincron && !contract && !cart) {
      setCart({
        id: '',
        delivery: { id: '', shouldShipFullOrder: false },
        willCall: { id: '' },
        erpSystemName: ErpSystemEnum.Mincron
      });
    }
  }, [cart, contract, isMincron]);

  /**
   * Callbacks
   */
  // 🟤 CB - Clear Quote
  const clearQuote = () => {
    // istanbul ignore next - Ignore because test kept treating `quoteShipToId` not covered even though `quoteId` is NOT falsy
    if ((quoteId || quoteShipToId) && previousCart) {
      setCart(previousCart);
    }
    setQuoteId(undefined);
    setQuoteShipToId(undefined);
  };

  // 🟤 CB - UpdateWillCallBranch (setCart with Cart type restrict)
  const updateWillCallBranch = (cart: Cart) => {
    setCart(cart);
  };

  // 🟤 CB - Create Cart
  const createCart = async (newShipTo?: EcommAccount) => {
    const checkShipTo = newShipTo || shipToId;
    if (userId && checkShipTo && isEclipse && shipToId && shippingBranchId) {
      const variables = {
        userId,
        shipToAccountId: newShipTo?.id ?? shipToId,
        branchId: shippingBranchId
      };
      return createCartMutation({ variables });
    }
  };

  // 🟤 CB - Add Item To Cart
  function addItemToCart(
    productId: string,
    qty: number,
    minIncrementQty: number,
    pricingData?: ProductPricing,
    customerNumber?: string
  ) {
    if (invalidCartState || contract) {
      !contract && handleCartError('addItemToCart', cart);
      return;
    }
    const qtyAvailable = selectedBranch?.isPricingOnly
      ? pricingData?.totalAvailableQty ?? 0
      : pricingData?.branchAvailableQty ?? 0;

    const itemInfoList = [
      {
        qty,
        productId,
        minIncrementQty,
        qtyAvailable,
        uom: pricingData?.orderUom ?? 'ea',
        pricePerUnit: pricingData?.sellPrice ?? 0
      }
    ];

    setItemLoading(productId);
    addItemsToCartMutation({
      variables: {
        cartId: cart.id,
        addItemsInput: {
          userId,
          shipToAccountId: shipToId,
          itemInfoList,
          customerNumber
        }
      },
      onCompleted: ({ addItemsToCart }) => {
        setCart(addItemsToCart);
        setItemAdded(true);
        stopItemLoading();
        if (
          addItemsToCart.products &&
          addItemsToCart.products.length >= MAX_CART_ITEMS
        ) {
          pushAlert(t('cart.atLimit'), { variant: 'warning' });
        }
      },
      onError: stopItemLoading
    });
  }

  // 🟤 CB - Add Multiple Items To Cart
  function addItemsToCart(items: AddedItemInfo[]) {
    if (invalidCartState || contract) {
      !contract && handleCartError('addItemsToCart', cart);
      return;
    }

    // Map over items to get qtyAvailable based on selected branch.
    const itemInfoList = items.map((item) => ({
      qty: item.qty,
      productId: item.productId,
      minIncrementQty: item.minIncrementQty,
      qtyAvailable: selectedBranch?.isPricingOnly
        ? item.pricingData?.totalAvailableQty ?? 0
        : item.pricingData?.branchAvailableQty ?? 0,
      uom: item.pricingData?.orderUom ?? 'ea',
      pricePerUnit: item.pricingData?.sellPrice ?? 0
    }));
    addItemsToCartMutation({
      variables: {
        cartId: cart.id,
        addItemsInput: { userId, shipToAccountId: shipToId, itemInfoList }
      },
      onCompleted: ({ addItemsToCart }) => {
        setCart(addItemsToCart);
        setItemAdded(true);
        stopItemLoading();
      },
      onError: stopItemLoading
    });
  }

  // 🟤 CB - Add Items from List to Cart
  function addAllListItemsToCart(listId: string, itemInfoList: ItemInfo[]) {
    if (invalidCartState || contract) {
      !contract && handleCartError('addAllListItemsToCart', cart);
      return;
    }

    if (itemCount + itemInfoList.length > MAX_CART_ITEMS) {
      pushAlert(t('cart.overLimit'), {
        variant: 'error'
      });
      return;
    }

    addAllListItemsToCartMutation({
      variables: {
        cartId: cart.id,
        listId: listId,
        addAllToCartUserInput: {
          itemInfoList,
          userId,
          shipToAccountId: shipToId
        }
      },
      onCompleted: ({ addAllListItemsToCart }) => {
        setCart(addAllListItemsToCart);
        stopItemLoading();
      },
      onError: stopItemLoading
    });
  }

  // 🟤 CB - Delete Item
  function deleteItem(itemId: string) {
    if (invalidCartState) {
      handleCartError('deleteItem', cart);
      return;
    }
    if (contract) {
      deleteContractItem(itemId);
      return;
    }
    deleteItemMutation({
      variables: {
        itemId,
        cartId: cart.id,
        userId,
        shipToAccountId: shipToId
      },
      onCompleted: ({ deleteItem }) => setCart(deleteItem)
    });
  }

  // 🟤 CB - Delete all items from cart
  async function deleteCartItems() {
    if (!cart || contract) {
      handleCartError('deleteCartItems', cart);
      return;
    }
    const cartId = cart.id;
    const res = await deleteCartItemsMutation({ variables: { cartId } });
    const newCart = res.data?.deleteCartItems;
    setCart(newCart);
    return newCart;
  }

  // 🟤 CB - Refresh cart
  async function refreshCart() {
    if (invalidCartState || contract) {
      !contract && handleCartError('refreshCart', cart);
      return;
    }
    const res = await refreshCartMutation({
      variables: { cartId: cart.id, shipToAccountId: shipToId, userId }
    });
    const refreshedCart = res.data?.refreshCart;
    setCart(refreshedCart);
    return refreshedCart;
  }

  // 🟤 CB - Get Cart details
  async function getUserCart(shipTo: EcommAccount) {
    if (!userId || !shipTo.id || contract) {
      return;
    }
    const res = await cartUserIdAccountIdRefetch({
      userId,
      accountId: shipTo.id,
      includeProducts: true
    });
    if (res?.error?.graphQLErrors) {
      handleEmptyCartError(res.error.graphQLErrors, shipTo);
      return;
    }

    const newCart = res?.data?.cartUserIdAccountId;
    setCart(newCart);

    return newCart;
  }

  // 🟤 CB - Update the entire cart
  async function updateCart(cartId: string, cartInput: CartInput) {
    if (!cart) {
      handleCartError('updateCart');
      return;
    }
    if (quoteId || contract) {
      setCart({ ...cart, ...cartInput } as Cart);
      return;
    }
    const updateCartVar = { cartId, cart: cartInput };
    const res = await updateCartMutation({ variables: updateCartVar });
    if (!res?.data?.updateCart) {
      return;
    }
    const newCart = res.data.updateCart;
    const newCartProps = pick(newCart, Object.keys(cartInput));
    const mergedCart = { ...cart, ...newCartProps };
    setCart(mergedCart);
    return mergedCart;
  }

  // 🟤 CB - Delete Credit Card From Cart
  async function deleteCreditCardFromCart(cartId: string) {
    const variables = { cartId };
    const res = await deleteCreditCardFromCartMutation({ variables });
    if (!res?.data?.deleteCreditCardFromCart) {
      return;
    }
    const newCart = { ...cart, creditCard: null } as Cart;
    setCart(newCart);
    return newCart;
  }

  // 🟤 CB - Add Item To Cart
  function updateCartFromQuote(cartInput: CartInput) {
    if (!cart) {
      handleCartError('updateCart');
      return;
    }
    setCart({ ...cart, ...cartInput } as Cart);
  }

  // 🟤 CB - Update Cart's delivery method
  async function updateDelivery(deliveryInfo: DeliveryInput) {
    if (!cart) {
      handleCartError('updateDelivery', cart);
      return;
    }
    if (quoteId || contract) {
      const delivery = { ...cart.delivery, ...deliveryInfo } as Delivery;
      setCart({ ...cart, delivery });
      return;
    }
    const res = await updateDeliveryMutation({ variables: { deliveryInfo } });
    if (!res?.data) {
      return;
    }
    const oldCart = { ...cart };
    const newCart = res.data.updateDelivery;
    const newDeliveryProps = pick(newCart.delivery, Object.keys(deliveryInfo));
    oldCart.delivery = { ...oldCart.delivery, ...newDeliveryProps };
    setCart(oldCart);
    return oldCart.delivery;
  }

  // 🟤 CB - Update an item qty in cart
  function updateItemQuantity(
    itemId: string,
    quantity: number,
    minIncrementQty: number
  ) {
    if (invalidCartState) {
      handleCartError('updateItemQuantity', cart);
      return;
    }
    if (contract) {
      updateContractItemQty(itemId, quantity);
      return;
    }
    setItemLoading(itemId);
    updateItemQuantityMutation({
      variables: {
        itemId,
        quantity,
        cartId: cart.id,
        userId,
        shipToAccountId: shipToId,
        minIncrementQty,
        customerNumber: selectedAccounts.billToErpAccount?.erpAccountId
      },
      onCompleted: ({ updateItemQuantity }) => {
        if (!updateItemQuantity || !cart.products) {
          return;
        }
        cart.subtotal = updateItemQuantity.subtotal;
        // The removed products need to be set to null so that the removed items dialog box does not
        //  continue to open every time a line item has an updated qty
        cart.removedProducts = null;
        const findProductIndex = cart.products!.findIndex(
          (item) => item.id === updateItemQuantity.product.id
        );
        if (findProductIndex !== -1) {
          // Already established that products is guaranteed NOT null when findProductIndex is NOT -1
          cart.products![findProductIndex] = updateItemQuantity.product;
        }
        setCart({ ...cart });
        stopItemLoading();
      },
      onError: stopItemLoading
    });
  }

  // 🟤 CB - Update willCall method
  async function updateWillCall(willCallInfo: WillCallInput) {
    if (!cart) {
      handleCartError('updateWillCallCb');
      return;
    }
    if (quoteId || contract) {
      const willCall = { ...cart.willCall, ...willCallInfo };
      setCart({ ...cart, willCall });
      return;
    }
    const res = await updateWillCallMutation({ variables: { willCallInfo } });
    if (!res.data) {
      return;
    }
    const oldCart = { ...cart };
    const newCart = res.data.updateWillCall;
    const newWillCallProps = pick(newCart.willCall, Object.keys(willCallInfo));
    oldCart.willCall = { ...oldCart.willCall, ...newWillCallProps };
    setCart(oldCart);
    return oldCart.willCall;
  }

  // 🟤 CB - Release contract to cart
  function releaseContractToCart(
    contractDetails?: ContractDetails,
    qtyMap?: Record<string, string>
  ) {
    const sameContract =
      contract?.data?.contractNumber === contractDetails?.contractNumber;

    if (!contractDetails?.contractProducts) {
      console.error('Failure to release contract to cart!');
      return;
    }
    if (cart && !contract) {
      setPreviousCart(cart);
    }

    const products = qtyMap
      ? pullContractProductFromMap(
          contractDetails,
          qtyMap!,
          sameContract,
          cart?.products!
        )
      : pullAllContractProducts(contractDetails);
    const id = contractDetails?.contractNumber ?? '';
    setCart({
      delivery: { id: '', shouldShipFullOrder: false },
      erpSystemName: ErpSystemEnum.Mincron,
      willCall: { id: '' },
      products,
      id,
      shipToId: contractDetails?.jobName
    } as Cart);

    setContract({ data: contractDetails, id, shipToId: '' });
  }

  // 🟤 CB - Update item in cart with contract
  function updateContractItemQty(itemId: string, quantity: number) {
    const { clonedProducts, index } = findCartProductIndex(itemId, cart);
    if (index === -1) {
      return;
    }
    clonedProducts[index]!.quantity = quantity;
    setCart({ ...cart, products: clonedProducts } as Cart);
  }

  // 🟤 CB - Clear contract
  function clearContract() {
    if (contract) {
      setContract(undefined);
      setCart(previousCart);
      setLineNotes({});
    }
  }

  // 🟤 CB - Delete item from cart with contract
  function deleteContractItem(itemId: string) {
    const { clonedProducts, index } = findCartProductIndex(itemId, cart);

    if (index === -1) {
      return;
    }
    clonedProducts.splice(index, 1);

    // Handle line note deletion
    if (!isUndefined(lineNotes[itemId])) {
      const mutableLineNotes = { ...lineNotes };
      delete mutableLineNotes[itemId];
      setLineNotes(mutableLineNotes);
    }

    setCart({ ...cart!, products: clonedProducts }); // When this is called, cart is guranteed NOT undefined
  }

  /**
   * Helpers
   */
  // 🔴 Error - Empty Cart
  function handleEmptyCartError(errors: GraphQLErrors, shipTo?: EcommAccount) {
    const is404 = errors?.some(
      (error) =>
        (error.extensions?.response as any)?.status === 404 ||
        error.extensions?.code === 404
    );
    if (is404) {
      // If the Shipping Branch ID has not been set, for some reason,
      // then retrieve it first before creating the Cart
      createCart(shipTo);
    }
  }
  // 🔴 Error - error handling
  const handleCartError = (name: string, errorCart?: Cart) => {
    if (!errorCart) {
      console.error(`[${name}] Error: cart not found`);
    }
    if (!userId) {
      console.error(`[${name}] Error: userId not found`);
    }
    if (!shipToId) {
      console.error(`[${name}] Error: shipTo account id not found`);
    }
    history.push('/error');
  };
  // 🟤 Stop Item Loading
  const stopItemLoading = () => setItemLoading(undefined);

  /**
   * Render
   */
  return (
    <CartContext.Provider
      value={{
        addItemToCart,
        addItemsToCart,
        addAllListItemsToCart,
        cart,
        cartLoading:
          cartByIdLoading ||
          cartUserIdAccountIdLoading ||
          cartFromQuoteLoading ||
          updateCartLoading,
        checkingOutWithQuote,
        contractBranch: getBranchData?.branch,
        clearContract,
        clearQuote,
        contract,
        deleteCartItems,
        deleteItem,
        disableAddToCart,
        getUserCart,
        itemAdded,
        itemCount,
        itemLoading,
        lineNotes,
        previousCart,
        quoteId,
        quoteData,
        quoteShipToId,
        releaseContractToCart,
        setContract,
        setItemAdded,
        setLineNotes,
        setQuoteData,
        setQuoteId,
        setQuoteShipToId,
        setPreviousCart,
        setSelectedBranch,
        updateCart,
        updateCartFromQuote,
        updateCartLoading,
        updateDelivery,
        updateItemQuantity,
        updateWillCall,
        updateWillCallBranch,
        refreshCart,
        isWillCall: cart?.deliveryMethod === DeliveryMethodEnum.Willcall,
        deleteCreditCardFromCart
      }}
    >
      {props.children}
    </CartContext.Provider>
  );
}

export const useCartContext = () => useContext(CartContext);

export default CartProvider;
