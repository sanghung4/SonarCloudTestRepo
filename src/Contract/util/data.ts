import { useMemo } from 'react';
import {
  AccountQuery,
  ContractDetails,
  ErpAccount,
  ErpSystemEnum,
  useAccountQuery,
  useGetContractDetailsQuery
} from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export default function useContractDetailsData(contractNumber: string) {
  /**
   * Custom Hooks
   */
  const { brand } = useDomainInfo();
  /**
   * Data
   */

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: contractDetailsQuery, loading: contratLoading } =
    useGetContractDetailsQuery({
      variables: {
        contractNumber,
        erpAccountId: selectedAccounts?.billTo?.erpAccountId ?? ''
      }
    });
  const { data: accountQuery, loading: accountLoading } = useAccountQuery({
    variables: {
      accountId: selectedAccounts.billTo?.id ?? '',
      brand
    }
  });

  /**
   * Memo/variables
   */
  const account = useMemo(() => accountMemo(accountQuery), [accountQuery]);
  const data = contractDetailsQuery?.contract as ContractDetails | undefined;
  const loading = contratLoading || accountLoading;

  // Return
  return { data, loading, account };
}

export function accountMemo(
  accountQuery: AccountQuery | undefined
): ErpAccount | undefined {
  const mincronAccounts = accountQuery?.account?.filter(
    (i) => i?.erpName === ErpSystemEnum.Mincron
  )[0];
  return mincronAccounts as ErpAccount | undefined;
}
