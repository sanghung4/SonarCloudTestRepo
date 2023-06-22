import CheckoutWarning from 'Cart/CheckoutWarning';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Cart - Warning', () => {
  it('expect to match snapshop on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<CheckoutWarning />);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshop on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<CheckoutWarning />);
    expect(container).toMatchSnapshot();
  });
});
