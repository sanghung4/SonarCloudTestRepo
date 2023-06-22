import { useEffect } from 'react';

import { FN } from '@reece/global-types';
import { useTranslation } from 'react-i18next';
import { TableInstance } from 'react-table';

import { CoercedUser, SearchParams } from 'CustomerApproval/util/types';

export type UseSyncQueryParamProps = {
  branch: string;
  filterValue: string;
  page: string;
  setQueryParams: FN<[SearchParams]>;
  sortBy: string[];
  tableInstance: TableInstance<CoercedUser>;
};

export default function useSyncQueryParam({
  branch,
  filterValue,
  page,
  setQueryParams,
  sortBy,
  tableInstance: { state: table }
}: UseSyncQueryParamProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Effects
   */
  useEffect(syncQueryParams, [
    branch,
    filterValue,
    page,
    setQueryParams,
    sortBy,
    t,
    table.filters,
    table.pageIndex,
    table.sortBy
  ]);

  /**
   * Effect defs
   */
  function syncQueryParams() {
    const tableBranch =
      table.filters.find((col) => col.id === 'branchId')?.value ??
      t('common.all');
    const tablePage = (
      isNaN(table.pageIndex) ? 1 : table.pageIndex + 1
    ).toString();
    const tableSortString = table.sortBy
      .map((s) => `${s.desc ? '!' : ''}${s.id}`)
      .join('|');

    if (
      tableBranch !== branch ||
      tablePage !== page ||
      (tableSortString !== '' && tableSortString !== sortBy.join('|'))
    ) {
      setQueryParams({
        branch: tableBranch,
        page: tablePage,
        sortBy: tableSortString.split('|')
      });
    }
  }
}
