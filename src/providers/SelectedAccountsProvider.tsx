import { createContext, useContext, useEffect, useMemo, useState } from 'react';

import { ApolloError, makeVar, useReactiveVar } from '@apollo/client';
import { noop } from 'lodash-es';
import { useLocation } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import SelectAccountsModal from 'common/SelectAccountsModal';
import {
  EcommAccount,
  ErpAccount,
  ErpSystemEnum,
  GetSelectedErpAccountQuery,
  GetSelectedErpAccountsQuery,
  useGetSelectedErpAccountLazyQuery,
  useGetSelectedErpAccountsLazyQuery
} from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { trackLogin } from 'utils/analytics';

/**
 * Types
 */
export type SelectedAccounts = {
  billTo?: EcommAccount;
  shipTo?: EcommAccount & {
    accountNumber?: string;
    accountName?: string;
  };
  billToErpAccount?: ErpAccount;
  shipToErpAccount?: ErpAccount;
  shippingBranchId?: string;
  erpSystemName?: ErpSystemEnum;
};

export type SelectedAccountsContextType = {
  selectedAccounts: SelectedAccounts;
  updateShippingBranchId: (branchId: string) => void;
  updateAccounts: (billTo?: EcommAccount, shipTo?: EcommAccount) => void;
  clearAccounts: () => void;
  loading: boolean;
  isMincron: boolean;
  isEclipse: boolean;
  error?: ApolloError;
};

type SelectedAccountsProviderProps = {
  debug?: string;
  children: React.ReactNode;
};

/**
 * Consts
 */
const allowedPaths = [
  'select-accounts',
  'terms-of-access',
  'privacy-policy',
  'terms-of-sale',
  'do-not-sell-my-info'
];

const selectedAccounts: Partial<SelectedAccounts> = JSON.parse(
  localStorage.getItem('selectedAccounts') || '{}'
);

export const selectedAccountsVar = makeVar<SelectedAccounts>({
  billTo: selectedAccounts.billTo,
  shipTo: selectedAccounts.shipTo,
  shippingBranchId: selectedAccounts.shippingBranchId,
  erpSystemName: selectedAccounts.erpSystemName
});

/**
 * Context
 */
export const SelectedAccountsContext =
  createContext<SelectedAccountsContextType>({
    selectedAccounts: {
      billTo: undefined,
      shipTo: undefined,
      shippingBranchId: undefined,
      erpSystemName: undefined
    },
    updateShippingBranchId: noop,
    updateAccounts: noop,
    clearAccounts: noop,
    loading: false,
    isMincron: false,
    isEclipse: false
  });

/**
 * Component
 */
function SelectedAccountsProvider({
  children,
  debug
}: SelectedAccountsProviderProps) {
  /**
   * Custom Hooks
   */
  const { brand, isWaterworks } = useDomainInfo();
  const selectedAccounts = useReactiveVar(selectedAccountsVar);
  const { pathname } = useLocation();

  /**
   * State
   */
  const [accountsModalOpen, setAccountsModalOpen] = useState(false);

  /**
   * Context
   */
  const { authState, ecommUser, firstName, lastName, user } =
    useContext(AuthContext);

  /**
   * Data
   */
  // 🟣 Query - Get selected erp account (singular)
  const [
    getSelectedErpAccount,
    { loading: getSelectedErpAccountLoading, error: getSelectedErpAccountError }
  ] = useGetSelectedErpAccountLazyQuery({
    fetchPolicy: 'cache-and-network',
    onCompleted: onSelectedErpAccountQueryCompleted
  });
  // 🟣 Query - Get selected erp accounts (plural)
  const [
    getSelectedErpAccounts,
    {
      loading: getSelectedErpAccountsLoading,
      error: getSelectedErpAccountsError
    }
  ] = useGetSelectedErpAccountsLazyQuery({
    fetchPolicy: 'cache-and-network',
    onCompleted: onSelectedErpAccountsQueryCompleted
  });

  /**
   * Memos
   */
  // 🔵 Memo -  shipToParsed
  const shipToParsed = useMemo(() => {
    const shipToDelimited = selectedAccounts.shipTo?.name?.split(' - ') ?? [];
    const accountName = shipToDelimited.join(' - ');
    const accountNumber = shipToDelimited.shift();
    return { accountNumber, accountName };
  }, [selectedAccounts.shipTo]);

  /**
   * Callbacks
   */
  // 🟤 CB - update accounts
  const updateAccounts = (billTo?: EcommAccount, shipTo?: EcommAccount) => {
    const updated = selectedAccountsVar({
      ...selectedAccounts,
      billTo,
      shipTo: shipTo ?? billTo,
      erpSystemName: billTo?.erpSystemName ?? undefined
    });
    storeSelectedAccounts(updated);
  };

  // 🟤 CB - clear accounts
  const clearAccounts = () => {
    selectedAccountsVar({});
    storeSelectedAccounts({});
  };

  // 🟤 CB - update shippingBranchId
  const updateShippingBranchId = (shippingBranchId: string) => {
    const updated = selectedAccountsVar({
      ...selectedAccounts,
      shippingBranchId
    });
    storeSelectedAccounts(updated);
  };

  /**
   * Effects
   */
  // 🟡 Check for selected accounts in required screens
  useEffect(() => {
    setAccountsModalOpen(
      !selectedAccounts.shipTo?.id &&
        !!authState?.isAuthenticated &&
        !allowedPaths.some((location) => pathname.includes(location))
    );
  }, [pathname, selectedAccounts.shipTo, authState]);

  // 🟡 Update erp accounts whenever bill to or ship to is changed
  useEffect(() => {
    const { billTo, shipTo } = selectedAccounts;

    // if it is waterworks, the ship to is the bill to
    if (isWaterworks && billTo?.id) {
      getSelectedErpAccount({
        variables: { accountId: billTo.id, brand }
      });
    }

    if (!isWaterworks && billTo?.id && shipTo?.id) {
      getSelectedErpAccounts({
        variables: { brand, billToId: billTo.id, shipToId: shipTo.id }
      });
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    brand,
    isWaterworks,
    selectedAccounts.billTo?.id,
    selectedAccounts.shipTo?.id
  ]);

  /**
   * Render
   */
  return (
    <SelectedAccountsContext.Provider
      value={{
        selectedAccounts: {
          ...selectedAccounts,
          shipTo: { ...selectedAccounts.shipTo, ...shipToParsed },
          erpSystemName: selectedAccounts.billTo?.erpSystemName ?? undefined
        },
        updateAccounts,
        clearAccounts,
        updateShippingBranchId,
        loading: getSelectedErpAccountLoading || getSelectedErpAccountsLoading,
        isMincron: selectedAccounts.erpSystemName === ErpSystemEnum.Mincron,
        isEclipse: selectedAccounts.erpSystemName === ErpSystemEnum.Eclipse,
        error: getSelectedErpAccountError || getSelectedErpAccountsError
      }}
    >
      <SelectAccountsModal open={accountsModalOpen} />
      {children}
    </SelectedAccountsContext.Provider>
  );

  /**
   * GQL Complete Defs
   */
  // 🟢 - get erp accounts (singular)
  function onSelectedErpAccountQueryCompleted(
    data: GetSelectedErpAccountQuery
  ) {
    const { billTo } = selectedAccounts;
    const account = data?.account?.find(
      ({ erpName }) => erpName === billTo?.erpSystemName
    );
    updateErpAccounts(account, account);
  }

  // 🟢 - get erp accounts (plural)
  function onSelectedErpAccountsQueryCompleted(
    data: GetSelectedErpAccountsQuery
  ) {
    const { billTo } = selectedAccounts;
    const billToErpAccount = data?.billToAccount?.find(
      ({ erpName }) => erpName === billTo?.erpSystemName
    );

    const shipToErpAccount = data?.shipToAccount?.find(
      ({ erpName }) => erpName === billTo?.erpSystemName
    );

    updateErpAccounts(billToErpAccount, shipToErpAccount);
  }

  /**
   * Util
   */
  // 🔣 util - store selectedAccounts to local storage
  function storeSelectedAccounts(data: SelectedAccounts) {
    localStorage.setItem('selectedAccounts', JSON.stringify(data));
  }

  // 🔣 util - onComplete update account logic
  function updateErpAccounts(
    billToErpAccount?: ErpAccount,
    shipToErpAccount?: ErpAccount
  ) {
    let updated = { ...selectedAccounts };

    if (billToErpAccount) {
      const isMincron = billToErpAccount.erp === ErpSystemEnum.Mincron;
      updated.billToErpAccount = billToErpAccount;

      if (isMincron) {
        updated.shippingBranchId = billToErpAccount.branchId ?? '';
      }

      trackLogin({
        billTo: updated.billTo?.erpAccountId,
        homeBranch: billToErpAccount.branchId,
        authenticated: authState?.isAuthenticated,
        user: authState?.isAuthenticated ? user?.email : null,
        firstName: authState?.isAuthenticated ? firstName : null,
        lastName: authState?.isAuthenticated ? lastName : null,
        phoneNumber: ecommUser?.phoneNumber
      });
    }

    if (shipToErpAccount) {
      const isMincron = shipToErpAccount?.erp === ErpSystemEnum.Mincron;
      updated.shipToErpAccount = shipToErpAccount;
      if (!isMincron) {
        updated.shippingBranchId = shipToErpAccount.branchId ?? '';
      }
    }

    storeSelectedAccounts(updated);
    selectedAccountsVar(updated);
  }
}

export const useSelectedAccountsContext = () =>
  useContext(SelectedAccountsContext);

export default SelectedAccountsProvider;
