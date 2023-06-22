import BrandPage from 'Brands';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Brands', () => {
  it('Should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<BrandPage />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<BrandPage />);
    expect(container).toMatchSnapshot();
  });
});
