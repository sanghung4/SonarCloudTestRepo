import React, { useMemo, useState } from 'react';

import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  CircularProgress,
  Grid,
  Select,
  MenuItem,
  TextField,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useFormik } from 'formik';
import { isEqual } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';
import * as Yup from 'yup';

import InputMask from 'common/InputMask';
import {
  useGetPhoneTypesQuery,
  useUpdateUserMutation,
  useDeleteUserMutation,
  useCheckUsersForApproverMutation
} from 'generated/graphql';
import { UserInfo } from 'User';
import DeleteDialog from 'User/DeleteDialog';
import ConfirmCancelDialog from 'User/ConfirmCancelDialog';
import Roles, { RoleInfo } from 'User/Roles';
import { phoneMask } from 'utils/masks';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import PurchaseApproverDialog from './PurchaseApproverDialog';

export type Props = {
  onCancel: () => void;
  loggedInId?: string;
  user: UserInfo;
};

type State = {
  tab?: string;
};

function EditUser(props: Props) {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { state } = useLocation<State>();
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * State
   */
  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [deleteModalOpen, setDeleteDialogOpen] = useState(false);
  const [purchaseApproverDialogOpen, setPurchaseApproverDialogOpen] =
    useState(false);

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: phoneTypesQuery } = useGetPhoneTypesQuery();
  const [updateUser, { loading: updateUserLoading }] = useUpdateUserMutation();
  const [deleteUser] = useDeleteUserMutation();
  const [
    checkUsersForApproverMutation,
    { data: usersForApproversData, loading: usersForApproversLoading }
  ] = useCheckUsersForApproverMutation({
    fetchPolicy: 'no-cache',
    onCompleted: (data) => {
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

  /**
   * Memo
   */
  const accountId = useMemo(
    () => selectedAccounts.billTo?.id,
    [selectedAccounts]
  );

  const initialValues = useMemo(
    () => ({
      firstName: props.user.firstName,
      lastName: props.user.lastName,
      phone: props.user.phone,
      email: props.user.email,
      roleId: props.user.roleId,
      approverId: props.user.approverId,
      phoneTypeId: props.user.phoneType
    }),
    [props]
  );

  const userManagementURL = useMemo(() => {
    return `/user-management${
      state.tab ? `?tab=${state.tab.split(' ').join('+')}` : ''
    }`;
  }, [state.tab]);

  /**
   * Form
   */
  const formik = useFormik({
    initialValues,
    validationSchema: Yup.object({
      firstName: Yup.string().required(t('validation.firstNameRequired')),
      lastName: Yup.string().required(t('validation.lastNameRequired')),
      phone: Yup.string().matches(/^\(\d{3}\) \d{3}-\d{4}$/, {
        message: t('validation.phoneNumberInvalid')
      }),
      email: Yup.string().email('Invalid Email Address'),
      roleId: Yup.string().required(),
      approverId: Yup.string().nullable().notRequired(),
      phoneTypeId: Yup.string().required()
    }),
    onSubmit: async (values) => {
      try {
        await updateUser({
          variables: {
            updateUserInput: {
              userId: props.user.id,
              accountId,
              firstName: values.firstName,
              lastName: values.lastName,
              phoneNumber: values.phone,
              email: values.email,
              roleId: values.roleId,
              approverId: values.approverId,
              phoneTypeId: values.phoneTypeId
            }
          }
        });
        pushAlert(t('user.informationSaved'), {
          variant: 'success'
        });
        history.push(userManagementURL);
      } catch (e) {
        pushAlert(t('user.informationFailed'), {
          variant: 'error'
        });
      }
    }
  });

  /**
   * Callbacks
   */
  const handleCancel = () => {
    if (!isEqual(initialValues, formik.values)) {
      setCancelDialogOpen(true);
    } else {
      props.onCancel();
    }
  };

  const handleCloseCancelDialog = (shouldSave: boolean) => {
    if (shouldSave === true) {
      setCancelDialogOpen(false);
      formik.submitForm();
    } else {
      props.onCancel();
    }
  };

  const handleRoleChange = (info: RoleInfo) => {
    formik.setFieldValue('roleId', info.id);
    formik.setFieldValue('approverId', info.approverId);
  };

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

  // TODO extract this logic so its not repetitive code
  const handleDelete = async (leftCompanyBoolean: boolean) => {
    try {
      await deleteUser({
        variables: {
          deleteUserInput: {
            accountId: selectedAccounts.billTo?.id ?? '',
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
      <ConfirmCancelDialog
        open={cancelDialogOpen}
        onClose={handleCloseCancelDialog}
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
      />
      <Box
        sx={{
          bgcolor: isSmallScreen ? 'primary.contrastText' : undefined
        }}
      >
        <form onSubmit={formik.handleSubmit} noValidate>
          {isSmallScreen ? (
            <Box p={3}>
              <Typography component="h4" variant="h4">
                {t('user.userInformation')}
              </Typography>
            </Box>
          ) : (
            <Card raised={false}>
              <CardHeader
                title={t('user.userInformation')}
                subheader={props.user.roleId ? null : t('user.unapprovedUser')}
                titleTypographyProps={{
                  display: isSmallScreen ? 'block' : 'inline',
                  variant: 'h4'
                }}
                subheaderTypographyProps={{
                  color: 'error',
                  display: 'inline',
                  sx: { ml: isSmallScreen ? 0 : 2 },
                  variant: 'body1'
                }}
                action={
                  <Button
                    onClick={handleDeleteDialogOpened}
                    variant="text"
                    data-testid="edit-user-delete-user-button"
                  >
                    {t('user.deleteUser')}
                  </Button>
                }
              />
            </Card>
          )}
          <Box p={3} py={isSmallScreen ? 1 : 4}>
            <Grid container spacing={isSmallScreen ? 0 : 3}>
              <Grid item xs={12}>
                <Typography variant="caption">
                  {t('user.nameChange')}
                </Typography>
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  id="firstName"
                  label={t('common.firstName')}
                  name="firstName"
                  type="text"
                  value={formik.values.firstName}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(
                    formik.touched.firstName && formik.errors.firstName
                  )}
                  helperText={
                    formik.touched.firstName && formik.errors.firstName
                      ? formik.errors.firstName
                      : ' '
                  }
                  FormHelperTextProps={{
                    //@ts-ignore
                    'data-testid': 'firstNameHelperText'
                  }}
                  required
                  fullWidth
                  inputProps={{ 'data-testid': 'edit-user-first-name-input' }}
                />
                <TextField
                  id="lastName"
                  label={t('common.lastName')}
                  name="lastName"
                  type="text"
                  value={formik.values.lastName}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(
                    formik.touched.lastName && formik.errors.lastName
                  )}
                  helperText={
                    formik.touched.lastName && formik.errors.lastName
                      ? formik.errors.lastName
                      : ' '
                  }
                  FormHelperTextProps={{
                    //@ts-ignore
                    'data-testid': 'lastNameHelperText'
                  }}
                  required
                  fullWidth
                  inputProps={{ 'data-testid': 'edit-user-last-name-input' }}
                />
                {phoneTypesQuery?.phoneTypes ? (
                  <Select
                    id="phone-type"
                    data-testid="phoneTypeDropdown"
                    name="phoneTypeId"
                    label={t('common.phoneType')}
                    placeholder="Select"
                    value={formik.values.phoneTypeId}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={Boolean(
                      formik.touched.phoneTypeId && formik.errors.phoneTypeId
                    )}
                    helperText={
                      formik.touched.phoneTypeId && formik.errors.phoneTypeId
                        ? formik.errors.phoneTypeId
                        : ' '
                    }
                    renderValue={(id) => {
                      const selectedType = phoneTypesQuery?.phoneTypes
                        .find((pt) => pt === id)
                        ?.toLowerCase();

                      return selectedType
                        ? t(`common.${selectedType}`)
                        : 'Select';
                    }}
                    fullWidth
                    required
                    inputProps={{ 'data-testid': 'edit-user-phone-type-input' }}
                  >
                    {phoneTypesQuery?.phoneTypes.length
                      ? phoneTypesQuery.phoneTypes.map((pt) => (
                          <MenuItem value={pt} key={pt}>
                            {t(`common.${pt.toLowerCase()}`)}
                          </MenuItem>
                        ))
                      : null}
                  </Select>
                ) : null}
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  id="email"
                  label={t('common.emailAddress')}
                  name="email"
                  type="text"
                  value={formik.values.email}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(formik.touched.email && formik.errors.email)}
                  helperText={
                    formik.touched.email && formik.errors.email
                      ? formik.errors.email
                      : ' '
                  }
                  fullWidth
                  disabled={props.user.id !== props.loggedInId}
                  inputProps={{ 'data-testid': 'edit-user-email-address' }}
                />
                <TextField
                  id="phone"
                  label={t('common.phoneNumber')}
                  name="phone"
                  type="text"
                  value={formik.values.phone}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(formik.touched.phone && formik.errors.phone)}
                  helperText={
                    formik.touched.phone && formik.errors.phone
                      ? formik.errors.phone
                      : ' '
                  }
                  FormHelperTextProps={{
                    //@ts-ignore
                    'data-testid': 'phoneNumberHelperText'
                  }}
                  InputProps={{
                    inputComponent: InputMask as any
                  }}
                  inputProps={{
                    ...phoneMask,
                    'data-testid': 'edit-user-phone-number-input'
                  }}
                  fullWidth
                />
              </Grid>
            </Grid>
          </Box>
          <Card square={isSmallScreen} raised={isSmallScreen}>
            <CardContent>
              <Roles
                userId={props.user.id}
                value={{
                  id: formik.values.roleId,
                  approverId: formik.values.approverId
                }}
                onChange={handleRoleChange}
              />
            </CardContent>
          </Card>
          <Box px={isSmallScreen ? 3 : 0} py={3}>
            <Button
              disabled={updateUserLoading}
              fullWidth={isSmallScreen}
              type="submit"
              variant="alternative"
              data-testid="edit-user-save-user-button"
            >
              {updateUserLoading ? (
                <CircularProgress size={20} />
              ) : (
                t('common.save')
              )}
            </Button>
            <Button
              fullWidth={isSmallScreen}
              variant="text"
              onClick={handleCancel}
              data-testid="edit-user-cancel-button"
            >
              {t('common.cancel')}
            </Button>
          </Box>
        </form>
      </Box>
    </>
  );
}

export default EditUser;
