import { Dispatch, useMemo } from 'react';

import { Box, Link } from '@dialexa/reece-component-library';
import { differenceInDays } from 'date-fns';
import { isUndefined, map } from 'lodash-es';
import { TFunction, useTranslation } from 'react-i18next';
import {
  Column,
  useFilters,
  useFlexLayout,
  usePagination,
  useSortBy,
  useTable
} from 'react-table';

import { CoercedUser } from 'CustomerApproval/util/types';
import { sortByParser } from 'utils/tableUtils';

export type UseCustomerApprovalTableProps = {
  branch: string;
  data: CoercedUser[];
  filterValue: string;
  numOfResults?: number;
  page: string;
  setFilterValue: Dispatch<string>;
  sortBy: string[];
};

export default function useCustomerApprovalTable({
  branch,
  data,
  filterValue,
  numOfResults,
  page,
  setFilterValue,
  sortBy
}: UseCustomerApprovalTableProps) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Memo
   */
  const columns = useMemo(getColumnDefinitionsMemo, [t]);
  const filters = useMemo(filtersMemo, [
    branch,
    data,
    numOfResults,
    setFilterValue,
    t
  ]);
  const pageIndex = useMemo(pageIndexMemo, [data, numOfResults, page]);

  /**
   * Table
   */
  const tableInstance = useTable<CoercedUser>(
    {
      data,
      columns,
      initialState: {
        filters,
        pageIndex,
        sortBy: sortByParser(sortBy)
      }
    },
    useFilters,
    useSortBy,
    usePagination,
    useFlexLayout
  );

  /**
   * Memo def
   */
  function getColumnDefinitionsMemo() {
    return [
      {
        accessor: 'email',
        Header: <Box pl={1.5}>{t('common.emailAddress')}</Box>,
        Cell: ({ value }) => (
          <Link sx={{ color: 'primary02.main', pl: 1.5 }}>{value}</Link>
        )
      },
      {
        accessor: 'firstName',
        Header: t('common.firstName') as string,
        width: 75
      },
      {
        accessor: 'lastName',
        Header: t('common.lastName') as string,
        width: 75
      },
      {
        accessor: 'branchId',
        Header: t('common.branch') as string,
        filter: 'equals',
        width: 50
      },
      {
        accessor: 'createdAt',
        Header: t('common.day(s)Pending') as string,
        width: 50,
        Cell: ({ value }) => {
          const date = new Date(value);
          const dayNum = Math.abs(differenceInDays(date, new Date()));
          const formattedDate = formatDays(dayNum, t);
          return formattedDate;
        }
      }
    ] as Column<CoercedUser>[];
  }

  function pageIndexMemo() {
    return pageIndexMemoLogic(page, data, numOfResults);
  }

  function filtersMemo() {
    return filterMemoLogic(branch, data, setFilterValue, t, numOfResults);
  }

  /**
   * Handle reset of filter
   */
  function handleFilterReset() {
    tableInstance.setAllFilters([]);
    setFilterValue(t('common.all'));
  }

  function handleSubmitFilter() {
    tableInstance.gotoPage(0);
    filterValue === t('common.all')
      ? tableInstance.setAllFilters([])
      : tableInstance.setFilter('branchId', filterValue);
  }

  /**
   * output
   */
  return { handleFilterReset, handleSubmitFilter, tableInstance };
}

export function formatDays(day: number, t: TFunction) {
  if (day > 1) {
    return `${day} ${t('common.days')}`;
  }
  if (day === 1) {
    return `1 ${t('common.day')}`;
  }
  return `< 1 ${t('common.day')}`;
}

export function pageIndexMemoLogic(
  page: string,
  data: CoercedUser[],
  numOfResults?: number
) {
  if (
    isNaN(parseInt(page)) ||
    (!isUndefined(numOfResults) && parseInt(page) > Math.ceil(data.length / 10))
  ) {
    return 0;
  }
  return parseInt(page) - 1;
}

export function filterMemoLogic(
  branch: string,
  data: CoercedUser[],
  setFilterValue: Dispatch<string>,
  t: TFunction,
  numOfResults?: number
) {
  if (branch === t('common.all')) {
    return [];
  }
  if (
    !isUndefined(numOfResults) &&
    map(data, 'branchId').indexOf(branch) === -1
  ) {
    setFilterValue(t('common.all'));
    return [];
  }
  setFilterValue(branch);
  return [{ id: 'branchId', value: branch }];
}
