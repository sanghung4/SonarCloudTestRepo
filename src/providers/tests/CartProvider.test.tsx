import { ApolloError } from '@apollo/client/errors';
import { act } from 'react-dom/test-utils';

import { UserContextType } from 'AuthProvider';
import { mockBranch } from 'Branches/tests/mocks';
import { mockCart, mockCartContract, mockCartProducts } from 'Cart/tests/mocks';
import {
  AddAllListItemsToCartMutation,
  AddItemsToCartMutation,
  AddressInput,
  CartFromQuoteQuery,
  CartQuery,
  CartUserIdAccountIdQuery,
  CreateCartMutation,
  DeleteCartItemsMutation,
  DeleteCreditCardFromCartMutation,
  DeleteItemMutation,
  ItemInfo,
  RefreshCartMutation,
  UpdateCartMutation,
  UpdateDeliveryMutation,
  UpdateItemQuantityMutation,
  UpdateWillCallMutation,
  useAddAllListItemsToCartMutation,
  useAddItemsToCartMutation,
  useCartFromQuoteQuery,
  useCartLazyQuery,
  useCartUserIdAccountIdLazyQuery,
  useCreateCartMutation,
  useDeleteCartItemsMutation,
  useDeleteCreditCardFromCartMutation,
  useDeleteItemMutation,
  useRefreshCartMutation,
  useUpdateCartMutation,
  useUpdateDeliveryMutation,
  useUpdateItemQuantityMutation,
  useUpdateWillCallMutation
} from 'generated/graphql';
import * as T from 'locales/en/translation.json';
import CartProvider, {
  CartContextType,
  useCartContext
} from 'providers/CartProvider';
import { SelectedAccountsContextType } from 'providers/SelectedAccountsProvider';
import {
  ADD_ALL_LIST_ITEMS_TO_CART_RES,
  ADD_ITEMS_TO_CART_RES,
  CART_FROM_QUOTE_EMPTY,
  CART_USER_ACCOUNT_ID_FULL,
  CART_USER_ACCOUNT_ID_RES,
  CREATE_CART_EMPTY,
  CREATE_CART_RES,
  DELETE_CART_ITEMS_RES,
  DELETE_CC_FROM_CART_RES,
  DELETE_ITEM_RES,
  GET_CART_RES_EMPTY,
  GQLErr404,
  GQLErrBlank,
  mockCartProductValid,
  mockCartWithCard,
  mockContractDetails,
  mockEmptyCart,
  mockMaxCart,
  mockProfile,
  REFRESH_CART_RES,
  UPDATE_CART_RES,
  UPDATE_DELIVERY_RES,
  UPDATE_ITEM_QTY_NOMATCH,
  UPDATE_ITEM_QTY_RES,
  UPDATE_WILL_CALL_RES
} from 'providers/tests/cart.mocks';
import { dummyEcommAccounts, dummyUserAccounts } from 'test-utils/dummyData';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type Mocks = {
  authConfig?: Partial<UserContextType>;
  selectedAccountsConfig?: Partial<SelectedAccountsContextType>;
  pathname: string;
  historyPush: jest.Mock;
  pushAlert: jest.Mock;
  useCartQuery: {
    data: CartQuery;
    loading: boolean;
  };
  useCartFromQuoteQuery: {
    data: CartFromQuoteQuery;
    called: boolean;
    loading: boolean;
  };
  useCartUidAidQuery: {
    data: CartUserIdAccountIdQuery;
    loading: boolean;
    called: boolean;
    error?: ApolloError;
  };
  useCreateCartMutation: {
    data: CreateCartMutation;
    error?: ApolloError;
  };
  useAddItemsToCartMutation: AddItemsToCartMutation;
  useAddAllListItemsToCartMutation: AddAllListItemsToCartMutation;
  useDeleteItemMutation: DeleteItemMutation;
  useDeleteCartItemsMutation: DeleteCartItemsMutation;
  useRefreshCartMutation: RefreshCartMutation;
  useUpdateCartMutation: {
    data?: UpdateCartMutation;
    loading: boolean;
  };
  useUpdateDeliveryMutation?: UpdateDeliveryMutation;
  useUpdateItemQuantityMutation?: UpdateItemQuantityMutation;
  useUpdateWillCallMutation?: UpdateWillCallMutation;
  useDeleteCreditCardFromCartMutation?: DeleteCreditCardFromCartMutation;
};
type MockGQLProp<R> = {
  fetchPolicy?: string;
  onCompleted?: (data: R) => void;
  onError?: (error: ApolloError) => void;
  refetchWritePolicy?: string;
  skip?: boolean;
};

/**
 * Mock values
 */
const defaultMocks: Mocks = {
  authConfig: {},
  selectedAccountsConfig: {},
  pathname: '/select-accounts',
  historyPush: jest.fn(),
  pushAlert: jest.fn(),
  useCartQuery: { data: { ...GET_CART_RES_EMPTY }, loading: false },
  useCartFromQuoteQuery: {
    data: { ...CART_FROM_QUOTE_EMPTY },
    called: false,
    loading: false
  },
  useCartUidAidQuery: {
    data: { ...CART_USER_ACCOUNT_ID_RES },
    loading: false,
    called: false
  },
  useCreateCartMutation: { data: { ...CREATE_CART_RES } },
  useAddItemsToCartMutation: { ...ADD_ITEMS_TO_CART_RES },
  useAddAllListItemsToCartMutation: { ...ADD_ALL_LIST_ITEMS_TO_CART_RES },
  useDeleteItemMutation: { ...DELETE_ITEM_RES },
  useDeleteCartItemsMutation: { ...DELETE_CART_ITEMS_RES },
  useRefreshCartMutation: { ...REFRESH_CART_RES },
  useUpdateCartMutation: { data: { ...UPDATE_CART_RES }, loading: false },
  useUpdateDeliveryMutation: { ...UPDATE_DELIVERY_RES },
  useUpdateItemQuantityMutation: { ...UPDATE_ITEM_QTY_RES },
  useUpdateWillCallMutation: { ...UPDATE_WILL_CALL_RES },
  useDeleteCreditCardFromCartMutation: { ...DELETE_CC_FROM_CART_RES }
};
let mocks = { ...defaultMocks };

/**
 * Mock methods
 */
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useCartLazyQuery: jest.fn(),
  useCartFromQuoteQuery: jest.fn(),
  useCartUserIdAccountIdLazyQuery: jest.fn(),
  useCreateCartMutation: jest.fn(),
  useAddItemsToCartMutation: jest.fn(),
  useAddAllListItemsToCartMutation: jest.fn(),
  useDeleteItemMutation: jest.fn(),
  useDeleteCartItemsMutation: jest.fn(),
  useRefreshCartMutation: jest.fn(),
  useUpdateCartMutation: jest.fn(),
  useUpdateDeliveryMutation: jest.fn(),
  useUpdateItemQuantityMutation: jest.fn(),
  useUpdateWillCallMutation: jest.fn(),
  useDeleteCreditCardFromCartMutation: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({ pathname: mocks.pathname }),
  useHistory: () => ({ push: mocks.historyPush })
}));
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

/**
 * Setup
 */
function setup({ authConfig, selectedAccountsConfig }: Mocks) {
  // Init output
  const cartContext = {} as CartContextType;

  // Create a dummy component to call the context
  function Component() {
    Object.assign(cartContext, useCartContext());
    return null;
  }

  // Render the dummy component wrapped with CartProvider
  render(
    <CartProvider>
      <Component />
    </CartProvider>,
    { authConfig, selectedAccountsConfig }
  );

  return cartContext;
}

/**
 * TEST
 */
describe('provider/CartProvider', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks, pushAlert: jest.fn() };
    mocks.useCartUidAidQuery.called = false;
    mocks.useCartUidAidQuery.error = undefined;
    mocks.useCartFromQuoteQuery.called = false;
    mocks.useCreateCartMutation.error = undefined;
  });

  // 🔵 MOCK GQL hooks (these are in the same declaration order as the Provider)
  beforeEach(() => {
    // 🔵 Hook - useCartLazyQuery
    (useCartLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<CartQuery>) => {
        const { data, loading } = mocks.useCartQuery;
        const call = jest.fn(() => param.onCompleted?.(data));
        return [call, { loading }];
      }
    );

    // 🔵 Hook - useCartFromQuoteQuery
    (useCartFromQuoteQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<CartFromQuoteQuery>) => {
        const { called, data, loading } = mocks.useCartFromQuoteQuery;
        const { skip, onCompleted } = param;
        if (!called && !skip) {
          mocks.useCartFromQuoteQuery.called = true;
          onCompleted?.(data);
        }
        return { loading };
      }
    );

    // 🔵 Hook - useCartLazyQuery
    (useCartUserIdAccountIdLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<CartUserIdAccountIdQuery>) => {
        const { data, error, called, loading } = mocks.useCartUidAidQuery;
        const call = jest.fn(() => {
          mocks.useCartUidAidQuery.called = true;
          error ? param.onError?.(error) : param.onCompleted?.(data);
          return { data, error };
        });
        return [call, { called, loading, refetch: call }];
      }
    );

    // 🔵 Hook - useCartLazyQuery
    (useCreateCartMutation as jest.Mock).mockImplementation(
      (param: MockGQLProp<CreateCartMutation>) => {
        const { data } = mocks.useCreateCartMutation;
        const call = jest.fn(() =>
          mocks.useCreateCartMutation.error
            ? param.onError?.(mocks.useCreateCartMutation.error)
            : param.onCompleted?.(data)
        );
        return [call];
      }
    );

    // 🔵 Hook - useAddItemsToCartMutation
    (useAddItemsToCartMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useAddItemsToCartMutation;
      const call = jest.fn((p: MockGQLProp<AddItemsToCartMutation>) =>
        p.onCompleted?.(data)
      );
      return [call];
    });

    // 🔵 Hook - useAddAllListItemsToCartMutation
    (useAddAllListItemsToCartMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useAddAllListItemsToCartMutation;
      const call = jest.fn((p: MockGQLProp<AddAllListItemsToCartMutation>) =>
        p.onCompleted?.(data)
      );
      return [call];
    });

    // 🔵 Hook - useDeleteItemMutation
    (useDeleteItemMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useDeleteItemMutation;
      const call = jest.fn((p: MockGQLProp<DeleteItemMutation>) =>
        p.onCompleted?.(data)
      );
      return [call];
    });

    // 🔵 Hook - useDeleteCartItemsMutation
    (useDeleteCartItemsMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useDeleteCartItemsMutation;
      const call = jest.fn(() => ({ data }));
      return [call];
    });

    // 🔵 Hook - useRefreshCartMutation
    (useRefreshCartMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useRefreshCartMutation;
      const call = jest.fn(() => ({ data }));
      return [call];
    });

    // 🔵 Hook - useUpdateCartMutation
    (useUpdateCartMutation as jest.Mock).mockImplementation(() => {
      const { data, loading } = mocks.useUpdateCartMutation;
      const call = jest.fn(() => ({ data }));
      return [call, { loading }];
    });

    // 🔵 Hook - useUpdateDeliveryMutation
    (useUpdateDeliveryMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useUpdateDeliveryMutation;
      const call = jest.fn(() => ({ data }));
      return [call];
    });

    // 🔵 Hook - useUpdateItemQuantityMutation
    (useUpdateItemQuantityMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useUpdateItemQuantityMutation;
      const call = jest.fn(
        (p: MockGQLProp<UpdateItemQuantityMutation | undefined>) =>
          p.onCompleted?.(data)
      );
      return [call];
    });

    // 🔵 Hook - useUpdateWillCallMutation
    (useUpdateWillCallMutation as jest.Mock).mockImplementation(() => {
      const data = mocks.useUpdateWillCallMutation;
      const call = jest.fn(() => ({ data }));
      return [call];
    });

    // 🔵 Hook - useDeleteCreditCardFromCartMutation
    (useDeleteCreditCardFromCartMutation as jest.Mock).mockImplementation(
      () => {
        const data = mocks.useDeleteCreditCardFromCartMutation;
        const call = jest.fn(() => ({ data }));
        return [call];
      }
    );
  });

  // 🟢 1 - Default
  it('Expect CartProvider to match snapshot with default values', () => {
    // Arrange
    mocks.authConfig = { profile: undefined };
    const cartContext = setup(mocks);

    // Assert
    expect(cartContext).toMatchSnapshot();
  });

  // 🟢 2 - useEffect -> getCartUserIdAccountId -> setCart
  it('Expect truthy cart by: useEffect > getCart', () => {
    // Arrange
    const authState = { isAuthenticated: true };
    const shipTo = { ...dummyUserAccounts[0] };
    const selectedAccounts = { shipTo, shippingBranchId: 'test' };
    mocks.pathname = '/';
    mocks.authConfig = { authState, profile: mockProfile };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Assert
    expect(cartContext.cart).toBeTruthy();
  });

  // 🟢 3 - [MINCRON] useEffect -> setCart
  it('Expect truthy cart by: useEffect > setCart under isMincron condition', () => {
    // Arrange
    mocks.selectedAccountsConfig = { isMincron: true };
    const cartContext = setup(mocks);

    // Assert
    expect(cartContext.cart).toBeTruthy();
  });

  // 🟢 4 - useEffect -> getCartUserIdAccountId -> onError (empty error) -> nothing called
  it('Expect falsey cart by: useEffect > getCart > blank error', () => {
    // Arrange
    const authState = { isAuthenticated: true };
    const shipTo = { ...dummyUserAccounts[0] };
    const selectedAccounts = { shipTo, shippingBranchId: 'test' };
    mocks.pathname = '/';
    mocks.authConfig = { authState, profile: mockProfile };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.useCartUidAidQuery.error = { ...GQLErrBlank };
    const cartContext = setup(mocks);

    // Assert
    expect(cartContext.cart).toBeFalsy();
  });

  // 🟢 5 - useEffect -> getCartUserIdAccountId -> onError (404) -> createCart -> createCartMutation -> setCart
  it('Expect truthy cart by: useEffect > getCart > 404 > createCart', () => {
    // Arrange
    const authState = { isAuthenticated: true };
    const shipTo = { ...dummyUserAccounts[0] };
    const selectedAccounts = { shipTo, shippingBranchId: 'test' };
    mocks.pathname = '/';
    mocks.authConfig = { authState, profile: mockProfile };
    mocks.selectedAccountsConfig = { selectedAccounts, isEclipse: true };
    mocks.useCartUidAidQuery.error = { ...GQLErr404 };
    const cartContext = setup(mocks);

    // Assert
    expect(cartContext.cart).toBeTruthy();
  });

  // 🟢 6 - useEffect -> getCartUserIdAccountId -> onError (404) -> createCart -> nothing happened (!isEclipse)
  it('Expect falsey cart by: useEffect > getCart > 404 > createCart with false isEclipse', () => {
    // Arrange
    const authState = { isAuthenticated: true };
    const shipTo = { ...dummyUserAccounts[0] };
    const selectedAccounts = { shipTo, shippingBranchId: 'test' };
    mocks.pathname = '/';
    mocks.authConfig = { authState, profile: { ...mockProfile } };
    mocks.selectedAccountsConfig = { selectedAccounts, isEclipse: false };
    mocks.useCartUidAidQuery.error = { ...GQLErr404 };
    const cartContext = setup(mocks);

    // Assert
    expect(cartContext.cart).toBeFalsy();
  });

  // 🟢 7 - useEffect -> getCartUserIdAccountId -> onError (404) -> createCart (empty) -> handleCartError
  it('Expect falsey cart and errors by: useEffect > getCart > 404 > createCart returns empty string', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const authState = { isAuthenticated: true };
    const shipTo = { ...dummyUserAccounts[0] };
    const selectedAccounts = { shipTo, shippingBranchId: 'test' };
    mocks.pathname = '/';
    mocks.authConfig = { authState, profile: mockProfile };
    mocks.selectedAccountsConfig = { selectedAccounts, isEclipse: true };
    mocks.useCartUidAidQuery.error = { ...GQLErr404 };
    mocks.useCreateCartMutation.data = { ...CREATE_CART_EMPTY };
    const cartContext = setup(mocks);

    // Assert
    expect(global.console.error).toBeCalledWith(
      '[getCart] Error: cart not found'
    );
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeFalsy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 8 - useEffect (setCartFrom) -> getCartUserIdAccountId -> setCart
  // ACT: setSelectedBranch and setQuoteId
  it('Expect truthy cart and previousCart by setting quoteId and selectedBranch', () => {
    // Arrange
    const authState = { isAuthenticated: true };
    const shipTo = { ...dummyUserAccounts[0] };
    const selectedAccounts = { shipTo, shippingBranchId: 'test' };
    mocks.pathname = '/';
    mocks.authConfig = { authState, profile: mockProfile };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.setSelectedBranch(mockBranch);
      cartContext.setQuoteId('testquote');
    });

    // Assert
    expect(cartContext.previousCart).toBeTruthy();
    expect(cartContext.cart).toBeTruthy();
  });

  // 🟢 9 - clearQuote
  it('Expect truthy cart and previousCart by getCartByQuote and then clearQuote', () => {
    // Arrange
    const authState = { isAuthenticated: true };
    const shipTo = { ...dummyUserAccounts[0] };
    const selectedAccounts = { shipTo, shippingBranchId: 'test' };
    mocks.pathname = '/';
    mocks.authConfig = { authState, profile: mockProfile };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1 - Set up the quote info
    act(() => {
      cartContext.setSelectedBranch(mockBranch);
      cartContext.setQuoteId('testquote');
      cartContext.setQuoteShipToId('testshiptoquote');
    });

    // Assert 1
    expect(cartContext.quoteId).toBeTruthy();
    expect(cartContext.quoteShipToId).toBeTruthy();

    // Act 2 - Clear quote
    act(() => {
      cartContext.clearQuote();
    });

    // Assert 2
    expect(cartContext.quoteId).toBeFalsy();
    expect(cartContext.quoteShipToId).toBeFalsy();
    expect(cartContext.previousCart).toBeTruthy();
    expect(cartContext.cart).toBeTruthy();
  });

  // 🟢 10 - updateWillCallBranch
  it('Expect truthy cart when updateWillCallBranch is called', () => {
    // Arrange
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });

    // Assert
    expect(cartContext.cart).toBeTruthy();
  });

  // 🟢 11 - addItemToCart -> Error (userId)
  it('Expect truthy cart and error when addItemToCart is called with no userId', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    mocks.authConfig = { profile: { ...mockProfile, userId: '' } };
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.addItemToCart('', 0, 0);
    });

    // Assert
    const errMsg = '[addItemToCart] Error: userId not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 12 - addItemToCart -> Error (shipToId)
  it('Expect truthy cart and error when addItemToCart is called with no shipTo.id', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.addItemToCart('', 0, 0);
    });

    // Assert
    const errMsg = '[addItemToCart] Error: shipTo account id not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 13 - addItemToCart -> Error (contract)
  it('Expect falsy cart when addItemToCart is called with contract', () => {
    // Arrange
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.setContract?.(mockCartContract);
    });
    act(() => {
      cartContext.addItemToCart('', 0, 0);
    });

    // Assert
    expect(cartContext.cart).toBeFalsy();
  });

  // 🟢 14 - addItemToCart (success)
  it('Expect truthy cart content when addItemToCart is called with valid data', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.addItemToCart('', 0, 0);
    });

    // Assert
    expect(cartContext.cart?.id).toBe(mockCart.id);
  });

  // 🟢 15 - addItemToCart (success selectedBranch.isPricingOnly)
  it('Expect truthy cart content when addItemToCart is called with valid data and selectedBranch isPricingOnly', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.setSelectedBranch({ ...mockBranch, isPricingOnly: true });
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.addItemToCart('', 0, 0);
    });

    // Assert
    expect(cartContext.cart?.id).toBe(mockCart.id);
  });

  // 🟢 16 - addItemToCart (warning max limit)
  it('Expect to call error toast from addItemToCart when extra item is added', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.useAddItemsToCartMutation = { addItemsToCart: { ...mockMaxCart } };
    mocks.pathname = '/select-accounts';
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    // Act 2
    act(() => {
      cartContext.addItemToCart('test', 1, 1);
    });

    // Assert 2
    expect(mocks.pushAlert).toBeCalledWith(T.cart.atLimit, {
      variant: 'warning'
    });
  });

  // 🟢 17 - addItemsToCart -> Error (shipToId)
  it('Expect truthy cart and error when addItemsToCart is called with no shipTo.id', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.addItemsToCart([]);
    });

    // Assert
    const errMsg = '[addItemsToCart] Error: shipTo account id not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 18 - addItemsToCart (success)
  it('Expect truthy cart when addItemsToCart is called with valid data', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      const item = { productId: '', qty: 0, minIncrementQty: 0 };
      cartContext.addItemsToCart([item]);
    });

    // Assert
    expect(cartContext.cart).toBeTruthy();
  });

  // 🟢 19 - addItemsToCart (success selectedBranch.isPricingOnly)
  it('Expect truthy cart product when addItemsToCart is called with valid data and selectedBranch isPricingOnly', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.setSelectedBranch({ ...mockBranch, isPricingOnly: true });
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart).toBeTruthy();
    expect(cartContext.cart?.products).toBeUndefined();

    // Act 2
    act(() => {
      const item = { productId: '', qty: 0, minIncrementQty: 0 };
      cartContext.addItemsToCart([item]);
    });
    // Assert 2
    expect(cartContext.cart?.products?.length).toBeTruthy();
  });

  // 🟢 20 - addAllListItemsToCart -> Error (shipToId)
  it('Expect truthy cart and error when addAllListItemsToCart is called with no shipTo.id', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.addAllListItemsToCart('', []);
    });

    // Assert
    const errMsg = '[addAllListItemsToCart] Error: shipTo account id not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 21 - addAllListItemsToCart -> Error (maxItem)
  it('Expect to call error toast when addAllListItemsToCart is called with maxItem', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockMaxCart);
    });
    act(() => {
      cartContext.addAllListItemsToCart('test', [{} as ItemInfo]);
    });

    // Assert
    expect(mocks.pushAlert).toBeCalledWith(T.cart.overLimit, {
      variant: 'error'
    });
  });

  // 🟢 22 - addAllListItemsToCart (success)
  it('Expect truthy cart products when addAllListItemsToCart is called with valid data', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart).toBeTruthy();
    expect(cartContext.cart?.products).toBeUndefined();

    // Act 2
    act(() => {
      cartContext.addAllListItemsToCart('', []);
    });
    // Assert 2
    expect(cartContext.cart?.products?.length).toBeTruthy();
  });

  // 🟢 23 - deleteItem -> Error (shipToId)
  it('Expect truthy cart and error when deleteItem is called with no shipTo.id', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.deleteItem('');
    });

    // Assert
    const errMsg = '[deleteItem] Error: shipTo account id not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 24 - deleteItem (contract & emptyCart) -> deleteContractItem -> nothing happened
  it('Expect falsy cart product when deleteItem is called with contract but empty cart', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
      cartContext.setContract(mockCartContract);
    });

    // Assert 1
    expect(cartContext.cart?.products).toBeFalsy();

    // Act 2
    act(() => {
      const itemToDelete = mockCartProducts[0].id!;
      cartContext.deleteItem(itemToDelete);
    });

    // Assert 2
    expect(cartContext.cart?.products).toBeFalsy();
  });

  // 🟢 25 - deleteItem (contract & cart) -> deleteContractItem -> setCart
  it('Expect updated cart product count when deleteItem is called with contract and matching cart', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
      cartContext.setContract(mockCartContract);
    });

    // Assert 1
    expect(cartContext.cart?.products?.length).toBe(mockCartProducts.length);

    // Act 2
    act(() => {
      const itemToDelete = mockCartProducts[0].id!;
      cartContext.deleteItem(itemToDelete);
    });

    // Assert 2
    expect(cartContext.cart?.products?.length).toBe(
      mockCartProducts.length - 1
    );
  });

  // 🟢 26 - deleteItem (contract & cart) -> deleteContractItem -> setLineNotes
  it('Expect deleted line notes when deleteItem is called with contract and matching cart', () => {
    // Arrange
    const itemId = mockCartProducts[0].id!;
    const noteContent =
      'I know it was 1216. One after Magna Carta. ~ Chuck McGill';
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.setLineNotes({ [itemId]: noteContent });
      cartContext.updateWillCallBranch?.(mockCart);
      cartContext.setContract(mockCartContract);
    });

    // Assert 1
    expect(cartContext.lineNotes[itemId]).toBe(noteContent);

    // Act 2
    act(() => {
      cartContext.deleteItem(itemId!);
    });

    // Assert 2
    expect(cartContext.lineNotes[itemId]).toBeUndefined();
  });

  // 🟢 27 - deleteItem (cart) -> deleteContractItem -> setCart
  it('Expect updated cart product count when deleteItem is called', () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      const itemToDelete = mockCartProducts[0].id!;
      cartContext.deleteItem(itemToDelete);
    });

    // Assert
    expect(cartContext.cart?.products?.length).toBe(mockCartProducts.length);
  });

  // 🟢 28 - deleteCartItems -> Error (contract)
  it('Expect truthy cart and error when deleteCartItems is called with contract', async () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
      cartContext.setContract(mockCartContract);
    });
    await act(async () => {
      await cartContext.deleteCartItems();
    });

    // Assert
    expect(global.console.error).not.toBeCalled();
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 29 - deleteCartItems -> setCart
  it('Expect truthy cart products when deleteCartItems is called with valid data', async () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    await act(async () => {
      await cartContext.deleteCartItems();
    });

    // Assert
    expect(cartContext.cart?.products?.length).toBeTruthy();
  });

  // 🟢 30 - refreshCart -> Error (shipToId)
  it('Expect truthy cart and error when refreshCart is called with no shipTo.id', async () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    await act(async () => {
      await cartContext.refreshCart?.();
    });

    // Assert
    const errMsg = '[refreshCart] Error: shipTo account id not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 31 - refreshCart -> Error (shipToId)
  it('Expect truthy cart products when refreshCart is called with valid data', async () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    await act(async () => {
      await cartContext.refreshCart?.();
    });

    // Assert
    expect(cartContext.cart?.products?.length).toBeTruthy();
  });

  // 🟢 32 - getUserCart (!userId)
  it('Expect falsey cart product and error when getUserCart is called with no userId', async () => {
    // Arrange
    mocks.authConfig = { profile: { ...mockProfile, userId: '' } };
    mocks.useCartUidAidQuery.data = { ...CART_USER_ACCOUNT_ID_FULL };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.products).toBeFalsy();

    // Act 2
    await act(async () => {
      await cartContext.getUserCart?.(dummyEcommAccounts[0]);
    });
    // Assert 2
    expect(cartContext.cart?.products).toBeFalsy();
  });

  // 🟢 33 - getUserCart (error)
  it('Expect falsy cart product and error when getUserCart is called with error', async () => {
    // Arrange
    mocks.useCartUidAidQuery.data = { ...CART_USER_ACCOUNT_ID_FULL };
    mocks.useCartUidAidQuery.error = new ApolloError({});
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.products).toBeFalsy();

    // Act 2
    await act(async () => {
      await cartContext.getUserCart?.(dummyEcommAccounts[0]);
    });
    // Assert 2
    expect(cartContext.cart?.products).toBeFalsy();
  });

  // 🟢 34 - getUserCart -> setCart
  it('Expect truthy cart product and error when getUserCart is called with valid data', async () => {
    // Arrange
    mocks.useCartUidAidQuery.data = { ...CART_USER_ACCOUNT_ID_FULL };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.products).toBeFalsy();

    // Act 2
    await act(async () => {
      await cartContext.getUserCart?.(dummyEcommAccounts[0]);
    });
    // Assert 2
    expect(cartContext.cart?.products).toBeTruthy();
  });

  // 🟢 35 - updateCart -> Error (cart)
  it('Expect error when updateCart is called with no cart', async () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    await act(async () => {
      await cartContext.updateCart?.('', {});
    });

    // Assert
    const errMsg = '[updateCart] Error: cart not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeFalsy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 36 - updateCart (contract) -> setCart
  it('Expect to update cart shipToId info when updateCart is called with contract', async () => {
    // Arrange
    const shipToId = 'testShipToId';
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
      cartContext.setContract(mockCartContract);
    });
    // Assert 1
    expect(cartContext.cart?.shipToId).toBeFalsy();

    // Act 2
    await act(async () => {
      await cartContext.updateCart?.('', { shipToId });
    });
    // Assert 2
    expect(cartContext.cart?.shipToId).toBe(shipToId);
  });

  // 🟢 37 - updateCart (!updateCartMutation) -> setCart
  it('Expect nothing happened cart shipToId when updateCart is called with no updateCartMutation data', async () => {
    // Arrange
    mocks.useUpdateCartMutation.data = undefined;
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.shipToId).toBeFalsy();

    // Act 2
    await act(async () => {
      await cartContext.updateCart?.('', {});
    });
    // Assert 2
    expect(cartContext.cart?.shipToId).toBeFalsy();
  });

  // 🟢 38 - updateCart (updateCartMutation) -> setCart
  it('Expect updated cart shipToId when updateCart is called with updateCartMutation data', async () => {
    // Arrange
    mocks.useUpdateCartMutation.data = { ...UPDATE_CART_RES };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.shipToId).toBeFalsy();

    // Act 2
    await act(async () => {
      await cartContext.updateCart?.('', { shipToId: mockCart.shipToId });
    });
    // Assert 2
    expect(cartContext.cart?.shipToId).toBeTruthy();
  });

  // 🟢 39 - updateCartFromQuote -> Error (cart)
  it('Expect error when updateCartFromQuote is called with no cart', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateCartFromQuote?.({});
    });

    // Assert
    const errMsg = '[updateCart] Error: cart not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeFalsy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 40 - updateCartFromQuote -> Error (cart)
  it('Expect updated cart shipToId when updateCartFromQuote is called with cart', () => {
    // Arrange
    const shipToId = 'test-ship-to';
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.shipToId).toBeFalsy();

    // Act 2
    act(() => {
      cartContext.updateCartFromQuote?.({ shipToId });
    });

    // Assert 2
    expect(cartContext.cart?.shipToId).toBe(shipToId);
  });

  // 🟢 41 - updateDelivery -> Error (cart)
  it('Expect error when updateDelivery is called with no cart', async () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    await act(async () => {
      const deliveryInput = { cartId: '', shouldShipFullOrder: false };
      await cartContext.updateDelivery?.(deliveryInput);
    });

    // Assert
    const errMsg = '[updateDelivery] Error: cart not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeFalsy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 42 - updateDelivery -> setCart (contract)
  it('Expect truthy cart shouldShipFullOrder when updateDelivery is called with contract', async () => {
    // Arrange
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
      cartContext.setContract(mockCartContract);
    });
    // Assert 1
    expect(cartContext.cart?.delivery.shouldShipFullOrder).toBeFalsy();

    // Act
    await act(async () => {
      const deliveryInput = { cartId: '', shouldShipFullOrder: true };
      await cartContext.updateDelivery?.(deliveryInput);
    });

    // Assert
    expect(cartContext.cart?.delivery.shouldShipFullOrder).toBeTruthy();
  });

  // 🟢 43 - updateDelivery (!res)
  it('Expect falsey cart delivery address when updateDelivery is called but undefined response', async () => {
    // Arrange
    mocks.useUpdateDeliveryMutation = undefined;
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.delivery.address).toBeFalsy();

    // Act
    await act(async () => {
      const deliveryInput = { cartId: '', shouldShipFullOrder: false };
      await cartContext.updateDelivery?.(deliveryInput);
    });

    // Assert
    expect(cartContext.cart?.delivery.address).toBeFalsy();
  });

  // 🟢 44 - updateDelivery (res) -> setCart
  it('Expect falsey cart delivery address when updateDelivery is called with response', async () => {
    // Arrange
    mocks.useUpdateDeliveryMutation = { ...UPDATE_DELIVERY_RES };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });
    // Assert 1
    expect(cartContext.cart?.delivery.address).toBeFalsy();

    // Act
    await act(async () => {
      const address = {} as AddressInput;
      const deliveryInput = { cartId: '', shouldShipFullOrder: false, address };
      await cartContext.updateDelivery?.(deliveryInput);
    });

    // Assert
    expect(cartContext.cart?.delivery.address).toBeTruthy();
  });

  // 🟢 45 - updateItemQuantity -> Error (shipToId)
  it('Expect truthy cart and error when updateItemQuantity is called with no shipTo.id', async () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    act(() => {
      cartContext.updateItemQuantity('', 0, 0);
    });

    // Assert
    const errMsg = '[updateItemQuantity] Error: shipTo account id not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeTruthy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 46 - updateItemQuantity -> updateContractItemQty (empty)
  it('Expect falsey cart products when updateItemQuantity is called with contract with no item', async () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
      cartContext.setContract(mockCartContract);
    });

    // Assert 1
    expect(cartContext.cart?.products?.length).toBeFalsy();

    // Act 2
    act(() => {
      cartContext.updateItemQuantity('test', 2, 1);
    });

    // Assert 2
    expect(cartContext.cart?.products?.length).toBeFalsy();
  });

  // 🟢 47 - updateItemQuantity -> updateContractItemQty (!empty)
  it('Expect updated cart products when updateItemQuantity is called with contract', async () => {
    // Arrange
    const newQty = 10;
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
      cartContext.setContract(mockCartContract);
    });

    // Assert 1
    expect(cartContext.cart?.products?.[0]?.quantity).toBe(
      mockCart.products![0].quantity
    );

    // Act 2
    act(() => {
      const id = cartContext.cart!.products![0].id!;
      cartContext.updateItemQuantity(id, newQty, 1);
    });

    // Assert 2
    expect(cartContext.cart?.products?.[0]?.quantity).toBe(newQty);
  });

  // 🟢 48 - updateItemQuantity -> updateItemQuantityMutation (!cartProducts)
  it('Expect falsey cart products when updateItemQuantity is called with empty cart', async () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockEmptyCart);
    });

    // Assert 1
    expect(cartContext.cart?.products).toBeFalsy();

    // Act 2
    act(() => {
      cartContext.updateItemQuantity(mockCart!.products![0].id!, 2, 1);
    });

    // Assert 2
    expect(cartContext.cart?.products).toBeFalsy();
  });

  // 🟢 49 - updateItemQuantity -> updateItemQuantityMutation (non-match)
  it('Expect same cart qty when updateItemQuantity is called with non-matching id', async () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.useUpdateItemQuantityMutation = { ...UPDATE_ITEM_QTY_NOMATCH };
    const originalQty = mockCartProducts[0].quantity!;
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });

    // Assert 1
    expect(cartContext.cart?.products?.[0].quantity).toBe(originalQty);

    // Act 2
    act(() => {
      cartContext.updateItemQuantity(mockCart!.products![0].id!, 2, 1);
    });

    // Assert 2
    expect(cartContext.cart?.products?.[0].quantity).toBe(originalQty);
  });

  // 🟢 50 - updateItemQuantity -> updateItemQuantityMutation (match)
  it('Expect updated cart qty when updateItemQuantity is called with matching id', async () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.useUpdateItemQuantityMutation = { ...UPDATE_ITEM_QTY_RES };
    const cartContext = setup(mocks);

    // Act 1
    const myCart = { ...mockCart, products: [mockCartProductValid] };
    const originalQty = mockCartProductValid.quantity!;
    act(() => {
      cartContext.updateWillCallBranch?.(myCart);
    });

    // Assert 1
    expect(cartContext.cart?.products?.[0].quantity).toBe(originalQty);

    // Act 2
    act(() => {
      cartContext.updateItemQuantity(mockCartProducts[0].id!, 5, 1);
    });

    // Assert 2
    expect(cartContext.cart?.products?.[0].quantity).toBe(
      UPDATE_ITEM_QTY_RES.updateItemQuantity.product.quantity
    );
  });

  // 🟢 51 - updateWillCall -> Error (cart)
  it('Expect error when updateWillCall is called with no cart', async () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act
    await act(async () => {
      await cartContext.updateWillCall?.({ cartId: '' });
    });

    // Assert
    const errMsg = '[updateWillCallCb] Error: cart not found';
    expect(global.console.error).toBeCalledWith(errMsg);
    expect(mocks.historyPush).toBeCalledWith('/error');
    expect(cartContext.cart).toBeFalsy();

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 52 - updateWillCall -> setCart (contract)
  it('Expect updated cart willCall branchId when updateWillCall is called with contract', async () => {
    // Arrange
    const branchId = '1234';
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
      cartContext.setContract(mockCartContract);
    });
    // Assert 1
    expect(cartContext.cart?.willCall.branchId).toBe(
      mockCart.willCall.branchId
    );

    // Act 2
    await act(async () => {
      await cartContext.updateWillCall?.({ cartId: '', branchId });
    });

    // Assert 2
    expect(cartContext.cart?.willCall.branchId).toBe(branchId);
  });

  // 🟢 53 - updateWillCall -> updateWillCallMutation (!res)
  it('Expect same cart when updateWillCall is called with no res data', async () => {
    // Arrange
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.useUpdateWillCallMutation = undefined;
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    // Assert 1
    expect(cartContext.cart?.willCall.branchId).toBe(
      mockCart.willCall.branchId
    );

    // Act 2
    await act(async () => {
      await cartContext.updateWillCall?.({ cartId: '' });
    });

    // Assert 2
    expect(cartContext.cart?.willCall.branchId).toBe(
      mockCart.willCall.branchId
    );
  });

  // 🟢 54 - updateWillCall -> updateWillCallMutation (res)
  it('Expect updated cart willCall branchId when updateWillCall is called with res data', async () => {
    // Arrange
    const branchId = '1234';
    const selectedAccounts = { shipTo: { ...dummyUserAccounts[0] } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.useUpdateWillCallMutation = { ...UPDATE_WILL_CALL_RES };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    // Assert 1
    expect(cartContext.cart?.willCall.branchId).toBe(
      mockCart.willCall.branchId
    );

    // Act 2
    await act(async () => {
      await cartContext.updateWillCall?.({ cartId: '', branchId });
    });

    // Assert 2
    expect(cartContext.cart?.willCall.branchId).toBe(
      UPDATE_WILL_CALL_RES.updateWillCall.willCall.branchId
    );
  });

  // 🟢 55 - releaseContractToCart -> Error (contractDetail)
  it('Expect error when releaseContractToCart is called with no contractDetail', () => {
    // Arrange
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.releaseContractToCart(undefined);
    });

    // Assert
    const errMsg = 'Failure to release contract to cart!';
    expect(global.console.error).toBeCalledWith(errMsg);

    // Reset
    global.console = { ...consoleBackup };
  });

  // 🟢 56 - releaseContractToCart -> setCart + setContract
  it('Expect no previousCart when releaseContractToCart is called with contractDetail', () => {
    // Arrange
    const cartContext = setup(mocks);

    // Act
    act(() => {
      cartContext.releaseContractToCart(mockContractDetails);
    });

    // Assert
    expect(cartContext.previousCart).toBeFalsy();
    expect(cartContext.cart).toBeTruthy();
    expect(cartContext.contract).toBeTruthy();
  });

  // 🟢 57 - releaseContractToCart -> setCart + setContract (with cart)
  it('Expect previousCart when releaseContractToCart is called with contractDetail', () => {
    // Arrange
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
    });
    // Assert 1
    expect(cartContext.previousCart).toBeFalsy();
    expect(cartContext.cart).toBeTruthy();
    expect(cartContext.contract).toBeFalsy();

    // Act 2
    act(() => {
      cartContext.releaseContractToCart(
        { ...mockContractDetails, contractNumber: null },
        {}
      );
    });

    // Assert 2
    expect(cartContext.previousCart).toBeTruthy();
    expect(cartContext.cart).toBeTruthy();
    expect(cartContext.contract).toBeTruthy();
  });

  // 🟢 58 - clearContract -> secContract (contract)
  it('Expect falsy contract when clearContract is called with contract', () => {
    // Arrange
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
      cartContext.setContract(mockCartContract);
    });
    // Assert 1
    expect(cartContext.contract).toBeTruthy();

    // Act 2
    act(() => {
      cartContext.clearContract();
    });

    // Assert 2
    expect(cartContext.contract).toBeFalsy();
  });

  // 🟢 59 - clearContract -> secContract (!contract)
  it('Expect same lineNotes when clearContract is called with contract', () => {
    // Arrange
    const expected = '1234';
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCart);
      cartContext.setLineNotes({ test: expected });
    });
    // Assert 1
    expect(cartContext.lineNotes.test).toBe(expected);

    // Act 2
    act(() => {
      cartContext.clearContract();
    });

    // Assert 2
    expect(cartContext.lineNotes.test).toBe(expected);
  });

  // 🟢 60 - deleteCreditCardFromCart (!res) -> nothing
  it('Expect truthy cart credit cart when deleteCreditCardFromCart is called with no res data', async () => {
    // Arrange
    mocks.useDeleteCreditCardFromCartMutation = undefined;
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCartWithCard);
    });
    // Assert 1
    expect(cartContext.cart?.creditCard).toBeTruthy();

    // Act 2
    await act(async () => {
      await cartContext.deleteCreditCardFromCart?.('');
    });
    // Assert 2
    expect(cartContext.cart?.creditCard).toBeTruthy();
  });

  // 🟢 61 - deleteCreditCardFromCart (res) -> setCart
  it('Expect falsey cart credit card when deleteCreditCardFromCart is called with res data', async () => {
    // Arrange
    mocks.useDeleteCreditCardFromCartMutation = { ...DELETE_CC_FROM_CART_RES };
    const cartContext = setup(mocks);

    // Act 1
    act(() => {
      cartContext.updateWillCallBranch?.(mockCartWithCard);
    });
    // Assert 1
    expect(cartContext.cart?.creditCard).toBeTruthy();

    // Act 2
    await act(async () => {
      await cartContext.deleteCreditCardFromCart?.('');
    });
    // Assert 2
    expect(cartContext.cart?.creditCard).toBeFalsy();
  });
});
