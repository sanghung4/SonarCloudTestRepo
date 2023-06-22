import {
  Box,
  Button,
  Grid,
  Link,
  TextField,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import ForgotPasswordConfirmation from 'ForgotPassword/Confirm';
import useForgetPasswordData from 'ForgotPassword/util/useForgetPasswordData';
import useForgetPasswordEffect from 'ForgotPassword/util/useForgetPasswordEffect';
import useForgetPasswordForm from 'ForgotPassword/util/useForgetPasswordForm';
import useOnCreatePassword from 'ForgotPassword/util/useOnCreatePassword';
import CreatePassword from 'Register-old/CreatePassword';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { ForgetPasswordContainer } from 'ForgotPassword/util/styles';

export type AuthParam = {
  email?: string;
  recoveryToken?: string;
};

export default function ForgotPassword() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('common.resetPassword'));

  /**
   * Data
   */
  const {
    email,
    isInviteSentQuery,
    requestSent,
    setEmail,
    setTransaction,
    transaction
  } = useForgetPasswordData();

  /**
   * Form
   */
  const formik = useForgetPasswordForm({ isInviteSentQuery, setEmail });

  /**
   * Effects
   */
  useForgetPasswordEffect({ setEmail, setTransaction, transaction });

  /**
   * Callbacks
   */
  const onCreatePassword = useOnCreatePassword(transaction);

  /**
   * Render
   */
  return transaction ? (
    <Box pt={2} mx={isSmallScreen ? 4 : 3}>
      <CreatePassword onCreatePassword={onCreatePassword} email={email} />
    </Box>
  ) : (
    <ForgetPasswordContainer maxWidth="sm">
      <Box
        component="form"
        onSubmit={formik.handleSubmit}
        noValidate
        height={1}
      >
        <Grid
          container
          direction="column"
          alignItems="center"
          height={1}
          px={isSmallScreen ? 2 : 5}
        >
          <Grid item>
            <Typography
              color="primary"
              component="h1"
              variant={isSmallScreen ? 'h4' : 'h3'}
              pb={isSmallScreen ? 4 : 3}
              fontWeight={900}
            >
              {t('common.resetPassword')}
            </Typography>
          </Grid>
          {!requestSent ? (
            <>
              <Grid item>
                <Typography variant="body1" textAlign="center">
                  {t('forgotPassword.enterEmail')}
                </Typography>
              </Grid>
              <Grid item width={1} pt={5} pb={4}>
                <TextField
                  id="email"
                  label={t('common.emailAddress')}
                  placeholder={t('common.enterEmailAddress')}
                  name="email"
                  type="text"
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  value={formik.values.email}
                  error={!!(formik.touched.email && formik.errors.email)}
                  helperText={
                    formik.touched.email && formik.errors.email
                      ? formik.errors.email
                      : ' '
                  }
                  inputProps={{ 'data-testid': 'email-address-input' }}
                  fullWidth
                  required
                />
              </Grid>
              <Grid item width={250}>
                <Button
                  type="submit"
                  data-testid="forgot-password-submit-button"
                  fullWidth
                >
                  {t('common.submit')}
                </Button>
              </Grid>
              <Grid container justifyContent="center" pt={2}>
                <Typography component="span" mr={1.5}>
                  {t('common.needAccount')}
                </Typography>
                <Link to="/register" component={RouterLink}>
                  <Box
                    component={Button}
                    color="primaryLight"
                    variant="inline"
                    data-testid="register-button"
                    minWidth="auto"
                  >
                    {t('common.register')}
                  </Box>
                </Link>
              </Grid>
            </>
          ) : (
            <ForgotPasswordConfirmation
              email={formik.values.email}
              onResendClicked={formik.submitForm}
            />
          )}
        </Grid>
      </Box>
    </ForgetPasswordContainer>
  );
}
