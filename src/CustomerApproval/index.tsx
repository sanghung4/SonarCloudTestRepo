import { useState } from 'react';

import {
  Box,
  Pagination,
  useScreenSize
} from '@dialexa/reece-component-library';
import { isUndefined } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import PermissionRequired, { Permission } from 'common/PermissionRequired';
import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import BranchFilter from 'CustomerApproval/BranchFilter';
import useCustomerApprovalData from 'CustomerApproval/util/useCustomerApprovalData';
import useCustomerApprovalTable from 'CustomerApproval/util/useCustomerApprovalTable';
import useHandleRowClick from 'CustomerApproval/util/useHandleRowClick';
import useSyncQueryParam from 'CustomerApproval/util/useSyncQueryParam';
import { SearchParams } from 'CustomerApproval/util/types';
import UserCard from 'UserManagement/UserCard';
import { handleGoToPage } from 'utils/tableUtils';
import { useQueryParams } from 'hooks/useSearchParam';
import useDocumentTitle from 'hooks/useDocumentTitle';

export default function CustomerApproval() {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const [queryParams, setQueryParams] = useQueryParams<SearchParams>({
    arrayKeys: ['sortBy']
  });
  const {
    branch = t('common.all'),
    page = '1',
    sortBy = ['createdAt']
  } = queryParams;
  useDocumentTitle(t('common.customerApproval'));

  /**
   * State
   */
  const [filterValue, setFilterValue] = useState(branch);
  const [numOfResults, setNumOfResults] = useState<number>();

  /**
   * Data
   */
  const { data, loading } = useCustomerApprovalData(setNumOfResults);

  /**
   * Table
   */
  const { handleFilterReset, handleSubmitFilter, tableInstance } =
    useCustomerApprovalTable({
      branch,
      filterValue,
      data,
      numOfResults,
      page,
      setFilterValue,
      sortBy
    });

  /**
   * Callbacks
   */
  const handleRowClick = useHandleRowClick();

  /**
   * Effects
   */
  useSyncQueryParam({
    branch,
    filterValue,
    page,
    setQueryParams,
    sortBy,
    tableInstance
  });

  /**
   * Render
   */
  return (
    <PermissionRequired
      permissions={[Permission.APPROVE_ALL_USERS]}
      redirectTo="/"
    >
      <TablePageLayout
        pageTitle={t('common.customerApproval')}
        filters={
          <BranchFilter
            userList={isUndefined(numOfResults) ? undefined : data}
            setFilter={setFilterValue}
            submitFilter={handleSubmitFilter}
            resetFilters={handleFilterReset}
            filterValue={filterValue}
          />
        }
        table={
          <>
            {isSmallScreen ? (
              <Box px={3} pt="1px" pb={2}>
                <Box display="flex" flexDirection="column" width={1}>
                  {tableInstance.rows?.map((user, i) => (
                    <UserCard
                      onRowClick={handleRowClick}
                      user={user.original}
                      key={user.original.email}
                      index={i}
                      viewUserId={t('common.customerApproval') as string}
                    />
                  ))}
                </Box>
                <Box px={3} py={2} display="flex" alignItems="center">
                  <Box flexGrow={1}>
                    {!!tableInstance.rows.length &&
                      `${tableInstance.rows.length} ${t('common.user', {
                        count: tableInstance.rows.length
                      })}`}
                  </Box>
                  {!!tableInstance.pageCount && (
                    <Pagination
                      current={tableInstance.state.pageIndex + 1}
                      count={tableInstance.pageCount}
                      ofText={t('common.of')}
                      onChange={handleGoToPage(tableInstance.gotoPage)}
                      onPrev={tableInstance.previousPage}
                      onNext={tableInstance.nextPage}
                      data-testid="pagination-container"
                      dataTestIdCurrentPage="current-page-number"
                      dataTestIdTotalNumberOfPages="total-number-of-pages"
                    />
                  )}
                </Box>
              </Box>
            ) : (
              <TableRenderer
                loading={loading}
                resultsCount={tableInstance.rows.length}
                noResultsMessage={t('user.noAccountRequests')}
                resultsCountText={t('common.user', {
                  count: tableInstance.rows.length
                })}
                onRowClick={handleRowClick}
                // @ts-ignore
                tableInstance={tableInstance}
                testId="table"
                primaryKey="email"
              />
            )}
          </>
        }
      />
    </PermissionRequired>
  );
}
