import { ApolloError } from '@apollo/client';

import { UserContextType } from 'AuthProvider';
import { useDomainInfo } from 'hooks/useDomainInfo';
import {
  GetSelectedErpAccountQuery,
  GetSelectedErpAccountsQuery,
  useGetSelectedErpAccountLazyQuery,
  useGetSelectedErpAccountsLazyQuery
} from 'generated/graphql';
import SelectedAccountsProvider, {
  SelectedAccounts,
  SelectedAccountsContextType,
  useSelectedAccountsContext
} from 'providers/SelectedAccountsProvider';
import {
  GET_ERP_ACCOUNT_EMPTY,
  GET_ERP_ACCOUNT_RES,
  GET_ERP_ACCOUNTS_EMPTY,
  GET_ERP_ACCOUNTS_RES,
  mockSelectedAccounts,
  mockMincronSelectedAccounts
} from 'providers/tests/selectedAccounts.mocks';
import { dummyUserAccounts } from 'test-utils/dummyData';
import { mockLocalStorage } from 'test-utils/mockGlobals';
import { render } from 'test-utils/TestWrapper';
import { trackLogin } from 'utils/analytics';

/**
 * Types
 */
type Mocks = {
  authConfig?: Partial<UserContextType>;
  domainInfo: ReturnType<typeof useDomainInfo>;
  pathname: string;
  selectedAccounts: SelectedAccounts;
  useGetSelectedErpAccountLazyQuery: {
    data: GetSelectedErpAccountQuery;
    loading: boolean;
    error?: ApolloError;
  };
  useGetSelectedErpAccountsLazyQuery: {
    data: GetSelectedErpAccountsQuery;
    loading: boolean;
    error?: ApolloError;
  };
  trackLogin: jest.Mock;
};
type MockGQLProp<R> = {
  fetchPolicy?: string;
  onCompleted?: (data: R) => void;
  onError?: (error: ApolloError) => void;
  refetchWritePolicy?: string;
  skip?: boolean;
};

/**
 * Mock values
 */
const mockDomainInfo: ReturnType<typeof useDomainInfo> = {
  companyList: [],
  companyNameLink: '',
  companyNameList: '',
  brand: '',
  engine: '',
  isWaterworks: false,
  port: '',
  rootDomain: '',
  subdomain: '',
  salesforceLink: ''
};
const defaultMocks: Mocks = {
  authConfig: undefined,
  domainInfo: { ...mockDomainInfo },
  pathname: '',
  selectedAccounts: {},
  useGetSelectedErpAccountLazyQuery: {
    data: { ...GET_ERP_ACCOUNT_EMPTY },
    loading: false,
    error: undefined
  },
  useGetSelectedErpAccountsLazyQuery: {
    data: { ...GET_ERP_ACCOUNTS_EMPTY },
    loading: false,
    error: undefined
  },
  trackLogin: jest.fn()
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock methods
 */
jest.mock('@apollo/client', () => ({
  ...jest.requireActual('@apollo/client'),
  useReactiveVar: () => mocks.selectedAccounts
}));
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useGetSelectedErpAccountLazyQuery: jest.fn(),
  useGetSelectedErpAccountsLazyQuery: jest.fn()
}));
jest.mock('hooks/useDomainInfo', () => ({
  ...jest.requireActual('hooks/useDomainInfo'),
  useDomainInfo: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({ pathname: mocks.pathname })
}));
jest.mock('utils/analytics', () => ({
  ...jest.requireActual('utils/analytics'),
  trackLogin: jest.fn()
}));

/**
 * Setup
 */
function setup({ authConfig }: Mocks) {
  // Init output
  const selectedAccountContext = {} as SelectedAccountsContextType;

  // Create a dummy component to call the context
  function Component() {
    Object.assign(selectedAccountContext, useSelectedAccountsContext());
    return null;
  }

  // Render the dummy component wrapped with CartProvider
  render(
    <SelectedAccountsProvider>
      <Component />
    </SelectedAccountsProvider>,
    { authConfig }
  );

  return selectedAccountContext;
}
/**
 * TEST
 */
describe('provider/SelectAccountsProvider', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    localStorage.clear();
    mocks = { ...defaultMocks };
  });

  // 🔵 MOCK method implementations (these are in the same declaration order as the Provider)
  beforeEach(() => {
    // 🔵 fn - mock localStorage
    mockLocalStorage();
    // 🔵 Hook - useDomainInfo
    (useDomainInfo as jest.Mock).mockReturnValue(mocks.domainInfo);
    // 🔵 Hook - useGetSelectedErpAccountLazyQuery
    (useGetSelectedErpAccountLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<GetSelectedErpAccountQuery>) => {
        const { data, error, loading } =
          mocks.useGetSelectedErpAccountLazyQuery;
        const call = jest.fn(() => param.onCompleted?.(data));
        return [call, { loading, error }];
      }
    );
    // 🔵 Hook - useGetSelectedErpAccountsLazyQuery
    (useGetSelectedErpAccountsLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<GetSelectedErpAccountsQuery>) => {
        const { data, error, loading } =
          mocks.useGetSelectedErpAccountsLazyQuery;
        const call = jest.fn(() => param.onCompleted?.(data));
        return [call, { loading, error }];
      }
    );
    // 🔵 fn - trackLogin
    (trackLogin as jest.Mock).mockImplementation(mocks.trackLogin);
  });

  // 🟢 1 - Default
  it('Expect selectedAccount to match default values', () => {
    // arrange
    const defaultValues = { shipTo: { accountName: '' } };

    // act
    const { selectedAccounts } = setup(mocks);

    // assert
    expect(selectedAccounts).toMatchObject(defaultValues);
  });

  // 🟢 2 - useEffect (eclipse & shipTo & billTo) > getSelectedErpAccounts > !trackLogin
  it('Expect trackLogin not to be called', () => {
    // arrange
    mocks.selectedAccounts = { ...mockSelectedAccounts };

    // act
    setup(mocks);

    // assert
    expect(mocks.trackLogin).not.toBeCalled();
  });

  // 🟢 3 - useEffect (eclipse & shipTo & billTo) > getSelectedErpAccounts > trackLogin
  it('Expect trackLogin to be called when authenticated', () => {
    // arrange
    mocks.selectedAccounts = { ...mockSelectedAccounts };
    mocks.useGetSelectedErpAccountsLazyQuery.data = { ...GET_ERP_ACCOUNTS_RES };

    // act
    setup(mocks);

    // assert
    expect(mocks.trackLogin).toBeCalledWith({
      billTo: mocks.selectedAccounts.billTo?.erpAccountId,
      homeBranch: GET_ERP_ACCOUNTS_RES.billToAccount[0].branchId,
      authenticated: true,
      user: undefined,
      firstName: undefined,
      lastName: undefined,
      phoneNumber: undefined
    });
  });

  // 🟢 4 - useEffect (eclipse & shipTo & billTo & !auth) > getSelectedErpAccounts > trackLogin
  it('Expect trackLogin to be called when NOT authenticated', () => {
    // arrange
    mocks.authConfig = { authState: { isAuthenticated: false } };
    mocks.selectedAccounts = { ...mockSelectedAccounts };
    mocks.useGetSelectedErpAccountsLazyQuery.data = { ...GET_ERP_ACCOUNTS_RES };

    // act
    setup(mocks);

    // assert
    expect(mocks.trackLogin).toBeCalledWith({
      billTo: mocks.selectedAccounts.billTo?.erpAccountId,
      homeBranch: GET_ERP_ACCOUNTS_RES.billToAccount[0].branchId,
      authenticated: false,
      user: null,
      firstName: null,
      lastName: null,
      phoneNumber: undefined
    });
  });

  // 🟢 5 - useEffect (mincron & shipTo & billTo) > getSelectedErpAccounts > !trackLogin
  it('Expect trackLogin not to be called as mincron', () => {
    // arrange
    mocks.selectedAccounts = { ...mockSelectedAccounts };
    mocks.domainInfo.isWaterworks = true;

    // act
    setup(mocks);

    // assert
    expect(mocks.trackLogin).not.toBeCalled();
  });

  // 🟢 6 - useEffect (eclipse & shipTo & billTo) > getSelectedErpAccounts > trackLogin
  it('Expect trackLogin to be called as mincron', () => {
    // arrange
    mocks.selectedAccounts = { ...mockMincronSelectedAccounts };
    mocks.useGetSelectedErpAccountLazyQuery.data = { ...GET_ERP_ACCOUNT_RES };
    mocks.domainInfo.isWaterworks = true;

    // act
    setup(mocks);

    // assert
    expect(mocks.trackLogin).toBeCalledWith({
      billTo: mocks.selectedAccounts.billTo?.erpAccountId,
      homeBranch: GET_ERP_ACCOUNT_RES.account[0].branchId,
      authenticated: true,
      user: undefined,
      firstName: undefined,
      lastName: undefined,
      phoneNumber: undefined
    });
  });

  // 🟢 7 - updateAccount
  it('Expect truthy billTo and shipTo from localStorage changed by updateAccount', () => {
    // arrange
    const ecommAccount = { ...dummyUserAccounts[0], erpSystemName: null };
    mocks.selectedAccounts = { ...mockSelectedAccounts, billTo: undefined };

    // act
    const context = setup(mocks);
    context.updateAccounts(ecommAccount);

    // assert
    const selectedAccounts = JSON.parse(
      localStorage.getItem('selectedAccounts')!
    ) as SelectedAccounts;
    expect(selectedAccounts.billTo).toBeTruthy();
    expect(selectedAccounts.shipTo).toBeTruthy();
  });

  // 🟢 8 - clearAccounts
  it('Expect falsey billTo and shipTo from localStorage changed by clearAccount', () => {
    // arrange
    mocks.selectedAccounts = { ...mockSelectedAccounts };

    // act 1
    const context = setup(mocks);
    // assert 1
    const selectedAccountsBefore = JSON.parse(
      localStorage.getItem('selectedAccounts')!
    ) as SelectedAccounts;
    expect(selectedAccountsBefore.billTo).toBeTruthy();
    expect(selectedAccountsBefore.shipTo).toBeTruthy();

    // act 2
    context.clearAccounts();
    // assert 2
    const selectedAccounts = JSON.parse(
      localStorage.getItem('selectedAccounts')!
    ) as SelectedAccounts;
    expect(selectedAccounts.billTo).toBeFalsy();
    expect(selectedAccounts.shipTo).toBeFalsy();
  });
  // 🟢 9 - updateShippingBranchId
  it('Expect truthy shippingBranchId from localStorage changed by updateShippingBranchId', () => {
    // arrange
    mocks.selectedAccounts = {
      ...mockSelectedAccounts,
      shippingBranchId: undefined
    };

    // act 1
    const context = setup(mocks);
    // assert 1
    const selectedAccountsBefore = JSON.parse(
      localStorage.getItem('selectedAccounts')!
    ) as SelectedAccounts;
    expect(selectedAccountsBefore.shippingBranchId).toBeFalsy();

    // act 2
    context.updateShippingBranchId('test');
    // assert 2
    const selectedAccounts = JSON.parse(
      localStorage.getItem('selectedAccounts')!
    ) as SelectedAccounts;
    expect(selectedAccounts.shippingBranchId).toBeTruthy();
  });
});
