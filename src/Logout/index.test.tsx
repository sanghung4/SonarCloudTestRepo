import { AuthContext } from 'AuthProvider';
import { mockAuthContext } from 'AuthProvider.mocks';
import Logout from 'Logout';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = { auth: { ...mockAuthContext } };
function MockRedirect(props: { to: string }) {
  return <div>{props.to}</div>;
}

/**
 * Mock methods
 */
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  Redirect: MockRedirect
}));

/**
 * Test setup function
 */
function setup(m: typeof mocks) {
  return render(
    <AuthContext.Provider value={m.auth}>
      <Logout />
    </AuthContext.Provider>
  );
}

/**
 * TEST
 */
describe('Logout', () => {
  afterEach(() => (mocks.auth = { ...mockAuthContext }));
  it('Expect to match snapshot when it is not isAuthenticated nor isLoggingOut', () => {
    mocks.auth.authState = null;
    mocks.auth.isLoggingOut = false;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot when it is isLoggingOut', () => {
    mocks.auth.authState = null;
    mocks.auth.isLoggingOut = true;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot when it is isAuthenticated', () => {
    mocks.auth.authState = { isAuthenticated: true };
    mocks.auth.isLoggingOut = false;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });
});
