import { screen } from '@testing-library/react';
import { useLocation, useNavigate } from 'react-router-dom';

import Sidebar from 'common/Sidebar';
import { menuItems } from 'common/Sidebar/Menu';
import { AuthContextType, defaultAuthContext } from 'providers/AuthProvider';
import { fireEvent, render } from 'test-util';

/**
 * Types
 */
type Mocks = {
  authContext: AuthContextType;
  navigate: jest.Mock;
  pathname: string;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  pathname: '',
  navigate: jest.fn(),
  authContext: { ...defaultAuthContext }
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: jest.fn(),
  useNavigate: jest.fn()
}));
jest.mock('@okta/okta-react', () => ({
  ...jest.requireActual('@okta/okta-react'),
  useOktaAuth: () => ({ oktaAuth: { signOut: jest.fn() } })
}));

/**
 * Test setup
 */
function setup(m: Mocks) {
  render(
    <Sidebar>
      <div data-testid="child" />
    </Sidebar>,
    { authContext: m.authContext }
  );
}

/**
 * TEST
 */
describe('common/Sidebar', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock methods
  beforeEach(() => {
    // 🔹 useLocation hook
    (useLocation as jest.Mock).mockImplementation(() => ({
      pathname: mocks.pathname
    }));
    // 🔹 useNavigate hook
    (useNavigate as jest.Mock).mockImplementation(() => mocks.navigate);
  });

  // 🟢 1 - NOT rendered
  it('Expect the sidebar component NOT to be rendered when session is invalid', () => {
    // arrange
    mocks.authContext.sessionId = null;
    // act
    setup(mocks);
    const sidebarContainer = screen.queryByTestId('sidebar_container');
    const child = screen.queryByTestId('child');
    // assert
    expect(sidebarContainer).not.toBeInTheDocument();
    expect(child).toBeInTheDocument();
  });

  // 🟢 2 - NOT rendered (/Login)
  it('Expect the sidebar component NOT to be rendered in the login page', () => {
    // arrange
    mocks.authContext.sessionId = 'TEST_SESSION_ID';
    mocks.pathname = '/login';
    // act
    setup(mocks);
    const sidebarContainer = screen.queryByTestId('sidebar_container');
    const child = screen.queryByTestId('child');
    // assert
    expect(sidebarContainer).not.toBeInTheDocument();
    expect(child).toBeInTheDocument();
  });

  // 🟢 3 - rendered
  it('Expect the sidebar component to be rendered', () => {
    // arrange
    mocks.authContext.sessionId = 'TEST_SESSION_ID';
    mocks.pathname = '/';
    // act
    setup(mocks);
    const sidebarContainer = screen.queryByTestId('sidebar_container');
    const child = screen.queryByTestId('child');
    // assert
    expect(sidebarContainer).toBeInTheDocument();
    expect(child).toBeInTheDocument();
  });

  // 🟢 4 - Navigate
  it('Expect navigate to be called when sidebar menu is clicked', async () => {
    // arrange
    mocks.authContext.sessionId = 'TEST_SESSION_ID';
    mocks.pathname = '/';
    // act
    setup(mocks);
    await fireEvent('click', 'sidebar-menu_item0');
    // assert
    expect(mocks.navigate).toBeCalledWith(menuItems[0].path);
  });
});
