import React from 'react';

import { fireEvent, act, waitFor } from '@testing-library/react';
import UpdatePasswordDialog from 'Account/UpdatePasswordDialog';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render, TestRender } from 'test-utils/TestWrapper';
import {
  MockUpdatePassword,
  mockUpdatePasswordFail,
  mockUpdatePasswordOktaFail,
  mockUpdatePasswordSuccess,
  mockUserInfo,
  UPDATE_PASSWORD_VAR_FAIL,
  UPDATE_PASSWORD_VAR_SUCCESS
} from './mocks';
import { SnackbarProvider } from '@dialexa/reece-component-library';
import * as t from 'locales/en/translation.json';

const setup = () =>
  render(<UpdatePasswordDialog open onClose={jest.fn()} user={mockUserInfo} />);

async function updateInputValue<T = HTMLInputElement>(
  element: HTMLElement,
  value: string
) {
  await act(() => {
    fireEvent.focus(element);
    fireEvent.change(element, { target: { value } });
    fireEvent.blur(element);
  });
}

const fillUpdatePasswordDialogFormInputs = async (
  utils: TestRender,
  variables: MockUpdatePassword
) => {
  const oldPasswordInput = utils.getByTestId('old-password-input');
  const newPasswordInput = utils.getByTestId('new-password-input');
  const confirmPasswordInput = utils.getByTestId('confirm-password-input');

  await updateInputValue(oldPasswordInput, variables.oldUserPassword);
  await updateInputValue(newPasswordInput, variables.newUserPassword);
  await updateInputValue(confirmPasswordInput, variables.newUserPassword);
};

describe('UpdatePasswordDialog tests', () => {
  it('should show submit change button when dialog open', async () => {
    setBreakpoint('desktop');

    const { getByTestId } = setup();

    const submitButton = getByTestId('submitChangesButton');

    expect(submitButton).toBeInTheDocument();
  });

  it('Show Password Required Message When Old Password filed is empty', async () => {
    const { getByTestId, baseElement } = setup();
    const oldPasswordInput = getByTestId('old-password-input');
    const oldPasswordHelper = baseElement.querySelector(
      '#old-password-helper-text'
    );

    await updateInputValue(oldPasswordInput, '');

    await act(async () => {
      fireEvent.click(getByTestId('submitChangesButton'));
    });

    expect(oldPasswordHelper).toHaveTextContent(t.validation.passwordRequired);
  });

  it('Show Invalid Password Message When Old Password filed is not strong', async () => {
    const { getByTestId, baseElement } = setup();
    const oldPasswordInput = getByTestId('old-password-input');
    const oldPasswordHelper = baseElement.querySelector(
      '#old-password-helper-text'
    );

    await updateInputValue(oldPasswordInput, 'password');

    await act(async () => {
      fireEvent.click(getByTestId('submitChangesButton'));
    });

    expect(oldPasswordHelper).toHaveTextContent(t.validation.passwordInvalid);
  });

  it('Show Password Required Message When New Password filed is empty', async () => {
    const { getByTestId, baseElement } = setup();
    const newPasswordInput = getByTestId('new-password-input');
    const newPasswordHelper = baseElement.querySelector(
      '#new-password-helper-text'
    );

    await updateInputValue(newPasswordInput, '');

    await act(async () => {
      fireEvent.click(getByTestId('submitChangesButton'));
    });

    expect(newPasswordHelper).toHaveTextContent(t.validation.passwordRequired);
  });

  it('Show Invalid Password Message When New Password filed is not strong', async () => {
    const { getByTestId, baseElement } = setup();
    const newPasswordInput = getByTestId('new-password-input');
    const newPasswordHelper = baseElement.querySelector(
      '#new-password-helper-text'
    );

    await updateInputValue(newPasswordInput, 'password');

    await act(async () => {
      fireEvent.click(getByTestId('submitChangesButton'));
    });

    expect(newPasswordHelper).toHaveTextContent(t.validation.passwordInvalid);
  });

  it('Show Password Required Message When Confirm Password filed is empty', async () => {
    const { getByTestId, baseElement } = setup();
    const confirmPasswordInput = getByTestId('confirm-password-input');
    const confirmPasswordHelper = baseElement.querySelector(
      '#confirm-password-helper-text'
    );

    await updateInputValue(confirmPasswordInput, '');

    await act(async () => {
      fireEvent.click(getByTestId('submitChangesButton'));
    });

    expect(confirmPasswordHelper).toHaveTextContent(
      t.validation.confirmPasswordRequired
    );
  });

  it('Show Password Not Match Message', async () => {
    const { getByTestId, baseElement } = setup();
    const newPasswordInput = getByTestId('new-password-input');
    const confirmPasswordInput = getByTestId('confirm-password-input');
    const confirmPasswordHelper = baseElement.querySelector(
      '#confirm-password-helper-text'
    );

    await updateInputValue(newPasswordInput, 'Password1');
    await updateInputValue(confirmPasswordInput, 'Password2');

    await act(async () => {
      fireEvent.click(getByTestId('submitChangesButton'));
    });

    expect(confirmPasswordHelper).toHaveTextContent(
      t.validation.passwordNoMatch
    );
  });
});

describe('UpdatePasswordDialog Form - Submit', () => {
  it('Should show Okta Error Message on Error', async () => {
    const utils = render(
      <SnackbarProvider>
        <UpdatePasswordDialog open onClose={jest.fn()} user={mockUserInfo} />
      </SnackbarProvider>,
      { mocks: [mockUpdatePasswordOktaFail] }
    );

    await fillUpdatePasswordDialogFormInputs(utils, UPDATE_PASSWORD_VAR_FAIL);

    await act(() => {
      fireEvent.click(utils.getByTestId('submitChangesButton'));
    });

    const errorMsg = await utils.findByText(
      'Update of credentials failed - Old Password is not correct'
    );
    expect(errorMsg).toBeInTheDocument();
  });

  it('Should show Success message when submit success', async () => {
    const utils = render(
      <SnackbarProvider>
        <UpdatePasswordDialog open onClose={jest.fn()} user={mockUserInfo} />
      </SnackbarProvider>,
      { mocks: [mockUpdatePasswordSuccess] }
    );

    await fillUpdatePasswordDialogFormInputs(
      utils,
      UPDATE_PASSWORD_VAR_SUCCESS
    );

    await act(() => {
      fireEvent.click(utils.getByTestId('submitChangesButton'));
    });

    const successMsg = await utils.findByText(t.user.informationSaved);
    expect(successMsg).toBeInTheDocument();
  });

  it('Should show error message when update password failed', async () => {
    const utils = render(
      <SnackbarProvider>
        <UpdatePasswordDialog open onClose={jest.fn()} user={mockUserInfo} />
      </SnackbarProvider>,
      { mocks: [mockUpdatePasswordFail] }
    );

    await fillUpdatePasswordDialogFormInputs(utils, UPDATE_PASSWORD_VAR_FAIL);

    await act(() => {
      fireEvent.click(utils.getByTestId('submitChangesButton'));
    });

    const failMsg = await utils.findByText(t.user.updatePasswordFailed);
    expect(failMsg).toBeInTheDocument();
  });

  it('Should show error message when update password failed in mobile view', async () => {
    jest.useFakeTimers();
    setBreakpoint('mobile');
    const utils = render(
      <SnackbarProvider>
        <UpdatePasswordDialog open onClose={jest.fn()} user={mockUserInfo} />
      </SnackbarProvider>,
      { mocks: [mockUpdatePasswordFail] }
    );

    await fillUpdatePasswordDialogFormInputs(utils, UPDATE_PASSWORD_VAR_FAIL);

    await act(() => {
      fireEvent.click(utils.getByTestId('submitChangesButton'));
    });

    const failMsg = await utils.findByText(t.user.updatePasswordFailed);
    expect(failMsg).toBeInTheDocument();
    jest.advanceTimersByTime(5000);
    expect(failMsg).not.toBeInTheDocument();
    jest.runOnlyPendingTimers();
  });
});
