import { AuthTransaction } from '@okta/okta-auth-js';
import { act } from '@testing-library/react';

import { onForgetPasswordFormSubmit } from 'ForgotPassword/util/useForgetPasswordForm';
import useOnCreatePassword from 'ForgotPassword/util/useOnCreatePassword';
import { mockHistory } from 'test-utils/mockRouter';

/**
 * Mock values
 */
const mockTransaction: AuthTransaction = {
  data: {},
  status: ''
};
const mocks = {
  history: { ...mockHistory },
  pushAlert: jest.fn(),
  transaction: { ...mockTransaction }
};

/**
 * Mock methods
 */
jest.mock('react', () => ({
  ...jest.requireActual('react'),
  useCallback: (fn: (_: string) => Promise<void>) => fn
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mocks.history
}));
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

/**
 * TEST
 */
describe('ForgetPassword - util', () => {
  afterEach(() => {
    mocks.history = { ...mockHistory };
    mocks.transaction = { ...mockTransaction };
    mocks.pushAlert = jest.fn();
  });

  it('Expect `useOnCreatePassword` to call historyPush', async () => {
    await useOnCreatePassword(mocks.transaction)('');
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.history.push).toBeCalledWith('/login');
  });

  it('Expect `useOnCreatePassword` to call pushAlert', async () => {
    const mockErrorMsg = 'test error';
    mocks.transaction.resetPassword = () => {
      throw new Error(mockErrorMsg);
    };
    await useOnCreatePassword(mocks.transaction)('');
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.pushAlert).toBeCalledWith(mockErrorMsg, { variant: 'error' });
  });

  it('Exoect `onForgetPasswordFormSubmit` to call its prop functions', () => {
    const fn = jest.fn();
    const props = { isInviteSentQuery: fn, setEmail: fn };
    onForgetPasswordFormSubmit(props)({ email: '' });
    expect(fn).toBeCalledTimes(2);
  });
});
