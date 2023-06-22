import { ApolloError } from '@apollo/client';
import { UserContextType } from 'AuthProvider';
import { mockBranch } from 'Branches/tests/mocks';
import { mockCart } from 'Cart/tests/mocks';
import { ErrorState, ErrorTypes } from 'common/ErrorBoundary/ErrorComponent';
import {
  FindBranchesQuery,
  GetBranchQuery,
  UpdateWillCallBranchMutation,
  useFindBranchesQuery,
  useGetBranchLazyQuery,
  useUpdateWillCallBranchMutation
} from 'generated/graphql';
import { Location } from 'history';
import { IPositionState, useGeolocation } from 'hooks/useGeolocation';
import BranchProvider, {
  BranchContext,
  BranchContextType
} from 'providers/BranchProvider';
import { CartContextType } from 'providers/CartProvider';
import { SelectedAccountsContextType } from 'providers/SelectedAccountsProvider';
import {
  FIND_BRANCHES_EMPTY,
  GET_BRANCH_RES,
  UPDATE_WILL_CALL_RES
} from 'providers/tests/branches.mocks';
import { useContext } from 'react';
import { act } from 'react-dom/test-utils';
import { mockGeolocation } from 'test-utils/mockGlobals';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type Mocks = {
  authConfig?: Partial<UserContextType>;
  selectedAccountsConfig?: Partial<SelectedAccountsContextType>;
  cartConfig?: Partial<CartContextType>;
  historyPush: jest.Mock;
  location: Location<ErrorState>;
  useGetBranchLazyQuery: {
    data: GetBranchQuery;
    loading: boolean;
    error?: ApolloError;
  };
  useGeolocation: IPositionState;
  useFindBranchesQuery: {
    called: boolean;
    data: FindBranchesQuery;
    loading: boolean;
  };
  useUpdateWillCallBranchMutation: {
    data: UpdateWillCallBranchMutation;
    loading: boolean;
  };
};
type MockGQLProp<R> = {
  fetchPolicy?: string;
  onCompleted?: (data: R) => void;
  skip?: boolean;
};

/**
 * Mock Values
 */
const defaultMocks: Mocks = {
  authConfig: {},
  selectedAccountsConfig: {},
  historyPush: jest.fn(),
  location: { pathname: '/', search: '', state: {}, hash: '' },
  useGetBranchLazyQuery: {
    data: { ...GET_BRANCH_RES },
    loading: false
  },
  useGeolocation: {
    position: null,
    positionError: null,
    positionLoading: false,
    previousPositions: null
  },
  useFindBranchesQuery: {
    called: false,
    data: { ...FIND_BRANCHES_EMPTY },
    loading: false
  },
  useUpdateWillCallBranchMutation: {
    data: { ...UPDATE_WILL_CALL_RES },
    loading: false
  }
};
let mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useGetBranchLazyQuery: jest.fn(),
  useFindBranchesQuery: jest.fn(),
  useUpdateWillCallBranchMutation: jest.fn()
}));
jest.mock('hooks/useGeolocation', () => ({
  ...jest.requireActual('hooks/useGeolocation'),
  useGeolocation: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => mocks.location,
  useHistory: () => ({ push: mocks.historyPush })
}));

/**
 * Setup function
 */
function setup({ authConfig, cartConfig, selectedAccountsConfig }: Mocks) {
  // Init output
  const branchContext = {} as BranchContextType;

  // Create a dummy component to call the context
  function Component() {
    Object.assign(branchContext, useContext(BranchContext));
    return null;
  }

  // Render the dummy component wrapped with BranchProvider
  render(
    <BranchProvider>
      <Component />
    </BranchProvider>,
    { authConfig, cartConfig, selectedAccountsConfig }
  );

  return branchContext;
}

/**
 * TEST
 */
describe('provider/BranchProvider', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
    mocks.useGetBranchLazyQuery.error = undefined;
  });

  // 🔵 MOCK GQL/custom hooks (these are in the same declaration order as the Provider)
  beforeEach(() => {
    // 🔵 Hook - useGetBranchLazyQuery
    (useGetBranchLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<GetBranchQuery>) => {
        const { data, error, loading } = mocks.useGetBranchLazyQuery;
        const call = jest.fn(() => {
          if (!error) {
            param.onCompleted?.(data);
          }
        });
        return [call, { loading, error }];
      }
    );

    // 🔵 Hook - useGeolocation
    (useGeolocation as jest.Mock).mockImplementation(
      () => mocks.useGeolocation
    );

    // 🔵 Hook - useFindBranchesQuery
    (useFindBranchesQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<FindBranchesQuery>) => {
        let output = undefined;
        if (!param.skip && !mocks.useFindBranchesQuery.called) {
          mocks.useFindBranchesQuery.called = true;
          output = mocks.useFindBranchesQuery.data;
        }
        return { data: output, loading: mocks.useFindBranchesQuery.loading };
      }
    );

    // 🔵 Hook - useUpdateWillCallBranchMutation
    (useUpdateWillCallBranchMutation as jest.Mock).mockImplementation(() => {
      const call = jest.fn((p: MockGQLProp<UpdateWillCallBranchMutation>) =>
        p.onCompleted?.(mocks.useUpdateWillCallBranchMutation.data)
      );
      return [call, { loading: mocks.useUpdateWillCallBranchMutation.loading }];
    });
  });

  // 🟢 1 - Default (eclipse)
  it('Expect BranchProvider to be falsey with default values', () => {
    // Assign
    mocks.authConfig = { profile: undefined };
    const res = setup(mocks);

    // Assert
    expect(res.homeBranch).toBeFalsy();
    expect(res.shippingBranch).toBeFalsy();
  });

  // 🟢 2 - Default (mincron)
  it('Expect BranchProvider to be falsey with default mincron values', () => {
    // Assign
    mocks.authConfig = { profile: undefined };
    mocks.selectedAccountsConfig = { isMincron: true };
    const res = setup(mocks);

    // Assert
    expect(res.homeBranch).toBeFalsy();
    expect(res.shippingBranch).toBeFalsy();
  });

  // 🟢 3 - useEffect (isAuthenticated) -> getBranch -> falsey
  it('Expect falsey home and shipping branch with only isAuthenticated', () => {
    // Assign
    const selectedAccounts = { shippingBranchId: 'test' };
    mocks.authConfig = { authState: { isAuthenticated: true } };
    mocks.selectedAccountsConfig = { selectedAccounts };

    // Act
    const res = setup(mocks);

    // Assert
    expect(res.homeBranch).toBeFalsy();
    expect(res.shippingBranch).toBeFalsy();
  });

  // 🟢 4 - useEffect (isAuthenticated) -> getBranch -> truthy
  it('Expect truthy home and shipping branch with the matching data', () => {
    // Assign
    const selectedAccounts = {
      shippingBranchId: mockBranch.branchId,
      billToErpAccount: { branchId: mockBranch.branchId }
    };
    mocks.authConfig = { authState: { isAuthenticated: true } };
    mocks.selectedAccountsConfig = { selectedAccounts };

    // Act
    const res = setup(mocks);

    // Assert
    expect(res.homeBranch).toBeTruthy();
    expect(res.shippingBranch).toBeTruthy();
  });

  // 🟢 5 - useEffect (cart shippingBranchId) -> getBranch -> truthy
  it('Expect truthy home and shipping branch with the matching data and cart shipping id', () => {
    // Assign
    const selectedAccounts = {
      shippingBranchId: mockBranch.branchId,
      billToErpAccount: { branchId: mockBranch.branchId }
    };
    mocks.cartConfig = { cart: { ...mockCart, shippingBranchId: 'test' } };
    mocks.selectedAccountsConfig = { selectedAccounts };

    // Act
    const res = setup(mocks);

    // Assert
    expect(res.homeBranch).toBeTruthy();
    expect(res.shippingBranch).toBeTruthy();
  });

  // 🟢 6 - useEffect (cart shippingBranchId & !isAuthenticated) -> getBranch -> falsey
  it('Expect falsey home and shipping branch with the cart shipping id but not isAuthenticated', () => {
    // Assign
    const selectedAccounts = {
      shippingBranchId: mockBranch.branchId,
      billToErpAccount: { branchId: mockBranch.branchId }
    };
    mocks.cartConfig = { cart: { ...mockCart, shippingBranchId: 'test' } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.authConfig = { authState: { isAuthenticated: false } };

    // Act
    const res = setup(mocks);

    // Assert
    expect(res.homeBranch).toBeFalsy();
    expect(res.shippingBranch).toBeFalsy();
  });

  // 🟢 7 - useEffect (cart shippingBranchId & differemt) -> getBranch -> falsey
  it('Expect falsey home and shipping branch with the cart shipping id but different', () => {
    // Assign
    const selectedAccounts = {
      shippingBranchId: 'test',
      billToErpAccount: { branchId: 'test' }
    };
    mocks.cartConfig = { cart: { ...mockCart, shippingBranchId: 'test' } };
    mocks.selectedAccountsConfig = { selectedAccounts };

    // Act
    const res = setup(mocks);

    // Assert
    expect(res.homeBranch).toBeFalsy();
    expect(res.shippingBranch).toBeFalsy();
  });

  // 🟢 8 - useEffect (cart shippingBranchId) -> setIsLocationDistance -> true
  it('Expect truthy isLocationDistance with the matching data and cart shipping id', () => {
    // Assign
    mockGeolocation(0, 0);

    // Act
    const res = setup(mocks);
    act(() => {
      res.setBranchSelectOpen(true);
    });

    // Assert
    expect(res.isLocationDistance).toBeTruthy();
  });

  // 🟢 9 - useEffect (cart shippingBranchId) -> setIsLocationDistance -> true
  it('Expect falsey isLocationDistance with the matching data and cart shipping id', () => {
    // Assign
    mocks.useGeolocation.positionError = {};

    // Act
    const res = setup(mocks);
    act(() => {
      res.setBranchSelectOpen(true);
    });

    // Assert
    expect(res.isLocationDistance).toBeFalsy();
  });

  // 🟢 10 - useEffect (/error location) -> '/'
  it('Expect to redirect to root with BRANCH_ERROR data', () => {
    // Assign
    mocks.useGeolocation.positionError = {};
    const selectedAccounts = {
      shippingBranchId: mockBranch.branchId,
      billToErpAccount: { branchId: mockBranch.branchId }
    };
    mocks.cartConfig = { cart: { ...mockCart, shippingBranchId: 'test' } };
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.location.pathname = '/error';
    mocks.location.state = { errorType: ErrorTypes.BRANCH_ERROR };

    // Act
    setup(mocks);

    // Assert
    expect(mocks.historyPush).toBeCalledWith('/');
  });

  // 🟢 11 - useEffect (error) -> '/error'
  it('Expect to redirect to error page', () => {
    // Assign
    const selectedAccounts = {
      shippingBranchId: mockBranch.branchId,
      billToErpAccount: { branchId: mockBranch.branchId }
    };
    mocks.authConfig = { authState: { isAuthenticated: true } };
    mocks.location.pathname = '/';
    mocks.selectedAccountsConfig = { selectedAccounts };
    mocks.useGetBranchLazyQuery.error = new ApolloError({});

    // Act
    setup(mocks);

    // Assert
    expect(mocks.historyPush).toBeCalledWith('/error', {
      errorType: ErrorTypes.BRANCH_ERROR
    });
  });

  // 🟢 12 - setShippingBranch (undefined) -> nothing happens
  it('Expect falsey branches when branchSelectOpen is called with nothing', () => {
    // Act
    const res = setup(mocks);
    act(() => {
      res.setBranchSelectOpen(true);
    });
    act(() => {
      res.setShippingBranch();
    });

    // Assert
    expect(res.homeBranch).toBeFalsy();
    expect(res.shippingBranch).toBeFalsy();
  });

  // 🟢 13 - setShippingBranch -> updateWillCallBranch
  it('Expect updateWillCallBranch is called by branchSelectOpen with valid data', () => {
    // Arrange
    mocks.cartConfig = { updateWillCallBranch: jest.fn() };
    const selectedAccounts = {
      shippingBranchId: mockBranch.branchId,
      billToErpAccount: { branchId: mockBranch.branchId },
      shipTo: { id: 'test' }
    };
    mocks.location.pathname = '/';
    mocks.authConfig = { authState: { isAuthenticated: true } };
    mocks.selectedAccountsConfig = { selectedAccounts };

    // Act
    const res = setup(mocks);
    act(() => {
      res.setShippingBranch('1234');
    });

    // Assert 2
    expect(mocks.cartConfig?.updateWillCallBranch).toBeCalled();
  });
});
