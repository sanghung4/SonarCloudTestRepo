import React from 'react';

import { render } from 'test-utils/TestWrapper';
import { fireEvent, waitFor } from '@testing-library/react';

import UserManagement from 'UserManagement';
import {
  mocks,
  mockAccountNotFoundError,
  mockOtherError
} from 'UserManagement/mocks';
import { mocks as homeMocks } from 'Home/tests/mocks';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';
import { mockHistory } from 'test-utils/mockRouter';

const selectedAccountsMock: SelectedAccounts = {
  billTo: {
    id: '123456'
  }
};

const mockFns = {
  history: { ...mockHistory },
  pushAlert: jest.fn()
};

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockFns.history
}));

jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mockFns.pushAlert })
}));

jest.mock('date-fns', () => ({
  ...jest.requireActual('date-fns'),
  differenceInDays: () => 3
}));

describe('User management tests', () => {
  it('User Management render matches the snapshot', () => {
    const { container } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });

    expect(container).toMatchSnapshot();
  });

  it('"User Management - Waiting for Approval" data is displayed correctly', async () => {
    const { container } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const check = mocks[0].result.data.unapprovedAccountRequests;

    expect(rows.length).toBe(check.length + 1); // plus the top header row
    expect(rows[1]).toHaveTextContent(check[0].email);
    expect(rows[2]).toHaveTextContent(check[1].email);
    expect(rows[3]).toHaveTextContent(check[2].email);
  });

  it('"User Management - Approved" data is displayed correctly', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const approvedTab = getByTestId('tab-Approved');
    fireEvent.click(approvedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const check = mocks[1].result.data.accountUsers;

    expect(rows.length).toBe(check.length + 1); // plus the top header row
    expect(rows[1]).toHaveTextContent(check[2].email);
    expect(rows[2]).toHaveTextContent(check[1].email);
  });

  it('"User Management - Rejected" data is displayed correctly', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const rejectedTab = getByTestId('tab-Rejected');
    fireEvent.click(rejectedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const check = mocks[2].result.data.rejectedAccountRequests;

    expect(rows.length).toBe(check.length + 1); // plus the top header row
    expect(rows[1]).toHaveTextContent(check[3].email);
    expect(rows[2]).toHaveTextContent(check[2].email);
    expect(rows[3]).toHaveTextContent(check[1].email);
    expect(rows[4]).toHaveTextContent(check[0].email);
  });

  it('Sort first name in ascending order on the "Rejected" table', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });
    const check = mocks[2].result.data.rejectedAccountRequests;

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const rejectedTab = getByTestId('tab-Rejected');
    fireEvent.click(rejectedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const firstNameColumn = rows[0].getElementsByTagName('th')[1];

    fireEvent.click(firstNameColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(rows[1]).toHaveTextContent(check[1].firstName);
    expect(rows[2]).toHaveTextContent(check[3].firstName);
    expect(rows[3]).toHaveTextContent(check[2].firstName);
    expect(rows[4]).toHaveTextContent(check[0].firstName);
  });

  it('Sort first name in descending order on the "Rejected" table', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });
    const check = mocks[2].result.data.rejectedAccountRequests;

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const rejectedTab = getByTestId('tab-Rejected');
    fireEvent.click(rejectedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const firstNameColumn = rows[0].getElementsByTagName('th')[1];

    fireEvent.click(firstNameColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    fireEvent.click(firstNameColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(rows[1]).toHaveTextContent(check[0].firstName);
    expect(rows[2]).toHaveTextContent(check[2].firstName);
    expect(rows[3]).toHaveTextContent(check[3].firstName);
    expect(rows[4]).toHaveTextContent(check[1].firstName);
  });

  it('Sort last name in ascending order on the "Rejected" table', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });
    const check = mocks[2].result.data.rejectedAccountRequests;

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const rejectedTab = getByTestId('tab-Rejected');
    fireEvent.click(rejectedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const lastNameColumn = rows[0].getElementsByTagName('th')[2];
    fireEvent.click(lastNameColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(rows[1]).toHaveTextContent(check[1].lastName);
    expect(rows[2]).toHaveTextContent(check[3].lastName);
    expect(rows[3]).toHaveTextContent(check[2].lastName);
    expect(rows[4]).toHaveTextContent(check[0].lastName);
  });

  it('Sort last name in descending order on the "Rejected" table', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });
    const check = mocks[2].result.data.rejectedAccountRequests;

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const rejectedTab = getByTestId('tab-Rejected');
    fireEvent.click(rejectedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const lastNameColumn = rows[0].getElementsByTagName('th')[2];
    fireEvent.click(lastNameColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(lastNameColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(rows[1]).toHaveTextContent(check[0].lastName);
    expect(rows[2]).toHaveTextContent(check[2].lastName);
    expect(rows[3]).toHaveTextContent(check[3].lastName);
    expect(rows[4]).toHaveTextContent(check[1].lastName);
  });

  it('show the Approved table sorted by Role in ascending order', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });
    const check = mocks[1].result.data.accountUsers;

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const approvedTab = getByTestId('tab-Approved');
    fireEvent.click(approvedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const roleColumn = rows[0].getElementsByTagName('th')[3];

    fireEvent.click(roleColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(rows[1]).toHaveTextContent(check[0].role.name);
    expect(rows[2]).toHaveTextContent(check[2].role.name);
    expect(rows[3]).toHaveTextContent(check[1].role.name);
  });

  it('show the Approved table sorted by Role in descending order', async () => {
    const { container, getByTestId } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks
    });
    const check = mocks[1].result.data.accountUsers;

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const approvedTab = getByTestId('tab-Approved');
    fireEvent.click(approvedTab);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const rows = container.getElementsByTagName('tr');
    const roleColumn = rows[0].getElementsByTagName('th')[3];

    fireEvent.click(roleColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    fireEvent.click(roleColumn);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(rows[1]).toHaveTextContent(check[1].role.name);
    expect(rows[2]).toHaveTextContent(check[2].role.name);
    expect(rows[3]).toHaveTextContent(check[0].role.name);
  });

  it('User Management render when account not found error in response for employee', async () => {
    const clearAccounts = jest.fn();
    const { container } = render(<UserManagement />, {
      selectedAccountsConfig: {
        selectedAccounts: selectedAccountsMock,
        clearAccounts: clearAccounts
      },
      authConfig: homeMocks.authorized,
      mocks: mockAccountNotFoundError
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(clearAccounts).toBeCalled();
    expect(mockFns.history.push).toBeCalledWith('/select-accounts');
  });

  it('User Management render when other error in response for employee', async () => {
    const clearAccounts = jest.fn();
    const { container } = render(<UserManagement />, {
      selectedAccountsConfig: {
        selectedAccounts: selectedAccountsMock,
        clearAccounts: clearAccounts
      },
      authConfig: homeMocks.authorized,
      mocks: mockOtherError
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockFns.pushAlert).toBeCalled();
  });

  it('User Management render when found an error in response for non employee', async () => {
    const { container } = render(<UserManagement />, {
      selectedAccountsConfig: { selectedAccounts: selectedAccountsMock },
      mocks: mockOtherError
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockFns.pushAlert).toBeCalled();
  });
});
