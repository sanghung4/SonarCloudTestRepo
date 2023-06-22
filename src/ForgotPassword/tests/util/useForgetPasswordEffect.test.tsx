import { OktaAuth } from '@okta/okta-auth-js';
import { act } from '@testing-library/react';

import { AuthContext } from 'AuthProvider';
import { mockAuthContext } from 'AuthProvider.mocks';
import { AuthParam } from 'ForgotPassword';
import {
  mockAuthParam,
  mockUseForgetPasswordEffectProps
} from 'ForgotPassword/tests/mocks';
import useForgetPasswordEffect from 'ForgotPassword/util/useForgetPasswordEffect';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mockRecoveryToken = 'test123';
const mockQueryParam: [
  AuthParam,
  (obj: AuthParam, path?: string | undefined) => void
] = [{ ...mockAuthParam }, jest.fn()];
const mocks = {
  auth: { ...mockAuthContext },
  queryParams: mockQueryParam,
  props: { ...mockUseForgetPasswordEffectProps },
  pushAlert: jest.fn()
};

/**
 * Mock methods
 */
jest.mock('hooks/useSearchParam', () => ({
  ...jest.requireActual('hooks/useSearchParam'),
  useQueryParams: () => mocks.queryParams
}));
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

/**
 * Setup
 */
function setup(m: typeof mocks) {
  function MockComponent() {
    useForgetPasswordEffect(m.props);
    return null;
  }
  render(
    <AuthContext.Provider value={m.auth}>
      <MockComponent />
    </AuthContext.Provider>
  );
}

/**
 * TEST
 */
describe('ForgetPassword - util/useForgetPasswordEffect', () => {
  afterEach(() => {
    mocks.auth = { ...mockAuthContext };
    mocks.queryParams = mockQueryParam;
    mocks.props = { ...mockUseForgetPasswordEffectProps };
    mocks.pushAlert = jest.fn();
  });

  it('Expect nothing to happen when recoveryToken is undefined', async () => {
    setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.props.setEmail).not.toBeCalled();
    expect(mocks.props.setTransaction).not.toBeCalled();
    expect(mocks.queryParams[1]).not.toBeCalled();
  });

  it('Expect nothing to happen when verifyRecoveryToken is undefined', async () => {
    mocks.queryParams[0].recoveryToken = mockRecoveryToken;
    setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.props.setEmail).not.toBeCalled();
    expect(mocks.props.setTransaction).not.toBeCalled();
    expect(mocks.queryParams[1]).not.toBeCalled();
  });

  it('Expect to call setEmail and setTransaction when response status is "PASSWORD_RESET"', async () => {
    mocks.queryParams[0].recoveryToken = mockRecoveryToken;
    const mockRes = {
      status: 'PASSWORD_RESET',
      user: { profile: { login: 'test@gmail.com' } }
    };
    mocks.auth.oktaAuth = {
      verifyRecoveryToken: () => mockRes
    } as unknown as OktaAuth;
    setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.props.setEmail).toBeCalledWith(mockRes.user.profile.login);
    expect(mocks.props.setTransaction).toBeCalledWith(mockRes);
  });

  it('Expect to call setQueryParams and pushAlert when okta function trigger an error', async () => {
    mocks.queryParams[0].recoveryToken = mockRecoveryToken;
    const mockErrorMsg = 'test error';
    mocks.auth.oktaAuth = {
      verifyRecoveryToken: () => {
        throw new Error(mockErrorMsg);
      }
    } as unknown as OktaAuth;
    setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.queryParams[1]).toBeCalledWith({});
    expect(mocks.pushAlert).toBeCalledWith(mockErrorMsg, { variant: 'error' });
  });
});
