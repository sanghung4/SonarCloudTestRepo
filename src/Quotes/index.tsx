import React, { useEffect, useMemo, useState } from 'react';

import { Box, Link, useTheme } from '@dialexa/reece-component-library';
import { camelCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import {
  Column,
  TableInstance,
  useFilters,
  useFlexLayout,
  useGlobalFilter,
  usePagination,
  useSortBy,
  useTable
} from 'react-table';

import Filters from './Filters';
import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import { Quote, useQuotesLazyQuery } from 'generated/graphql';
import { format as formatCurrency } from 'utils/currency';
import { compareDates } from 'utils/dates';
import { useQueryParams } from 'hooks/useSearchParam';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

type QuotesParams = {
  searchBy: string;
  page: string;
  sortBy: string[];
};

function Quotes() {
  /**
   * Custom Hooks
   */
  const { search } = useLocation();
  const [queryParams, setQueryParams] = useQueryParams<QuotesParams>({
    arrayKeys: ['sortBy']
  });
  const theme = useTheme();
  const { t } = useTranslation();

  const { searchBy = '', page = '1', sortBy = ['requestedDate'] } = queryParams;
  useDocumentTitle(t('common.quote_other'));

  /**
   * State
   */
  const [searchValue, setSearchValue] = useState(searchBy);

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const [getQuotes, { data: quotesQuery, loading: quotesLoading, called }] =
    useQuotesLazyQuery({
      fetchPolicy: 'no-cache',
      variables: {
        accountId: selectedAccounts?.billTo?.erpAccountId ?? ''
      }
    });

  /**
   * Memo
   */
  const data = useMemo(quotesMemo, [quotesQuery]);
  const colorMap = useMemo(colorMapMemo, [theme]);
  const columns = useMemo(columnsMemo, [colorMap, search, t]);

  /**
   * Table
   */
  const tableInstance = useTable<Quote>(
    {
      data,
      columns,
      initialState: {
        globalFilter: searchBy || undefined,
        pageIndex: parseInt(page) - 1 || 0,
        sortBy: sortBy.map((s) => ({
          id: s.replace('!', ''),
          desc: s.includes('!')
        }))
      }
    },
    useFilters,
    useGlobalFilter,
    useSortBy,
    usePagination,
    useFlexLayout
  );

  /**
   * Effects
   */
  useEffect(callOnLoad, [called, getQuotes]);
  useEffect(syncQueryParams, [
    page,
    queryParams,
    setQueryParams,
    sortBy,
    tableInstance.state.pageIndex,
    tableInstance.state.sortBy
  ]);

  return (
    <TablePageLayout
      pageTitle={t('common.quotes')}
      filters={
        <Filters
          search={searchValue}
          setSearch={setSearchValue}
          onSubmit={onFiltersSubmit}
          onReset={onFiltersReset}
          resultsCount={tableInstance.rows.length}
        />
      }
      table={
        <TableRenderer
          loading={quotesLoading}
          resultsCount={tableInstance.rows.length}
          noResultsMessage={t('quotes.noQuotes')}
          resultsCountText={t('common.quote', {
            count: tableInstance.rows.length
          })}
          testId="quotes-table"
          tableInstance={tableInstance}
          primaryKey="orderNumber"
        />
      }
    />
  );

  function onFiltersSubmit() {
    tableInstance.setGlobalFilter(searchValue || undefined);

    setQueryParams({
      ...queryParams,
      searchBy: searchValue || ''
    });
  }

  function onFiltersReset() {
    setSearchValue('');

    tableInstance.setGlobalFilter(undefined);

    setQueryParams({
      ...queryParams,
      searchBy: ''
    });
  }

  /**
   * Memo defs
   */
  function quotesMemo(): Quote[] {
    return (quotesQuery?.quotes as Quote[]) ?? [];
  }

  function columnsMemo() {
    return [
      {
        accessor: (row): string =>
          `${row.orderNumber}${row.invoiceNumber ? '.' : ''}${
            row.invoiceNumber?.toString().padStart(3, '0') ?? ''
          }`,
        Cell: ({ value }: TableInstance) => (
          <Link
            to={{ pathname: `/quote/${value}`, state: { search } }}
            component={RouterLink}
            underline="none"
            sx={{ color: 'primary02.main' }}
            data-testid={`invoice_${value}`}
          >
            {value}
          </Link>
        ),
        Header: t('quotes.quoteNumber') ?? '',
        width: 125
      },
      {
        accessor: 'webStatus',
        Cell: ({ value }: TableInstance) => (
          <Box color={colorMap[value as WebStatus]}>
            {t(`quotes.${camelCase(value)}`) ?? ''}
          </Box>
        ),
        Header: t('common.status') ?? '',
        filter: 'equals',
        width: 100
      },
      {
        accessor: 'shipToId',
        Header: t('common.acctNumber') ?? '',
        width: 75
      },
      {
        accessor: 'shipToName',
        Cell: ({ value }: TableInstance) => (
          <Box whiteSpace="nowrap" overflow="hidden" textOverflow="ellipsis">
            {value}
          </Box>
        ),
        Header: t('common.jobName') ?? '',
        width: 150
      },
      {
        accessor: 'customerPO',
        Cell: ({ value }: TableInstance) => (
          <Box whiteSpace="nowrap" overflow="hidden" textOverflow="ellipsis">
            {value}
          </Box>
        ),
        Header: t('common.poNumber') ?? '',
        width: 100
      },
      {
        accessor: 'orderDate',
        Header: t('quotes.requestedDate') ?? '',
        width: 100,
        sortType: compareDates
      },
      {
        accessor: 'bidExpireDate',
        Header: t('quotes.expirationDate') ?? '',
        width: 100,
        sortType: compareDates
      },
      {
        accessor: 'orderTotal',
        Cell: ({ value }: TableInstance) => formatCurrency(value ?? 0),
        Header: t('quotes.quoteTotal') ?? '',
        width: 100
      }
    ] as Column<Quote>[];
  }

  function colorMapMemo() {
    return {
      ACTIVE: theme.palette.success.main,
      EXPIRED: theme.palette.error.main,
      ORDER_PENDING: theme.palette.secondary.main,
      REQUESTED: theme.palette.secondary02.main,
      SUBMITTED: theme.palette.purple.main
    };
  }

  type WebStatus = keyof ReturnType<typeof colorMapMemo>;

  /**
   * Effect defs
   */
  function callOnLoad() {
    if (!called) getQuotes();
  }

  function syncQueryParams() {
    const tablePage = tableInstance.state.pageIndex + 1;
    const tableSortString = tableInstance.state.sortBy
      .map((s) => `${s.desc ? '!' : ''}${s.id}`)
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

export default Quotes;
