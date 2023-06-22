import { act, fireEvent } from '@testing-library/react';

import { mockCartContext, mockCartContract } from 'Cart/tests/mocks';

import SwitchAccounts from 'common/Header/SwitchAccounts';
import {
  authConfigLoggedIn,
  authConfigLoggedOut
} from 'common/Header/tests/mocks';
import {
  mockUserAccountQueryError,
  mockUserAccountQueryMulti,
  mockUserAccountQueryMultiShipTo,
  mockUserAccountQuerySuccess
} from 'common/Header/tests/SwitchAccounts.mock';
import { CartContext, CartContextType } from 'providers/CartProvider';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockHistory = { push: jest.fn() };

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));

describe('common - Header - SwitchAccounts', () => {
  afterEach(() => {
    mockHistory.push = jest.fn();
  });
  it('expect to match snapshot on desktop as logged out user', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedOut,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
  it('expect to match snapshot on mobile as logged out user', async () => {
    setBreakpoint('mobile');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedOut,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on desktop as logged in user', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQuerySuccess],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
  it('expect to match snapshot on mobile as logged in user', async () => {
    setBreakpoint('mobile');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQuerySuccess],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on desktop as logged in user with multiple accounts', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQueryMulti],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
  it('expect to match snapshot on mobile as logged in user with multiple accounts', async () => {
    setBreakpoint('mobile');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQueryMulti],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on desktop as logged in user with multiple shipto accounts', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQueryMultiShipTo],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
  it('expect to match snapshot on mobile as logged in user with multiple shipto accounts', async () => {
    setBreakpoint('mobile');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQueryMultiShipTo],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on desktop as logged in user with shipTo accounts on hold', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQuerySuccess],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: null },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3',
          shipToErpAccount: { creditHold: true }
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to call history.push on data error', async () => {
    setBreakpoint('desktop');
    render(<SwitchAccounts />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQueryError],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockHistory.push).toBeCalledWith('/error', {
      error: new Error(mockUserAccountQueryError.error!.message)
    });
  });

  it('expect to call onChange and history.push when the Change account is clicked', async () => {
    setBreakpoint('mobile');

    const onChange = jest.fn();
    const { getByTestId } = render(<SwitchAccounts onChange={onChange} />, {
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQueryMulti],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            id: 'f8149f07-735b-4b54-81c7-07e39d3749a5',
            name: 'bill to'
          },
          shipTo: {
            id: '2882e752-7323-41cc-976a-7bec199aaa9a',
            name: 'ship to'
          },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('change-button'));
    expect(mockHistory.push).toBeCalledWith('/select-accounts');
    expect(onChange).toBeCalled();
  });

  it('expect to match snapshot with cart contract data', async () => {
    setBreakpoint('desktop');
    const mockCart: CartContextType = {
      ...mockCartContext,
      contract: mockCartContract
    };
    const { container } = render(<SwitchAccounts />, {
      cartConfig: mockCart,
      authConfig: authConfigLoggedIn,
      mocks: [mockUserAccountQuerySuccess],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should link to select accounts', async () => {
    // Arrange
    const { queryByTestId } = render(<SwitchAccounts />, {
      authConfig: { authState: { isAuthenticated: true } },
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '1', name: 'bill to' },
          shipTo: { id: '2', name: 'ship to' },
          shippingBranchId: '3'
        }
      }
    });
    const link = queryByTestId('change-link');
    // Assert - link should not be rendered unless there are multiple accounts
    expect(link).toEqual(null);
  });
});
