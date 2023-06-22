import { mockBranch, mockBranchContext } from 'Branches/tests/mocks';
import { BranchContext } from 'providers/BranchProvider';

import { mockCart, mockCartContext, mockCartContract } from 'Cart/tests/mocks';
import Checkout from 'Checkout';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { Step } from 'Checkout/util/types';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import {
  SelectedAccounts,
  SelectedAccountsContextType
} from 'providers/SelectedAccountsProvider';
import { CartContext } from 'providers/CartProvider';
import { act, fireEvent, waitFor } from '@testing-library/react';
import { DeliveryMethodEnum } from 'generated/graphql';

/**
 * Types
 */
type MockRedirectToObj = {
  pathname?: string;
  state?: unknown;
};
type MockRedirectProp = {
  to: string | MockRedirectToObj;
};

/**
 * Mock values
 */
const mockSelectedAccounts: SelectedAccounts = {
  billTo: { erpAccountId: '12345' },
  shipTo: { id: 'shipto' },
  shippingBranchId: '9876'
};

const mockSelectedAccountsConfig: Partial<SelectedAccountsContextType> = {
  selectedAccounts: mockSelectedAccounts,
  isMincron: false
};

const mockProviders = {
  branch: { ...mockBranchContext },
  cart: { ...mockCartContext },
  checkout: { ...mockCheckoutContext }
};
function MockRedirect({ to }: MockRedirectProp) {
  return (
    <span data-testid="redirect">
      {typeof to === 'string' ? to : to.pathname}
    </span>
  );
}

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({
    pushAlert: jest.fn()
  })
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  Redirect: MockRedirect
}));
window.scrollTo = jest.fn();

/**
 * Main setup
 */
function mainRender(mock: typeof mockProviders) {
  const wrapper = render(
    <CartContext.Provider value={mock.cart}>
      <CheckoutContext.Provider value={mock.checkout}>
        <Checkout />
      </CheckoutContext.Provider>
    </CartContext.Provider>,
    { selectedAccountsConfig: mockSelectedAccountsConfig }
  );
  return wrapper;
}

/**
 * TEST
 */
describe('Checkout', () => {
  afterEach(() => {
    mockProviders.cart = { ...mockCartContext };
    mockProviders.checkout = { ...mockCheckoutContext };
    mockSelectedAccounts.billTo = { erpAccountId: '12345' };
    mockSelectedAccounts.shipTo = { id: 'shipto' };
    mockSelectedAccounts.shippingBranchId = '9876';
  });

  it('Expect to Redirect as mocked component when there is no cart data', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = undefined;
    mockProviders.cart.checkingOutWithQuote = false;
    mockProviders.checkout.orderedCart = undefined;
    const { getByTestId } = mainRender(mockProviders);
    expect(getByTestId('redirect')).toBeInTheDocument();
  });

  it('Expect to Redirect as mocked component when account is on creditHold at step 0', () => {
    setBreakpoint('desktop');
    mockProviders.cart.checkingOutWithQuote = true;
    mockProviders.checkout.step = 0;
    mockSelectedAccounts.billTo = { erpAccountId: '12345' };
    mockSelectedAccounts.billToErpAccount = { creditHold: true };
    const { getByTestId } = mainRender(mockProviders);
    expect(getByTestId('redirect')).toBeInTheDocument();
  });

  it('Expect Loader to be rendered with cart data and checking out with quote', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = undefined;
    mockProviders.cart.checkingOutWithQuote = true;
    mockProviders.checkout.orderedCart = undefined;
    mockSelectedAccounts.billToErpAccount = undefined;

    const { getByTestId } = mainRender(mockProviders);
    expect(getByTestId('loader-component')).toBeInTheDocument();
  });

  it('Expect Loader to be rendered with no cart data and tempCartItems but with orderedCart', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = undefined;
    mockProviders.checkout.orderedCart = { ...mockCart, products: [] };
    mockProviders.checkout.tempCartItems = [];
    const { getByTestId } = mainRender(mockProviders);
    expect(getByTestId('loader-component')).toBeInTheDocument();
  });

  it('Should match snapshot with basic cart on desktop', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });
  it('Should match snapshot with basic cart on mobile', () => {
    setBreakpoint('mobile');
    mockProviders.cart.cart = mockCart;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot when cart is loading', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.cartLoading = true;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with cart and contract on desktop', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.contract = mockCartContract;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });
  it('Should match snapshot with cart and contract on mobile', () => {
    setBreakpoint('mobile');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.contract = mockCartContract;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with cart and orderedContract on desktop', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.orderedContract = mockCartContract;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });
  it('Should match snapshot with cart and orderedContract on mobile', () => {
    setBreakpoint('mobile');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.orderedContract = mockCartContract;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with cart and orderedContract, but contract has missing info', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.orderedContract = mockCartContract;
    mockProviders.checkout.orderedContract.data!.contractNumber = undefined;
    mockProviders.checkout.orderedContract.data!.contractDescription =
      undefined;
    mockProviders.checkout.orderedContract.data!.jobName = undefined;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on the PAYMENT step', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.step = Step.PAYMENT;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });
  it('Should match snapshot on the REVIEW step', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.step = Step.REVIEW;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });
  it('Should match snapshot on the CONFIRMATION step', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.step = Step.CONFIRMATION;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on the PAYMENT step with contract', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.contract = mockCartContract;
    mockProviders.checkout.step = Step.PAYMENT;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on the REVIEW step with contract', async () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.cart.deliveryMethod = DeliveryMethodEnum.Willcall;
    mockProviders.cart.contract = mockCartContract;
    mockProviders.checkout.step = Step.REVIEW;
    const { getByTestId, container } = mainRender(mockProviders);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const button = getByTestId('checkout-top-button');
    expect(button).toBeInTheDocument();
    expect(button).not.toBeDisabled();
    expect(button).toHaveTextContent('Submit Order');
    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toHaveTextContent('Info');
  });

  it('Should match snapshot on the CONFIRMATION step with contract', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.contract = mockCartContract;
    mockProviders.checkout.step = Step.CONFIRMATION;
    const { container } = mainRender(mockProviders);
    expect(container).toHaveTextContent('Confirmation');
  });

  it('Should match snapshot with with shipping branch', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.branch.shippingBranch = mockBranch;
    const { container } = render(
      <BranchContext.Provider value={mockProviders.branch}>
        <CartContext.Provider value={mockProviders.cart}>
          <CheckoutContext.Provider value={mockProviders.checkout}>
            <Checkout />
          </CheckoutContext.Provider>
        </CartContext.Provider>
      </BranchContext.Provider>
    );
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on the PAYMENT step for mincron', async () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.contract = mockCartContract;
    mockProviders.checkout.step = Step.PAYMENT;
    mockSelectedAccountsConfig.isMincron = true;
    const { getByTestId } = mainRender(mockProviders);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('po-number-input')).toBeInTheDocument();
    expect(getByTestId('po-number-input')).toHaveAttribute('maxLength', '22');
  });
});
