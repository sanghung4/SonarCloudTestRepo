import DeliveryMethod from 'Checkout/DeliveryMethod';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Checkout - DeliveryMethod', () => {
  it('should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<DeliveryMethod />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<DeliveryMethod />);
    expect(container).toMatchSnapshot();
  });
});
