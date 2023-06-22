import { render } from 'test-utils/TestWrapper';
import { useScrollToTop } from 'hooks/useScrollToTop';

function MockComponent() {
  useScrollToTop();
  return null;
}

describe('Utils - useScrollToTop', () => {
  it('ScrollToTop scrolls to (0,0) when loaded', async () => {
    window.scrollTo = jest.fn();
    render(<MockComponent />);
    expect(window.scrollTo).toHaveBeenCalledWith(0, 0);
  });
});
