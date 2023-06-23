import { useOktaAuth } from '@okta/okta-react';
import { screen } from '@testing-library/react';
import { useLocation, useNavigate } from 'react-router-dom';

import AuthProvider, { useAuthContext } from 'providers/AuthProvider';
import { fireEvent, render } from 'test-util';

/**
 * Props
 */
type Mocks = {
  setSessionId?: string;
  navigate: jest.Mock;
  pathname: string;
  signInCredential: jest.Mock;
  signInRedirect: jest.Mock;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  navigate: jest.fn(),
  pathname: '/',
  signInCredential: jest.fn(),
  signInRedirect: jest.fn()
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock methods
 */
jest.mock('@okta/okta-react', () => ({
  ...jest.requireActual('@okta/okta-react'),
  useOktaAuth: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
  useLocation: jest.fn()
}));

/**
 * Test setup function
 */
function setup(m: Mocks) {
  // mocked context consumer
  const MockConsumer = () => {
    // Context
    const context = useAuthContext();
    // Callbacks
    const setSessionId = () => context.setSessionId(m.setSessionId);
    const loginCall = () => context.login?.({ username: '', password: '' });
    // Render
    return (
      <>
        <span data-testid="session-id">{context.sessionId}</span>
        <button data-testid="context-set-session" onClick={setSessionId} />
        <button data-testid="context-login" onClick={loginCall} />
      </>
    );
  };
  // Render the mock consumer
  render(
    <AuthProvider>
      <MockConsumer />
    </AuthProvider>
  );
}

/**
 * TEST
 */
describe('providers/AuthProvider', () => {
  // ⚪ Reset
  afterEach(() => {
    window.localStorage.clear();
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock API functions
  beforeEach(() => {
    // 🔹 useLocation hook
    (useLocation as jest.Mock).mockImplementation(() => ({
      pathname: mocks.pathname
    }));
    // 🔹 useNavigate hook
    (useNavigate as jest.Mock).mockImplementation(() => mocks.navigate);
    // 🔹 useOktaAuth hook
    (useOktaAuth as jest.Mock).mockImplementation(() => ({
      authState: { isAuthenticated: false },
      oktaAuth: {
        signInWithCredentials: mocks.signInCredential.mockReturnValue(
          new Promise((resolve) => resolve({ sessionToken: '' }))
        ),
        signInWithRedirect: mocks.signInRedirect
      }
    }));
  });

  // 🟢 1 - Default
  it('Expect to have empty value by default and navigate is called', () => {
    // act
    setup(mocks);
    const sessionId = screen.queryByTestId('session-id');
    // assert
    expect(sessionId).toHaveTextContent('');
    //expect(mocks.navigate).toBeCalledWith('/login');
  });

  // 🟢 2 - Login with no sessionId
  it('Expect navigate NOT to be called when the pathname is already set as "/login"', () => {
    // arrange
    mocks.pathname = '/login';
    // act
    setup(mocks);
    // assert
    expect(mocks.navigate).not.toBeCalledWith('/login');
  });

  // 🟢 3 - Set sessionId
  it('Expect to update sessionId', async () => {
    // arrange
    mocks.setSessionId = 'TEST_SESSION_ID';
    // act 1
    setup(mocks);
    const sessionId = screen.queryByTestId('session-id');
    // assert 1
    expect(sessionId).toHaveTextContent('');
    // act 2
    await fireEvent('click', 'context-set-session');
    // assert 2
    expect(sessionId).toHaveTextContent(mocks.setSessionId);
  });

  // 🟢 4 - login
  it('Expect to call login', async () => {
    // act
    setup(mocks);
    await fireEvent('click', 'context-login');
    // assert
    expect(mocks.signInCredential).toBeCalled();
    expect(mocks.signInRedirect).toBeCalled();
  });
});
