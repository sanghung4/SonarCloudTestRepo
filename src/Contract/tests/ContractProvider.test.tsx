import { act, fireEvent } from '@testing-library/react';
import { VALUE_OVER_10MIL } from 'Cart/util';

import { DialogEnum } from 'Contract/ContractProvider';
import { MockProviderTestComponent } from 'Contract/tests/mockComponents';
import { mockData } from 'Contract/tests/mocks';
import { ErpSystemEnum } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';

const mockPush = jest.fn();
let mockAccount: SelectedAccounts = {
  billTo: { erpAccountId: '' },
  shipTo: { erpAccountId: '' },
  shippingBranchId: '',
  erpSystemName: ErpSystemEnum.Eclipse
};

jest.mock('react-router-dom', () => ({
  useHistory: () => ({
    push: mockPush
  })
}));

describe('Contract - Provider', () => {
  it('Expect "goToCart" function is called', () => {
    const { getByTestId } = render(<MockProviderTestComponent />, {
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    fireEvent.click(getByTestId('mockfn-0'));
    expect(mockPush).toBeCalled();
  });

  it('Expect "handleQtyClear" function is called', async () => {
    const { getByTestId } = render(<MockProviderTestComponent />, {
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    fireEvent.click(getByTestId('mockfn-1'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('mock-dialog')).toHaveTextContent(`${DialogEnum.Clear}`);
  });

  it('Expect "handleReleaseAll" function is called', async () => {
    const { getByTestId } = render(<MockProviderTestComponent />, {
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    fireEvent.click(getByTestId('mockfn-2'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('mock-dialog')).toHaveTextContent(
      `${DialogEnum.Release}`
    );
  });

  it('Expect "handleReleaseAll" function is called with large', async () => {
    const mockContract = { ...mockData };
    mockContract.contractProducts![0]!.netPrice! = VALUE_OVER_10MIL * 2;
    mockAccount.erpSystemName = ErpSystemEnum.Mincron;
    const { getByTestId } = render(
      <MockProviderTestComponent contract={mockContract} />,
      {
        selectedAccountsConfig: {
          selectedAccounts: {
            ...mockAccount,
            erpSystemName: ErpSystemEnum.Mincron
          }
        }
      }
    );
    fireEvent.click(getByTestId('mockfn-2'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('mock-dialog')).toHaveTextContent(
      `${DialogEnum.SubtotalToLarge}`
    );
  });

  it('Expect "resetDialog" function is called', async () => {
    const { getByTestId } = render(<MockProviderTestComponent />, {
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    fireEvent.click(getByTestId('mockfn-3'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('mock-dialog')).toHaveTextContent(`${undefined}`);
  });

  it('Expect "setQtyInputMap" function is called', async () => {
    const { getByTestId } = render(<MockProviderTestComponent />, {
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    fireEvent.click(getByTestId('mockfn-4'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('mock-qtys')).toHaveTextContent('2');
  });

  it('Expect "resetDialog" function is called for over 10mil', async () => {
    const { getByTestId } = render(<MockProviderTestComponent />, {
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    fireEvent.click(getByTestId('mockfn-5'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('mock-dialog')).toHaveTextContent(
      `${DialogEnum.SubtotalToLarge}`
    );
  });
});
