import { act, fireEvent } from '@testing-library/react';

import MobileDrawer from 'common/Header/MobileDrawer';
import {
  authConfigLoggedIn,
  authConfigLoggedOut
} from 'common/Header/tests/mocks';
import { ErpSystemEnum } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import { waterworksCompany } from 'hooks/utils/useDomainInfo';
import { mockUseDomainInfoReturn } from 'hooks/tests/mocks/useDomainInfo.mocks';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';

/**
 * Mock values
 */
const mockHistory = { push: jest.fn() };
const mockAccount: SelectedAccounts = {
  billTo: { erpAccountId: '' },
  shipTo: { erpAccountId: '' },
  shippingBranchId: ''
};
let mockDomainInfo: ReturnType<typeof useDomainInfo> = {
  ...mockUseDomainInfoReturn
};

/**
 * Mock Methods
 */
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));

jest.mock('hooks/useDomainInfo', () => ({
  ...jest.requireActual('hooks/useDomainInfo'),
  useDomainInfo: () => mockDomainInfo
}));

/**
 * Test
 */
describe('common - Header - MobileDrawer', () => {
  afterEach(() => {
    mockHistory.push = jest.fn();
    mockDomainInfo = { ...mockUseDomainInfoReturn };
  });

  it('should match snapshot for logged out user', async () => {
    const { container } = render(<MobileDrawer open setOpen={jest.fn()} />, {
      authConfig: authConfigLoggedOut,
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot for logged in user', async () => {
    const { container } = render(<MobileDrawer open setOpen={jest.fn()} />, {
      authConfig: authConfigLoggedIn,
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot for logged in user as waterworks', async () => {
    mockDomainInfo.subdomain = waterworksCompany.sub;
    const { container } = render(<MobileDrawer open setOpen={jest.fn()} />, {
      authConfig: authConfigLoggedIn,
      selectedAccountsConfig: {
        selectedAccounts: {
          ...mockAccount,
          erpSystemName: ErpSystemEnum.Mincron
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot for logged in user with hold alert', async () => {
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const { container } = render(<MobileDrawer open setOpen={jest.fn()} />, {
      authConfig: authConfigLoggedIn,
      selectedAccountsConfig: {
        selectedAccounts: {
          ...mockAccount,
          shipToErpAccount: { creditHold: true }
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('expect history is called to the brands page when brands is clicked', async () => {
    const { getByTestId } = render(<MobileDrawer open setOpen={jest.fn()} />, {
      authConfig: authConfigLoggedOut,
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('brands-button'));
    expect(mockHistory.push).toBeCalledWith('/brands');
  });

  it('expect setOpen is called as false when the close button is clicked', async () => {
    const setOpen = jest.fn();
    const { getByTestId } = render(<MobileDrawer open setOpen={setOpen} />, {
      authConfig: authConfigLoggedOut,
      selectedAccountsConfig: { selectedAccounts: mockAccount }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('mobile-close-button'));
    expect(setOpen).toBeCalledWith(false);
  });

  it('expect to match snapshot of categories when browse product is clicked', async () => {
    const { container, getByTestId } = render(
      <MobileDrawer open setOpen={jest.fn()} />,
      {
        authConfig: authConfigLoggedIn,
        selectedAccountsConfig: { selectedAccounts: mockAccount }
      }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('browse-products-button'));
    await act(() => new Promise((res) => setTimeout(res, 100)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot with my lists', async () => {
    const { container, getByTestId } = render(
      <MobileDrawer open setOpen={jest.fn()} />,
      {
        authConfig: authConfigLoggedIn,
        selectedAccountsConfig: { selectedAccounts: mockAccount }
      }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const myListsButton = getByTestId('my-lists-button');
    expect(myListsButton).toHaveTextContent(`My Lists`);
    expect(myListsButton).toBeInTheDocument();
    await act(() => new Promise((res) => setTimeout(res, 100)));
    expect(container).toMatchSnapshot();
  });
});
