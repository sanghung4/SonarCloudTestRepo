import { fireEvent, waitFor, act } from '@testing-library/react';
import { ErpSystemEnum } from 'generated/graphql';
import Invoices from 'Invoices';

import { handleDownloadClickedCb } from 'Invoices/util';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { success } from 'Invoices/mocks';
import { render } from 'test-utils/TestWrapper';

jest.mock('Invoices/util/lib', () => ({
  handleDownloadClickedCb: jest.fn()
}));

describe('Invoices List - Misc', () => {
  it('Download PDF should appear when one row is selected', async () => {
    jest.setTimeout(10000);
    setBreakpoint('desktop');

    const { container, findByTestId, getByTestId } = render(<Invoices />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '123456' },
          shipTo: { erpAccountId: '' },
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });

    const filterResultsButton = getByTestId('branch-filter-set-filter');
    expect(filterResultsButton).toBeInTheDocument();
    fireEvent.click(filterResultsButton);

    const statusButton = getByTestId('invoice-status-toggle-button');
    const allStatusButton = statusButton.children[2];
    expect(allStatusButton.textContent).toEqual('All');
    fireEvent.click(allStatusButton);
    fireEvent.click(filterResultsButton);
    await act(() => new Promise((res) => setTimeout(res, 300)));

    const firstCell = await findByTestId('checkbox-0');
    const checkBox = firstCell.querySelector('input[type="checkbox"]');
    fireEvent.click(checkBox!);

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const downloadButton = await findByTestId('download-pdf-button');
    expect(downloadButton).toBeInTheDocument();

    fireEvent.click(downloadButton);
    expect(handleDownloadClickedCb).toBeCalledTimes(1);
  });
});
