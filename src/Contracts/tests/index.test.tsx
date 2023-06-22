import { fireEvent, RenderResult, waitFor } from '@testing-library/react';
import { AuthContext } from 'AuthProvider';

import Contracts from 'Contracts';
import {
  empty,
  invoiceOnlyAuth,
  loading,
  nullish,
  success
} from 'Contracts/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

jest.mock('hooks/useSearchParam', () => ({
  useQueryParams: () => [
    {
      from: '',
      to: ''
    },
    jest.fn()
  ]
}));

describe('Contracts', () => {
  it('Snapshot match when user has "Invoice Only" access', async () => {
    setBreakpoint('desktop');
    const { container } = render(
      <AuthContext.Provider value={invoiceOnlyAuth}>
        <Contracts />
      </AuthContext.Provider>,
      {
        selectedAccountsConfig: {
          selectedAccounts: {
            billTo: {
              erpAccountId: '210852'
            }
          }
        }
      }
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 300)));
    expect(container).toMatchSnapshot();
  });

  it('Snapshot match for null results', async () => {
    setBreakpoint('desktop');
    const { container } = render(<Contracts />, {
      mocks: [nullish],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Desktop snapshot match', async () => {
    setBreakpoint('desktop');
    const { container } = render(<Contracts />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Desktop Snapshot match with empty data', async () => {
    setBreakpoint('desktop');
    const { container } = render(<Contracts />, {
      mocks: [empty],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match', async () => {
    setBreakpoint('mobile');
    const { container } = render(<Contracts />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match with empty data', async () => {
    setBreakpoint('mobile');
    const { container } = render(<Contracts />, {
      mocks: [empty],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match while loading', () => {
    setBreakpoint('mobile');
    const { container } = render(<Contracts />, {
      mocks: [loading],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Index should render table with the correct row/column counts', async () => {
    setBreakpoint('desktop');
    const { container } = render(<Contracts />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 50)));
    expect(container.getElementsByTagName('tr').length).toEqual(8);
    expect(container.getElementsByTagName('th').length).toEqual(6);
  });
});

describe('Search Filter For Contracts Table', () => {
  let utils: RenderResult<
      typeof import('@testing-library/dom/types/queries'),
      HTMLElement
    >,
    enterSearchFilter: { (fieldValue: string): Promise<void> },
    clickOnViewResult: { (): void; (): Promise<void> };

  beforeEach(async () => {
    setBreakpoint('desktop');
    utils = render(<Contracts />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            erpAccountId: '210852'
          }
        }
      }
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 50)));
    expect(utils.container).toMatchSnapshot();

    await waitFor(() => new Promise((res) => setTimeout(res, 50)));

    enterSearchFilter = async (fieldValue: string) => {
      const searchInput = utils.getByTestId('search-contracts-input');
      fireEvent.change(searchInput, { target: { value: fieldValue } });
      await waitFor(() => new Promise((res) => setTimeout(res, 0)));
      expect(utils).toMatchSnapshot();
    };
    clickOnViewResult = async () => {
      const viewResult = utils.getByTestId('branch-filter-set-filter');
      fireEvent.click(viewResult);
      await waitFor(() => new Promise((res) => setTimeout(res, 0)));
      expect(utils).toMatchSnapshot();
    };
  });

  it('Should show the results based on search filter - result count', async () => {
    enterSearchFilter('5432705');
    clickOnViewResult();
    expect(utils.container.getElementsByTagName('tr').length).toEqual(2);
    expect(utils.container.getElementsByTagName('th').length).toEqual(6);
  });

  it('Should show the only searched contract number', async () => {
    enterSearchFilter('5432705');
    clickOnViewResult();
    const rowOneColTwo = utils.container.getElementsByTagName('td')[1];
    expect(rowOneColTwo.textContent).toEqual('5432705');
  });

  it('Should be as it is with blank search', async () => {
    enterSearchFilter('');
    clickOnViewResult();
    expect(utils.getByTestId('search-contracts-input')).toHaveValue('');
    expect(utils.container.getElementsByTagName('tr').length).toEqual(8);
    expect(utils.container.getElementsByTagName('th').length).toEqual(6);
  });

  it('Reset button test', async () => {
    enterSearchFilter('5432705');
    clickOnViewResult();
    const resetButton = await utils.findByTestId('reset-button');
    fireEvent.click(resetButton);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(utils.container.getElementsByTagName('tr').length).toEqual(8);
    expect(utils.container.getElementsByTagName('th').length).toEqual(6);
  });
});
