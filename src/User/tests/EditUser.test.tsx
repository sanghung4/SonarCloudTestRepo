import { act, fireEvent, waitFor } from '@testing-library/react';
import { render } from 'test-utils/TestWrapper';
import EditUser from 'User/EditUser';
import {
  GetPhoneTypesQuery,
  GetPhoneTypesQueryVariables,
  RejectionReason,
  useGetPhoneTypesQuery
} from 'generated/graphql';
import * as t from 'locales/en/translation.json';
import { userInfo } from './mocks';
import { RoleInfo } from 'User/Roles';
import { Radio } from '@dialexa/reece-component-library';
import { ApolloError } from '@apollo/client/errors';
import { QueryHookOptions } from '@apollo/client';

/**
 * Mock values
 */
const mocks = {
  pushAlert: jest.fn(),
  mockCancelFn: jest.fn(),
  handleCancel: jest.fn(),
  handleCloseCancelDialog: jest.fn(),
  handleRoleChange: jest.fn(),
  handleDeleteDialogOpened: jest.fn(),
  handleDelete: jest.fn(),
  useGetPhoneTypesQuery: {
    data: { phoneTypes: ['MOBILE', 'HOME', 'OFFICE'] },
    loading: false
  }
};

const mockState = {
  selectedUser: { ...userInfo },
  search: ''
};

type DeleteDialogProps = {
  onSubmit: (leftCompanyBoolean: boolean) => void;
  onClose: () => void;
};
type RejectionDialogProps = {
  onSubmit: (reason: string, description: string) => void;
};
type PurchaseApproverDialogProps = {
  onClose: () => void;
};
type ConfirmCancelDialogProps = {
  onClose: (shouldSave: boolean) => void;
};
type UserRoleProps = {
  onChange: (info: RoleInfo) => void;
};
type MockGQLProp<V, R> = {
  fetchPolicy?: string;
  onCompleted?: (data: R) => void;
  onError?: (error: ApolloError) => void;
  refetchWritePolicy?: string;
  skip?: boolean;
  variables?: V;
};
type MockGetPhoneTypesQueryVariables = QueryHookOptions<
  GetPhoneTypesQuery,
  GetPhoneTypesQueryVariables
>;

/**
 * Mock functions
 */
function MockRoles({ onChange }: UserRoleProps) {
  const mockChange = () => {
    onChange({
      id: '12345',
      approverId: ''
    });
  };
  return (
    <>
      <Radio
        data-testid="mock-role-radio-button"
        value="12345"
        onClick={mockChange}
      />
    </>
  );
}

function MockPurchaseApproverDialog({ onClose }: PurchaseApproverDialogProps) {
  return (
    <button
      data-testid="mock-purchase-approver-dialog-close"
      onClick={onClose}
    />
  );
}

function MockConfirmCancelDialog({ onClose }: ConfirmCancelDialogProps) {
  const onCloseTrue = () => onClose(true);
  const onCloseFalse = () => onClose(false);
  return (
    <>
      <button
        data-testid="mock-confirm-cancel-dialog-close"
        onClick={onCloseTrue}
      />
      <button
        data-testid="mock-confirm-cancel-dialog-close-false"
        onClick={onCloseFalse}
      />
    </>
  );
}

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

/**
 * Mock methods
 */
jest.mock('react-router-dom', () => ({
  useHistory: () => ({
    push: jest.fn()
  }),
  useLocation: () => ({
    pathname: `/user/561ad597-6e0d-4096-a291-dfdbcd8bf714`,
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

jest.mock('User/ConfirmCancelDialog', () => ({
  ...jest.requireActual('User/ConfirmCancelDialog'),
  __esModule: true,
  default: MockConfirmCancelDialog
}));

jest.mock('User/PurchaseApproverDialog', () => ({
  ...jest.requireActual('User/PurchaseApproverDialog'),
  __esModule: true,
  default: MockPurchaseApproverDialog
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

jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useDeleteUserMutation: () => [mocks.handleDelete],
  checkUsersForApproverMutation: () => [
    mocks.handleDeleteDialogOpened,
    { loading: false }
  ],
  useGetPhoneTypesQuery: jest.fn()
}));

describe('Edit User tests', () => {
  beforeEach(() => {
    (useGetPhoneTypesQuery as jest.Mock).mockImplementation(
      (
        _param: MockGQLProp<MockGetPhoneTypesQueryVariables, GetPhoneTypesQuery>
      ) => {
        const { data, loading } = mocks.useGetPhoneTypesQuery;
        return { data, loading };
      }
    );
  });

  it('Should display the delete user button', async () => {
    const { findByTestId } = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );
    const deleteUserBtn = await findByTestId('edit-user-delete-user-button');
    expect(deleteUserBtn).toBeInTheDocument();
    fireEvent.click(deleteUserBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(deleteUserBtn).toHaveTextContent('Delete User');
  });

  it('Should test the Save and Cancel buttons', async () => {
    const { findByTestId } = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );

    const saveBtn = await findByTestId('edit-user-save-user-button');
    const cancelBtn = await findByTestId('edit-user-cancel-button');
    expect(saveBtn).toBeInTheDocument();
    expect(cancelBtn).toBeInTheDocument();
    fireEvent.click(cancelBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.mockCancelFn).toBeCalled();
  });

  it('should call handleDelete function for DeleteDialog', async () => {
    const { findByTestId } = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
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

  it('Should test handleDelete function with error', async () => {
    mocks.handleDelete = jest.fn(() => {
      throw new Error();
    });
    const utils = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );

    const closeBtn = await utils.findByTestId('mock-delete-onclose');
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(closeBtn);
    expect(closeBtn).toBeInTheDocument();

    const deleteBtn = await utils.findByTestId('mock-delete-onsubmit-true');
    fireEvent.click(deleteBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.handleDelete).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userDeleteError, {
      variant: 'error'
    });
  });

  it('should test handlePurchaseApproverDialogClose for PurchaseApproverDialog', async () => {
    const utils = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );

    const closeBtn = await utils.findByTestId(
      'mock-purchase-approver-dialog-close'
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(closeBtn);
    expect(closeBtn).toBeInTheDocument();
  });

  it('should test handlePurchaseApproverDialogClose for PurchaseApproverDialog', async () => {
    const utils = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );

    const closeBtn = await utils.findByTestId(
      'mock-confirm-cancel-dialog-close'
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(closeBtn);
    expect(closeBtn).toBeInTheDocument();
    const closeWithoutSaveBtn = await utils.findByTestId(
      'mock-confirm-cancel-dialog-close-false'
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(closeWithoutSaveBtn);
    expect(closeWithoutSaveBtn).toBeInTheDocument();
  });

  it('should test handleRoleChange for UserRoles', async () => {
    const utils = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );

    const userRole = await utils.findByTestId('mock-role-radio-button');
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(userRole);
    expect(userRole).toBeInTheDocument();
  });

  it('should test the formik input fields', async () => {
    const { findByTestId } = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );

    const firstName = await findByTestId('edit-user-first-name-input');
    const lastName = await findByTestId('edit-user-last-name-input');
    const email = await findByTestId('edit-user-email-address');
    const phoneNumber = await findByTestId('edit-user-phone-number-input');
    const phoneTypeDropdown = await findByTestId('phoneTypeDropdown');

    fireEvent.change(firstName, { target: { value: 'test' } });
    fireEvent.change(lastName, { target: { value: 'user' } });
    fireEvent.change(phoneNumber, { target: { value: '(654) 654-6546' } });
    fireEvent.change(email, { target: { value: 'testuser@gmail.com' } });

    expect(firstName).toHaveDisplayValue('test');
    expect(lastName).toHaveDisplayValue('user');
    expect(phoneNumber).toHaveDisplayValue('(654) 654-6546');
    expect(email).toHaveDisplayValue('testuser@gmail.com');
    expect(phoneTypeDropdown).toBeInTheDocument();
  });

  it('should test the first Name input field with error message', async () => {
    const { getByTestId } = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );
    const firstNameInput = getByTestId('edit-user-first-name-input');
    const firstNameHelper = getByTestId('firstNameHelperText');

    fireEvent.focus(firstNameInput);
    fireEvent.change(firstNameInput, { target: { value: '' } });
    fireEvent.blur(firstNameInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(firstNameHelper).toHaveTextContent(t.validation.firstNameRequired);
  });

  it('should test the lastName input field with error message', async () => {
    const { getByTestId } = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );
    const lastNameInput = getByTestId('edit-user-last-name-input');
    const lastNameHelper = getByTestId('lastNameHelperText');

    fireEvent.focus(lastNameInput);
    fireEvent.change(lastNameInput, { target: { value: '' } });
    fireEvent.blur(lastNameInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(lastNameHelper).toHaveTextContent(t.validation.lastNameRequired);
  });

  it('should test the phoneNumber input field with error message', async () => {
    const { getByTestId } = render(
      <EditUser onCancel={mocks.mockCancelFn} user={userInfo} />
    );
    const phoneNumberInput = getByTestId('edit-user-phone-number-input');
    const phoneNumberHelper = getByTestId('phoneNumberHelperText');

    fireEvent.focus(phoneNumberInput);
    fireEvent.change(phoneNumberInput, { target: { value: '43432' } });
    fireEvent.blur(phoneNumberInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(phoneNumberHelper).toHaveTextContent(
      t.validation.phoneNumberInvalid
    );
  });
});
