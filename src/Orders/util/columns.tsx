import { useMemo } from 'react';

import { camelCase } from 'lodash-es';
import { TFunction, useTranslation } from 'react-i18next';
import { Link, useLocation } from 'react-router-dom';
import { Column, Row, TableInstance } from 'react-table';

import { Order } from 'generated/graphql';
import { format as formatCurrency } from 'utils/currency';
import { compareDates } from 'utils/dates';
import {
  TruncatedCell,
  TruncatedCellWithCentralEllipsis
} from 'utils/tableUtils';

/**
 * Columns config
 */
const eclipseColumns = (t: TFunction, search: string) =>
  [
    // 0 - Order Date
    {
      accessor: 'orderDate',
      Header: t('common.orderDate') as string,
      sortType: sortOrderDate
    },
    // 1 - Order Number (aggregation)
    {
      accessor: 'orderNumber',
      Cell:
        // Don't render anything when expanded
        // Not possible to test coverage this
        /* istanbul ignore next */
        () => null,
      id: 'orderNumber',
      width: 16
    },
    // 2 - Invoice Number
    {
      accessor: invoiceNumberAccessor,
      Cell: orderUrlCell(search, false),
      Header: t('common.orderNumber') as string,
      width: 200
    },
    // 3 - PO Number
    {
      accessor: 'customerPO',
      Cell: TruncatedCellWithCentralEllipsis,
      Header: t('common.poNumber') as string
    },
    // 4 - Order Status
    {
      accessor: 'webStatus',
      Cell: orderStatusCell(t),
      Header: t('common.status') as string
    },
    // 5 - Ship Date
    {
      accessor: 'shipDate',
      Header: t('common.shipDate') as string,
      sortType: sortOrderDate
    },
    // 6 - Order Total
    {
      accessor: ({ amount }) => formatCurrency(parseFloat(amount ?? '0')),
      id: 'amount',
      Header: t('orders.orderTotal') as string,
      sortType: sortOrderCurrency
    }
  ] as Column<Order>[];

const mincronColumns = (t: TFunction, search: string) =>
  [
    // 0 - Order Date
    {
      accessor: 'orderDate',
      Header: t('common.orderDate') as string,
      sortType: sortOrderDate,
      width: 100
    },
    // 1 - Order Number
    {
      accessor: 'orderNumber',
      Cell: orderUrlCell(search, true),
      Header: t('common.orderNumber') as string,
      width: 100
    },
    // 2 - PO Number
    {
      accessor: 'customerPO',
      Cell: TruncatedCellWithCentralEllipsis,
      Header: t('common.poNumber') as string
    },
    // 3 - Job Name
    {
      accessor: 'shipToName',
      Cell: TruncatedCell,
      Header: t('common.jobName') as string,
      sortInverted: true
    },
    // 4 - Job Number
    {
      accessor: 'jobNumber',
      Header: t('common.jobNumber') as string,
      sortInverted: true
    },
    // 5 - Order Status
    {
      accessor: 'webStatus',
      Cell: orderStatusCell(t),
      Header: t('common.status') as string,
      width: 80
    },
    // 6 - Ship Date
    {
      accessor: 'shipDate',
      Header: t('common.shipDate') as string,
      sortType: sortOrderDate
    },
    // 7 - Order Total
    {
      accessor: formatOrderTotal,
      id: 'orderTotal',
      Header: t('orders.orderTotal') as string,
      sortType: sortOrderCurrency
    }
  ] as Column<Order>[];

/**
 * Exports
 */
export function useOrdersColumnsMemo(isMincron: boolean) {
  /**
   * Custom hooks
   */
  const { search } = useLocation();
  const { t } = useTranslation();

  /**
   * Memo
   */
  return useMemo(
    () => (isMincron ? mincronColumns(t, search) : eclipseColumns(t, search)),
    [isMincron, t, search]
  );
}

/**
 * Accessors
 */
function invoiceNumberAccessor(row: Order) {
  return `${row.orderNumber}${row.invoiceNumber ? '.' : ''}${
    row?.invoiceNumber?.toString()?.padStart(3, '0') ?? ''
  }`;
}
export function formatOrderTotal({ orderTotal }: Order) {
  return formatCurrency(orderTotal ?? 0);
}

/**
 * Cells
 */
function orderUrlCell(searchState: string, isMincron: boolean) {
  return ({ value, row }: TableInstance<Order>) => {
    const pathname = `/order/${value}`;
    const search = isMincron ? `?orderStatus=${row.values.webStatus}` : '';
    const state = { search: searchState };
    return (
      <Link to={{ pathname, search, state }} className="orders__link">
        {value}
      </Link>
    );
  };
}

function orderStatusCell(t: TFunction) {
  return ({ value }: TableInstance) => (
    <div className={`orders__status-cell ${value?.toLowerCase()}`}>
      {t(`orders.${camelCase(value)}`)}
    </div>
  );
}

/**
 * Sorts
 */
export function sortOrderDate(a: Row<Order>, b: Row<Order>, id: string) {
  let emptyA = a.values[id] === '';
  let emptyB = b.values[id] === '';

  if (emptyA && !emptyB) {
    return 1;
  }
  if (!emptyA && emptyB) {
    return -1;
  }
  if (emptyA && emptyB) {
    return -1;
  }

  return compareDates(a, b, id, true);
}

export function sortOrderCurrency(a: Row<Order>, b: Row<Order>, id: string) {
  const pattern = /(,|\$)+/gm; // Match "," or "$" with 1 or more times
  const actualValueA = parseFloat(a.values[id].replace(pattern, ''));
  const actualValueB = parseFloat(b.values[id].replace(pattern, ''));
  return actualValueA > actualValueB ? -1 : 1;
}
