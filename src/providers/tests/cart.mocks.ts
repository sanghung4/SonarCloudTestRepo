import { ApolloError } from '@apollo/client';
import { GraphQLError } from 'graphql';

import { Profile } from 'AuthProvider';
import { mockCart, mockCartProducts } from 'Cart/tests/mocks';
import {
  AddAllListItemsToCartMutation,
  AddItemsToCartMutation,
  Cart,
  CartFromQuoteQuery,
  CartQuery,
  CartUserIdAccountIdQuery,
  ContractDetails,
  CreateCartMutation,
  CreditCard,
  DeleteCartItemsMutation,
  DeleteCreditCardFromCartMutation,
  DeleteItemMutation,
  ErpSystemEnum,
  LineItem,
  RefreshCartMutation,
  UpdateCartMutation,
  UpdateDeliveryMutation,
  UpdateItemQuantityMutation,
  UpdateWillCallMutation
} from 'generated/graphql';
import { mockData } from 'Contract/tests/mocks';
import { MAX_CART_ITEMS } from 'providers/CartProvider';

export const GQLErrBlank = new ApolloError({});
export const GQLErr404 = new ApolloError({
  graphQLErrors: [{ extensions: { code: 404 } } as unknown as GraphQLError]
});

export const mockEmptyCart: Cart = {
  id: 'testcart',
  erpSystemName: ErpSystemEnum.Eclipse,
  delivery: { id: 'testdeliveryid', shouldShipFullOrder: false },
  willCall: { id: 'testwillcallid' }
};

const maxProducts = Array.from(Array<LineItem>(MAX_CART_ITEMS), (_, index) => ({
  ...mockCartProducts[0],
  id: `id-${index}`,
  erpPartNumber: `erp-${index}`
}));
export const mockMaxCart: Cart = { ...mockCart, products: maxProducts };

const mockCreditCard: CreditCard = {
  cardHolder: 'Test',
  creditCardNumber: '1111222233334444',
  creditCardType: 'Visa',
  elementPaymentAccountId: '1234567890',
  expirationDate: {
    __typename: undefined,
    date: '10/10/2028'
  },
  postalCode: '91801',
  streetAddress: '108 Juan Tabo Dr'
};
export const mockCartWithCard: Cart = {
  ...mockCart,
  creditCard: { ...mockCreditCard }
};

export const mockProfile: Profile = {
  userId: 'test',
  permissions: [],
  isEmployee: false,
  isVerified: false
};

export const mockCartProductValid: LineItem = {
  id: 'testid',
  cartId: 'testcart',
  customerPartNumber: null,
  pricePerUnit: 43,
  priceLastUpdatedAt: null,
  erpPartNumber: '33860',
  quantity: 1,
  qtyAvailableLastUpdatedAt: null,
  qtyAvailable: 215,
  __typename: 'LineItem'
};

export const mockContractDetails: ContractDetails = {
  ...mockData
};

export const GET_CART_RES_EMPTY: CartQuery = {
  cart: { ...mockEmptyCart }
};
export const CREATE_CART_RES: CreateCartMutation = {
  createCart: 'testcart'
};
export const CREATE_CART_EMPTY: CreateCartMutation = {
  createCart: ''
};
export const CART_USER_ACCOUNT_ID_RES: CartUserIdAccountIdQuery = {
  cartUserIdAccountId: { ...mockEmptyCart }
};
export const CART_USER_ACCOUNT_ID_FULL: CartUserIdAccountIdQuery = {
  cartUserIdAccountId: { ...mockCart }
};
export const CART_FROM_QUOTE_EMPTY: CartFromQuoteQuery = {
  cartFromQuote: { ...mockEmptyCart }
};
export const CART_FROM_QUOTE_RES: CartFromQuoteQuery = {
  cartFromQuote: { ...mockCart }
};
export const ADD_ITEMS_TO_CART_RES: AddItemsToCartMutation = {
  addItemsToCart: { ...mockCart }
};
export const ADD_ALL_LIST_ITEMS_TO_CART_RES: AddAllListItemsToCartMutation = {
  addAllListItemsToCart: { ...mockCart }
};
export const DELETE_ITEM_RES: DeleteItemMutation = {
  deleteItem: { ...mockCart }
};
export const DELETE_CART_ITEMS_RES: DeleteCartItemsMutation = {
  deleteCartItems: { ...mockCart }
};
export const REFRESH_CART_RES: RefreshCartMutation = {
  refreshCart: { ...mockCart }
};
export const UPDATE_CART_RES: UpdateCartMutation = {
  updateCart: { ...mockCart }
};
export const UPDATE_DELIVERY_RES: UpdateDeliveryMutation = {
  updateDelivery: { ...mockCart }
};
export const UPDATE_ITEM_QTY_RES: UpdateItemQuantityMutation = {
  updateItemQuantity: {
    product: { ...mockCartProductValid, quantity: 5 },
    subtotal: 5
  }
};
export const UPDATE_ITEM_QTY_NOMATCH: UpdateItemQuantityMutation = {
  updateItemQuantity: {
    product: { ...mockCartProductValid, id: 'no-match' },
    subtotal: 1
  }
};
export const UPDATE_WILL_CALL_RES: UpdateWillCallMutation = {
  updateWillCall: { ...mockCart, willCall: { id: 'test', branchId: '1234 ' } }
};
export const DELETE_CC_FROM_CART_RES: DeleteCreditCardFromCartMutation = {
  deleteCreditCardFromCart: { ...mockCart, creditCard: null }
};
