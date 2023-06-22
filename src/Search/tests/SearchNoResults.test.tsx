import SearchNoResults from 'Search/SearchNoResults';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

const Props = {
  searchTerm: 'test'
};

describe('Search No Results', () => {
  it('show the zero results search suggestions in desktop', async () => {
    setBreakpoint('desktop');
    const { findByTestId } = render(<SearchNoResults {...Props} />);

    const zeroResultsMsg = await findByTestId('zero-results-message');

    expect(zeroResultsMsg).toBeInTheDocument();
  });

  it('show the zero results search suggestions in mobile', async () => {
    setBreakpoint('mobile');
    const { findByTestId } = render(<SearchNoResults {...Props} />);

    const zeroResultsMsg = await findByTestId('zero-results-message');

    expect(zeroResultsMsg).toBeInTheDocument();
  });
});
