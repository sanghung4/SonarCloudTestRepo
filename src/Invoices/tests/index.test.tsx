import { act, fireEvent } from '@testing-library/react';

import { ErpSystemEnum } from 'generated/graphql';
import Invoices from 'Invoices';
import { success, successMincron } from 'Invoices/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import * as t from 'locales/en/translation.json';
import { formatDate } from 'utils/dates';
import { subMonths } from 'date-fns';

describe('Invoices List', () => {
  // Eclipse (desktop)
  it('Should show loading and load correctly on desktop in Eclipse', async () => {
    setBreakpoint('desktop');

    const { container, getByTestId } = render(<Invoices />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });

    await act(() => new Promise((res) => setTimeout(res, 300)));

    /** Initially the invoices table will be empty by default and clicking on view Results button will display invoices */
    const filterResultsButton = getByTestId('branch-filter-set-filter');
    expect(filterResultsButton).toBeInTheDocument();
    fireEvent.click(filterResultsButton);
    await act(() => new Promise((res) => setTimeout(res, 300)));

    /** When user clicks viewResults button for the first time, it should filter all the invoices from last 30 days */
    expect(container.getElementsByTagName('tr').length).toEqual(4);
  });

  // Eclipse (mobile)
  it('Should show loading and load correctly on mobile in Eclipse', async () => {
    setBreakpoint('mobile');

    const { container, getByTestId } = render(<Invoices />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });

    await act(() => new Promise((res) => setTimeout(res, 300)));

    /** Initially the invoices table will be empty by default and clicking on view Results button will display invoices */
    const filterResultsButton = getByTestId('branch-filter-set-filter');
    expect(filterResultsButton).toBeInTheDocument();
    fireEvent.click(filterResultsButton);
    await act(() => new Promise((res) => setTimeout(res, 300)));

    expect(container.getElementsByTagName('tr').length).toEqual(4);
  });

  // Mincron (desktop)
  it('Should render on desktop in Mincron', async () => {
    setBreakpoint('desktop');

    const { container } = render(<Invoices />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    await act(() => new Promise((res) => setTimeout(res, 300)));
    expect(container.getElementsByTagName('tr').length).toEqual(2);
  });

  // Mincron (mobile)
  it('Should render on mobile in Mincron', async () => {
    jest.setTimeout(10000);
    setBreakpoint('mobile');

    const { container } = render(<Invoices />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    await act(() => new Promise((res) => setTimeout(res, 300)));
    expect(container.getElementsByTagName('tr').length).toEqual(2);
  });

  // Mincron status filter
  it('Should filter based on open status in Mincron', async () => {
    setBreakpoint('desktop');

    const { container, getByTestId } = render(<Invoices />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    const filterResultsButton = getByTestId('branch-filter-set-filter');
    fireEvent.click(filterResultsButton);
    expect(filterResultsButton).toBeInTheDocument();

    const statusButton = getByTestId('invoice-status-toggle-button');
    const openStatusButton = statusButton.children[0];
    expect(openStatusButton.textContent).toEqual('Open');
    fireEvent.click(openStatusButton);
    fireEvent.click(filterResultsButton);
    await act(() => new Promise((res) => setTimeout(res, 300)));

    /** Selecting All will filter all the invocies that were 30 days old by default */
    expect(container.getElementsByTagName('tr').length).toEqual(4);
  });

  it('Should filter based on closed status in Mincron', async () => {
    setBreakpoint('desktop');

    const { container, getByTestId } = render(<Invoices />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    const filterResultsButton = getByTestId('branch-filter-set-filter');
    fireEvent.click(filterResultsButton);
    expect(filterResultsButton).toBeInTheDocument();

    const statusButton = getByTestId('invoice-status-toggle-button');
    const closedStatusButton = statusButton.children[1];
    expect(closedStatusButton.textContent).toEqual('Closed');
    fireEvent.click(closedStatusButton);
    fireEvent.click(filterResultsButton);
    await act(() => new Promise((res) => setTimeout(res, 300)));

    expect(container.getElementsByTagName('tr').length).toEqual(3);
  });

  // Search filter
  it('Search filter should filter invoices based on Job Name', async () => {
    const { container, getByTestId } = render(<Invoices />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    const searchInput = getByTestId('search-invoices-input');
    fireEvent.change(searchInput, { target: { value: 'AIKENON' } });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('branch-filter-set-filter'));
    await act(() => new Promise((res) => setTimeout(res, 200)));

    expect(container.getElementsByTagName('tr').length).toEqual(2);
  });

  // Date range filter - Eclipse
  it('Date filter should filter invoices based on date range on Eclipse', async () => {
    const { container, getByTestId } = render(<Invoices />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    const filterResultsButton = getByTestId('branch-filter-set-filter');
    fireEvent.click(filterResultsButton);
    expect(filterResultsButton).toBeInTheDocument();
    const target1 = { value: formatDate(new Date()) };
    const target2 = { value: formatDate(subMonths(new Date(), 1)) };
    fireEvent.change(getByTestId('range-from'), { target2 });
    fireEvent.change(getByTestId('range-to'), { target1 });
    fireEvent.blur(getByTestId('range-to'));
    fireEvent.click(getByTestId('branch-filter-set-filter'));
    await act(() => new Promise((res) => setTimeout(res, 300)));

    expect(container.getElementsByTagName('tr').length).toEqual(4);
  });

  // Date range filter - Mincron
  it('Date filter should filter invoices based on date range on Mincron', async () => {
    const { container, getByTestId } = render(<Invoices />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });
    const filterResultsButton = getByTestId('branch-filter-set-filter');
    fireEvent.click(filterResultsButton);
    expect(filterResultsButton).toBeInTheDocument();
    const target1 = { value: formatDate(new Date()) };
    const target2 = { value: formatDate(subMonths(new Date(), 1)) };
    fireEvent.change(getByTestId('range-from'), { target2 });
    fireEvent.change(getByTestId('range-to'), { target1 });
    fireEvent.blur(getByTestId('range-to'));
    fireEvent.click(getByTestId('branch-filter-set-filter'));
    await act(() => new Promise((res) => setTimeout(res, 300)));

    expect(container.getElementsByTagName('tr').length).toEqual(6);
  });

  // Eclipse Search Input Placeholder
  it('Search input should show eclipse specific placeholder', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<Invoices />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });

    await act(() => new Promise((res) => setTimeout(res, 300)));
    expect(getByTestId('search-invoices-input')).toHaveAttribute(
      'placeholder',
      t.invoices.invoiceSearchPlaceHolder
    );
  });

  // Mincron Search Input Placeholder
  it('Search input should show mincron specific placeholder', async () => {
    setBreakpoint('desktop');

    const { getByTestId } = render(<Invoices />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    await act(() => new Promise((res) => setTimeout(res, 300)));
    expect(getByTestId('search-invoices-input')).toHaveAttribute(
      'placeholder',
      t.invoices.invoiceSearchPlaceHolderForMincron
    );
  });

  // undefined
  it('Should show nothing when shipTo and erpSystemName are undefined', async () => {
    setBreakpoint('mobile');

    const { container } = render(<Invoices />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {},
          shipTo: { erpAccountId: '' },
          shippingBranchId: ''
        }
      }
    });

    await act(() => new Promise((res) => setTimeout(res, 300)));
    expect(container.getElementsByTagName('tr').length).toEqual(2);
  });
});
