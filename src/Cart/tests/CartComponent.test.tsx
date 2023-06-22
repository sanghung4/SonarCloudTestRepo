import { act } from '@testing-library/react';

import CartList from 'Cart/CartList';

import { mockCart, mockCartContext, mockCartContract } from 'Cart/tests/mocks';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockContext = { ...mockCartContext };
const mockCartNoProduct = {
  ...mockCart,
  products: null
};
const mockCheckout = {
  ...mockCheckoutContext
};

describe('Cart - CartComponent', () => {
  it("Should match snapshot when there's nothing in the cart on desktop", async () => {
    setBreakpoint('desktop');
    const { container } = render(<CartList cart={mockCartNoProduct} />, {
      cartConfig: mockContext
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it("Should match snapshot when there's nothing in the cart on mobile", async () => {
    setBreakpoint('mobile');
    const { container } = render(<CartList cart={mockCartNoProduct} />, {
      cartConfig: mockContext
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it("Should match snapshot when there's nothing in the cart while loading", async () => {
    mockContext.cartLoading = true;
    setBreakpoint('desktop');
    const { container } = render(<CartList cart={mockCartNoProduct} />, {
      cartConfig: mockContext
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it("Should match snapshot when there's contract and tempcartitem on mobile", async () => {
    setBreakpoint('mobile');
    mockContext.contract = mockCartContract;
    mockCheckout.tempCartItems = [{}];
    const { container } = render(
      <CheckoutContext.Provider value={mockCheckout}>
        <CartList cart={mockCartNoProduct} />
      </CheckoutContext.Provider>,
      {
        cartConfig: mockContext
      }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
});
