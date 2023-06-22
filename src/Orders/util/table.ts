import {
  Column,
  PluginHook,
  TableOptions,
  useExpanded,
  useFilters,
  useFlexLayout,
  useGlobalFilter,
  useGroupBy,
  usePagination,
  useRowSelect,
  useSortBy,
  useTable
} from 'react-table';

import { Order } from 'generated/graphql';
import { useOrdersColumnsMemo } from 'Orders/util/columns';
import { useOrdersQueryParam } from 'Orders/util/queryParam';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { defaultCellValue, handlePage, sortByParser } from 'utils/tableUtils';

type OrdersTableProps = {
  pageCount?: number;
  columns: Column<Order>[];
  data: Order[];
  page: string;
  sortBy: string[];
};

export function useOrdersTable(data: Order[], showPageCount: boolean) {
  /**
   * Custom hooks
   */
  const { isMincron } = useSelectedAccountsContext();
  const [{ page, sortBy }] = useOrdersQueryParam();

  // Default table plugins
  const plugins: PluginHook<Order>[] = [
    useFilters,
    useGlobalFilter,
    useSortBy,
    usePagination,
    useFlexLayout
  ];
  // Additional plugins
  // They have to be placed at a very specific position
  //   in order for the hook to operate correctly
  if (isMincron) {
    plugins.splice(4, 0, useRowSelect);
  } else {
    plugins.splice(2, 0, useGroupBy);
    plugins.splice(4, 0, useExpanded);
  }

  /**
   * Memo
   */
  const columns = useOrdersColumnsMemo(isMincron);

  /**
   * Table
   */
  // config
  const pageCount = showPageCount ? Math.ceil(data.length) : undefined;
  const props = { columns, data, page, pageCount, sortBy };
  const config = isMincron ? mincronConfigs(props) : eclipseConfigs(props);
  // useTable hook
  return useTable<Order>(config, ...plugins);
}

const eclipseConfigs = (props: OrdersTableProps) =>
  ({
    defaultColumn: { aggregate: 'first', Cell: defaultCellValue },
    initialState: {
      groupBy: ['orderNumber'],
      pageIndex: handlePage(props.page),
      sortBy: sortByParser(props.sortBy)
    },
    aggregations: {
      first: (values: any[]) => values[0]
    },
    pageCount: props.pageCount,
    paginateExpandedRows: false,
    columns: props.columns,
    data: props.data,
    autoResetGlobalFilter: false
  } as TableOptions<Order>);

const mincronConfigs = (props: OrdersTableProps) =>
  ({
    defaultColumn: { aggregate: 'first', Cell: defaultCellValue },
    initialState: {
      pageIndex: handlePage(props.page),
      sortBy: sortByParser(props.sortBy)
    },
    pageCount: props.pageCount,
    columns: props.columns,
    data: props.data,
    autoResetGlobalFilter: false
  } as TableOptions<Order>);

// I think these will be used globally as tableUtil (in src/utils/tableUtils.ts) once we refactor other components later on
