import { useEffect, useMemo, useState } from 'react';

import { DateRange as DateRangeType } from '@dialexa/reece-component-library';
import { differenceInCalendarDays, isSameDay, subMonths } from 'date-fns';
import { useTranslation } from 'react-i18next';

import DateRange from 'common/DateRange';
import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import {
  ErpSystemEnum,
  Order,
  OrdersQueryVariables,
  useOrdersLazyQuery
} from 'generated/graphql';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useOrdersQueryParam } from 'Orders/util/queryParam';
import 'Orders/util/styles.scss';
import { useOrdersTable } from 'Orders/util/table';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import SearchCard from 'Search/SearchCard';
import { formatDate } from 'utils/dates';
import trimSpaces from 'utils/trimSpaces';

/**
 * TODO
 * [ ] Sort in collapsed
 */

/**
 * Const
 */
export const defaultRange = {
  from: subMonths(new Date(), 1),
  to: new Date()
};

/**
 * Component
 */
export default function Orders() {
  /**
   * Custom hooks
   */
  const [queryParam, setQueryParams] = useOrdersQueryParam();
  const { searchBy, from, to, page, sortBy } = queryParam;
  const { t } = useTranslation();
  useDocumentTitle(t('common.myOrders'));

  /**
   * Context
   */
  const { selectedAccounts, isEclipse } = useSelectedAccountsContext();
  const erpName = selectedAccounts?.erpSystemName ?? ErpSystemEnum.Eclipse;
  const accountKind = isEclipse ? 'shipTo' : 'billTo';
  const accountId = selectedAccounts?.[accountKind]?.erpAccountId ?? '';

  /**
   * State
   */
  const initialRange: DateRangeType = {
    from: new Date(from),
    to: new Date(to)
  };
  const [range, setRange] = useState<DateRangeType>(initialRange);
  const [searchValue, setSearchValue] = useState(searchBy);

  /**
   * Data
   */
  const [getOrders, { data: ordersQuery, loading: ordersLoading, called }] =
    useOrdersLazyQuery({
      fetchPolicy: 'cache-and-network',
      onCompleted: () => setQueryParams({ page: '1' })
    });

  /**
   * Memo
   */
  // These has to be memorized to keep React Tables stable or it will result in infinite re-rendering
  const data = useMemo(getOrdersOrEmpty, [ordersQuery]);

  const warningMessage = useMemo(() => {
    const differenceInDays = differenceInCalendarDays(range.to!, range.from!);
    if (
      isNaN(differenceInDays) ||
      (isSameDay(range.to!, new Date()) &&
        isSameDay(range.from!, subMonths(new Date(), 1)))
    ) {
      return;
    } else if (differenceInDays > 30) {
      return 'warning';
    }
    return '';
  }, [range]);

  /**
   * Table
   */
  const tableInstance = useOrdersTable(data, !ordersLoading && called);

  /**
   * Effect
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(callOnLoad, [accountId, called, erpName]);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(syncQueryParamEffect, [page, sortBy, tableInstance]);

  /**
   * Render
   */
  return (
    <DateRange value={range} onChange={setRange} onClear={resetRange}>
      <TablePageLayout
        pageTitle={t('common.orders')}
        filters={
          <SearchCard
            placeholder={
              isEclipse
                ? t('orders.orderSearchPlaceholder')
                : t('orders.orderSearchPlaceholderWaterworks')
            }
            search={searchValue}
            setSearch={setSearchValue}
            onReset={onReset}
            onViewResultsClicked={onViewResults}
            resultsCount={tableInstance.rows.length}
            dataTestId="search-orders-input"
            ordersPage
            warningMessage={warningMessage}
          />
        }
        table={
          <TableRenderer
            hasGroups={isEclipse}
            loading={ordersLoading}
            resultsCount={tableInstance.rows.length}
            noResultsMessage={
              isEclipse ? t('orders.noOrders') : t('orders.noOrdersWaterworks')
            }
            resultsCountText={t('common.orders').toLowerCase()}
            tableInstance={tableInstance}
            testId="orders-table"
            isWaterworks={!isEclipse}
            noResultsContactMessage={t('orders.noOrdersContactMessage')}
            noResultsContactBranch={t('orders.noOrdersContactBranch')}
            primaryKey="orderNumber"
          />
        }
      />
    </DateRange>
  );

  /**
   * Callbacks
   */
  // 🟤 CB - Reset Search
  function onReset() {
    resetRange();

    setSearchValue('');
    tableInstance.setGlobalFilter(undefined);
    tableInstance.gotoPage(0);

    const myDates = {
      from: formatDate(defaultRange.from),
      to: formatDate(defaultRange.to)
    };

    setQueryParams({ searchBy: '', ...myDates });

    callGetOrders({ startDate: myDates.from, endDate: myDates.to });
  }

  // 🟤 CB - Apply Search Result
  function onViewResults() {
    const trimmedSearchValue = trimSpaces(searchValue);
    setSearchValue(trimmedSearchValue);

    tableInstance.setGlobalFilter(trimmedSearchValue);
    tableInstance.gotoPage(0);

    const myDates = { from: formatDate(range.from), to: formatDate(range.to) };

    setQueryParams({ searchBy: trimmedSearchValue, ...myDates });

    callGetOrders({ startDate: myDates.from, endDate: myDates.to });
  }

  /**
   * Memos
   */
  // 🔵 Memo -  Assemble orders GQL res data (needed to prevent unneeded re-renders which cause react-tables to re-render and freeze)
  function getOrdersOrEmpty(): Order[] {
    return (ordersQuery?.orders.orders as Order[]) ?? [];
  }

  /**
   * Effect defs
   */
  // 🟡 Effect - call getOrders when components is mounted and selected variables are truthy
  function callOnLoad() {
    if (accountId && erpName && !called) {
      callGetOrders();
    }
  }
  // 🟡 Effect - Sync queryParam effect
  function syncQueryParamEffect() {
    const tablePage = tableInstance.state.pageIndex + 1;
    const tableSortString = tableInstance.state.sortBy.map(
      // Note - some instances were ignored since React Table is nearly impossible to mock/test
      // istanbul ignore next
      (s) => (s.desc ? '!' : '') + s.id
    );
    const matchSortBy =
      tableSortString.join('|') !== sortBy.join('|') &&
      // istanbul ignore next
      tableSortString.length;

    // istanbul ignore next
    if (matchSortBy || tablePage !== parseInt(page)) {
      setQueryParams({ page: tablePage.toString(), sortBy: tableSortString });
    }
  }

  /**
   * Util
   */
  // 🔣 util - reset date range
  function resetRange() {
    setRange({ ...defaultRange });
  }
  // 🔣 util - call getOrders with pre-arranged variabled
  function callGetOrders(override: Partial<OrdersQueryVariables> = {}) {
    const variables = {
      accountId,
      startDate: formatDate(from),
      endDate: formatDate(to),
      erpName,
      ...override
    };
    getOrders({ variables });
  }
}
