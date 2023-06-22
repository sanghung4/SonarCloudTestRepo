import { OktaAuth } from '@okta/okta-auth-js';

import useForgetPasswordData, {
  onComplete,
  OnCompleteProps,
  onError,
  OnErrorProps
} from 'ForgotPassword/util/useForgetPasswordData';
import { mockHistory } from 'test-utils/mockRouter';
import { render } from 'test-utils/TestWrapper';

const mockT = (i: string) => i;
const mockOnCompleteProps: OnCompleteProps = {
  email: '',
  history: { ...mockHistory },
  sendInviteEmail: jest.fn()
};

const mockOnErrorProps: OnErrorProps = {
  email: '',
  pushAlert: jest.fn(),
  setRequestSent: jest.fn(),
  t: mockT
};

describe('ForgetPassword - util/useForgetPasswordData', () => {
  afterEach(() => {
    mockOnCompleteProps.email = '';
    mockOnCompleteProps.history = { ...mockHistory };
    mockOnCompleteProps.sendInviteEmail = jest.fn();
    mockOnErrorProps.email = '';
    mockOnErrorProps.pushAlert = jest.fn();
    mockOnErrorProps.setRequestSent = jest.fn();
    mockOnErrorProps.t = mockT;
  });

  it('expect to match snapshot for the hook', () => {
    const data = {};
    function MockComponent() {
      Object.assign(data, useForgetPasswordData());
      return null;
    }
    render(<MockComponent />);
    expect(data).toMatchSnapshot();
  });

  it('expect onComplete to to call some functions when response is false', () => {
    onComplete(mockOnCompleteProps)({ invitedUserEmailSent: false });
    expect(mockOnCompleteProps.sendInviteEmail).toBeCalledWith({
      variables: { legacyUserEmail: '' }
    });
    expect(mockOnCompleteProps.history.replace).toBeCalledWith({
      pathname: '/max-welcome',
      state: { email: '' }
    });
  });
  it('expect onComplete to to call some functions when response is true', () => {
    onComplete(mockOnCompleteProps)({ invitedUserEmailSent: true });
    expect(mockOnCompleteProps.sendInviteEmail).not.toBeCalled();
    expect(mockOnCompleteProps.history.replace).toBeCalledWith({
      pathname: '/max-welcome',
      state: { email: '' }
    });
  });

  it('expect onError to to call some functions when no failure', () => {
    onError(mockOnErrorProps)();
    expect(mockOnErrorProps.setRequestSent).toBeCalledWith(true);
    expect(mockOnErrorProps.pushAlert).toBeCalledWith(
      'forgotPassword.resetSent',
      { variant: 'info' }
    );
  });
  it('expect onError to to call some functions with failure', () => {
    mockOnErrorProps.oktaAuth = {
      forgotPassword: jest.fn(() => {
        throw new Error();
      })
    } as unknown as OktaAuth;
    onError(mockOnErrorProps)();
    expect(mockOnErrorProps.oktaAuth?.forgotPassword).toBeCalledWith({
      username: '',
      factorType: 'EMAIL'
    });
    expect(mockOnErrorProps.setRequestSent).not.toBeCalledWith();
    expect(mockOnErrorProps.pushAlert).toBeCalledWith(
      'forgotPassword.unableToReset',
      { variant: 'error' }
    );
  });
});
