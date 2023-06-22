import { MockedResponse } from '@apollo/client/testing';
import { subMonths } from 'date-fns';

import { OrdersQuery, useOrdersLazyQuery } from 'generated/graphql';
import { useQueryParams } from 'hooks/useSearchParam';
import * as t from 'locales/en/translation.json';
import Orders from 'Orders';
import { ORDERS_SUCCESS } from 'Orders/tests/mocks';
import { OrdersParams } from 'Orders/util/queryParam';
import { SelectedAccountsContextType } from 'providers/SelectedAccountsProvider';
import { clickButton, fillFormTextInput } from 'test-utils/actionUtils';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { formatDate } from 'utils/dates';

/**
 * Types
 */
type Mocks = {
  api?: MockedResponse<Record<string, any>>;
  queryParams: OrdersParams;
  setQueryParam: jest.Mock;
  selectedAccounts: Partial<SelectedAccountsContextType>;
  useOrdersLazyQuery: {
    data?: OrdersQuery;
    loading: boolean;
    called: boolean;
  };
};
type MockGQLProp<R> = {
  fetchPolicy?: string;
  onCompleted?: (data?: R) => void;
};

/**
 * Configs
 */
const rowRegEx = /^(row_)/gm;

/**
 * Mock values
 */
const defaultMocks: Mocks = {
  queryParams: { searchBy: '', from: '', to: '', page: '', sortBy: [] },
  selectedAccounts: {},
  setQueryParam: jest.fn(),
  useOrdersLazyQuery: {
    loading: false,
    called: false
  }
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock methods
 */
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useOrdersLazyQuery: jest.fn()
}));
jest.mock('hooks/useSearchParam', () => ({
  useQueryParams: jest.fn()
}));

/**
 * Setup
 */
function setup(m: Mocks) {
  return render(<Orders />, {
    selectedAccountsConfig: { ...m.selectedAccounts }
  });
}

/**
 * TEST
 */
describe('Orders', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 MOCK method implementations
  beforeEach(() => {
    // 🔵 Hook - useQueryParams
    (useQueryParams as jest.Mock).mockReturnValue([
      mocks.queryParams,
      mocks.setQueryParam
    ]);
    // 🔵 Hook - useOrdersLazyQuery
    (useOrdersLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<OrdersQuery>) => {
        const { data, loading } = mocks.useOrdersLazyQuery;
        const call = jest.fn(() => {
          mocks.useOrdersLazyQuery.called = true;
          param.onCompleted?.(data);
        });
        const { called } = mocks.useOrdersLazyQuery;
        return [call, { data, loading, called }];
      }
    );
  });

  // 🟢 1 - No Orders (eclipse)
  it('Expect no orders found text with default null settings as Eclipse', async () => {
    // arrange
    const isEclipse = true;
    const selectedAccounts = { shipTo: {} };
    mocks.selectedAccounts = { isEclipse, selectedAccounts };

    // act
    const { findByText } = setup(mocks);
    const noOrdersFound = await findByText(t.orders.noOrders);

    // assert
    expect(noOrdersFound).toBeInTheDocument();
  });

  // 🟢 2 - No Orders (Mincron)
  it('Expect no orders found text with default null settings as Mincron', async () => {
    // arrange
    const isEclipse = false;
    const selectedAccounts = { billTo: {} };
    mocks.selectedAccounts = { isEclipse, selectedAccounts };

    // act
    const { findByText } = setup(mocks);
    const noOrdersFound = await findByText(t.orders.noOrdersWaterworks);

    // assert
    expect(noOrdersFound).toBeInTheDocument();
  });

  // 🟢 3 - Loading
  it('Expect loading component when API is responding as loading', async () => {
    // arrange
    mocks.useOrdersLazyQuery.loading = true;

    // act
    const { findAllByRole } = setup(mocks);
    const progressbars = await findAllByRole('progressbar');

    // assert
    expect(progressbars.length).toBeTruthy();
  });

  // 🟢 4 - Has orders (Eclipse)
  it('Expect 10 row count match by getOrders call as Eclipse', async () => {
    // arrange
    const isEclipse = true;
    const isMincron = false;
    const selectedAccounts = { shipTo: { erpAccountId: 'test' } };
    mocks.selectedAccounts = { isEclipse, isMincron, selectedAccounts };
    mocks.useOrdersLazyQuery.loading = false;
    mocks.useOrdersLazyQuery.called = false;
    mocks.useOrdersLazyQuery.data = { ...ORDERS_SUCCESS };

    // act
    const { findAllByTestId } = setup(mocks);
    const rows = await findAllByTestId(rowRegEx);

    // assert
    expect(rows.length).toBe(10);
  });

  // 🟢 5 - Has orders (Mincron)
  it('Expect 10 row count match by getOrders call as Mincron', async () => {
    // arrange
    const isEclipse = false;
    const isMincron = true;
    const selectedAccounts = { billTo: { erpAccountId: 'test' } };
    mocks.selectedAccounts = { isEclipse, isMincron, selectedAccounts };
    mocks.useOrdersLazyQuery.loading = false;
    mocks.useOrdersLazyQuery.called = false;
    mocks.useOrdersLazyQuery.data = { ...ORDERS_SUCCESS };

    // act
    const { findAllByTestId } = setup(mocks);
    const rows = await findAllByTestId(rowRegEx);

    // assert
    expect(rows.length).toBe(10);
  });

  // 🟢 6 - Date Range set
  it('Expect from/to input have the matching date range from queryParam', async () => {
    // arrange
    mocks.queryParams.from = '03/16/2020';
    mocks.queryParams.to = '04/30/2020';

    // act
    const { findByTestId } = setup(mocks);
    const fromInput = await findByTestId('range-from');
    const toInput = await findByTestId('range-to');

    // assert
    expect(fromInput).toHaveValue(mocks.queryParams.from);
    expect(toInput).toHaveValue(mocks.queryParams.to);
  });

  // 🟢 7 - Date Range reset
  it('Expect from/to input to reset to default when the reset button is pressed', async () => {
    mocks.queryParams.from = '03/16/2020';
    mocks.queryParams.to = '04/30/2020';

    // act
    const { findByTestId } = setup(mocks);
    const fromInput = await findByTestId('range-from');
    const toInput = await findByTestId('range-to');
    await clickButton('reset-button');

    // assert
    expect(fromInput).toHaveValue(formatDate(subMonths(new Date(), 1)));
    expect(toInput).toHaveValue(formatDate(new Date()));
  });

  // 🟢 8 - View Results
  it('Expect to call setQuaryParam by input search and click View Results button', async () => {
    // arrange
    const search = 'test';
    mocks.useOrdersLazyQuery.called = false;
    mocks.useOrdersLazyQuery.loading = true;
    (useQueryParams as jest.Mock).mockReturnValue([{}, mocks.setQueryParam]);

    // act
    setup(mocks);
    await fillFormTextInput('search-orders', search);
    await clickButton('branch-filter-set-filter');

    // assert
    expect(mocks.setQueryParam).toBeCalled();
  });

  // 🟢 9 - Date Range more than 30 days shows warning message
  it('Expect date range input more than 30 days shows warning message', async () => {
    setBreakpoint('mobile');
    mocks.queryParams.from = '01/16/2023';
    mocks.queryParams.to = '04/30/2023';

    // act
    const { findByTestId } = setup(mocks);
    const warningMessage = await findByTestId('orders-search-warning-alert');

    // assert
    expect(warningMessage).toBeInTheDocument();
  });

  it('Expect default date range input not to show warning message', async () => {
    setBreakpoint('desktop');
    mocks.queryParams.from = subMonths(new Date(), 1);
    mocks.queryParams.to = new Date();

    // act
    const { queryByTestId } = setup(mocks);
    const warningMessage = await queryByTestId('orders-search-warning-alert');

    // assert
    expect(warningMessage).not.toBeInTheDocument();
  });
});
