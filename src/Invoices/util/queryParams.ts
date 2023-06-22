import { Dispatch, useEffect, useMemo } from 'react';

import { DateRange as DateRangeType } from '@dialexa/reece-component-library';
import { TableInstance } from 'react-table';

import { Invoice } from 'generated/graphql';
import { Age, defaultTimeRange, emptyDateRange } from 'Invoices/util';
import { invoiceFilterOptions, statusOptions } from 'Invoices/index';
import { formatDate } from 'utils/dates';
import { useQueryParams } from 'hooks/useSearchParam';
import { appliedRangeMemo } from 'utils/tableUtils';
import trimSpaces from 'utils/trimSpaces';

/**
 * Types
 */
export type InvoiceParams = {
  bucket: Age | string;
  from?: string;
  page?: string;
  sortBy?: string[];
  to?: string;
  searchBy?: string;
  invoiceStatus?: string;
};
export type InvoiceParamsUtilProps = {
  loading: boolean;
  range: DateRangeType;
  searchValue: string;
  setRange: Dispatch<DateRangeType>;
  setSearchValue: Dispatch<string>;
  tableInstance: TableInstance<Invoice>;
  statusFilter: string;
  setStatusFilter: Dispatch<string>;
  invociesAgingFilterValue: string;
};
export type SyncInvoiceParamEffectProps = {
  loading: boolean;
  page: string;
  queryParams: InvoiceParams;
  setQueryParams: (obj: InvoiceParams, path?: string | undefined) => void;
  sortBy: string[];
  tableInstance: TableInstance<Invoice>;
};
export type HandleInvoiceCBProps = {
  setRange: (value: DateRangeType) => void;
  setSearchValue: (value: string) => void;
  setQueryParams: (obj: InvoiceParams, path?: string | undefined) => void;
  tableInstance: TableInstance<Invoice>;
  queryParams: InvoiceParams;
  range: DateRangeType;
  searchValue: string;
  statusFilter: string;
  setStatusFilter: (value: string) => void;
  invociesAgingFilterValue: string;
};

/**
 * Main hooks
 */
// Apply this hook before table hooks
// Primarily used for init query Param
export function useInvoicesQueryParam() {
  /**
   * Hooks
   */
  const [queryParams, setQueryParams] = useQueryParams<InvoiceParams>({
    arrayKeys: ['sortBy']
  });
  const {
    bucket = 'Total',
    page = '1',
    sortBy = ['!invoiceDate'],
    from,
    to,
    searchBy = '',
    invoiceStatus = 'All'
  } = queryParams;

  const appliedRange = useMemo(
    () => appliedRangeMemo({ from, to }),
    [from, to]
  );

  return {
    appliedRange,
    bucket,
    from,
    invoiceStatus,
    queryParams,
    page,
    searchBy,
    setQueryParams,
    sortBy,
    to
  };
}

// Apply this hook ater table hooks
// Sync Query Param effect and value/query param functions
export function useInvoicesQueryParamUtil(props: InvoiceParamsUtilProps) {
  const {
    loading,
    range,
    searchValue,
    setRange,
    setSearchValue,
    statusFilter,
    setStatusFilter,
    tableInstance,
    invociesAgingFilterValue
  } = props;
  const { queryParams, setQueryParams, page, sortBy } = useInvoicesQueryParam();

  /**
   * Effect
   */
  useEffect(
    () =>
      syncQueryParamEffect({
        loading,
        page,
        queryParams,
        setQueryParams,
        sortBy,
        tableInstance
      }),
    [loading, page, queryParams, setQueryParams, sortBy, tableInstance]
  );

  /**
   * Callbacks Props
   */
  const cbProps: HandleInvoiceCBProps = {
    setRange,
    setSearchValue,
    setStatusFilter,
    setQueryParams,
    tableInstance,
    queryParams,
    range,
    searchValue,
    statusFilter,
    invociesAgingFilterValue
  };

  return {
    clearRange: clearRange(cbProps),
    handleBucketChange: handleBucketChange(cbProps),
    handleReset: handleReset(cbProps),
    handleViewResults: handleViewResults(cbProps)
  };
}

/**
 * Hoists
 */
// Effect - Sync Query Param
export function syncQueryParamEffect(p: SyncInvoiceParamEffectProps) {
  const { loading, page, queryParams, setQueryParams, sortBy, tableInstance } =
    p;
  const tablePage = tableInstance.state.pageIndex + 1;
  const pageLen = tableInstance.pageCount;
  const tableSortString = tableInstance.state.sortBy
    .map((s) => `${s.desc ? '!' : ''}${s.id}`)
    .join('|');

  if (loading) {
    return;
  }
  if (
    tablePage !== parseInt(page) ||
    (tableSortString !== '' && tableSortString !== sortBy.join('|'))
  ) {
    setQueryParams({
      ...queryParams,
      page: tablePage.toString(),
      sortBy: tableSortString.split('|')
    });
    return;
  }
  if (tablePage > pageLen) {
    tableInstance.gotoPage(pageLen - 1);
  }
}

// Callbacks
export function clearRange({ setRange }: HandleInvoiceCBProps) {
  return () => {
    setRange(emptyDateRange);
  };
}
export function handleReset({
  range,
  setRange,
  setSearchValue,
  setStatusFilter,
  setQueryParams,
  tableInstance
}: HandleInvoiceCBProps) {
  return () => {
    setRange(defaultTimeRange);
    setSearchValue('');
    setStatusFilter(statusOptions.All);
    setQueryParams({
      bucket: '',
      page: '',
      sortBy: [''],
      from: '',
      to: '',
      searchBy: '',
      invoiceStatus: ''
    });
    tableInstance.setGlobalFilter(undefined);
    tableInstance.gotoPage(0);
  };
}
export function handleViewResults({
  setQueryParams,
  tableInstance,
  queryParams,
  range,
  invociesAgingFilterValue,
  searchValue,
  setSearchValue,
  statusFilter
}: HandleInvoiceCBProps) {
  return () => {
    if (range !== undefined) {
      const trimmedSearchValue = trimSpaces(searchValue);
      setSearchValue(trimmedSearchValue);
      let bucketValue = queryParams.bucket;
      if (invociesAgingFilterValue !== invoiceFilterOptions.Total) {
        const indexOfS = Object.values(invoiceFilterOptions).indexOf(
          invociesAgingFilterValue as unknown as invoiceFilterOptions
        );
        const key = Object.keys(invoiceFilterOptions)[indexOfS];
        bucketValue = key as Age;
      } else {
        bucketValue = 'Total';
      }
      if (
        statusFilter === statusOptions.All ||
        statusFilter === statusOptions.Closed
      ) {
        bucketValue = 'Total';
      }
      setQueryParams({
        ...queryParams,
        bucket: bucketValue,
        from: formatDate(range.from ?? ''),
        to: formatDate(range.to ?? ''),
        searchBy: trimmedSearchValue,
        invoiceStatus: statusFilter || ''
      });
      tableInstance.gotoPage(0);
      tableInstance.autoResetGlobalFilter = false;
      tableInstance.setGlobalFilter(trimmedSearchValue);
    }
  };
}
export function handleBucketChange({
  setQueryParams,
  tableInstance,
  queryParams
}: HandleInvoiceCBProps) {
  return (bucket: Age) => {
    tableInstance.gotoPage(0);
    setQueryParams({
      ...queryParams,
      from: '',
      to: '',
      invoiceStatus: '',
      bucket
    });
  };
}
