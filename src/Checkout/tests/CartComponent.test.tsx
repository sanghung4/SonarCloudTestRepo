import CheckoutCart from 'Checkout/CheckoutCart';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { lineItemMocks } from 'Order/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockProvider = {
  checkout: { ...mockCheckoutContext }
};

describe('Checkout - CartComponent', () => {
  afterEach(() => {
    mockProvider.checkout = { ...mockCheckoutContext };
  });
  it('should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <CheckoutContext.Provider value={mockProvider.checkout}>
        <CheckoutCart />
      </CheckoutContext.Provider>
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(
      <CheckoutContext.Provider value={mockProvider.checkout}>
        <CheckoutCart />
      </CheckoutContext.Provider>
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with line items', () => {
    setBreakpoint('desktop');
    mockProvider.checkout.orderData.lineItems = lineItemMocks;
    const { container } = render(
      <CheckoutContext.Provider value={mockProvider.checkout}>
        <CheckoutCart />
      </CheckoutContext.Provider>
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile with line items', () => {
    setBreakpoint('mobile');
    mockProvider.checkout.orderData.lineItems = lineItemMocks;
    const { container } = render(
      <CheckoutContext.Provider value={mockProvider.checkout}>
        <CheckoutCart />
      </CheckoutContext.Provider>
    );
    expect(container).toMatchSnapshot();
  });
});
