import React from 'react';

import { render } from 'test-utils/TestWrapper';
import { userInfo } from './mocks';

import User from 'User';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';
import { act, fireEvent } from '@testing-library/react';

const selectedAccountsMock: SelectedAccounts = {
  billTo: {
    id: '123456'
  }
};

const mockState = {
  selectedUser: { ...userInfo, roleId: '' },
  search: ''
};

jest.mock('react-router-dom', () => ({
  useHistory: () => ({
    push: jest.fn()
  }),
  useLocation: () => ({
    pathname: `/user/1234`,
    state: mockState
  })
}));

describe('User tests', () => {
  it('View User render with button for approval', () => {
    const { getByTestId } = render(<User />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      authConfig: {
        authState: {
          isAuthenticated: true
        },
        profile: {
          userId: 'testuser',
          permissions: [],
          isEmployee: true,
          isVerified: true
        }
      }
    });
    const approveButton = getByTestId('approve-user-button');
    expect(approveButton).toHaveTextContent('Approve user');
  });

  it('Edit User render matches the snapshot', async () => {
    mockState.selectedUser.roleId = '1234567';

    const { getByTestId } = render(<User />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      authConfig: {
        authState: {
          isAuthenticated: true
        },
        profile: {
          userId: 'testuser',
          permissions: [],
          isEmployee: true,
          isVerified: true
        }
      }
    });

    const editButton = getByTestId('edit-user-button');
    expect(editButton).toBeTruthy();

    fireEvent.click(editButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    const editUserDeleteButton = getByTestId('edit-user-delete-user-button');
    expect(editUserDeleteButton).toBeTruthy();
  });
});
