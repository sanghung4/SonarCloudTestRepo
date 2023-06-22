import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

import WorksForYou from 'WorksForYou';

describe('Works For You', () => {
  it('should match the snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<WorksForYou />);
    expect(container).toMatchSnapshot();
  });
  it('should match the snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<WorksForYou />);
    expect(container).toMatchSnapshot();
  });
});
