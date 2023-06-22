import { act, fireEvent, within } from '@testing-library/react';

import { Approver, Role } from 'generated/graphql';
import InviteUser, { InviteUserFormValues } from 'Invite/InviteUser';
import { mockApprovers, mockRoles } from 'Invite/tests/mocks';
import * as t from 'locales/en/translation.json';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
type MockValues = {
  approvers?: { approvers: Approver[] };
  roles?: { roles: Role[] };
  submit: (invite: InviteUserFormValues) => void;
};
const mocks: MockValues = { submit: jest.fn() };

/**
 * Mock Methods
 */
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useGetApproversQuery: () => ({ data: mocks.approvers }),
  useGetRolesQuery: () => ({ data: mocks.roles })
}));

/**
 * Setup function
 */
function setup(m: MockValues) {
  return render(<InviteUser onSubmitInvite={m.submit} />);
}
function changeInputValue(e: HTMLElement, value: string) {
  fireEvent.focus(e);
  fireEvent.change(e, { target: { value } });
  fireEvent.blur(e);
}

/**
 * TEST
 */
describe('Invite - InviteUser', () => {
  afterEach(() => {
    mocks.approvers = undefined;
    mocks.roles = undefined;
    mocks.submit = jest.fn();
  });

  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });
  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with roles', async () => {
    setBreakpoint('desktop');
    mocks.roles = { roles: mockRoles };
    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('Expect all helper texts to show errors on required forms when submitting as empty', async () => {
    setBreakpoint('desktop');
    mocks.roles = { roles: mockRoles };
    const { getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    const errors = {
      email: getByTestId('email-msg'),
      firstName: getByTestId('firstname-msg'),
      lastName: getByTestId('lastname-msg'),
      role: getByTestId('invite-user-role-grid').getElementsByTagName('p')[0]
    };

    await act(async () => {
      fireEvent.click(getByTestId('inviteUserButton'));
    });
    expect(errors.email).toHaveTextContent(t.validation.emailRequired);
    expect(errors.firstName).toHaveTextContent(t.validation.firstNameRequired);
    expect(errors.lastName).toHaveTextContent(t.validation.lastNameRequired);
    expect(errors.role).toHaveTextContent(t.validation.roleRequired);
  });

  it('Expect Purchase of Approval and required forms when submitting as empty', async () => {
    setBreakpoint('desktop');
    mocks.roles = { roles: mockRoles };
    const { getByText, getByRole, getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    const input = getByText(t.common.select);
    const errors = {
      email: getByTestId('email-msg'),
      firstName: getByTestId('firstname-msg'),
      lastName: getByTestId('lastname-msg')
    };

    await act(async () => {
      fireEvent.mouseDown(input);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      const listbox = within(getByRole('listbox'));
      fireEvent.click(listbox.getByText(/Purchase with Approval/i));
      fireEvent.click(getByTestId('inviteUserButton'));
    });

    const errorApprover = getByTestId(
      'invite-user-approver-grid'
    ).getElementsByTagName('p')[0];
    expect(errors.email).toHaveTextContent(t.validation.emailRequired);
    expect(errors.firstName).toHaveTextContent(t.validation.firstNameRequired);
    expect(errors.lastName).toHaveTextContent(t.validation.lastNameRequired);
    expect(errorApprover).toHaveTextContent(t.validation.approverRequired);
  });

  it('Expect to select Approver text', async () => {
    setBreakpoint('desktop');
    mocks.roles = { roles: mockRoles };
    mocks.approvers = { approvers: mockApprovers };
    const { getAllByRole, getByText, getByRole, getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    await act(async () => {
      // Select role
      const inputRole = getByText(t.common.select);
      fireEvent.mouseDown(inputRole);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      const listbox = within(getByRole('listbox'));
      fireEvent.click(listbox.getByText(/Purchase with Approval/i));

      // Select Approver
      const inputApprover = getAllByRole('button')[1];
      fireEvent.mouseDown(inputApprover);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      const listbox2 = within(getByRole('listbox'));
      const [{ firstName, lastName }] = mockApprovers;
      const findName = `${firstName} ${lastName}`;
      fireEvent.click(listbox2.getByText(findName));
    });

    const approverInput = getByTestId('invite-user-approver-input');
    expect(approverInput).toHaveValue(mockApprovers[0].id);
  });

  it('Expect to call onSubmit with filled form', async () => {
    setBreakpoint('desktop');
    mocks.roles = { roles: mockRoles };
    const { getByText, getByRole, getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    await act(async () => {
      // Email
      const emailInput = getByTestId('invite-user-email-input');
      changeInputValue(emailInput, 'farmer.macjoy@yahoo.com');
      // First name
      const firstNameInput = getByTestId('invite-user-firstname-input');
      changeInputValue(firstNameInput, 'Farmer');
      // last name
      const lastNameInput = getByTestId('invite-user-lastname-input');
      changeInputValue(lastNameInput, 'MacJoy');
      // Role
      fireEvent.mouseDown(getByText(t.common.select));
      await act(() => new Promise((res) => setTimeout(res, 0)));
      const listbox = within(getByRole('listbox'));
      fireEvent.click(listbox.getByText(/Standard Access/i));
      // Submit
      fireEvent.click(getByTestId('inviteUserButton'));
    });

    expect(mocks.submit).toBeCalled();
  });
});
