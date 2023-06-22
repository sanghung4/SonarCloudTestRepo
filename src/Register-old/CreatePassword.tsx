import React from 'react';

import {
  Box,
  Checkbox,
  CircularProgress,
  FormControlLabel,
  Grid,
  Link,
  LoadingButton,
  TextField,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import * as Yup from 'yup';

import { UserInput } from 'generated/graphql';
import { InfoIcon } from 'icons';
import useUsernameParts from 'hooks/useUsernameParts';

interface Props {
  loading?: boolean;
  userInput?: UserInput;
  onCreatePassword: (password: string) => void;
  email?: string;
}

function CreatePassword(props: Props) {
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const usernamePartsToMatch = useUsernameParts(
    props.userInput?.email || props.email
  );

  /**
   * Form
   */
  const formik = useFormik({
    initialValues: {
      password: '',
      confirmPassword: '',
      termsOfSale: false,
      privacyPolicy: false
    },
    validationSchema: Yup.object({
      password: Yup.string()
        .required(t('validation.passwordRequired'))
        .test('isValid', t('validation.passwordInvalid'), isValid),
      confirmPassword: Yup.string()
        .required(t('validation.confirmPasswordRequired'))
        .oneOf([Yup.ref('password'), ''], t('validation.passwordNoMatch')),
      termsOfSale: Yup.boolean().oneOf([true]),
      privacyPolicy: Yup.boolean().oneOf([true])
    }),
    onSubmit: ({ password }) => {
      props.onCreatePassword(password);
    }
  });

  /**
   * Output
   */
  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      pt={isSmallScreen ? 3 : 6}
      pb={isSmallScreen ? 6 : 0}
    >
      <Typography
        variant={isSmallScreen ? 'h4' : 'h3'}
        component="h1"
        color="primary"
        sx={{ fontWeight: 700 }}
      >
        {t('register.createPassword')}
      </Typography>
      {props.userInput?.email ? (
        <Typography variant="body1" color="textSecondary" sx={{ pt: 3 }}>
          {t('common.username')}: {props.userInput?.email}
        </Typography>
      ) : null}
      <Box
        component="form"
        onSubmit={formik.handleSubmit}
        noValidate
        sx={{ pt: 3 }}
      >
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              autoFocus
              id="password"
              data-testid="passwordInput"
              type="password"
              name="password"
              label={t('common.password')}
              placeholder={t('common.enterPassword')}
              value={formik.values.password}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(formik.touched.password && formik.errors.password)}
              helperText={
                formik.touched.password && formik.errors.password
                  ? formik.errors.password
                  : ' '
              }
              inputProps={{
                'data-testid': 'registration-password-input'
              }}
              FormHelperTextProps={{
                // @ts-ignore
                'data-testid': 'password-msg'
              }}
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="confirm-password"
              type="password"
              name="confirmPassword"
              label={t('register.confirmPassword')}
              placeholder={t('register.confirmPassword')}
              value={formik.values.confirmPassword}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(
                formik.touched.confirmPassword && formik.errors.confirmPassword
              )}
              helperText={
                formik.touched.confirmPassword && formik.errors.confirmPassword
                  ? formik.errors.confirmPassword
                  : ' '
              }
              inputProps={{
                'data-testid': 'registration-confirm-password-input'
              }}
              fullWidth
              required
            />
          </Grid>
        </Grid>
        <Grid container item xs={12} direction="column">
          <Typography
            variant="body1"
            sx={{
              display: 'flex',
              alignItems: 'center',
              pt: 2,
              color:
                (formik.touched.password &&
                  formik.errors.password === 'Invalid password') ||
                (formik.touched.confirmPassword &&
                  formik.errors.confirmPassword === 'Invalid password')
                  ? 'error.main'
                  : 'secondary02.main'
            }}
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
        <Box mt={4.5}>
          <Grid
            container
            item
            xs={12}
            direction="column"
            justifyContent="center"
            spacing={1}
          >
            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Checkbox
                    data-testid="terms-of-sale-checkbox"
                    color="primary"
                    size="small"
                    name="termsOfSale"
                    value={formik.values.termsOfSale}
                    checked={formik.values.termsOfSale}
                    onChange={formik.handleChange}
                    sx={{ my: -1 }}
                  />
                }
                label={
                  <Typography variant="body1">
                    {t('common.agreeTo')}&nbsp;
                    <Link
                      to="/terms-of-sale"
                      component={RouterLink}
                      underline="always"
                      target="_blank"
                      sx={{ color: 'primary02.main' }}
                      data-testid="terms-of-sale-link"
                    >
                      {t('common.termsOfSale')}
                    </Link>
                  </Typography>
                }
              />
            </Grid>
            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Checkbox
                    data-testid="privacy-policy-checkbox"
                    color="primary"
                    size="small"
                    name="privacyPolicy"
                    value={formik.values.privacyPolicy}
                    checked={formik.values.privacyPolicy}
                    onChange={formik.handleChange}
                    sx={{ my: -1 }}
                  />
                }
                label={
                  <Typography variant="body1">
                    {t('common.agreeTo')}&nbsp;
                    <Link
                      to="/privacy-policy"
                      component={RouterLink}
                      underline="always"
                      target="_blank"
                      sx={{ color: 'primary02.main' }}
                      data-testid="privacy-policy-link"
                    >
                      {t('common.privacyPolicy')}
                    </Link>
                  </Typography>
                }
              />
            </Grid>
            <Grid item xs={12}>
              {formik.touched.termsOfSale &&
                formik.touched.privacyPolicy &&
                (formik.errors.termsOfSale || formik.errors.privacyPolicy) && (
                  <Box ml={3.375} color="orangeRed.main">
                    <Typography
                      color="error"
                      variant="caption"
                      data-testid="agree-to-terms-error-msg"
                    >
                      {t('validation.termsPrivacyRequired')}
                    </Typography>
                  </Box>
                )}
            </Grid>
          </Grid>
        </Box>
        <Grid container item xs={12} justifyContent="center">
          <Box
            width={isSmallScreen ? 0.75 : 0.5}
            mt={isSmallScreen ? 3 : 4}
            pb={4}
          >
            <LoadingButton
              variant="contained"
              disableElevation
              type="submit"
              data-testid="continueButton"
              loading={props.loading}
              fullWidth
              loadingIndicator={
                <CircularProgress color="primary02.main" size={20} />
              }
            >
              {t('common.continue')}
            </LoadingButton>
          </Box>
        </Grid>
      </Box>
    </Box>
  );

  /**
   * Form Hoists
   */
  function isValid(password: string | null | undefined) {
    // Okta password should not contains username
    const oktaPasswordResult = !usernamePartsToMatch.some((part) =>
      (password ?? '').toLowerCase().includes(part)
    );
    return (
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/.test(
        password || ''
      ) && oktaPasswordResult
    );
  }
}

export default CreatePassword;
