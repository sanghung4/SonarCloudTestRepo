import { screen } from '@testing-library/react';

import { CustomerListResponse, useApiCustomerList } from 'api/customer.api';
import Home from 'pages/Home';
import mockData from 'pages/Home/tests/home.mocks';
import { expectRowCount, render } from 'test-util';

/**
 * Types
 */
type Mocks = {
  apiLoading: boolean;
  apiData?: CustomerListResponse;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  apiLoading: false
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('api/customer.api', () => ({
  ...jest.requireActual('api/customer.api'),
  useApiCustomerList: jest.fn()
}));

/**
 * TEST
 */
describe('pages/Home', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock API functions
  beforeEach(() => {
    // 🔹 useApiCustomerList hook
    (useApiCustomerList as jest.Mock).mockImplementation(() => ({
      loading: mocks.apiLoading,
      data: mocks.apiData
    }));
  });

  // 🟢 1 - Rendered as loading
  it('Expect to render Home when API is loading', () => {
    // arrange
    mocks.apiLoading = true;
    mocks.apiData = { ...mockData };
    // act
    render(<Home />);
    const table = screen.queryByTestId('home_table');
    const loading = screen.queryByTestId('home_table-loading');
    // assert
    expect(table).toBeInTheDocument();
    expect(loading).toBeInTheDocument();
  });

  // 🟢 2 - Rendered as empty
  it('Expect to render no results when API returns empty data', () => {
    // act
    render(<Home />);
    const table = screen.queryByTestId('home_table');
    const noResults = screen.queryByTestId('home_table-no-results');
    // assert
    expect(table).toBeInTheDocument();
    expect(noResults).toBeInTheDocument();
  });

  // 🟢 3 - Rendered as populated table
  it('Expect to match table row counts with API data', () => {
    // arrange
    mocks.apiData = { ...mockData };
    // act
    render(<Home />);
    const table = screen.queryByTestId('home_table');
    // assert
    expect(table).toBeInTheDocument();
    expectRowCount('home_table', mockData.customers.length);
  });
});
