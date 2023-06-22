import Company from 'Company';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Company', () => {
  it('Should match snapshop on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<Company />);
    expect(container).toMatchSnapshot();
  });
  it('Should match snapshop on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<Company />);
    expect(container).toMatchSnapshot();
  });
});
