import { waitFor } from '@testing-library/react';

import Search from 'Search';
import mocks, { mockNoProducts } from 'Search/index.mocks';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

const selectedAccountsMock: SelectedAccounts = {
  shipTo: {
    id: 'shipto'
  }
};

jest.mock('Search/util/useSearchQueryParams', () => ({
  ...jest.requireActual('Search/util/useSearchQueryParams'),
  __esModule: true,
  default: () => [
    {
      criteria: 'copper',
      categories: [],
      filters: [],
      page: 1
    },
    jest.fn()
  ]
}));

jest.mock('hooks/useDomainInfo', () => ({
  useDomainInfo: () => ({
    engine: 'plumbing_hvac'
  })
}));

describe('Search tests', () => {
  beforeAll(() => setBreakpoint());

  it('Should make separate calls for the product and the pricing & availability', async () => {
    const { getByTestId } = render(<Search />, {
      mocks
    });

    const searchResultContainer = getByTestId('search-container');
    const searchResultSkeleton = getByTestId('search-result-skeleton-0');
    expect(searchResultContainer).toMatchSnapshot('1 > loading');
    expect(searchResultSkeleton).toBeInTheDocument();

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(searchResultContainer).toMatchSnapshot('2 > price loading');
    expect(searchResultSkeleton).not.toBeInTheDocument();

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(searchResultContainer).toMatchSnapshot('3 > loaded');
  });

  it('show search results for the given criteria', async () => {
    const { container, getByTestId } = render(<Search />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const zeroResultsMsg = getByTestId('search-container');

    expect(zeroResultsMsg).toBeInTheDocument();
  });

  it('empty products show show the zero results message', async () => {
    const { getByTestId } = render(<Search />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks: mockNoProducts
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const zeroResultsMsg = getByTestId('zero-results-message');

    expect(zeroResultsMsg).toBeInTheDocument();
  });
});
