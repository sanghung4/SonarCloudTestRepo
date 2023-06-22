import { AuthContext } from 'AuthProvider';
import { mockAuthContext } from 'AuthProvider.mocks';
import { mockSignInFormHook } from 'Home/tests/mocks';
import useHomeSignInForm from 'Home/util/useHomeSignInForm';
import { mockHistory } from 'test-utils/mockRouter';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mockData = {
  username: 'farmer.macjoy@yahoo.com',
  password: 'eIeI0!'
};
const mocks = {
  auth: { ...mockAuthContext },
  history: { ...mockHistory },
  isInviteSentQuery: jest.fn(),
  pushAlert: jest.fn(),
  sendInviteEmail: jest.fn()
};

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));
jest.mock('react-i18next', () => ({
  ...jest.requireActual('react-i18next'),
  useTranslation: () => ({ t: (t: string) => t })
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mocks.history
}));
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useInvitedUserEmailSentLazyQuery: () => [mocks.isInviteSentQuery],
  useResendLegacyInviteEmailMutation: () => [mocks.sendInviteEmail]
}));

/**
 * Setup function
 */
function setup(m: typeof mocks) {
  const output = { ...mockSignInFormHook };
  function MockCompoment() {
    Object.assign(output, useHomeSignInForm());
    return null;
  }
  render(
    <AuthContext.Provider value={m.auth}>
      <MockCompoment />
    </AuthContext.Provider>
  );
  return output;
}

/**
 * TEST
 */
describe('Home - util/useHomeSignInForm', () => {
  afterEach(() => {
    mocks.auth = { ...mockAuthContext };
    mocks.history = { ...mockHistory };
    mocks.isInviteSentQuery = jest.fn();
    mocks.pushAlert = jest.fn();
    mocks.sendInviteEmail = jest.fn();
  });

  it('Expect hook output to match snapshot', () => {
    const result = setup(mocks);
    expect(result).toMatchSnapshot();
  });

  it('Expect onComplete to call replace & sendInviteEmail when res is false', () => {
    setup(mocks).onCompleted({ invitedUserEmailSent: false });
    expect(mocks.sendInviteEmail).toBeCalledWith({
      variables: { legacyUserEmail: '' }
    });
    expect(mocks.history.replace).toBeCalledWith({
      pathname: '/max-welcome',
      state: { email: '' }
    });
  });
  it('Expect onComplete to call replace but no sendInviteEmail when res is true', () => {
    setup(mocks).onCompleted({ invitedUserEmailSent: true });
    expect(mocks.sendInviteEmail).not.toBeCalled();
    expect(mocks.history.replace).toBeCalledWith({
      pathname: '/max-welcome',
      state: { email: '' }
    });
  });

  it('Expect onError to call pushAlert', () => {
    setup(mocks).onError();
    expect(mocks.pushAlert).toBeCalledWith('validation.incorrectCreds', {
      variant: 'error'
    });
  });

  it('Expect onSubmit to call nothing when login is undefined', async () => {
    mocks.auth.login = undefined;
    await setup(mocks).onSubmit(mockData);
    expect(mocks.isInviteSentQuery).not.toBeCalled();
    expect(mocks.pushAlert).not.toBeCalled();
  });
  it('Expect onSubmit to call login when login is a function', async () => {
    mocks.auth.login = jest.fn();
    await setup(mocks).onSubmit(mockData);
    expect(mocks.auth.login).toBeCalled();
    expect(mocks.isInviteSentQuery).not.toBeCalled();
    expect(mocks.pushAlert).not.toBeCalled();
  });
  it('Expect onSubmit to call some functions when login is a function that throws error', async () => {
    mocks.auth.login = jest.fn(() => {
      throw new Error();
    });
    await setup(mocks).onSubmit(mockData);
    expect(mocks.auth.login).toBeCalled();
    expect(mocks.isInviteSentQuery).toBeCalledWith({
      variables: { email: mockData.username }
    });
    expect(mocks.pushAlert).not.toBeCalled();
  });
  it('Expect onSubmit to call some functions when login and isInviteSentQuery are functions that throws error', async () => {
    mocks.auth.login = jest.fn(() => {
      throw new Error();
    });
    mocks.isInviteSentQuery = jest.fn(() => {
      throw new Error();
    });
    await setup(mocks).onSubmit(mockData);
    expect(mocks.auth.login).toBeCalled();
    expect(mocks.isInviteSentQuery).toBeCalledWith({
      variables: { email: mockData.username }
    });
    expect(mocks.pushAlert).toBeCalledWith('validation.incorrectCreds', {
      variant: 'error'
    });
  });
});
