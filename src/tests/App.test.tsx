import { OktaAuth } from '@okta/okta-auth-js';
import { Security } from '@okta/okta-react';
import {
  OnAuthRequiredFunction,
  RestoreOriginalUriFunction
} from '@okta/okta-react/bundles/types/OktaContext';
import { WrapperProps } from '@reece/global-types';
import { screen } from '@testing-library/react';
import { useNavigate } from 'react-router-dom';

import App from 'App';
import { render } from 'test-util/render';
import { fireEvent } from 'test-util';

/**
 * Types
 */
type Mocks = {
  accessToken?: string;
  navigate: jest.Mock;
};
type MockOktaSecurityProps = {
  oktaAuth: OktaAuth;
  onAuthRequired: OnAuthRequiredFunction;
  restoreOriginalUri: RestoreOriginalUriFunction;
  children?: React.ReactNode;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  navigate: jest.fn()
};
let mocks = { ...defaultMocks };
let consoleErrorBackup = global.console.error;
const mockAuth = { isEmployee: true, isVerified: true };
const mockToken = window.btoa(JSON.stringify(mockAuth));
const mockTokenInvalid = window.btoa(
  JSON.stringify({ ...mockAuth, isVerified: false })
);

/**
 * Mock methods
 */
jest.mock('@okta/okta-react', () => ({
  ...jest.requireActual('@okta/okta-react'),
  Security: jest.fn()
}));
jest.mock('providers/AuthProvider', () => ({
  ...jest.requireActual('providers/AuthProvider'),
  __esModule: true,
  default: ({ children }: WrapperProps) => (
    <div data-testid="auth-provider">{children}</div>
  )
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn()
}));
jest.mock('Routes', () => ({
  ...jest.requireActual('Routes'),
  __esModule: true,
  default: () => <div data-testid="routes" />
}));

/**
 * TEST
 */
describe('App', () => {
  // ⚪ Set mocks
  beforeEach(() => {
    consoleErrorBackup = global.console.error;
    global.console.error = jest.fn();
  });
  // ⚪ Reset mocks
  afterEach(() => {
    global.console.error = consoleErrorBackup;
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock functions
  beforeEach(() => {
    // 🔹 useNavigate hook
    (useNavigate as jest.Mock).mockImplementation(() => mocks.navigate);
    // 🔹 Security component
    (Security as jest.Mock).mockImplementation(
      (props: MockOktaSecurityProps) => {
        // OktaAuth setup
        const getAccessToken = () => mocks.accessToken;
        const oktaAuth = { getAccessToken } as OktaAuth;
        const onAuth = () => props.onAuthRequired(oktaAuth);
        const onRestore = () => props.restoreOriginalUri(oktaAuth, '');
        // Render
        return (
          <>
            <button onClick={onAuth} data-testid="okta-auth-req" />
            <button onClick={onRestore} data-testid="okta-restore" />
            {props.children}
          </>
        );
      }
    );
  });

  // 🟢 1 - default
  it('Expect App contents to be rendered', () => {
    // act
    render(<App />);
    const routes = screen.queryByTestId('routes');
    // assert
    expect(routes).toBeInTheDocument();
  });

  // 🟢 2 - onAuthRequired
  it('Expect onAuthRequired to call navigate', async () => {
    // act
    render(<App />);
    await fireEvent('click', 'okta-auth-req');
    // assert
    expect(mocks.navigate).toBeCalledWith('/login');
  });

  // 🟢 3 - restoreOriginalUri undefined
  it('Expect restoreOriginalUri to call navigate under undefined token', async () => {
    // act
    render(<App />);
    await fireEvent('click', 'okta-restore');
    // assert
    expect(mocks.navigate).toBeCalledWith('/login', { replace: true });
  });

  // 🟢 4 - restoreOriginalUri invalid token
  it('Expect restoreOriginalUri NOT to call navigate under invalid token', async () => {
    // arrange
    mocks.accessToken = 'invalid';
    // act
    render(<App />);
    await fireEvent('click', 'okta-restore');
    // assert
    expect(mocks.navigate).not.toBeCalled();
    expect(global.console.error).toBeCalled();
  });

  // 🟢 5 - restoreOriginalUri incorrect token position
  it('Expect restoreOriginalUri NOT to call navigate under incorrect token position', async () => {
    // arrange
    mocks.accessToken = `${mockToken}.`;
    // act
    render(<App />);
    await fireEvent('click', 'okta-restore');
    // assert
    expect(mocks.navigate).not.toBeCalled();
    expect(global.console.error).toBeCalled();
  });

  // 🟢 6 - restoreOriginalUri valid token
  it('Expect restoreOriginalUri to call navigate under valid token', async () => {
    // arrange
    mocks.accessToken = `.${mockToken}`;
    // act
    render(<App />);
    await fireEvent('click', 'okta-restore');
    // assert
    expect(mocks.navigate).toBeCalledWith('/', { replace: true });
  });

  // 🟢 7 - restoreOriginalUri unauthorized token
  it('Expect restoreOriginalUri to call navigate under valid unauthorized token', async () => {
    // arrange
    mocks.accessToken = `.${mockTokenInvalid}`;
    // act
    render(<App />);
    await fireEvent('click', 'okta-restore');
    // assert
    expect(mocks.navigate).toBeCalledWith('/login', { replace: true });
  });
});
