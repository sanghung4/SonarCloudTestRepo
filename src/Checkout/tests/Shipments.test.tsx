import { mockCartContext } from 'Cart/tests/mocks';
import Shipments from 'Checkout/Shipments';
import { CartContext } from 'providers/CartProvider';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Main setup
 */
function mainRender(shouldShipFull: boolean) {
  const wrapper = render(
    <CartContext.Provider value={mockCartContext}>
      <Shipments shouldShipFullOrder={shouldShipFull} />
    </CartContext.Provider>
  );
  return wrapper;
}

/**
 * Test
 */
describe('Checkout - Shipments', () => {
  it('should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = mainRender(false);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = mainRender(false);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with should ship full', () => {
    setBreakpoint('desktop');
    const { container } = mainRender(true);
    expect(container).toMatchSnapshot();
  });
});
