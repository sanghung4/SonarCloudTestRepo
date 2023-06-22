import { ReactNode } from 'react';
import { fireEvent, screen, waitFor } from '@testing-library/react';

import { AuthContext } from 'AuthProvider';
import { ErpSystemEnum } from 'generated/graphql';
import Order, { OrderRouterState } from 'Order';
import { eclipseMocks, mincronMocks } from 'Order/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const params = {
  id: 'S123456.001'
};
const location: { state: OrderRouterState } = {
  state: {}
};
const queryParam = {
  orderStatus: 'Invoiced'
};

function MockEmptyAuthProvider(props: { children: ReactNode }) {
  return (
    <AuthContext.Provider value={{ authState: null }}>
      {props.children}
    </AuthContext.Provider>
  );
}

jest.mock('react-router-dom', () => ({
  useParams: () => params,
  useLocation: () => location
}));

jest.mock('hooks/useSearchParam', () => ({
  useQueryParams: () => [queryParam, jest.fn()]
}));

const renderProp = {
  mocks: eclipseMocks
};

describe('Order - index', () => {
  afterEach(() => {
    location.state.fromInvoices = undefined;
    location.state.search = undefined;
    queryParam.orderStatus = 'Invoiced';
  });
  it.skip('Should match snapshot loading and display correctly for Eclipse', async () => {
    setBreakpoint('desktop');
    renderProp.mocks = [eclipseMocks[0]];

    const { container } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot loading and display correctly for Mincron', async () => {
    setBreakpoint('desktop');
    renderProp.mocks = [mincronMocks[0]];

    const { container } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });
    expect(container).toMatchSnapshot();
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot when ids are undefined in Eclipse', async () => {
    setBreakpoint('desktop');

    renderProp.mocks = [eclipseMocks[0]];

    const { container } = render(
      <MockEmptyAuthProvider>
        <Order />
      </MockEmptyAuthProvider>,
      {
        ...renderProp,
        selectedAccountsConfig: {
          selectedAccounts: {
            billTo: {},
            shipTo: {},
            shippingBranchId: '',
            erpSystemName: ErpSystemEnum.Eclipse
          }
        }
      }
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot when ids are undefined in Mincron', async () => {
    setBreakpoint('desktop');

    renderProp.mocks = [eclipseMocks[0]];

    const { container } = render(
      <MockEmptyAuthProvider>
        <Order />
      </MockEmptyAuthProvider>,
      {
        ...renderProp,
        selectedAccountsConfig: {
          selectedAccounts: {
            billTo: {},
            shipTo: {},
            shippingBranchId: '',
            erpSystemName: ErpSystemEnum.Mincron
          }
        }
      }
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot when it is Invoiced', async () => {
    setBreakpoint('desktop');

    renderProp.mocks = [mincronMocks[0]];

    const { getByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('order-status')).toMatchSnapshot();
  });

  it('Should match snapshot when it is Submitted', async () => {
    setBreakpoint('desktop');

    renderProp.mocks = [mincronMocks[1]];
    queryParam.orderStatus = 'Submitted';

    const { getByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('order-status')).toMatchSnapshot();
  });

  it('Should show warning alert when order is with back ordered items', async () => {
    setBreakpoint('desktop');

    renderProp.mocks = [mincronMocks[1]];
    queryParam.orderStatus = 'Submitted';

    const { getByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' }
        },
        isMincron: true
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('back-order-warning-alert')).toBeInTheDocument();
  });

  it('Should show warning alert and icons when order is with back ordered items', async () => {
    setBreakpoint('mobile');

    renderProp.mocks = [mincronMocks[1]];
    queryParam.orderStatus = 'Submitted';

    const { getByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' }
        },
        isMincron: true
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('back-order-warning-alert')).toBeInTheDocument();
  });

  it('Should not show warning alert when order has no back ordered items', async () => {
    setBreakpoint('desktop');

    renderProp.mocks = [mincronMocks[0]];
    queryParam.orderStatus = 'Submitted';

    const { queryByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' }
        },
        isMincron: true
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(queryByTestId('back-order-warning-alert')).not.toBeInTheDocument();
  });

  it('Should not show warning alert when order has no back ordered items on mobile', async () => {
    setBreakpoint('mobile');

    renderProp.mocks = [mincronMocks[0]];
    queryParam.orderStatus = 'Submitted';

    const { queryByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' }
        },
        isMincron: true
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(queryByTestId('back-order-warning-alert')).not.toBeInTheDocument();
  });

  it('Should match snapshot when it is Unknown', async () => {
    setBreakpoint('desktop');

    renderProp.mocks = [mincronMocks[2]];
    queryParam.orderStatus = 'Unknown';

    const { getByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('order-status')).toMatchSnapshot();
  });

  it.skip("Should match snapshot when it's from Invoices", async () => {
    setBreakpoint('desktop');
    renderProp.mocks = [eclipseMocks[0]];
    location.state.fromInvoices = true;

    const { container } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it.skip("Should match snapshot when it's from Invoices and search is validd", async () => {
    setBreakpoint('desktop');
    renderProp.mocks = [eclipseMocks[0]];
    location.state.fromInvoices = true;
    location.state.search = 'test';

    const { container } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it.skip('Should show add items to cart button as disabled', async () => {
    setBreakpoint('desktop');
    renderProp.mocks = [eclipseMocks[0]];

    const { getByTestId } = render(<Order />, {
      ...renderProp,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { id: '123456' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    expect(getByTestId('add-all-to-cart-button')).toBeDisabled();
  });

  // it('expect print button is pressed', async () => {
  //   setBreakpoint('desktop');
  //   const print = jest.spyOn(window, 'print');
  //   renderProp.mocks = [eclipseMocks[0]];

  //   const { getByTestId } = render(<Order />, renderProp);
  //   await waitFor(() => new Promise((res) => setTimeout(res, 0)));
  //   fireEvent.click(getByTestId('print-button'));
  //   expect(print).toBeCalled();
  // });
});
