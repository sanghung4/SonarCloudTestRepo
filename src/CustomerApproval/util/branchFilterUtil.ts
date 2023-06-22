import { Dispatch, useMemo } from 'react';

import { uniqBy } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import { CoercedUser } from 'CustomerApproval/util/types';

export function useBranchFilterAutocomplete(userList?: CoercedUser[]) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Memo def
   */
  function memoDef() {
    if (!userList?.length) {
      return [];
    }
    const result = uniqBy(userList, 'branchId')
      .map((user) => user.branchId ?? '-')
      .sort((a, b) => (a < b ? -1 : 1));
    return [t('common.all'), ...result];
  }

  /**
   * Output
   */
  return useMemo(memoDef, [userList, t]);
}

export function autocompleteOnChange(setFilter: Dispatch<string>) {
  return (_: any, opt: string) => setFilter(opt === '-' ? '' : opt);
}
