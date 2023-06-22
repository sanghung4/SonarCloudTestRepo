import React, { useContext, useEffect, useMemo, useState } from 'react';

import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  CircularProgress,
  Divider,
  Grid,
  IconButton,
  List,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { format } from 'date-fns';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';

import {
  useApproveUserMutation,
  useGetRejectionReasonsQuery,
  useDeleteUserMutation,
  useRejectUserMutation,
  useRefreshContactMutation,
  RejectionReason,
  useGetRolesQuery,
  useCheckUsersForApproverMutation
} from 'generated/graphql';

import { AuthContext } from 'AuthProvider';
import { Permission } from 'common/PermissionRequired';
import { DeleteBinIcon, EditIcon, RefreshFullIcon } from 'icons';
import { UserInfo } from 'User';
import DeleteDialog from 'User/DeleteDialog';
import RejectionDialog from 'User/RejectionDialog';
import Roles, { RoleInfo } from 'User/Roles';
import ViewTableListItem from 'User/ViewUserListItem';
import { trackApproval } from 'utils/analytics';
import { Configuration } from 'utils/configuration';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import PurchaseApproverDialog from './PurchaseApproverDialog';

export type Props = {
  onEditClicked: () => void;
  user: UserInfo;
  customerApproval?: boolean;
};

type State = {
  tab?: string;
};

// TODO: tw - need to get role id from the previous screen / data fetch (DPS666)

function ViewUser(props: Props) {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { state } = useLocation<State>();
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * State
   */
  const [selectedRole, setSelectedRole] = useState<RoleInfo>({
    id: props.user.roleId,
    approverId: props.user.approverId
  });

  const [rejectionModalOpen, setRejectionDialogOpen] = useState(false);
  const [deleteModalOpen, setDeleteDialogOpen] = useState(false);
  const [purchaseApproverDialogOpen, setPurchaseApproverDialogOpen] =
    useState(false);
  /**
   * Data
   */
  const { data: rolesQuery } = useGetRolesQuery();
  const { data: rejectionReasonsQuery } = useGetRejectionReasonsQuery();
  const [rejectUser] = useRejectUserMutation();
  const [deleteUser, { loading: deleteUserLoading }] = useDeleteUserMutation();
  const [
    checkUsersForApproverMutation,
    { data: usersForApproversData, loading: usersForApproversLoading }
  ] = useCheckUsersForApproverMutation({
    fetchPolicy: 'no-cache',
    onCompleted: (data) => {
      // Unable to access/test this
      // istanbul ignore next
      if (!data?.checkUsersForApprover?.users?.length) {
        setPurchaseApproverDialogOpen(false);
        setDeleteDialogOpen(true);
      }
    },
    onError: () => {
      setPurchaseApproverDialogOpen(false);
      pushAlert(t('user.userDeleteError'), {
        variant: 'error'
      });
    }
  });
  const [approveUser, { loading: approveUserLoading }] = useApproveUserMutation(
    {
      variables: {
        user: {
          userId: props.user.id,
          userRoleId: selectedRole.id,
          approverId: selectedRole?.approverId
        }
      }
    }
  );
  const [refreshContact, { loading: refreshContactLoading }] =
    useRefreshContactMutation({
      variables: {
        emails: [props.user.email]
      },
      refetchQueries: ['getApprovedAccountRequests'],
      onCompleted: () => {
        // Unable to access/test this
        // istanbul ignore next
        pushAlert('Successfully refreshed Eclipse contact', {
          variant: 'success'
        });
        history.push('/user-management?tab=Approved&page=1');
      },
      onError: () => {
        pushAlert('Unable to refresh Eclipse contact', { variant: 'error' });
      }
    });

  /**
   * Effects
   */

  useEffect(setRoleToAdmin, [props.customerApproval, rolesQuery]);

  /**
   * Memo
   */
  const requiresApproverRoleId = useMemo(() => {
    return rolesQuery?.roles?.find((r) => r?.name === 'Purchase with Approval')
      ?.id;
  }, [rolesQuery]);

  const userManagementURL = useMemo(() => {
    return `/user-management${
      state.tab ? `?tab=${state.tab.split(' ').join('+')}` : ''
    }`;
  }, [state.tab]);

  /**
   * Callbacks
   */
  const onRoleChanged = (info: RoleInfo) => setSelectedRole(info);

  const handleRejectionDialogOpened = () => setRejectionDialogOpen(true);
  const handleRejectionDialogClose = () => setRejectionDialogOpen(false);

  const handleDeleteDialogOpened = async () => {
    setPurchaseApproverDialogOpen(true);

    await checkUsersForApproverMutation({
      variables: {
        checkUsersForApproverInput: {
          accountId: selectedAccounts.billTo?.id ?? '',
          userId: props.user.id
        }
      }
    }).catch(() => {
      pushAlert(t('user.userDeleteError'), {
        variant: 'error'
      });
    });
  };
  const handleDeleteDialogClose = () => setDeleteDialogOpen(false);
  const handlePurchaseApproverDialogClose = () =>
    setPurchaseApproverDialogOpen(false);

  const handleReject = async (reason: string, description: string) => {
    try {
      await rejectUser({
        variables: {
          rejectUserInput: {
            accountRequestId: props.user.id,
            rejectionReasonType: reason,
            customRejectionReason: description
          }
        }
      });
      pushAlert(t('user.userRejectSuccess'), { variant: 'success' });
      if (props.customerApproval) {
        history.push('/customer-approval');
      } else history.push(userManagementURL);
    } catch {
      pushAlert(t('user.userRejectError'), {
        variant: 'error'
      });
    }
  };

  const handleApprove = async () => {
    if (!selectedRole.id) {
      pushAlert(t('user.mustSelectRole'), { variant: 'warning' });
      return;
    } else if (
      selectedRole.id === requiresApproverRoleId &&
      !selectedRole.approverId
    ) {
      pushAlert(t('user.mustSelectApprover'), { variant: 'warning' });
      return;
    }

    try {
      await approveUser();
      pushAlert(t('user.userAddSuccess'), {
        variant: 'success'
      });
      if (props.customerApproval) {
        history.push('/customer-approval');
      } else history.push(userManagementURL);
      trackApproval({
        user: props.user.email,
        firstName: props.user.firstName,
        lastName: props.user.lastName,
        phoneNumber: props.user.phone
      });
    } catch (e) {
      pushAlert((e as any).message, {
        variant: 'error'
      });
    }
  };

  const handleDelete = async (leftCompanyBoolean: boolean) => {
    try {
      await deleteUser({
        variables: {
          deleteUserInput: {
            accountId: selectedAccounts?.billTo?.id ?? '',
            userId: props.user.id,
            userLeftCompany: leftCompanyBoolean
          }
        }
      });
      pushAlert(t('user.userDeleteSuccess'), { variant: 'success' });
      history.push(userManagementURL);
    } catch {
      pushAlert(t('user.userDeleteError'), {
        variant: 'error'
      });
    }
  };

  return (
    <>
      <RejectionDialog
        open={rejectionModalOpen}
        onClose={handleRejectionDialogClose}
        onSubmit={handleReject}
        rejectionReasons={
          (rejectionReasonsQuery?.rejectionReasons ?? []) as RejectionReason[]
        }
      />
      <PurchaseApproverDialog
        open={purchaseApproverDialogOpen}
        onClose={handlePurchaseApproverDialogClose}
        user={props.user}
        loading={usersForApproversLoading}
        usersForApprover={usersForApproversData?.checkUsersForApprover?.users}
      />
      <DeleteDialog
        open={deleteModalOpen}
        onClose={handleDeleteDialogClose}
        onSubmit={handleDelete}
        user={props.user}
        loading={deleteUserLoading}
      />
      <Card square={isSmallScreen} sx={{ mb: isSmallScreen ? 2 : 4 }}>
        <CardHeader
          title={t('user.userInformation')}
          subheader={props.user.roleId ? null : t('user.awaitingApproval')}
          titleTypographyProps={{
            display: isSmallScreen ? 'block' : 'inline',
            variant: 'h4',
            'data-testid': 'user-information-label'
          }}
          subheaderTypographyProps={{
            color: 'error',
            display: 'inline',
            sx: { ml: isSmallScreen ? 0 : 2 },
            variant: 'body1',
            'data-testid': 'awaiting-approval-label'
          }}
          action={
            props.user.roleId ? (
              <>
                {profile?.permissions.includes(Permission.REFRESH_CONTACT) &&
                Configuration.environment !== 'production' ? (
                  <IconButton
                    onClick={() => refreshContact()}
                    disabled={refreshContactLoading}
                    size="large"
                    data-testid="refresh-user-button"
                  >
                    <RefreshFullIcon />
                  </IconButton>
                ) : null}
                <IconButton
                  onClick={props.onEditClicked}
                  size="large"
                  data-testid="edit-user-button"
                >
                  <EditIcon />
                </IconButton>
                <IconButton
                  onClick={handleDeleteDialogOpened}
                  size="large"
                  data-testid="delete-user-button"
                >
                  <DeleteBinIcon />
                </IconButton>
              </>
            ) : null
          }
        />
        {isSmallScreen ? <Divider /> : null}
        <CardContent sx={{ p: isSmallScreen ? 0 : undefined }}>
          <Grid direction="column" container>
            <Grid container sx={{ px: isSmallScreen ? 3 : 0 }}>
              {profile?.permissions.includes(Permission.REFRESH_CONTACT) &&
              Configuration.environment !== 'production' ? (
                <Grid item xs={12} sx={{ pb: 3 }}>
                  {props.user.contactUpdatedAt
                    ? `Contact last updated by ${format(
                        new Date(props.user.contactUpdatedAt),
                        'MM/dd/yy hh:mm a'
                      )} by ${props.user?.contactUpdatedBy}`
                    : 'Contact last updated before 01/05/22'}
                </Grid>
              ) : null}
              <Grid item md={6} xs={12} sx={{ pr: isSmallScreen ? 0 : 2 }}>
                <List>
                  {/* User First Name */}
                  <ViewTableListItem
                    label={t('common.firstName')}
                    value={props.user.firstName}
                    dataTestId="user-first-name"
                  />
                  <Divider />
                  {/* User Last Name */}
                  <ViewTableListItem
                    label={t('common.lastName')}
                    value={props.user.lastName}
                    dataTestId="user-last-name"
                  />
                  <Divider />
                  {props.customerApproval ? (
                    <>
                      {/* User Phone Number */}
                      <ViewTableListItem
                        label={t('common.phoneNumber')}
                        value={props.user.phone}
                        dataTestId="user-phone-number"
                      />
                      <Divider />
                      {/* User Email Address */}
                      <ViewTableListItem
                        label={t('common.emailAddress')}
                        value={props.user.email}
                        dataTestId="user-email-address"
                      />
                    </>
                  ) : (
                    <>
                      {/* User Phone Number */}
                      <ViewTableListItem
                        label={t('common.phoneType')}
                        value={t(
                          `common.${props.user.phoneType?.toLowerCase()}`
                        )}
                        dataTestId="user-phone-type"
                      />
                    </>
                  )}
                  {isSmallScreen ? <Divider /> : null}
                </List>
              </Grid>
              <Grid item md={6} xs={12} sx={{ pl: isSmallScreen ? 0 : 2 }}>
                <List>
                  {props.customerApproval ? (
                    <>
                      {/* User Account Number */}
                      <ViewTableListItem
                        label={t('common.accountNumber')}
                        value={props.user.accountNumber ?? ''}
                        dataTestId="user-account-number"
                      />
                      <Divider />
                      {/* User Company */}
                      <ViewTableListItem
                        label={t('common.company')}
                        value={props.user.company}
                        dataTestId="user-company"
                      />
                      <Divider />
                      {/* User Branch ID*/}
                      <ViewTableListItem
                        label={t('common.branch')}
                        value={props.user.branchId ?? ''}
                        dataTestId="user-branch-id"
                      />
                    </>
                  ) : (
                    <>
                      {/* User Email Address */}
                      <ViewTableListItem
                        label={t('common.emailAddress')}
                        value={props.user.email}
                        dataTestId="user-email-address"
                      />
                      <Divider />
                      {/* User Phone Number */}
                      <ViewTableListItem
                        label={t('common.phoneNumber')}
                        value={props.user.phone}
                        dataTestId="user-phone-number"
                      />
                    </>
                  )}
                </List>
              </Grid>
            </Grid>
            <Box pt={isSmallScreen ? 0 : 4} pb={4}>
              <Divider />
            </Box>
            <Box px={isSmallScreen ? 3 : 0} pb={isSmallScreen ? 3 : 0}>
              <Roles
                disabled={Boolean(props.user.roleId)}
                value={selectedRole}
                onChange={onRoleChanged}
                data-testid="user-role-change"
                customerApproval={props.customerApproval}
              />
            </Box>
          </Grid>
        </CardContent>
      </Card>
      {!props.user.roleId ? (
        <Box px={isSmallScreen ? 3 : 0} py={3}>
          <Button
            disabled={approveUserLoading}
            fullWidth={isSmallScreen}
            onClick={handleApprove}
            variant="alternative"
            data-testid="approve-user-button"
          >
            {approveUserLoading ? (
              <CircularProgress size={20} />
            ) : (
              t('user.approveUser')
            )}
          </Button>
          <Button
            fullWidth={isSmallScreen}
            color="primaryLight"
            variant="text"
            onClick={handleRejectionDialogOpened}
            data-testid="reject-user-button"
          >
            {t('user.rejectUser')}
          </Button>
        </Box>
      ) : null}
    </>
  );

  /**
   * Effect Defs
   */

  function setRoleToAdmin() {
    if (props.customerApproval) {
      const adminRole = rolesQuery?.roles?.find(
        (r) => r?.name === 'Account Admin'
      )?.id;
      if (adminRole) setSelectedRole({ id: adminRole });
    }
  }
}

export default ViewUser;
