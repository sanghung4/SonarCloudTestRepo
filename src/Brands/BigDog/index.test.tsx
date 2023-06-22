import BigDogMarketing from 'Brands/BigDog/index';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Brands - BigDog', () => {
  it('index should match the snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<BigDogMarketing />);
    expect(container).toMatchSnapshot();
  });
  it('index should match the snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<BigDogMarketing />);
    expect(container).toMatchSnapshot();
  });
});
