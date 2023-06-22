import { Invoice } from 'generated/graphql';
import {
  Column,
  useFilters,
  useFlexLayout,
  useGlobalFilter,
  usePagination,
  useRowSelect,
  useSortBy,
  useTable
} from 'react-table';
import { defaultCellValue, handlePage, sortByParser } from 'utils/tableUtils';

type InvoiceTableProps = {
  columns: Column<Invoice>[];
  data: Invoice[];
  page: string;
  sortBy: string[];
  searchBy: string;
};

export function useInvoicesTable({
  columns,
  data,
  page,
  sortBy,
  searchBy
}: InvoiceTableProps) {
  return useTable<Invoice>(
    {
      defaultColumn: {
        aggregate: 'first',
        Cell: defaultCellValue
      },
      columns,
      data,
      initialState: {
        pageIndex: handlePage(page),
        sortBy: sortByParser(sortBy),
        globalFilter: searchBy
      }
    },
    useFilters,
    useGlobalFilter,
    useSortBy,
    usePagination,
    useRowSelect,
    useFlexLayout
  );
}
