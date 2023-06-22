import {
  ReactNode,
  createContext,
  useState,
  Dispatch,
  useContext,
  useEffect
} from 'react';

import {
  Branch,
  Cart,
  GetBranchQuery,
  MileRadiusEnum,
  Stock,
  useFindBranchesQuery,
  useGetBranchLazyQuery,
  useUpdateWillCallBranchMutation
} from 'generated/graphql';
import {
  defaultBranchContext,
  MAX_PAGES,
  PAGE_SIZE,
  VALID_ROUTES_WHEN_BRANCH_IS_MISSING
} from 'Branches/util/config';

import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { AuthContext } from 'AuthProvider';
import { useGeolocation } from 'hooks/useGeolocation';
import { useHistory, useLocation } from 'react-router-dom';
import { ErrorState, ErrorTypes } from 'common/ErrorBoundary/ErrorComponent';
import { useCartContext } from './CartProvider';

export enum Divisions {
  NONE = 'none',
  PLUMBING = 'plumbing',
  HVAC = 'hvac',
  WATERWORKS = 'waterworks',
  BANDK = 'bandk'
}

export type BranchContextType = {
  homeBranch?: Branch;
  homeBranchLoading: boolean;
  homeBranchError: string;
  shippingBranch?: Branch;
  shippingBranchLoading: boolean;
  nearbyBranches?: Branch[];
  nearbyBranchesLoading: boolean;
  branchSelectOpen: boolean;
  division: string;
  productId?: string;
  stock?: Stock; // TODO: remove
  isLocationDistance?: boolean;
  setBranchSelectOpen: Dispatch<boolean>;
  setStock: Dispatch<Stock | undefined>; // TODO: remove
  setShippingBranch: (branchId?: string) => void;
  setDivision: Dispatch<Divisions>;
  setProductId: Dispatch<string | undefined>;
};

export const BranchContext =
  createContext<BranchContextType>(defaultBranchContext);

function BranchProvider({ children }: { children: ReactNode }) {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const location = useLocation<ErrorState>();
  const isOnErrorPage = location.pathname === '/error';

  /**
   * State
   */
  const [branchSelectOpen, setBranchSelectOpen] = useState(false);
  const [stock, setStock] = useState<Stock>();
  const [division, setDivision] = useState<Divisions>(Divisions.NONE);
  const [homeBranch, setHomeBranch] = useState<Branch>();
  const [isLocationDistance, setIsLocationDistance] = useState(false);
  const [shippingBranch, setShippingBranch] = useState<Branch>();
  const [productId, setProductId] = useState<string>();

  /**
   * Context
   */
  const {
    cart,
    cartLoading,
    updateCartLoading,
    updateWillCallBranch,
    setSelectedBranch
  } = useCartContext();
  const { selectedAccounts, isMincron } = useSelectedAccountsContext();
  const { profile, authState } = useContext(AuthContext);

  const hasAccount = isMincron
    ? !selectedAccounts?.billToErpAccount
    : !selectedAccounts?.shipToErpAccount;

  /**
   * Data
   */
  // 🟣 Query - Get branch (by id)
  const [getBranch, { loading: getBranchLoading, error: getBranchError }] =
    useGetBranchLazyQuery({
      onCompleted: onBranchQueryCompleted,
      fetchPolicy: 'cache-and-network'
    });
  // 🟣 Geolocation
  const { position, positionLoading, positionError } = useGeolocation({
    skip: !branchSelectOpen && !getBranchError
  });

  // 🟣 Query - Find Nearby Branches
  const { data: nearbyBranchesQuery, loading: fetchNearbyBranchesLoading } =
    useFindBranchesQuery({
      skip:
        !(branchSelectOpen || (getBranchError && !positionError)) || hasAccount,
      variables: {
        branchSearch: {
          latitude: position?.coords?.latitude ?? homeBranch?.latitude,
          longitude: position?.coords?.longitude ?? homeBranch?.longitude,
          count: MAX_PAGES * PAGE_SIZE,
          isShoppable: true,
          branchSearchRadius: MileRadiusEnum.Miles_400,
          territory: isMincron
            ? selectedAccounts?.billToErpAccount?.territory
            : selectedAccounts?.shipToErpAccount?.territory
        }
      }
    });

  // 🟣 Mutation - Update WillCall Branch
  const [updateBranch, { loading: updateBranchLoading }] =
    useUpdateWillCallBranchMutation({
      refetchQueries: ['SearchProduct', 'GetProduct']
    });

  /**
   * Callbacks
   */
  // 🟤 CB - Set Shipping Branch
  function setShippingBranchOld(branchId?: string) {
    if (
      branchId &&
      branchId !== shippingBranch?.branchId &&
      profile?.userId &&
      selectedAccounts.shipTo?.id &&
      updateWillCallBranch
    ) {
      updateBranch({
        variables: {
          cartId: cart?.id ?? '',
          branchId,
          userId: profile.userId,
          shipToAccountId: selectedAccounts.shipTo.id
        },
        onCompleted: (res) => {
          updateWillCallBranch(res.updateWillCallBranch as Cart);
        }
      });

      setBranchSelectOpen(false);
    }
  }

  /**
   * Effects
   */
  // 🟡 Effect - Get branch by shippingBranchId
  useEffect(() => {
    if (authState?.isAuthenticated && selectedAccounts.shippingBranchId) {
      getBranch({ variables: { branchId: selectedAccounts.shippingBranchId } });
    } else {
      setHomeBranch(undefined);
    }
  }, [
    authState?.isAuthenticated,
    selectedAccounts.shippingBranchId,
    getBranch
  ]);

  // 🟡 Effect - Get Branch by cart shippingBranchId
  useEffect(() => {
    if (cart?.shippingBranchId) {
      getBranch({ variables: { branchId: cart.shippingBranchId } });
    } else {
      setShippingBranch(homeBranch);
      setSelectedBranch(homeBranch as Branch);
    }
  }, [
    cart?.shippingBranchId,
    getBranch,
    homeBranch,
    selectedAccounts.shippingBranchId,
    setSelectedBranch
  ]);

  // 🟡 Effect - Get Branch by billToErpAccount
  useEffect(() => {
    if (
      selectedAccounts.billToErpAccount?.branchId &&
      selectedAccounts.billToErpAccount?.branchId !== homeBranch?.branchId
    ) {
      getBranch({
        variables: { branchId: selectedAccounts.billToErpAccount?.branchId }
      });
    }
  }, [
    selectedAccounts.billToErpAccount?.branchId,
    homeBranch?.branchId,
    getBranch
  ]);

  // 🟡 Effect - adjust isLocationDistance
  useEffect(() => {
    if (navigator.geolocation && branchSelectOpen) {
      setIsLocationDistance(true);
    }
    if (positionError && branchSelectOpen) {
      setIsLocationDistance(false);
    }
  }, [setIsLocationDistance, positionError, branchSelectOpen]);

  // 🟡 Effect - Redirect Error to home
  useEffect(() => {
    setBranchSelectOpen(false);
    if (
      homeBranch &&
      isOnErrorPage &&
      location.state?.errorType === ErrorTypes.BRANCH_ERROR
    ) {
      history.push('/');
    }
  }, [
    history,
    homeBranch,
    isOnErrorPage,
    location.state?.errorType,
    setBranchSelectOpen
  ]);

  // 🟡 Effect - Redirect to error page
  useEffect(() => {
    const matchLocation = VALID_ROUTES_WHEN_BRANCH_IS_MISSING.includes(
      location.pathname
    );
    if (
      !matchLocation &&
      !isOnErrorPage &&
      getBranchError &&
      !homeBranch &&
      authState?.isAuthenticated
    ) {
      history.push('/error', { errorType: ErrorTypes.BRANCH_ERROR });
    }
  }, [
    history,
    homeBranch,
    getBranchError,
    isOnErrorPage,
    location.pathname,
    authState?.isAuthenticated
  ]);

  /**
   * Render
   */
  return (
    <BranchContext.Provider
      value={{
        homeBranch,
        homeBranchLoading: getBranchLoading,
        homeBranchError: getBranchError?.message ?? '',
        shippingBranch,
        shippingBranchLoading:
          getBranchLoading ||
          cartLoading ||
          updateBranchLoading ||
          !!updateCartLoading,
        nearbyBranches: nearbyBranchesQuery?.branchSearch.branches as Branch[],
        nearbyBranchesLoading:
          getBranchLoading || fetchNearbyBranchesLoading || positionLoading,
        branchSelectOpen,
        division,
        isLocationDistance,
        productId,
        stock,
        setBranchSelectOpen,
        setStock,
        setShippingBranch: setShippingBranchOld,
        setDivision,
        setProductId
      }}
    >
      {children}
    </BranchContext.Provider>
  );

  /**
   * GQP onCompleted Def
   */
  function onBranchQueryCompleted({ branch }: GetBranchQuery) {
    if (!authState?.isAuthenticated) {
      return;
    }
    /* 
    the if statements below have been modified to prevent the selected branch from rendering as the home branch and to 
    prevent the previously selected home branch from rendering as the current home branch when user stays logged in but
    swtiches between accounts
    */
    if (branch.branchId === selectedAccounts.billToErpAccount?.branchId) {
      setHomeBranch(branch as Branch);
    }

    if (
      branch.branchId === selectedAccounts.shippingBranchId ||
      cart?.shippingBranchId !== selectedAccounts.shippingBranchId
    ) {
      setShippingBranch(branch as Branch);
    }
  }
}

export default BranchProvider;
