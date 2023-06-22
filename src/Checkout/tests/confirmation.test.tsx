import { act, fireEvent } from '@testing-library/react';

import { mockCart, mockCartContext, mockCartContract } from 'Cart/tests/mocks';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import Confirmation from 'Checkout/Confirmation';
import { mockDeliveryMethodObjWillCall } from 'Checkout/tests/mocks/deliveryMethodObject.mock';
import {
  backOrderdLineItemsMock,
  lineItemsMock,
  mockOrderResponse
} from 'Checkout/tests/mocks/orderResponse.mocks';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { PreferredTimeEnum } from 'generated/graphql';
import { CartContext } from 'providers/CartProvider';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockContext = {
  cart: { ...mockCartContext },
  checkout: { ...mockCheckoutContext }
};
const mockHistory = { push: jest.fn(), listen: jest.fn() };
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));

describe('Checkout - Confirmation', () => {
  afterEach(() => {
    mockContext.cart = { ...mockCartContext };
    mockContext.checkout = { ...mockCheckoutContext };
    mockHistory.push = jest.fn();
  });

  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <Confirmation />
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <Confirmation />
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with orderData', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with orderData, contract, and cart', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.orderedCart = mockCart;
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with orderData and contract', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderedContract = mockCartContract;
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with orderData and contract but some parameters are invalid', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.orderData.deliveryMethod = 'Telekinesis';
    mockContext.checkout.orderedContract.data!.contractNumber = '';
    mockContext.checkout.orderedContract.data!.contractDescription = '';
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with orderData, contract, and deliveryMethodObject', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with orderData, contract, and deliveryMethodObject (not asap)', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    mockContext.checkout.deliveryMethodObject.preferredTime =
      PreferredTimeEnum.Afternoon;
    const { container } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to call history.push when "checkout-top-button" is clicked', async () => {
    setBreakpoint('desktop');
    mockContext.cart.cart = mockCart;
    const { getByTestId } = render(
      <CartContext.Provider value={mockContext.cart}>
        <Confirmation />
      </CartContext.Provider>
    );
    fireEvent.click(getByTestId('checkout-top-button'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockHistory.push).toBeCalledWith('/orders');
  });

  it('expect to call history.push when "additional-cta-button" is clicked', async () => {
    setBreakpoint('desktop');
    mockContext.cart.cart = mockCart;
    const { getByTestId } = render(
      <CartContext.Provider value={mockContext.cart}>
        <Confirmation />
      </CartContext.Provider>
    );
    fireEvent.click(getByTestId('additional-cta-button'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockHistory.push).toBeCalledWith('/');
  });

  it('should not show the back ordered warning in desktop', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderData.lineItems = lineItemsMock;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    const { queryByTestId } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const warningBox = queryByTestId('back-order-warning-box');
    expect(warningBox).not.toBeInTheDocument();
  });

  it('should show the back ordered warning in desktop', async () => {
    setBreakpoint('desktop');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderData.lineItems = backOrderdLineItemsMock;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    const { findByTestId } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const warningBox = await findByTestId('back-order-warning-box');
    expect(warningBox).toBeInTheDocument();
  });

  it('should not show the back ordered warning in mobile', async () => {
    setBreakpoint('mobile');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderData.lineItems = lineItemsMock;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    const { queryByTestId } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const warningBox = queryByTestId('back-order-warning-box');
    expect(warningBox).not.toBeInTheDocument();
  });

  it('should show the back ordered warning in mobile', async () => {
    setBreakpoint('mobile');
    mockContext.checkout.orderData = mockOrderResponse;
    mockContext.checkout.orderData.lineItems = backOrderdLineItemsMock;
    mockContext.checkout.orderedContract = mockCartContract;
    mockContext.checkout.deliveryMethodObject = mockDeliveryMethodObjWillCall;
    const { findByTestId } = render(
      <CartContext.Provider value={mockContext.cart}>
        <CheckoutContext.Provider value={mockContext.checkout}>
          <Confirmation />
        </CheckoutContext.Provider>
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const warningBox = await findByTestId('back-order-warning-box');
    expect(warningBox).toBeInTheDocument();
  });

  // it('expect to call window.print when "print-button" is clicked', async () => {
  //   const print = jest.spyOn(window, 'print');
  //   setBreakpoint('desktop');
  //   mockContext.cart.cart = mockCart;
  //   const { getByTestId } = render(
  //     <CartContext.Provider value={mockContext.cart}>
  //       <Confirmation />
  //     </CartContext.Provider>
  //   );
  //   await act(() => new Promise((res) => setTimeout(res, 0)));
  //   fireEvent.click(getByTestId('print-button'));
  //   expect(print).toBeCalled();
  // });
});
