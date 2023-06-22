import React, { useMemo, useState } from 'react';

import {
  Box,
  Button,
  Card,
  CardHeader,
  CircularProgress,
  Grid,
  MenuItem,
  Select,
  TextField,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { isEqual } from 'lodash-es';

import {
  useGetPhoneTypesQuery,
  useUpdateUserMutation
} from 'generated/graphql';
import { phoneMask } from 'utils/masks';
import InputMask from 'common/InputMask';
import ConfirmEmailChangeDialog from 'Account/ConfirmEmailChangeDialog';
import ConfirmCancelDialog from 'User/ConfirmCancelDialog';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

interface Props {
  user: any;
  onFinished: (values?: any) => void;
}

function EditAccount(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * State
   */
  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [confirmEmailDialogOpen, setConfirmEmailDialogOpen] = useState(false);
  const [submitForm, setSubmitForm] = useState(false);

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: phoneTypesQuery } = useGetPhoneTypesQuery();
  const [updateUser, { loading: updateUserLoading }] = useUpdateUserMutation();

  /**
   * Memo
   */
  const initialValues = useMemo(
    () => ({
      firstName: props.user.firstName,
      lastName: props.user.lastName,
      phoneNumber: props.user.phoneNumber,
      phoneTypeId: props.user.phoneTypeId,
      email: props.user.email
    }),
    [props]
  );
  const accountId = useMemo(
    () => selectedAccounts.billTo?.id,
    [selectedAccounts]
  );

  /**
   * Form
   */
  const formik = useFormik({
    initialValues,
    validationSchema: Yup.object({
      firstName: Yup.string().required(t('validation.firstNameRequired')),
      lastName: Yup.string().required(t('validation.lastNameRequired')),
      phoneNumber: Yup.string().matches(/^\(\d{3}\) \d{3}-\d{4}$/, {
        message: t('validation.phoneNumberInvalid')
      }),
      email: Yup.string()
        .email(t('validation.emailInvalidVerbose'))
        .required(t('validation.emailRequired')),
      phoneTypeId: Yup.string().required()
    }),
    onSubmit: async (values) => {
      if (values.email !== props.user.email && submitForm === false) {
        setConfirmEmailDialogOpen(true);
      } else {
        try {
          await updateUser({
            variables: {
              updateUserInput: {
                accountId,
                userId: props.user.id,
                firstName: values.firstName,
                lastName: values.lastName,
                phoneNumber: values.phoneNumber,
                email: values.email,
                phoneTypeId: values.phoneTypeId
              }
            }
          });
          pushAlert(t('user.accountInformationUpdated'), {
            variant: 'success'
          });
          props.onFinished(values);
        } catch (error) {
          try {
            pushAlert(JSON.parse((error as any).message).error, {
              variant: 'error'
            });
          } catch {
            console.error('Could not parse error response');
            pushAlert(t('user.informationFailed'), {
              variant: 'error'
            });
          }
        }
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
      props.onFinished();
    }
  };

  const handleCloseCancelDialog = (shouldSave: boolean) => {
    if (shouldSave === true) {
      setCancelDialogOpen(false);
      setSubmitForm(true);
      formik.submitForm();
    } else {
      props.onFinished();
    }
  };

  const handleCloseConfirmEmailDialog = (shouldSave: boolean) => {
    if (shouldSave === true) {
      setConfirmEmailDialogOpen(false);
      setSubmitForm(true);
      formik.submitForm();
    } else {
      props.onFinished();
    }
  };

  return (
    <Box data-testid="editaccount-component">
      <ConfirmCancelDialog
        open={cancelDialogOpen}
        onClose={handleCloseCancelDialog}
      />
      <ConfirmEmailChangeDialog
        open={confirmEmailDialogOpen}
        onClose={handleCloseConfirmEmailDialog}
        user={props.user}
        email={formik.values.email}
      />
      {isSmallScreen ? (
        <Box pl={2}>
          <Typography
            component="h4"
            variant="h4"
            data-testid="account-info-title"
          >
            {t('common.accountInformation')}
          </Typography>
        </Box>
      ) : (
        <Card square={isSmallScreen} raised={!isSmallScreen}>
          <Box p={1}>
            <CardHeader
              title={t('common.accountInformation')}
              titleTypographyProps={{ variant: isSmallScreen ? 'h5' : 'h4' }}
              data-testid="account-info-title"
            />
          </Box>
        </Card>
      )}
      <Box
        component="form"
        sx={{ mt: 3, pl: isSmallScreen ? 2 : 3, pr: isSmallScreen ? 2 : 6 }}
        onSubmit={formik.handleSubmit}
        noValidate
      >
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Box mb={isSmallScreen ? 1 : 0}>
              <Typography variant="caption">{t('user.nameChange')}</Typography>
            </Box>
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="firstName"
              data-testid="firstName"
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
              required
              fullWidth
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="lastName"
              data-testid="lastName"
              label={t('common.lastName')}
              name="lastName"
              type="text"
              value={formik.values.lastName}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(formik.touched.lastName && formik.errors.lastName)}
              helperText={
                formik.touched.lastName && formik.errors.lastName
                  ? formik.errors.lastName
                  : ' '
              }
              required
              fullWidth
            />
          </Grid>
          <Grid item xs={12} lg={6}>
            <TextField
              id="phoneNumber"
              data-testid="phoneNumber-input"
              label={t('common.phoneNumber')}
              name="phoneNumber"
              type="text"
              value={formik.values.phoneNumber}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(
                formik.touched.phoneNumber && formik.errors.phoneNumber
              )}
              helperText={
                formik.touched.phoneNumber && formik.errors.phoneNumber
                  ? formik.errors.phoneNumber
                  : ' '
              }
              inputProps={phoneMask}
              InputProps={{ inputComponent: InputMask as any }}
              required
              fullWidth
            />
          </Grid>
          <Grid item xs={12} lg={6}>
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

                  return selectedType ? t(`common.${selectedType}`) : 'Select';
                }}
                required
                fullWidth
                sx={{ mt: '0 !important' }}
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
          <Grid item xs={12}>
            <TextField
              id="email"
              data-testid="email-input"
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
              required
              fullWidth
            />
          </Grid>
          <Grid item>
            <Button
              disabled={updateUserLoading}
              type="submit"
              variant="alternative"
              data-testid="submit-button"
            >
              {updateUserLoading ? (
                <CircularProgress size={20} />
              ) : (
                t('common.save')
              )}
            </Button>
            <Button variant="text" onClick={handleCancel}>
              {t('common.cancel')}
            </Button>
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
}

export default EditAccount;
