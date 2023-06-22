import { act, fireEvent } from '@testing-library/react';

import { mockBasicBranch } from 'Branches/tests/mocks';

import { mockCart, mockCartContext, mockCartContract } from 'Cart/tests/mocks';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import Review from 'Checkout/Review';
import { mockCreditCard } from 'Checkout/tests/mocks/creditCart.mocks';
import {
  mockDeliveryMethodObjDelivery,
  mockDeliveryMethodObjWillCall
} from 'Checkout/tests/mocks/deliveryMethodObject.mock';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { Step } from 'Checkout/util/types';
import {
  DeliveryMethodEnum,
  PaymentMethodTypeEnum,
  PreferredTimeEnum
} from 'generated/graphql';
import { CartContext } from 'providers/CartProvider';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mockProviders = {
  cart: { ...mockCartContext },
  checkout: { ...mockCheckoutContext }
};
const mockHistory = { push: jest.fn() };

/**
 * Mock methods
 */
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));

/**
 * Main setup
 */
function mainRender(mock: typeof mockProviders) {
  const wrapper = render(
    <CartContext.Provider value={mock.cart}>
      <CheckoutContext.Provider value={mock.checkout}>
        <Review />
      </CheckoutContext.Provider>
    </CartContext.Provider>
  );
  return wrapper;
}

/**
 * Test
 */
describe('Checkout - Review', () => {
  afterEach(() => {
    mockProviders.cart = { ...mockCartContext };
    mockProviders.checkout = { ...mockCheckoutContext };
    mockHistory.push = jest.fn();
  });

  it('should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    mockProviders.cart.cart = mockCart;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with invalid Cart', () => {
    setBreakpoint('desktop');
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with no items in cart', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = { ...mockCart, products: null };
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with cart and contract', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.contract = mockCartContract;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with cart and contract as willcall', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.cart.contract = mockCartContract;
    mockProviders.cart.contractBranch = mockBasicBranch;
    mockProviders.checkout.deliveryMethod = DeliveryMethodEnum.Willcall;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot as credit card payment', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = { ...mockCart, creditCard: mockCreditCard };
    mockProviders.checkout.paymentData.paymentMethodType =
      PaymentMethodTypeEnum.Creditcard;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with willcall deliveryMethodObject', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    mockProviders.checkout.deliveryMethod = DeliveryMethodEnum.Willcall;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot on mobile with willcall deliveryMethodObject', () => {
    setBreakpoint('mobile');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    mockProviders.checkout.deliveryMethod = DeliveryMethodEnum.Willcall;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with delivery deliveryMethodObject and set time as afternoon', () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    mockProviders.checkout.deliveryMethodObject = {
      ...mockDeliveryMethodObjDelivery,
      preferredTime: PreferredTimeEnum.Afternoon
    };
    mockProviders.checkout.deliveryMethod = DeliveryMethodEnum.Delivery;
    const { container } = mainRender(mockProviders);
    expect(container).toMatchSnapshot();
  });

  it('expect to set step as PAYMENT when "edit-payment-info" is clicked', async () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    const { getByTestId } = mainRender(mockProviders);
    fireEvent.click(getByTestId('edit-payment-info'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockProviders.checkout.setStep).toBeCalledWith(Step.PAYMENT);
  });

  it('expect to set step as INFO when "edit-delivery-info" is clicked', async () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    const { getByTestId } = mainRender(mockProviders);
    fireEvent.click(getByTestId('edit-delivery-info'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockProviders.checkout.setStep).toBeCalledWith(Step.INFO);
  });

  it('expect to set step as INFO when "edit-special-instructions" is clicked', async () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    const { getByTestId } = mainRender(mockProviders);
    fireEvent.click(getByTestId('edit-special-instructions'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockProviders.checkout.setStep).toBeCalledWith(Step.INFO);
  });

  it('expect to call history.goBack when "edit-cart" is clicked', async () => {
    setBreakpoint('desktop');
    mockProviders.cart.cart = mockCart;
    const { getByTestId } = mainRender(mockProviders);
    fireEvent.click(getByTestId('edit-cart'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockHistory.push).toBeCalledWith('/cart');
  });
});
