import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState
} from 'react';

import {
  Box,
  Button,
  Tab,
  Tabs,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { differenceInDays } from 'date-fns';
import { isEqual } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';
import {
  Column,
  usePagination,
  useSortBy,
  useTable,
  useFlexLayout
} from 'react-table';

import { AuthContext } from 'AuthProvider';
import AccordionWrapper from 'common/AccordionWrapper';
import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import PermissionRequired, { Permission } from 'common/PermissionRequired';
import {
  PhoneType,
  RejectionReason,
  Role,
  useUnapprovedAccountRequestsQuery,
  useGetApprovedAccountRequestsQuery,
  useRejectedAccountRequestsQuery
} from 'generated/graphql';
import { AddIcon } from 'icons';
import { UserInfo } from 'User';
import UserCard from 'UserManagement/UserCard';
import { formatDate } from 'utils/dates';
import { trimAccessor } from 'utils/tableUtils';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useQueryParams } from 'hooks/useSearchParam';
import { getRejectionReason } from 'utils/rejectionReasons';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { ApolloError } from '@apollo/client';

export type CoercedUser = {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  role: Role;
  companyName: string;
  phoneType: PhoneType;
  approverId: string;
  createdAt: string;
  rejectedBy: string;
  rejectedAt: string;
  rejectionReason?: RejectionReason;
  accountNumber?: string;
  contactUpdatedAt?: string;
  contactUpdatedBy?: string;
};

type SearchParams = {
  tab: string;
  page: string;
  sortBy: string[];
};

function UserManagement() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { search } = useLocation();
  const [queryParams, setQueryParams] = useQueryParams<SearchParams>({
    arrayKeys: ['sortBy']
  });
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  useDocumentTitle(t('common.userManagement'));

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { selectedAccounts, clearAccounts } = useSelectedAccountsContext();

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
  const [tabIndex, setTabIndex] = useState<number>(tabs.indexOf(tab));

  /**
   * Data
   */
  const onError = () =>
    pushAlert(t('common.encountedError'), { variant: 'error' });

  const onUnapprovedError = (error: ApolloError) => {
    try {
      const errorMessage = JSON.parse(error.message)?.error;
      if (errorMessage === 'Account with given ID not found.') {
        clearAccounts();
        history.push('/select-accounts');
      } else {
        pushAlert(t('common.encountedError'), { variant: 'error' });
      }
    } catch {
      pushAlert(t('common.encountedError'), { variant: 'error' });
    }
  };

  const { data: unapprovedData, loading: unapprovedLoading } =
    useUnapprovedAccountRequestsQuery({
      variables: {
        accountId: selectedAccounts?.billTo?.id || ''
      },
      skip: tabIndex !== 0 && !isSmallScreen,
      onError: (error) =>
        profile?.isEmployee ? onUnapprovedError(error) : onError()
    });
  const { data: approvedData, loading: approvedLoading } =
    useGetApprovedAccountRequestsQuery({
      variables: {
        accountId: selectedAccounts?.billTo?.id
      },
      skip: tabIndex !== 1 && !isSmallScreen,
      onError
    });
  const { data: rejectedData, loading: rejectedLoading } =
    useRejectedAccountRequestsQuery({
      variables: {
        accountId: selectedAccounts?.billTo?.id || ''
      },
      skip: tabIndex !== 2 && !isSmallScreen,
      onError
    });

  /**
   * Memos
   */
  const data = useMemo(dataMemo, [
    unapprovedData,
    approvedData,
    rejectedData,
    tabIndex
  ]);
  const columns = useMemo(columnMemo, [tabIndex, t]);
  const pageIndex = useMemo(pageIndexMemo, [data, numOfResults, page]);

  /**
   * Callbacks
   */
  const handleRowClick = useCallback(handleRowClickCb, [history, search]);

  /**
   * Table
   */
  const tableInstance = useTable<CoercedUser>(
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
        pageTitle={t('common.userManagement')}
        headerAction={
          <Button
            onClick={() => history.push('/invite-user')}
            color="primaryLight"
            variant="inline"
            startIcon={<AddIcon />}
            data-testid="invite-new-user-button"
          >
            {t('common.inviteNewUser')}
          </Button>
        }
        table={
          <>
            {isSmallScreen ? null : (
              <Tabs
                value={tabIndex}
                onChange={handleTabChange}
                aria-label={t('user.selectedUsers')}
                sx={{ pl: 2.5, pt: 2, mb: 1 }}
              >
                {tabs.map((t) => (
                  <Tab label={t} key={t} data-testid={`tab-${t}`} />
                ))}
              </Tabs>
            )}

            {isSmallScreen ? (
              <Box bgcolor="primary.contrastText" px={3} pt="1px" pb={5} mb={2}>
                <AccordionWrapper
                  label={`${
                    unapprovedData?.unapprovedAccountRequests?.length ?? 0
                  } ${t('user.waitingForApproval')}`}
                  testId={`${t('user.waitingForApproval')}`}
                >
                  <Box display="flex" flexDirection="column" width={1}>
                    {unapprovedData?.unapprovedAccountRequests?.map(
                      (r: any, i: number) => (
                        <UserCard
                          onRowClick={handleRowClick}
                          user={r as CoercedUser}
                          key={r?.email ?? ''}
                          index={i}
                          viewUserId={`${t('user.waitingForApproval')}`}
                        />
                      )
                    )}
                  </Box>
                </AccordionWrapper>
                <AccordionWrapper
                  label={`${approvedData?.accountUsers?.length ?? 0} ${t(
                    'user.approved'
                  )}`}
                  testId={`${t('user.approved')}`}
                >
                  <Box display="flex" flexDirection="column" width={1}>
                    {approvedData?.accountUsers?.map((r: any, i: number) => (
                      <UserCard
                        onRowClick={handleRowClick}
                        user={r as CoercedUser}
                        key={r?.id || ''}
                        index={i}
                        viewUserId={`${t('user.approved')}`}
                      />
                    ))}
                  </Box>
                </AccordionWrapper>
                <PermissionRequired
                  permissions={[Permission.APPROVE_ALL_USERS]}
                >
                  <AccordionWrapper
                    label={`${
                      rejectedData?.rejectedAccountRequests?.length ?? 0
                    } ${t('user.rejected')}`}
                    testId={`${t('user.rejected')}`}
                  >
                    <Box display="flex" flexDirection="column" width={1}>
                      {rejectedData?.rejectedAccountRequests?.map(
                        (r: any, i: number) => (
                          <UserCard
                            onRowClick={handleRowClick}
                            user={r as CoercedUser}
                            key={r?.id || ''}
                            rejectedUser
                            index={i}
                            viewUserId={`${t('user.rejected')}`}
                          />
                        )
                      )}
                    </Box>
                  </AccordionWrapper>
                </PermissionRequired>
              </Box>
            ) : (
              <TableRenderer
                loading={
                  unapprovedLoading || approvedLoading || rejectedLoading
                }
                resultsCount={tableInstance.rows.length}
                noResultsMessage={t('common.noResultsFound')}
                resultsCountText={t('common.results')}
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

  /**
   * Memo defs
   */

  function tabsMemo() {
    let tabs = [t('user.waitingForApproval'), t('user.approved')];

    if (profile?.permissions.includes(Permission.APPROVE_ALL_USERS)) {
      return [...tabs, t('user.rejected')];
    } else {
      return tabs;
    }
  }

  function dataMemo() {
    let tempData: CoercedUser[] | undefined;
    switch (tabIndex) {
      case 0:
        tempData = unapprovedData?.unapprovedAccountRequests as CoercedUser[];
        break;
      case 1:
        tempData = approvedData?.accountUsers as CoercedUser[];
        break;
      case 2:
        tempData = rejectedData?.rejectedAccountRequests as CoercedUser[];
        break;
    }

    setNumOfResults(tempData ? tempData.length : 0);
    return tempData ?? [];
  }

  function columnMemo() {
    const emailCell = ({ value }: { value: string }) => (
      <span title={value}>
        <Box
          sx={{ color: 'primary02.main' }}
          whiteSpace="nowrap"
          overflow="hidden"
          textOverflow="ellipsis"
          data-testid={`link-${value}`}
        >
          {value}
        </Box>
      </span>
    );

    const defaultColumns = [
      {
        accessor: 'email',
        Header: t('common.emailAddress'),
        Cell: emailCell,
        width: 100
      },
      {
        accessor: trimAccessor<CoercedUser>('firstName'),
        Header: t('common.firstName') as string,
        width: 65,
        sortType: 'string'
      },
      {
        accessor: trimAccessor<CoercedUser>('lastName'),
        Header: t('common.lastName') as string,
        width: 65,
        sortType: 'string'
      }
    ] as Column<CoercedUser>[];

    const roleCol = {
      accessor: ({ role }: CoercedUser) => role?.name ?? t('common.na'),
      Header: t('common.role'),
      width: 65
    } as Column<CoercedUser>;

    switch (tabIndex) {
      case 0:
        return [
          ...defaultColumns,
          roleCol,
          {
            accessor: 'createdAt',
            Header: t('common.day(s)Pending'),
            Cell: ({ value }) => {
              const date = new Date(value);
              const dayNum = Math.abs(differenceInDays(date, new Date()));
              const formattedDate =
                dayNum > 1
                  ? `${dayNum} ${t('common.days')}`
                  : dayNum === 1
                  ? `${dayNum} ${t('common.day')}`
                  : `< 1 ${t('common.day')}`;
              return formattedDate;
            },
            width: 65
          }
        ] as Column<CoercedUser>[];
      case 1:
        return [...defaultColumns, roleCol];
      case 2:
        return [
          ...defaultColumns,
          {
            accessor: 'rejectedBy',
            Header: t('user.rejectedBy'),
            Cell: ({ value }) => emailCell({ value }) ?? t('common.na'),
            width: 100
          },
          {
            accessor: 'rejectedAt',
            Header: t('user.rejectedAt'),
            Cell: ({ value }) => formatDate(value),
            width: 45
          },
          {
            accessor: ({ rejectionReason }: CoercedUser) =>
              t(getRejectionReason(rejectionReason)),
            Header: t('user.rejectedReason'),
            Cell: ({ value }: { value: any }) => (
              <span title={value}>
                <Box
                  whiteSpace="nowrap"
                  overflow="hidden"
                  textOverflow="ellipsis"
                >
                  {value}
                </Box>
              </span>
            ),
            width: 100
          }
        ] as Column<CoercedUser>[];
      default:
        return [];
    }
  }

  function pageIndexMemo() {
    return isNaN(parseInt(page))
      ? 0
      : numOfResults !== undefined &&
        parseInt(page) > Math.ceil(data.length / 10)
      ? 0
      : parseInt(page) - 1;
  }

  /**
   * Callback defs
   */

  function handleRowClickCb(row: CoercedUser) {
    if (!row.rejectedAt) {
      const userInfo: UserInfo = {
        id: row.id,
        firstName: row.firstName,
        lastName: row.lastName,
        language: 'English',
        email: row.email,
        company: row.companyName,
        phone: row.phoneNumber,
        roleId: row.role?.id ?? '',
        approverId: row.approverId,
        phoneType: row.phoneType,
        contactUpdatedAt: row.contactUpdatedAt,
        contactUpdatedBy: row.contactUpdatedBy
      };

      history.push({
        pathname: `/user/${row.id}`,
        state: { selectedUser: userInfo, search }
      });

      // TODO: do this in a better way, should be able to invalidate cache
      history.go(0);
    }
  }

  function handleTabChange(_: any, val: any) {
    setTabIndex(val);

    const tabSortBy = tableSortBy[val].map((s) => ({
      id: s.replace('!', ''),
      desc: s.includes('!')
    }));

    tableInstance.setSortBy(tabSortBy);
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

export default UserManagement;
