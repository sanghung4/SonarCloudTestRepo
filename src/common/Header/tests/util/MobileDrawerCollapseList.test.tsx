import { act, fireEvent } from '@testing-library/react';
import {
  authConfigLoggedIn,
  authConfigLoggedOut
} from 'common/Header/tests/mocks';
import {
  MobileDrawerCollapseList,
  MobileDrawerCollapseCompanyList
} from 'common/Header/util';
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

describe('common - Header - util - MobileDrawerCollapseList', () => {
  describe('MobileDrawerCollapseList', () => {
    it('should match snapshot for max under Eclipse as logged out user while closed', async () => {
      const { container } = render(
        <MobileDrawerCollapseList
          item="max"
          open={false}
          setOpen={jest.fn()}
          urlHandler={jest.fn()}
        >
          {mockChild}
        </MobileDrawerCollapseList>,
        {
          authConfig: authConfigLoggedOut,
          selectedAccountsConfig: { selectedAccounts: mockAccount }
        }
      );
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot for max under Eclipse as logged out user', async () => {
      const { container } = render(
        <MobileDrawerCollapseList
          item="max"
          open
          setOpen={jest.fn()}
          urlHandler={jest.fn()}
        >
          {mockChild}
        </MobileDrawerCollapseList>,
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
        <MobileDrawerCollapseList
          item="max"
          open
          setOpen={jest.fn()}
          urlHandler={jest.fn()}
        >
          {mockChild}
        </MobileDrawerCollapseList>,
        {
          authConfig: authConfigLoggedIn,
          selectedAccountsConfig: { selectedAccounts: mockAccount }
        }
      );
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot for max under Eclipse as logged in Mincron user', async () => {
      const { container } = render(
        <MobileDrawerCollapseList
          item="max"
          open
          setOpen={jest.fn()}
          urlHandler={jest.fn()}
        >
          {mockChild}
        </MobileDrawerCollapseList>,
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

    it('expect to call setOpen as false when ListItem is clicked', async () => {
      const setOpen = jest.fn();
      const { getByTestId } = render(
        <MobileDrawerCollapseList
          item="max"
          open
          setOpen={setOpen}
          urlHandler={jest.fn()}
        >
          {mockChild}
        </MobileDrawerCollapseList>,
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
      fireEvent.click(getByTestId('max-dropdown-button'));
      expect(setOpen).toBeCalledWith(false);
    });
  });

  describe('MobileDrawerCollapseCompanyList', () => {
    it('should match snapshot', async () => {
      const { container } = render(<MobileDrawerCollapseCompanyList />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot when opened', async () => {
      const { container, getByTestId } = render(
        <MobileDrawerCollapseCompanyList />
      );
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('reece-companies-button'));
      await act(() => new Promise((res) => setTimeout(res, 300)));
      expect(container).toMatchSnapshot();
    });
  });
});
