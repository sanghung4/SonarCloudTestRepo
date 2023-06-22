import { useMemo } from 'react';

import { Box } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link, useLocation } from 'react-router-dom';
import { Column, Row, TableInstance } from 'react-table';

import { Contract } from 'generated/graphql';
import { compareDates } from 'utils/dates';
import { TruncatedCell } from 'utils/tableUtils';

export default function useContractColumns() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { search } = useLocation();

  /**
   * Memo def
   */
  function getColumnDefinitions() {
    return [
      {
        accessor: 'lastReleaseDate',
        Header: t('contracts.lastReleaseDate') as string,
        width: 90,
        sortType: sortDate(false)
      },
      {
        accessor: 'contractNumber',
        Header: t('contracts.contractNumber') as string,
        sortType: 'string',
        width: 60,
        sortDescFirst: true,
        Cell: ContractNumberCell(search)
      },
      {
        accessor: 'description',
        Cell: TruncatedCell,
        Header: t('contracts.contractName') as string
      },
      {
        accessor: 'jobNumber',
        width: 80,
        Header: t('contracts.jobNumber') as string,
        sortType: 'string',
        sortDescFirst: true
      },
      {
        accessor: 'jobName',
        width: 120,
        Cell: TruncatedCell,
        Header: t('common.jobName') as string
      },
      {
        accessor: 'firstReleaseDate',
        Header: t('contracts.firstReleaseDate') as string,
        width: 100,
        sortType: sortDate(true)
      }
    ] as Column<Contract>[];
  }

  /**
   * Output
   */
  return useMemo(getColumnDefinitions, [t, search]);
}

/**
 * Components
 */
export function ContractNumberCell(search: string) {
  return ({ value }: TableInstance) => (
    <Box
      component={Link}
      to={{
        pathname: `/contract/${value}`,
        state: { fromContracts: true, search }
      }}
      color="primary02.main"
      sx={{ textDecoration: 'none' }}
    >
      {value}
    </Box>
  );
}

/**
 * Logics
 */
export function sortDate(desc?: boolean) {
  return (a: Row<Contract>, b: Row<Contract>, id: string) => {
    let emptyA = a.values[id] === '';
    let emptyB = b.values[id] === '';

    if (emptyA && !emptyB) {
      return 1;
    } else if (!emptyA && emptyB) {
      return -1;
    } else if (emptyA && emptyB) {
      return -1;
    }

    return compareDates(a, b, id, desc);
  };
}
