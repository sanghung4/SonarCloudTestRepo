import { waitFor } from '@testing-library/react';

import ContractDetails from 'Contract';
import {
  GetContractDetailsLoadingMock,
  GetContractDetailsMock
} from 'Contract/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';

const account: SelectedAccounts = {
  billTo: { erpAccountId: '210852' },
  shipTo: { erpAccountId: '' },
  shippingBranchId: ''
};
jest.mock('react-router-dom', () => ({
  useParams: () => ({
    id: '5342749'
  }),
  useLocation: () => ({
    state: {}
  }),
  useHistory: jest.fn()
}));

describe('Contract Details', () => {
  it('Desktop snapshot match', async () => {
    setBreakpoint('desktop');

    const { container } = render(<ContractDetails />, {
      mocks: [GetContractDetailsMock],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '210852' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: ''
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Desktop snapshot match when loading', () => {
    setBreakpoint('desktop');
    const { container } = render(<ContractDetails />, {
      mocks: [GetContractDetailsLoadingMock],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '210852' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: ''
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match', async () => {
    setBreakpoint('mobile');
    const { container } = render(<ContractDetails />, {
      mocks: [GetContractDetailsMock],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '210852' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: ''
        }
      }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match when loading', () => {
    setBreakpoint('mobile');
    const { container } = render(<ContractDetails />, {
      mocks: [GetContractDetailsLoadingMock],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '210852' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: ''
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Snapshot match when erpAccountId is null', () => {
    setBreakpoint('desktop');
    const { container } = render(<ContractDetails />, {
      mocks: [GetContractDetailsLoadingMock],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: null },
          shipTo: { erpAccountId: '' },
          shippingBranchId: ''
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  // it('expect print button is pressed', async () => {
  //   const print = jest.spyOn(window, 'print');
  //   setBreakpoint('desktop');
  //   account.billTo.erpAccountId = '210852';
  //   const { getByTestId } = render(<ContractDetails />, {
  //     mocks: [GetContractDetailsMock]
  //   });
  //   await waitFor(() => new Promise((res) => setTimeout(res, 0)));
  //   fireEvent.click(getByTestId('print-button'));
  //   expect(print).toBeCalled();
  // });
});
