import { render } from 'test-utils/TestWrapper';
import ViewUser from 'User/ViewUser';
import { userInfo, userInfoWithNoName, mockSelectedRole } from './mocks';
import { act, fireEvent } from '@testing-library/react';
import * as t from 'locales/en/translation.json';
import { RejectionReason } from 'generated/graphql';

/**
 * Mock values
 */
const mocks = {
  onEditClicked: jest.fn(),
  refreshContact: jest.fn(),
  pushAlert: jest.fn(),
  handleDeleteDialogOpened: jest.fn(),
  setRoleToAdmin: jest.fn(),
  handleApprove: jest.fn(),
  handleReject: jest.fn(),
  handleDelete: jest.fn()
};
const mockState = {
  selectedUser: { ...userInfo },
  search: ''
};

/**
 * Types
 */
type DeleteDialogProps = {
  onSubmit: (leftCompanyBoolean: boolean) => void;
  onClose: () => void;
};
type RejectionDialogProps = {
  onSubmit: (reason: string, description: string) => void;
};
type RoleInfo = {
  id?: string;
  approverId?: string;
};
type RolesProps = {
  onChange: (info: RoleInfo) => void;
};

/**
 * Mock function
 */
function MockDeleteDialog({ onClose, onSubmit }: DeleteDialogProps) {
  const onSubmitTrue = () => onSubmit(true);
  const onSubmitFalse = () => onSubmit(false);
  return (
    <>
      <button data-testid="mock-delete-onclose" onClick={onClose} />
      <button data-testid="mock-delete-onsubmit-true" onClick={onSubmitTrue} />
      <button
        data-testid="mock-delete-onsubmit-false"
        onClick={onSubmitFalse}
      />
    </>
  );
}

function MockRejectionDialog({ onSubmit }: RejectionDialogProps) {
  const onReject = () => onSubmit(RejectionReason.Other, 'OTHER');
  return (
    <>
      <button data-testid="mock-reject-onsubmit" onClick={onReject} />
    </>
  );
}

function MockRoles({ onChange }: RolesProps) {
  const onRoleChange = () => onChange(mockSelectedRole);
  return (
    <>
      <button data-testid="mock-user-role-change" onClick={onRoleChange} />
    </>
  );
}

/**
 * Mock methods
 */
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useRefreshContactMutation: () => [mocks.refreshContact, { loading: false }],
  useDeleteUserMutation: () => [mocks.handleDelete, { loading: false }],
  useApproveUserMutation: () => [mocks.handleApprove, { loading: false }],
  useRejectUserMutation: () => [mocks.handleReject],
  CheckUsersForApproverMutation: () => [mocks.handleDeleteDialogOpened],
  useGetRolesQuery: () => [jest.fn()]
}));

jest.mock('react-router-dom', () => ({
  useHistory: () => ({
    push: jest.fn()
  }),
  useLocation: () => ({
    pathname: `/user/ab4e4f52-4ea9-4a19-950c-4404b7c4bb2d`,
    state: mockState
  })
}));
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

jest.mock('User/DeleteDialog', () => ({
  ...jest.requireActual('User/DeleteDialog'),
  __esModule: true,
  default: MockDeleteDialog
}));

jest.mock('User/RejectionDialog', () => ({
  ...jest.requireActual('User/RejectionDialog'),
  __esModule: true,
  default: MockRejectionDialog
}));

jest.mock('User/Roles', () => ({
  ...jest.requireActual('User/Roles'),
  __esModule: true,
  default: MockRoles
}));

describe('View User', () => {
  it('Expect to render', () => {
    const { container } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfo}
        customerApproval={false}
      />
    );
    expect(container).toBeInTheDocument();
  });

  it('Expect refresh contact to be called on-click of Refresh button', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfo}
        customerApproval={false}
      />
    );
    const refreshBtn = await findByTestId('refresh-user-button');
    expect(refreshBtn).toBeInTheDocument();

    fireEvent.click(refreshBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.refreshContact).toBeCalled();
  });

  it('Expect handleClick function to be called on click of Delete', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfo}
        customerApproval={false}
      />
    );

    const deleteBtnViewUser = await findByTestId('delete-user-button');
    expect(deleteBtnViewUser).toBeInTheDocument();

    fireEvent.click(deleteBtnViewUser);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userDeleteError, {
      variant: 'error'
    });
  });

  it('Expect handleDelete function to be called for DeleteDialog', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={true}
      />
    );

    const deleteBtn = await findByTestId('mock-delete-onsubmit-true');
    fireEvent.click(deleteBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.handleDelete).toBeCalled();
    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userDeleteSuccess, {
      variant: 'success'
    });
  });

  it('Expect handleDelete function to be called with error', async () => {
    mocks.handleDelete = jest.fn(() => {
      throw new Error();
    });
    const utils = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={true}
      />
    );

    const deleteBtn = await utils.findByTestId('mock-delete-onsubmit-true');
    fireEvent.click(deleteBtn);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.handleDelete).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userDeleteError, {
      variant: 'error'
    });
  });

  it('Expect handleReject function to be called for RejectionDialog', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={true}
      />
    );

    const rejectBtn = await findByTestId('mock-reject-onsubmit');
    expect(rejectBtn).toBeInTheDocument();
    fireEvent.click(rejectBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.handleReject).toBeCalled();
    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userRejectSuccess, {
      variant: 'success'
    });
  });

  it('Expect handleReject function to be called with customerApproval as false', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={false}
      />
    );

    const rejectBtn = await findByTestId('mock-reject-onsubmit');
    expect(rejectBtn).toBeInTheDocument();
    fireEvent.click(rejectBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.handleReject).toBeCalled();
    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userRejectSuccess, {
      variant: 'success'
    });
  });

  it('Expect handleReject function to be called with error', async () => {
    mocks.handleReject = jest.fn(() => {
      throw new Error();
    });
    const utils = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={true}
      />
    );

    const deleteBtn = await utils.findByTestId('mock-reject-onsubmit');
    fireEvent.click(deleteBtn);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.handleReject).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userRejectError, {
      variant: 'error'
    });
  });

  it('Expect Must-Select-Role warning when role is null', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={true}
      />
    );

    const approveUserBtn = await findByTestId('approve-user-button');
    expect(approveUserBtn).toBeInTheDocument();

    fireEvent.click(approveUserBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.mustSelectRole, {
      variant: 'warning'
    });
  });

  it('Expect User to be successfully added when approve button is clicked', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={true}
      />
    );

    const roleChngBtn = await findByTestId('mock-user-role-change');
    expect(roleChngBtn).toBeInTheDocument();
    fireEvent.click(roleChngBtn);

    const approveUserBtn = await findByTestId('approve-user-button');
    expect(approveUserBtn).toBeInTheDocument();

    fireEvent.click(approveUserBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userAddSuccess, {
      variant: 'success'
    });
  });

  it('Expect User to be successfully added when approve button is clicked but customer approval is false', async () => {
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={false}
      />
    );

    const roleChngBtn = await findByTestId('mock-user-role-change');
    expect(roleChngBtn).toBeInTheDocument();
    fireEvent.click(roleChngBtn);

    const approveUserBtn = await findByTestId('approve-user-button');
    expect(approveUserBtn).toBeInTheDocument();

    fireEvent.click(approveUserBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userAddSuccess, {
      variant: 'success'
    });
  });

  it('Expect error alert when approve button is clicked', async () => {
    mocks.handleApprove = jest.fn(() => {
      throw new Error();
    });
    const { findByTestId } = render(
      <ViewUser
        onEditClicked={mocks.onEditClicked}
        user={userInfoWithNoName}
        customerApproval={true}
      />
    );

    const roleChngBtn = await findByTestId('mock-user-role-change');
    expect(roleChngBtn).toBeInTheDocument();
    fireEvent.click(roleChngBtn);

    const approveUserBtn = await findByTestId('approve-user-button');
    expect(approveUserBtn).toBeInTheDocument();

    fireEvent.click(approveUserBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.pushAlert).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith('', {
      variant: 'error'
    });
  });
});
