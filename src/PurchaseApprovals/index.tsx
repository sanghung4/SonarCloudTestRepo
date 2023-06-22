import { useCallback, useContext, useEffect, useMemo, useState } from 'react';

import { Box, useScreenSize } from '@dialexa/reece-component-library';
import { isEqual } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import {
  Column,
  usePagination,
  useSortBy,
  useTable,
  useFlexLayout
} from 'react-table';

import { AuthContext } from 'AuthProvider';
import AccordionWrapper from 'common/AccordionWrapper';
import ItemCard from './ItemCard';
import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import PermissionRequired, { Permission } from 'common/PermissionRequired';
import { useGetOrdersPendingApprovalQuery } from 'generated/graphql';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useQueryParams } from 'hooks/useSearchParam';
import { useHistory, useLocation } from 'react-router-dom';
import { formatFromCents } from 'utils/currency';

export type OrderItem = {
  orderId: string;
  purchaseOrderNumber: string;
  submissionDate: string;
  submittedByName: string;
  orderTotal: number;
  testId: string;
};

type SearchParams = {
  tab: string;
  page: string;
  sortBy: string[];
};

function PurchaseApprovals() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { search } = useLocation();
  const [queryParams, setQueryParams] = useQueryParams<SearchParams>({
    arrayKeys: ['sortBy']
  });
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('common.purchaseApprovals'));

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);

  /**
   * State
   */
  const tabs = useMemo(tabsMemo, [profile, t]);
  const [tablePageIndex, setTablePageIndex] = useState(['1', '1', '1']);
  const [tableSortBy, setTableSortBy] = useState([['createdAt'], [], []]);
  const [numOfResults, setNumOfResults] = useState<number>();

  const {
    tab = tabs[0],
    page = tablePageIndex[tabs.indexOf(tab)],
    sortBy = tableSortBy[tabs.indexOf(tab)]
  } = queryParams;
  const tabIndex = tabs.indexOf(tab);
  /**
   * Data
   */

  const { data: unapprovedData, loading: unapprovedLoading } =
    useGetOrdersPendingApprovalQuery({
      fetchPolicy: 'no-cache'
    });
  /**
   * Memos
   */
  const data = useMemo(dataMemo, [unapprovedData, tabIndex]);
  const columns = useMemo(columnMemo, [tabIndex, t]);
  const pageIndex = useMemo(pageIndexMemo, [data, numOfResults, page]);

  /**
   * Callbacks
   */
  const handleRowClick = useCallback(handleRowClickCb, [history, search]);
  /**
   * Table
   */
  const tableInstance = useTable<OrderItem>(
    {
      data,
      columns,
      initialState: {
        pageIndex,
        sortBy: sortBy.map((s) => ({
          id: s.replace('!', ''),
          desc: s.includes('!')
        }))
      }
    },
    useSortBy,
    usePagination,
    useFlexLayout
  );

  /**
   * Effects
   */
  useEffect(syncQueryParams, [
    page,
    setQueryParams,
    sortBy,
    tab,
    tabIndex,
    tableInstance.state.pageIndex,
    tableInstance.state.sortBy,
    tablePageIndex,
    tableSortBy,
    tabs
  ]);

  return (
    <PermissionRequired permissions={[Permission.MANAGE_ROLES]} redirectTo="/">
      <TablePageLayout
        flatCards
        pageTitle={t('common.purchaseApprovals')}
        table={
          <>
            {isSmallScreen ? (
              <Box bgcolor="primary.contrastText" px={3} pt="1px" pb={5} mb={2}>
                <AccordionWrapper
                  label={`${
                    unapprovedData?.ordersPendingApproval?.length ?? 0
                  } ${t('user.waitingForApproval')}`}
                  testId={`${t('user.waitingForApproval')}`}
                >
                  <Box display="flex" flexDirection="column" width={1}>
                    {unapprovedData?.ordersPendingApproval?.map((r) => (
                      <ItemCard
                        onRowClick={handleRowClick}
                        order={r as OrderItem}
                        key={r?.orderId ?? ''}
                      />
                    ))}
                  </Box>
                </AccordionWrapper>
              </Box>
            ) : (
              <TableRenderer
                loading={unapprovedLoading}
                resultsCount={tableInstance.rows.length}
                noResultsMessage={t('common.noResultsFound')}
                resultsCountText={t('common.results')}
                onRowClick={handleRowClick}
                // @ts-ignore
                tableInstance={tableInstance}
                testId="table"
                primaryKey="orderId"
              />
            )}
          </>
        }
      />
    </PermissionRequired>
  );

  /**
   * Memo defs
   */

  function tabsMemo() {
    let tabs = [t('user.waitingForApproval')];

    if (profile?.permissions.includes(Permission.APPROVE_ALL_USERS)) {
      return [...tabs, t('user.rejected')];
    } else {
      return tabs;
    }
  }

  function dataMemo() {
    let tempData: OrderItem[] | undefined;
    switch (tabIndex) {
      case 0:
        tempData = unapprovedData?.ordersPendingApproval as OrderItem[];
        break;
    }

    setNumOfResults(tempData ? tempData.length : 0);
    return tempData ?? [];
  }

  function columnMemo() {
    const defaultColumns = [
      {
        accessor: 'orderId',
        Header: t('common.orderNumber'),
        width: 65
      },
      {
        accessor: 'purchaseOrderNumber',
        Header: t('common.poNumber') ?? '',
        width: 65
      },
      {
        accessor: 'submissionDate',
        Header: t('common.submissionDate') ?? '',
        width: 65
      },
      {
        accessor: 'submittedByName',
        Header: t('common.submittedBy') ?? '',
        width: 65
      },
      {
        accessor: 'orderTotal',
        Header: t('common.orderTotal') ?? '',
        width: 65,
        Cell: ({ value }: { value: string }) => {
          return formatFromCents(parseInt(value));
        }
      }
    ] as Column<OrderItem>[];

    switch (tabIndex) {
      case 0:
        return [...defaultColumns] as Column<OrderItem>[];
      default:
        return [];
    }
  }

  function pageIndexMemo() {
    return isNaN(parseInt(page))
      ? 0
      : numOfResults !== undefined &&
        parseInt(page) > Math.floor(data.length / 10)
      ? 0
      : parseInt(page) - 1;
  }

  function handleRowClickCb(row: OrderItem) {
    const orderInfo: OrderItem = {
      ...row
    };

    history.push({
      pathname: `/purchase-approvals/${row.orderId}`,
      state: { selectedOrder: orderInfo, search }
    });
  }

  /**
   * Effect defs
   */
  function syncQueryParams() {
    const tablePage = (
      isNaN(tableInstance.state.pageIndex)
        ? 1
        : tableInstance.state.pageIndex + 1
    ).toString();

    const pageChanged = tablePage !== tablePageIndex[tabIndex];

    if (pageChanged) {
      const newTablePageIndex = [...tablePageIndex];
      newTablePageIndex[tabIndex] = tablePage;
      setTablePageIndex(newTablePageIndex);
    }

    const formattedSortBy = tableInstance.state.sortBy.map(
      (s) => `${s.desc ? '!' : ''}${s.id}`
    );

    const sortChanged = !isEqual(formattedSortBy, tableSortBy[tabIndex]);

    if (sortChanged) {
      const newTableSortBy = [...tableSortBy];
      newTableSortBy[tabIndex] = formattedSortBy;
      setTableSortBy(newTableSortBy);
    }

    if (tab !== tabs[tabIndex] || pageChanged || sortChanged) {
      setQueryParams({
        tab: tabs[tabIndex],
        page: tablePage,
        sortBy: formattedSortBy
      });
    }
  }
}

export default PurchaseApprovals;
