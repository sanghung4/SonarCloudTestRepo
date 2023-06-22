import React, { useState } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Container,
  Dialog,
  DialogContent,
  DialogTitle,
  Grid,
  IconButton,
  TextField,
  Typography,
  useScreenSize,
  useSnackbar,
  Alert
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';
import { useFormik } from 'formik';
import * as Yup from 'yup';

import { useUpdateUserPasswordMutation } from 'generated/graphql';
import { CloseIcon, InfoIcon } from 'icons';
import { UserInfo } from 'User';
import { formatOktaErrors } from 'utils/formatOktaErrors';
import useUsernameParts from 'hooks/useUsernameParts';

type Props = {
  open: boolean;
  onClose: (save: boolean) => void;
  user: UserInfo;
};

function UpdatePasswordDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const history = useHistory();
  const usernamePartsToMatch = useUsernameParts(props.user?.email);
  const [message, setMessage] = useState('');

  /**
   * Callbacks
   */
  const handleSave = () => props.onClose(true);
  const handleCancel = () => props.onClose(false);

  /**
   * Data
   */
  const [updateUserPassword, { loading: updatePasswordLoading }] =
    useUpdateUserPasswordMutation();

  /**
   * Form
   */
  const formik = useFormik({
    initialValues: {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    },
    validationSchema: Yup.object({
      oldPassword: Yup.string()
        .required(t('validation.passwordRequired'))
        .test('isValid', t('validation.passwordInvalid'), isValidOldPassword),
      newPassword: Yup.string()
        .required(t('validation.passwordRequired'))
        .test('isValid', t('validation.passwordInvalid'), isValidNewPassword),
      confirmPassword: Yup.string()
        .required(t('validation.confirmPasswordRequired'))
        .oneOf([Yup.ref('newPassword'), ''], t('validation.passwordNoMatch'))
    }),
    onSubmit
  });

  /**
   * Output
   */
  return (
    <Dialog
      open={props.open}
      onClose={props.onClose}
      maxWidth="sm"
      fullWidth
      fullScreen={isSmallScreen}
    >
      <DialogTitle>
        <IconButton
          onClick={handleCancel}
          size="large"
          sx={(theme) => ({
            position: 'absolute',
            top: theme.spacing(3.5),
            right: theme.spacing(1)
          })}
        >
          <CloseIcon />
        </IconButton>
        <Box mt={3} pl={3}>
          <Typography variant="h5" color="primary" align="left">
            {t('user.changePassword')}
          </Typography>
        </Box>
      </DialogTitle>
      <DialogContent>
        {Boolean(message) && (
          <Alert
            severity="error"
            sx={{
              position: 'absolute',
              width: '100%',
              bottom: 10,
              left: 0,
              zIndex: 99999
            }}
          >
            {message}
          </Alert>
        )}
        <Container sx={{ pt: 2, pb: 4 }}>
          <form onSubmit={formik.handleSubmit} noValidate>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  id="old-password"
                  type="password"
                  name="oldPassword"
                  label={t('common.oldPassword')}
                  placeholder={'............'}
                  value={formik.values.oldPassword}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(
                    formik.touched.oldPassword && formik.errors.oldPassword
                  )}
                  helperText={
                    formik.touched.oldPassword && formik.errors.oldPassword
                      ? formik.errors.oldPassword
                      : ' '
                  }
                  inputProps={{ 'data-testid': 'old-password-input' }}
                  fullWidth
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  id="new-password"
                  type="password"
                  name="newPassword"
                  label={t('common.newPassword')}
                  placeholder={'............'}
                  value={formik.values.newPassword}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(
                    formik.touched.newPassword && formik.errors.newPassword
                  )}
                  helperText={
                    formik.touched.newPassword && formik.errors.newPassword
                      ? formik.errors.newPassword
                      : ' '
                  }
                  inputProps={{ 'data-testid': 'new-password-input' }}
                  fullWidth
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  id="confirm-password"
                  type="password"
                  name="confirmPassword"
                  label={t('common.confirmNewPassword')}
                  placeholder={'............'}
                  value={formik.values.confirmPassword}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(
                    formik.touched.confirmPassword &&
                      formik.errors.confirmPassword
                  )}
                  helperText={
                    formik.touched.confirmPassword &&
                    formik.errors.confirmPassword
                      ? formik.errors.confirmPassword
                      : ' '
                  }
                  inputProps={{ 'data-testid': 'confirm-password-input' }}
                  fullWidth
                  required
                />
              </Grid>
            </Grid>
            <Grid container item xs={12} direction="column">
              <Typography
                variant="body1"
                color={
                  (formik.touched.newPassword &&
                    formik.errors.newPassword === 'Invalid Password') ||
                  (formik.touched.confirmPassword &&
                    formik.errors.confirmPassword === 'Invalid Password')
                    ? 'error'
                    : 'inherit'
                }
                sx={{ display: 'flex', alignItems: 'center', pt: 2 }}
              >
                <Box component={InfoIcon} sx={{ mr: 1 }} />
                {t('register.passwordRequirements')}
              </Typography>
              <Box pt={1} pl={4} sx={{ color: 'secondary02.main' }}>
                <Typography variant="caption" component="p">
                  {t('register.minimum')}
                </Typography>
                <Typography variant="caption" component="p">
                  {t('register.uppercase')}
                </Typography>
                <Typography variant="caption" component="p">
                  {t('register.number')}
                </Typography>
              </Box>
            </Grid>
            <Box pt={3}>
              <Grid container item justifyContent="center">
                <Grid item xs={6}>
                  <Button onClick={handleCancel} variant="text" fullWidth>
                    {t('common.cancel')}
                  </Button>
                </Grid>
                <Grid item xs={6}>
                  <Button
                    type="submit"
                    fullWidth
                    data-testid="submitChangesButton"
                    onClick={() => formik.submitForm}
                    disabled={updatePasswordLoading}
                  >
                    {updatePasswordLoading ? (
                      <CircularProgress size={20} />
                    ) : (
                      t('common.submitChanges')
                    )}
                  </Button>
                </Grid>
              </Grid>
            </Box>
          </form>
        </Container>
      </DialogContent>
    </Dialog>
  );

  /**
   * Form Hoists
   */
  function isValidOldPassword(oldPassword: string | null | undefined) {
    return /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/.test(
      oldPassword || ''
    );
  }

  function isValidNewPassword(newPassword: string | null | undefined) {
    // Okta password should not contains username
    const oktaPasswordResult = !usernamePartsToMatch.some((part) =>
      (newPassword ?? '').toLowerCase().includes(part)
    );
    return (
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/.test(
        newPassword || ''
      ) && oktaPasswordResult
    );
  }

  async function onSubmit(p: { newPassword: string; oldPassword: string }) {
    try {
      await updateUserPassword({
        variables: {
          updateUserPasswordInput: {
            userId: props.user.id,
            oldUserPassword: p.oldPassword,
            newUserPassword: p.newPassword
          }
        }
      });
      pushAlert(t('user.informationSaved'), {
        variant: 'success'
      });
      history.push('/account');
      handleSave();
    } catch (error) {
      try {
        if (isSmallScreen) {
          setMessage(formatOktaErrors(error as string));
        } else {
          pushAlert(formatOktaErrors(error as string), { variant: 'error' });
        }
      } catch {
        console.error('Could not parse error response');
        if (isSmallScreen) {
          setMessage(t('user.updatePasswordFailed'));
        } else {
          pushAlert(t('user.updatePasswordFailed'), { variant: 'error' });
        }
      }
      if (isSmallScreen) {
        setTimeout(() => {
          setMessage('');
        }, 5000);
      }
    }
  }
}

export default UpdatePasswordDialog;
