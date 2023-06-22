import { useMemo } from 'react';

import { ContractsParams } from 'Contracts';
import { Contract, useContractsQuery } from 'generated/graphql';
import { formatDate } from 'utils/dates';
import { sortDirectionString } from 'utils/tableUtils';
import { useQueryParams } from 'hooks/useSearchParam';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export default function useContractData() {
  /**
   * Custom hooks
   */
  const [queryParams, setQueryParams] = useQueryParams<ContractsParams>({
    arrayKeys: ['sortBy']
  });
  const {
    page = '1',
    searchBy = '',
    sortBy = ['lastReleaseDate'],
    from,
    to
  } = queryParams;
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Special Memo
   */
  const sortByObj = useMemo(() => sortByMemo(sortBy), [sortBy]);

  /**
   * Data
   */
  const {
    data: contractsQuery,
    loading,
    called,
    refetch
  } = useContractsQuery({
    skip: !selectedAccounts?.billTo?.erpAccountId,
    variables: {
      erpAccountId: selectedAccounts?.billTo?.erpAccountId!,
      pageNumber: page,
      searchFilter: searchBy,
      fromDate: formatDate(from),
      toDate: formatDate(to),
      sortOrder: sortByObj[0].id,
      sortDirection: sortDirectionString(sortByObj[0].direction)
    },
    onCompleted: () =>
      setQueryParams({
        ...queryParams,
        page: page
      }),
    fetchPolicy: 'cache-and-network'
  });

  /**
   * Memo
   */
  const data = useMemo(
    () => (contractsQuery?.contracts?.results ?? []) as Contract[],
    [contractsQuery]
  );

  /**
   * Output
   */
  return { contractsQuery, data, loading, called, refetch };
}

export function sortByMemo(sortBy: string[]) {
  /* 
    The name for the sorting fields in the Mincron program call don't include "date" for "firstRelease" and "lastRelease". t
    Therefore the conditional checks below are necessary.
    */
  return sortBy.map((s) => ({
    id: s.includes('lastReleaseDate')
      ? 'lastRelease'
      : s.includes('firstReleaseDate')
      ? 'firstRelease'
      : s.replace('!', ''),
    direction: s.includes('!')
  }));
}
