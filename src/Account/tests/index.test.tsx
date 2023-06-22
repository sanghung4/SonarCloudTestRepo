import { OktaAuth } from '@okta/okta-auth-js';
import { waitFor } from '@testing-library/react';

import Account from 'Account';
import { ErpSystemEnum } from 'generated/graphql';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { mockAccountDetails, mockUserwithMultiAccounts } from './mocks';

export const authConfig = {
  authState: {
    isAuthenticated: true
  },
  oktaAuth: new OktaAuth({ issuer: 'http://test.com' }),
  profile: {
    userId: '11111111-1111-1111-1111-111111111111',
    permissions: [],
    isEmployee: false,
    isVerified: true
  }
};

describe('Account Tests', () => {
  it('should render the Account information page', async () => {
    const { container, getByTestId } = render(<Account />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: ''
        }
      }
    });

    await waitFor(() =>
      expect(getByTestId('loader-component')).toBeInTheDocument()
    );

    expect(container).toMatchSnapshot();
  });

  it('should render the Account information page with correct company info for Mincron User', async () => {
    setBreakpoint('desktop');
    const { container, getByTestId, queryByText } = render(<Account />, {
      authConfig,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            id: '22222222-2222-2222-2222-222222222222',
            erpAccountId: ''
          },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Mincron
        }
      },
      mocks: [mockUserwithMultiAccounts, mockAccountDetails]
    });

    await waitFor(() =>
      expect(getByTestId('editaccount-button')).toBeInTheDocument()
    );

    await waitFor(() => {
      expect(queryByText('HORIZON PLUMBING LTD')).toBeInTheDocument();
    });

    expect(container).toMatchSnapshot();
  });

  it('should render the Account information page with correct company info for Eclipse User', async () => {
    setBreakpoint('mobile');
    const { container, queryByText } = render(<Account />, {
      authConfig,
      mocks: [mockUserwithMultiAccounts, mockAccountDetails],
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: {
            id: '22222222-2222-2222-2222-222222222222',
            erpAccountId: ''
          },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });

    await waitFor(() => {
      expect(queryByText('TEXAS MILITARY DEPARTMENT SHOP')).toBeInTheDocument();
    });

    expect(container).toMatchSnapshot();
  });
});
