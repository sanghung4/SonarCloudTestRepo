import { fireEvent } from '@testing-library/react';
import Login from 'Login';
import { act } from 'react-dom/test-utils';
import { render } from 'test-utils/TestWrapper';
import {
  mockInvitedUserEmailSentError,
  mockInvitedUserEmailSentSuccess,
  mockResendLegacyInviteEmailSuccess
} from 'test-utils/mockResponses';
import { testIds } from 'test-utils/testIds';

/**
 * TestIDs
 */
const TEST_IDS = testIds.SignIn;

/**
 * Mocks
 */
const mockLogin = jest.fn();
const mockPushAlert = jest.fn();
const mockReplace = jest.fn();

jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mockPushAlert })
}));

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({ replace: mockReplace })
}));

/**
 * TEST
 */
describe('Login', () => {
  it('Should redirect if page is authenticated', async () => {
    const { queryByTestId } = render(<Login />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    const page = queryByTestId(TEST_IDS.page);
    expect(page).toBeFalsy();
  });
  it('Should render page', async () => {
    const { queryByTestId } = render(<Login />, {
      authConfig: { authState: { isAuthenticated: false } }
    });

    const page = queryByTestId(TEST_IDS.page);
    expect(page).toBeInTheDocument();
  });
  it('Should render errors if email or password are not entered', async () => {
    const { findByTestId, findByText } = render(<Login />, {
      authConfig: { authState: { isAuthenticated: false } }
    });

    const signInButton = await findByTestId(TEST_IDS.signInButton);
    fireEvent.click(signInButton);

    const emailMessage = await findByText('Email Address is required');
    const passwordMessage = await findByText('Password is required');

    expect(emailMessage).toBeInTheDocument();
    expect(passwordMessage).toBeInTheDocument();
  });
  it('Should allow user to submit user name and password', async () => {
    const { findByTestId } = render(<Login />, {
      authConfig: { authState: { isAuthenticated: false }, login: mockLogin }
    });

    await act(async () => {
      const emailInput = await findByTestId(TEST_IDS.emailInput);
      fireEvent.change(emailInput!, { target: { value: 'example@email.com' } });

      const passwordInput = await findByTestId(TEST_IDS.passwordInput);
      fireEvent.change(passwordInput!, { target: { value: '12345' } });

      const signInButton = await findByTestId(TEST_IDS.signInButton);
      fireEvent.click(signInButton);
    });

    expect(mockLogin).toBeCalled();
  });
  it('Should send email invite', async () => {
    const { findByTestId } = render(<Login />, {
      authConfig: {
        authState: { isAuthenticated: false },
        login: () =>
          new Promise((res, rej) => {
            rej();
          })
      },
      mocks: [
        mockInvitedUserEmailSentSuccess,
        mockResendLegacyInviteEmailSuccess
      ]
    });

    await act(async () => {
      const emailInput = await findByTestId(TEST_IDS.emailInput);
      fireEvent.change(emailInput!, { target: { value: 'example@email.com' } });

      const passwordInput = await findByTestId(TEST_IDS.passwordInput);
      fireEvent.change(passwordInput!, { target: { value: '12345' } });

      const signInButton = await findByTestId(TEST_IDS.signInButton);
      fireEvent.click(signInButton);
    });

    expect(mockReplace).toBeCalled();
  });
  it('Should throw error for incorrect credentials', async () => {
    const { findByTestId } = render(<Login />, {
      authConfig: {
        authState: { isAuthenticated: false },
        login: () =>
          new Promise((res, rej) => {
            rej();
          })
      },
      mocks: [mockInvitedUserEmailSentError]
    });

    await act(async () => {
      const emailInput = await findByTestId(TEST_IDS.emailInput);
      fireEvent.change(emailInput!, { target: { value: 'example@email.com' } });

      const passwordInput = await findByTestId(TEST_IDS.passwordInput);
      fireEvent.change(passwordInput!, { target: { value: '12345' } });

      const signInButton = await findByTestId(TEST_IDS.signInButton);
      fireEvent.click(signInButton);
    });

    expect(mockPushAlert).toBeCalled();
  });
});
