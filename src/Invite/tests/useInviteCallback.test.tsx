import { AccountErpIdQuery } from 'generated/graphql';
import { mockInvite } from 'Invite/tests/mocks';
import useInviteCallback from 'Invite/util/useInviteCallback';
import * as t from 'locales/en/translation.json';
import { mockHistory } from 'test-utils/mockRouter';
import { render } from 'test-utils/TestWrapper';
import { defaultCompany } from 'hooks/utils/useDomainInfo';
import { mockSelectedAccounts } from 'hooks/tests/mocks/useSelectedAccounts.mocks';

/**
 * Mock values
 */
const mocks = {
  accounts: undefined as AccountErpIdQuery | undefined,
  getErpAccount: jest.fn(),
  history: { ...mockHistory },
  inviteUser: jest.fn(),
  pushAlert: jest.fn(),
  selectedAccounts: { ...mockSelectedAccounts }
};
const mockOutput: ReturnType<typeof useInviteCallback> = {
  inviteUserLoading: false,
  handleInviteUser: jest.fn()
};

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useAccountErpIdLazyQuery: () => [
    mocks.getErpAccount,
    { data: mocks.accounts }
  ],
  useInviteUserMutation: () => [mocks.inviteUser, { loading: false }]
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mocks.history
}));

/**
 * Test setup fn
 */
function setup() {
  const output = mockOutput;
  function MockComponent() {
    Object.assign(output, useInviteCallback());
    return null;
  }
  render(<MockComponent />, {
    selectedAccountsConfig: { selectedAccounts: mocks.selectedAccounts }
  });
  return output;
}

/**
 * TEST
 */
describe('Invite - util/useInviteCallback', () => {
  afterEach(() => {
    mocks.accounts = undefined;
    mocks.getErpAccount = jest.fn();
    mocks.history = { ...mockHistory };
    mocks.pushAlert = jest.fn();
    mocks.selectedAccounts = { ...mockSelectedAccounts };
  });

  it('Expect `handleInviteUser` to call nothing when there is no erpAccountData', () => {
    setup().handleInviteUser(mockInvite);
    expect(mocks.inviteUser).not.toBeCalled();
    expect(mocks.pushAlert).not.toBeCalled();
    expect(mocks.history.push).not.toBeCalled();
  });

  it('Expect `handleInviteUser` to call functions with erpAccountData', async () => {
    mocks.accounts = { account: [] };
    const { handleInviteUser } = setup();
    await handleInviteUser(mockInvite);
    expect(mocks.inviteUser).toBeCalled();
    expect(mocks.pushAlert).toBeCalledWith(t.user.userInviteSuccess, {
      variant: 'success'
    });
    expect(mocks.history.push).toBeCalledWith('/user-management');
  });

  it('Expect`handleInviteUser` to parse error object when calling', async () => {
    mocks.accounts = { account: [] };
    mocks.inviteUser = jest.fn(() => {
      throw new Error('{ "error": "test" }');
    });
    const { handleInviteUser } = setup();
    await handleInviteUser(mockInvite);
    expect(mocks.pushAlert).toBeCalledWith('test', { variant: 'error' });
  });

  it('Expect`handleInviteUser` to parse invalid error object when calling', async () => {
    mocks.accounts = { account: [] };
    mocks.inviteUser = jest.fn(() => {
      throw new Error('INVALID JSON');
    });
    const { handleInviteUser } = setup();
    await handleInviteUser(mockInvite);
    expect(mocks.pushAlert).toBeCalledWith(t.user.userInviteError, {
      variant: 'error'
    });
  });

  it('Expect effect to call `getErpAccount` when selected billTo account id is valid', () => {
    mocks.selectedAccounts.billTo = { id: 'test123' };
    setup();
    expect(mocks.getErpAccount).toBeCalledWith({
      variables: {
        accountId: mocks.selectedAccounts.billTo.id,
        brand: defaultCompany.list
      }
    });
  });
});
