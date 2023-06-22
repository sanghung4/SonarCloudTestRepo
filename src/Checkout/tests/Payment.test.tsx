import { act } from '@testing-library/react';

import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { AuthContext } from 'AuthProvider';
import { SelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import Payment from 'Checkout/Payment';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { DeliveryMethodEnum, PaymentMethodTypeEnum } from 'generated/graphql';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { Permission } from 'common/PermissionRequired';
import { mockSelectedAccountsContext } from './mocks/accounts.mocks';

/**
 * Mock providers
 */
const mockProvider = {
  checkout: { ...mockCheckoutContext }
};

const mockAuthProvider = {
  activeFeatures: ['CHECKOUT_WITH_CARD'],
  profile: {
    permissions: [Permission.MANAGE_PAYMENT_METHODS, Permission.SUBMIT_CART_WITHOUT_APPROVAL],
    userId: '',
    isEmployee: false,
    isVerified: true
  },
  authState: {}
}

let mockAccountProvider = {
  ...mockSelectedAccountsContext
}

describe('Checkout - Payment', () => {
  afterEach(() => {
    mockProvider.checkout = { ...mockCheckoutContext };
    mockAccountProvider = { ...mockSelectedAccountsContext };
  });
  it('should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <CheckoutContext.Provider value={mockProvider.checkout}>
        <Payment />
      </CheckoutContext.Provider>
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(
      <CheckoutContext.Provider value={mockProvider.checkout}>
        <Payment />
      </CheckoutContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on error', async () => {
    setBreakpoint('desktop');
    mockProvider.checkout.poNumberError = true;
    const { container } = render(
      <CheckoutContext.Provider value={mockProvider.checkout}>
        <Payment />
      </CheckoutContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with Credit Card as payment method', async () => {
    setBreakpoint('desktop');
    mockProvider.checkout.paymentData = {
      paymentMethodType: PaymentMethodTypeEnum.Creditcard
    };
    const { container } = render(
      <AuthContext.Provider value={mockAuthProvider}>
        <CheckoutContext.Provider value={mockProvider.checkout}>
          <Payment />
        </CheckoutContext.Provider>
      </AuthContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when paymentMethodType has an invalid option', async () => {
    setBreakpoint('desktop');
    mockProvider.checkout.paymentData = {
      paymentMethodType: PaymentMethodTypeEnum.Payinstore,
    };
    mockProvider.checkout.deliveryMethod = DeliveryMethodEnum.Delivery;
    mockAccountProvider.selectedAccounts.billToErpAccount!.alwaysCod = true;
    const { container } = render(
      <SelectedAccountsContext.Provider value={mockAccountProvider}>
        <CheckoutContext.Provider value={mockProvider.checkout}>
          <Payment />
        </CheckoutContext.Provider>
      </SelectedAccountsContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
});
