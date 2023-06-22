import { act, fireEvent } from '@testing-library/react';
import {
  authConfigLoggedIn,
  authConfigLoggedOut
} from 'common/Header/tests/mocks';
import { DesktopSubheaderMenu } from 'common/Header/util';
import { ErpSystemEnum } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';

const mockChild = 'test';
const mockAccount: SelectedAccounts = {
  billTo: { erpAccountId: '' },
  shipTo: { erpAccountId: '' },
  shippingBranchId: '',
  erpSystemName: ErpSystemEnum.Eclipse
};

describe('common - Header - util - DesktopSubheaderMenu', () => {
  it('should match snapshot for max under Eclipse as logged out user', async () => {
    const { container } = render(
      <DesktopSubheaderMenu item="max">{mockChild}</DesktopSubheaderMenu>,
      {
        authConfig: authConfigLoggedOut,
        selectedAccountsConfig: { selectedAccounts: mockAccount }
      }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot for max under Eclipse as logged in user', async () => {
    const { container } = render(
      <DesktopSubheaderMenu item="max">{mockChild}</DesktopSubheaderMenu>,
      {
        authConfig: authConfigLoggedIn,
        selectedAccountsConfig: { selectedAccounts: mockAccount }
      }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot for max under Mincron as logged in user', async () => {
    const { container } = render(
      <DesktopSubheaderMenu item="max">{mockChild}</DesktopSubheaderMenu>,
      {
        authConfig: authConfigLoggedIn,
        selectedAccountsConfig: {
          selectedAccounts: {
            ...mockAccount,
            erpSystemName: ErpSystemEnum.Mincron
          }
        }
      }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot for resources to click on an item', async () => {
    const { container, getByTestId, getAllByText } = render(
      <DesktopSubheaderMenu item="resources">{mockChild}</DesktopSubheaderMenu>,
      {
        authConfig: authConfigLoggedIn,
        selectedAccountsConfig: { selectedAccounts: mockAccount }
      }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getAllByText(mockChild)[0]);
    await act(() => new Promise((res) => setTimeout(res, 100)));
    fireEvent.click(getByTestId('desktop-nav-credit-forms'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
});
