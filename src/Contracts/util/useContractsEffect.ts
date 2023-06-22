import { useEffect } from 'react';
import { SortingRule, TableInstance } from 'react-table';

import { ContractsParams } from 'Contracts';
import { Contract } from 'generated/graphql';
import { useQueryParams } from 'hooks/useSearchParam';

export default function useContractsEffect(
  tableInstance: TableInstance<Contract>
) {
  /**
   * Custom hooks
   */
  const [queryParams, setQueryParams] = useQueryParams<ContractsParams>({
    arrayKeys: ['sortBy']
  });
  const { page = '1', sortBy = ['lastReleaseDate'] } = queryParams;

  /**
   * Effects
   */
  useEffect(syncQueryParams, [
    page,
    queryParams,
    setQueryParams,
    sortBy,
    tableInstance
  ]);

  /**
   * Effect defs
   */
  function syncQueryParams() {
    const tablePage = tableInstance.state.pageIndex + 1;
    const tableSortString = tableInstance.state.sortBy
      .map(tableSortRule)
      .join('|');
    if (
      tablePage !== parseInt(page) ||
      (tableSortString !== '' && tableSortString !== sortBy.join('|'))
    ) {
      setQueryParams({
        ...queryParams,
        page: tablePage.toString(),
        sortBy: tableSortString.split('|')
      });
    }
  }
}

export function tableSortRule(s: SortingRule<Contract>) {
  return `${s.desc ? '!' : ''}${s.id}`;
}
