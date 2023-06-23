import { useOktaAuth } from '@okta/okta-react';
import { screen } from '@testing-library/react';

import { useApiLogin, LoginResponse } from 'api/login.api';
import { mockLoginFail, mockLoginSuccess } from 'api/mocks/login.mocks';
import { apiMockResponse } from 'api/tests/core.mocks';
import Login from 'pages/Login';
import { fillTextInput, fireEvent, render } from 'test-util';
import { Environments } from 'util/configurations';
import * as constants from 'util/configurations';

/**
 * Types
 */
type Mocks = {
  apiLoading: boolean;
  apiError: boolean;
  apiData?: LoginResponse;
  env: Environments;
  oktaSignIn: jest.Mock;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  apiLoading: false,
  apiError: false,
  env: Environments.DEV,
  oktaSignIn: jest.fn()
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('@okta/okta-react', () => ({
  ...jest.requireActual('@okta/okta-react'),
  useOktaAuth: jest.fn()
}));
jest.mock('api/login.api', () => ({
  ...jest.requireActual('api/login.api'),
  useApiLogin: jest.fn()
}));

/**
 * TEST
 */
describe('pages/Login', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock methods
  beforeEach(() => {
    // 🔹 useApiLogin hook
    (useApiLogin as jest.Mock).mockImplementation(() => ({
      loading: mocks.apiLoading,
      call: () => apiMockResponse(mocks.apiError ? null : mocks.apiData)
    }));
    // 🔹 useOktaAuth hook
    (useOktaAuth as jest.Mock).mockImplementation(() => ({
      oktaAuth: { signInWithRedirect: mocks.oktaSignIn }
    }));
  });

  // 🟢 1 - default
  it('Expect to be rendered', () => {
    // act
    render(<Login />);
    const container = screen.queryByTestId('login_container-dev');
    // assert
    expect(container).toBeInTheDocument();
  });

  // 🟢 2 - prod
  it('Expect to be rendered as prodcution', () => {
    // arrange
    const prevEnv = constants.configuration.environment;
    constants.configuration.environment = Environments.PROD;
    // act
    render(<Login />);
    const container = screen.queryByTestId('login_container');
    // assert
    expect(container).toBeInTheDocument();
    // reset
    constants.configuration.environment = prevEnv;
  });

  // 🟢 1 - Email Input Required
  it('Expect email helper text to display email required', async () => {
    // act
    render(<Login />);
    await fillTextInput('login_email', '');
    // assert
    const helperText = screen.queryByTestId('login_email-helper-text');
    expect(helperText).toHaveTextContent('Email address is required');
  });
  // 🟢 2 - Email Input Invalid
  it('Expect email helper text to display email invalid', async () => {
    // act
    render(<Login />);
    await fillTextInput('login_email', 'not_an_email');
    // assert
    const helperText = screen.queryByTestId('login_email-helper-text');
    expect(helperText).toHaveTextContent('Please enter valid email address');
  });
  // 🟢 3 - Email Input Valid
  it('Expect email helper text to display nothing', async () => {
    // act
    render(<Login />);
    await fillTextInput('login_email', 'name@test.com');
    // assert
    const helperText = screen.queryByTestId('login_email-helper-text');
    expect(helperText).toHaveTextContent('');
  });

  // 🟢 4 - Password Input Required
  it('Expect password helper text to display password required', async () => {
    // act
    render(<Login />);
    await fillTextInput('login_password', '');
    // assert
    const helperText = screen.queryByTestId('login_password-helper-text');
    expect(helperText).toHaveTextContent('Password is required');
  });
  // 🟢 5 - Password Input Valid
  it('Expect password helper text to display nothing', async () => {
    // act
    render(<Login />);
    await fillTextInput('login_password', 'testpassword');
    // assert
    const helperText = screen.queryByTestId('login_password-helper-text');
    expect(helperText).toHaveTextContent('');
  });

  // 🟢 6 - Submit login success
  it('Expect login failure container not to be rendered when login response is good', async () => {
    // arrange
    mocks.apiData = { ...mockLoginSuccess };
    // act
    render(<Login />);
    await fillTextInput('login_email', 'name@test.com');
    await fillTextInput('login_password', 'testpassword');
    await fireEvent('click', 'login_submit');
    const errorContainer = screen.queryByTestId('login_error-container');
    // assert
    expect(errorContainer).not.toBeInTheDocument();
  });
  // 🟢 7 - Submit login fail
  it('Expect login failure container and then close it', async () => {
    // arrange
    mocks.apiData = { ...mockLoginFail };
    // act 1
    render(<Login />);
    await fillTextInput('login_email', 'name@test.com');
    await fillTextInput('login_password', 'testpassword');
    await fireEvent('click', 'login_submit');
    const errorContainer = screen.queryByTestId('login_error-container');
    // assert 1
    expect(errorContainer).toBeInTheDocument();
    // act 2
    await fireEvent('click', 'login_error-close-button');
    // assert 2
    expect(errorContainer).not.toBeInTheDocument();
  });
  // 🟢 8 - Submit login exception
  it('Expect login failure container not to be rendered with exception', async () => {
    // arrange
    mocks.apiError = true;
    mocks.apiData = { ...mockLoginFail };
    // act
    render(<Login />);
    await fillTextInput('login_email', 'name@test.com');
    await fillTextInput('login_password', 'testpassword');
    await fireEvent('click', 'login_submit');
    const errorContainer = screen.queryByTestId('login_error-container');
    // assert
    expect(errorContainer).not.toBeInTheDocument();
  });
  // 🟢 9 - Okta sign in
  it('Expect Okta sign in button is clicked', async () => {
    // act
    render(<Login />);
    await fireEvent('click', 'login_okta-button');
    // assert
    expect(mocks.oktaSignIn).toBeCalled();
  });
});
