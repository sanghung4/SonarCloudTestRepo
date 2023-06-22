import { ApolloError } from '@apollo/client';
import { GraphQLErrors } from '@apollo/client/errors';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import {
  mockCreditCardElementInfoQuery,
  mockCreditCardFn,
  mockCreditCardFnProps,
  mockCreditCardListQuery,
  mockCreditCardSetupUrlQuery,
  mockDeleteCreditCardMutation
} from 'CreditCard/tests/mocks/useCreditCardData.mocks';
import { CreditCardState } from 'CreditCard/util/config';
import useCreditCardFn, {
  windowMessageEvent
} from 'CreditCard/util/Provider/useCreditCardFn';
import * as t from 'locales/en/translation.json';
import { render } from 'test-utils/TestWrapper';
import { mockSelectedAccounts } from 'hooks/tests/mocks/useSelectedAccounts.mocks';
import { mockCart, mockCartContext } from 'Cart/tests/mocks';
import { CartContext } from 'providers/CartProvider';

/**
 * Mock values
 */
const mockError: ApolloError = {
  name: 'error',
  message: 'test error',
  graphQLErrors: [],
  clientErrors: [],
  networkError: null,
  extraInfo: null
};
const graphQLErrors: GraphQLErrors = [
  {
    locations: undefined,
    path: undefined,
    nodes: undefined,
    source: undefined,
    positions: undefined,
    originalError: undefined,
    extensions: { code: 409 },
    toJSON: jest.fn(),
    name: '',
    message: '',
    [Symbol.toStringTag]: ''
  }
];
const mocks = {
  cart: { ...mockCartContext },
  checkout: { ...mockCheckoutContext },
  props: { ...mockCreditCardFnProps },
  pushAlert: jest.fn(),
  selectedAccounts: { ...mockSelectedAccounts }
};

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

/**
 * Mock function
 */
function setup(p: typeof mocks) {
  const output = { ...mockCreditCardFn };
  function MockComponent() {
    Object.assign(output, useCreditCardFn(p.props));
    return null;
  }
  render(
    <CartContext.Provider value={p.cart}>
      <CheckoutContext.Provider value={p.checkout}>
        <MockComponent />
      </CheckoutContext.Provider>
    </CartContext.Provider>
  );
  return output;
}

/**
 * Tests
 */
describe('CreditCard - util/Provider/useCreditCardFn', () => {
  afterEach(() => {
    mocks.checkout = { ...mockCheckoutContext };
    mocks.props = { ...mockCreditCardFnProps };
    mocks.pushAlert = jest.fn();
    mocks.selectedAccounts = { ...mockSelectedAccounts };
  });

  it('expect `deleteComplete` to call some functions', () => {
    const matchCreditCard = {
      ...mockCreditCard,
      elementPaymentAccountId: mockDeleteCreditCardMutation.deleteCreditCard
    };
    mocks.props.creditCardData = [{ ...mockCreditCard }, matchCreditCard];
    setup(mocks).deleteComplete({ ...mockDeleteCreditCardMutation });
    expect(mocks.props.setCreditCardData).toBeCalledWith([
      { ...mockCreditCard }
    ]);
    expect(mocks.props.setUpdatingList).toBeCalledWith(false);
  });

  it('expect `deleteComplete` to call updateCart from CartContext while deleting with matching cart CC info', () => {
    // Arrange
    const elementPaymentAccountId =
      mockDeleteCreditCardMutation.deleteCreditCard;
    const matchCreditCard = { ...mockCreditCard, elementPaymentAccountId };
    mocks.props.creditCardData = [{ ...mockCreditCard }, matchCreditCard];
    mocks.cart.cart = { ...mockCart, creditCard: matchCreditCard };
    mocks.cart.updateCart = jest.fn();

    // Act
    setup(mocks).deleteComplete({ ...mockDeleteCreditCardMutation });

    // Assert
    expect(mocks.cart.updateCart).toBeCalled();
  });

  it('expect `deleteComplete` to call deleteCreditCardFromCart from CartContext', () => {
    // Arrange
    const elementPaymentAccountId =
      mockDeleteCreditCardMutation.deleteCreditCard;
    const matchCreditCard = { ...mockCreditCard, elementPaymentAccountId };
    mocks.props.creditCardData = [matchCreditCard];
    mocks.cart.cart = { ...mockCart, creditCard: { ...matchCreditCard } };
    mocks.cart.deleteCreditCardFromCart = jest.fn();

    // Act
    setup(mocks).deleteComplete({ ...mockDeleteCreditCardMutation });

    // Assert
    expect(mocks.cart.deleteCreditCardFromCart).toBeCalled();
  });

  it('expect `deleteError` to call some functions', () => {
    setup(mocks).deleteError({ ...mockError });
    expect(mocks.pushAlert).toBeCalledWith(t.creditCard.errorDelete, {
      variant: 'error'
    });
    expect(mocks.props.setUpdatingList).toBeCalledWith(false);
  });
  it('expect `deleteError` to call some functions with error 409', () => {
    setup(mocks).deleteError({ ...mockError, graphQLErrors });
    expect(mocks.pushAlert).toBeCalledWith(t.creditCard.cardInUse, {
      variant: 'error'
    });
    expect(mocks.props.setUpdatingList).toBeCalledWith(false);
  });

  it('expect `elementInfoComplete` to call some functions with basic data', () => {
    setup(mocks).elementInfoComplete({ ...mockCreditCardElementInfoQuery });
    expect(mocks.props.setResUrl).toBeCalledWith(undefined);
    expect(mocks.props.setParsingCCResponse).toBeCalledWith(false);
    expect(mocks.props.setGetCreditCardLoading).toBeCalledWith(false);
    expect(mocks.props.setCreditCardState).toBeCalledWith(CreditCardState.NONE);
  });
  it('expect `elementInfoComplete` to call some functions with "/checkout" and saveCreditCard"', () => {
    Object.defineProperty(window, 'location', {
      value: { pathname: '/checkout' }
    });
    mocks.props.shouldSaveCreditCard = true;
    setup(mocks).elementInfoComplete({ ...mockCreditCardElementInfoQuery });
    expect(mocks.props.setCreditCard).toBeCalled();
    expect(mocks.props.updateCartCreditCard).toBeCalled();
    expect(mocks.props.setCreditCardState).toBeCalledWith(
      CreditCardState.SELECTED
    );
    expect(mocks.props.setShouldSaveCreditCard).toBeCalledWith(false);
    expect(mocks.props.setUpdatingList).toBeCalledWith(true);
  });

  it('expect `listComplete` to call some functions with basic data', () => {
    setup(mocks).listComplete({});
    expect(mocks.props.setCreditCardData).toBeCalled();
    expect(mocks.props.setCreditCardState).toBeCalledWith(CreditCardState.ADD);
  });
  it('expect `listComplete` to call some functions with card data', () => {
    setup(mocks).listComplete({ ...mockCreditCardListQuery });
    expect(mocks.props.setCreditCardData).toBeCalled();
    expect(mocks.props.setCreditCardState).toBeCalledWith(
      CreditCardState.CHANGE
    );
  });

  it('expect `setupUrlComplete` to not call setIframeUrl with basic data', () => {
    setup(mocks).setupUrlComplete({ ...mockCreditCardSetupUrlQuery });
    expect(mocks.props.setIframeUrl).not.toBeCalled();
  });
  it('expect `setupUrlComplete` to call setIframeUrl with proper data', () => {
    const data = {
      creditCardSetupUrl: { elementSetupUrl: 'test', elementSetupId: 'tset' }
    };
    setup(mocks).setupUrlComplete(data);
    expect(mocks.props.setIframeUrl).toBeCalled();
  });

  it('expect `windowMessageEvent` to not call any mocked functions when conditions are invalid', () => {
    const [fnA, fnB] = [jest.fn(), jest.fn()];
    const mockEvent = {} as MessageEvent<any>;
    windowMessageEvent(fnA, fnB)(mockEvent);
    expect(fnA).not.toBeCalled();
    expect(fnB).not.toBeCalled();
  });
  it('expect `windowMessageEvent` to call mocked functions when conditions are valid', () => {
    const [fnA, fnB] = [jest.fn(), jest.fn()];
    const mockEvent = {
      data: `${window.location.origin}/credit_callback`
    } as MessageEvent<any>;
    windowMessageEvent(fnA, fnB)(mockEvent);
    expect(fnA).toBeCalled();
    expect(fnB).toBeCalled();
  });
});
