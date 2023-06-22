import {
  useExpanded,
  useFilters,
  useFlexLayout,
  useGlobalFilter,
  useGroupBy,
  usePagination,
  useSortBy,
  useTable
} from 'react-table';

import { Contract } from 'generated/graphql';
import { ContractsParams } from 'Contracts';
import useContractColumns from 'Contracts/util/useContractsColumns';
import useContractData from 'Contracts/util/useContractsData';
import { useQueryParams } from 'hooks/useSearchParam';

export default function useContractsTable() {
  const [queryParams] = useQueryParams<ContractsParams>({
    arrayKeys: ['sortBy']
  });
  const { page = '1', sortBy = ['lastReleaseDate'] } = queryParams;
  /**
   * Memo
   */
  const { contractsQuery, data, loading, called, refetch } = useContractData();
  const columns = useContractColumns();

  /**
   * Table
   */
  const tableInstance = useTable<Contract>(
    {
      defaultColumn: {
        aggregate: 'first',
        Cell: ({ value }: { value: string }) => (value ? value : '-')
      },
      columns,
      data,
      initialState: {
        groupBy: [],
        pageIndex: parseInt(page) - 1 || 0,
        sortBy: sortBy.map((s) => ({
          id: s.replace('!', ''),
          desc: s.includes('!')
        }))
      },
      paginateExpandedRows: false,
      manualPagination: true,
      pageCount:
        !loading && called
          ? Math.ceil(contractsQuery?.contracts?.totalRows! / 10)
          : undefined,
      manualSortBy: true
    },
    useFilters,
    useGlobalFilter,
    useGroupBy,
    useSortBy,
    useExpanded,
    usePagination,
    useFlexLayout
  );

  /**
   * Output
   */
  return { tableInstance, contractsQuery, data, loading, called, refetch };
}
