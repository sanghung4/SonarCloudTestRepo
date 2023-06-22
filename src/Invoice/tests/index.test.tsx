import React from 'react';
import { waitFor } from '@testing-library/react';
import { successMincron } from 'Invoice/mocks';
import { ErpSystemEnum } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import Invoice from 'Invoice';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

jest.mock('react-router-dom', () => ({
  useParams: () => ({
    id: '5460026'
  }),
  useLocation: () => ({
    state: {}
  })
}));

describe('Invoice tests', () => {
  it('Should show loading and display correctly for Mincron accounts', async () => {
    setBreakpoint('desktop');

    const { container } = render(<Invoice />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '5427' },
          shipTo: { erpAccountId: '123456' },
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    expect(container).toMatchSnapshot();

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('Should show info card for the invoice', async () => {
    setBreakpoint('desktop');

    const { container, getByTestId } = render(<Invoice />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { erpAccountId: '91492' },
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();

    const infoCard = getByTestId('invoice-info-header');
    expect(infoCard).toBeInTheDocument();
  });

  it('Should summary card info for the invoice', async () => {
    setBreakpoint('mobile');

    const { container, getByTestId } = render(<Invoice />, {
      mocks: [successMincron],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { id: '123456' },
          shipTo: { erpAccountId: '91492' },
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();

    const infoCard = getByTestId('invoice-summary-header');
    expect(infoCard).toBeInTheDocument();
  });
});
