import {
  Box,
  Button,
  CircularProgress,
  Divider,
  Grid,
  Link,
  TextField,
  Typography,
  useScreenSize,
  LoadingButton
} from '@dialexa/reece-component-library';
import { useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import * as Yup from 'yup';
import { useDomainInfo } from 'hooks/useDomainInfo';

interface Props {
  onSubmitAccount: (accountNumber?: string, erpName?: string) => void;
  loading?: boolean;
  resetAccountNotFound: () => void;
  accountNotFound: boolean;
}

function EnterAccount(props: Props) {
  /**
   * Custom hooks
   */
  const { isWaterworks } = useDomainInfo();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Form
   */
  const formik = useFormik({
    initialValues: {
      accountNumber: ''
    },
    validationSchema: Yup.object({
      accountNumber: Yup.string()
        .matches(/^[a-zA-Z0-9]+$/)
        .required(t('validation.accountRequired'))
    }),
    onSubmit: ({ accountNumber }) => {
      const erpName = isWaterworks ? 'MINCRON' : 'ECLIPSE';
      props.onSubmitAccount(accountNumber, erpName);
    }
  });

  return (
    <>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        pt={isSmallScreen ? 3 : 6}
        pb={isSmallScreen ? 5 : 0}
      >
        <Typography
          variant={isSmallScreen ? 'h4' : 'h3'}
          component="h1"
          color="primary"
          sx={{ fontWeight: 700 }}
        >
          {t('common.register')}
        </Typography>
        {HelperLink(
          t('register.alreadyRegistered'),
          t('common.signIn'),
          '/login'
        )}
        <Box
          component="form"
          onSubmit={formik.handleSubmit}
          noValidate
          sx={{ width: 1 }}
        >
          <Grid container spacing={4}>
            <Grid item xs={12}>
              <TextField
                data-testid="accountNumberInput"
                id="account-number"
                name="accountNumber"
                label={
                  <Typography
                    variant="body2"
                    display="inline"
                    component="label"
                  >
                    {t('common.accountNumber')}
                  </Typography>
                }
                placeholder={t('common.enterAccountNumber')}
                value={formik.values.accountNumber}
                onChange={(input) => {
                  formik.handleChange(input);
                  props.resetAccountNotFound();
                }}
                onBlur={formik.handleBlur}
                error={Boolean(
                  (formik.touched.accountNumber &&
                    formik.errors.accountNumber) ||
                    props.accountNotFound
                )}
                helperText={
                  formik.touched.accountNumber && formik.errors.accountNumber
                    ? formik.errors.accountNumber
                    : ' '
                }
                fullWidth
                required
                inputProps={{ 'data-testid': 'account-number-input' }}
              />
            </Grid>
            <Grid container item xs={12} md={12} justifyContent="center">
              <Grid item md xs={12}>
                <LoadingButton
                  variant="contained"
                  disableElevation
                  type="submit"
                  data-testid="registration-continue-button"
                  loading={props.loading}
                  fullWidth
                  loadingIndicator={
                    <CircularProgress color="primary02.main" size={20} />
                  }
                >
                  {t('common.continue')}
                </LoadingButton>
              </Grid>
            </Grid>
          </Grid>
        </Box>
        <Box width={1}>
          {HelperLink(
            t('register.forgotAccountNumber'),
            t('register.contactABranch'),
            '/location-search'
          )}
          <Divider />
          <Box
            display="flex"
            flexDirection="row"
            justifyContent="center"
            flexWrap="wrap"
            pt={3}
          >
            <Box pt={1} pr={isSmallScreen ? 2 : 3} pb={isSmallScreen ? 2 : 0}>
              <Typography>{t('register.reeceEmployee')}</Typography>
            </Box>
            <Box pb={3}>
              <Button
                variant="secondary"
                type="submit"
                onClick={() => props.onSubmitAccount()}
                data-testid="employee-button"
              >
                {t('common.clickHere')}
              </Button>
            </Box>
          </Box>
        </Box>
      </Box>
    </>
  );

  function HelperLink(helperText: string, linkText: string, linkPath: string) {
    return (
      <Box pt={isSmallScreen ? 4 : 7} pb={isSmallScreen ? 4 : 7}>
        <Grid container justifyContent="center" spacing={1}>
          <Grid item>
            <Typography display="inline">{helperText}</Typography>
          </Grid>
          <Grid item>
            <Link to={linkPath} component={RouterLink}>
              <Button
                color="primaryLight"
                variant="inline"
                data-testid="helper-button"
              >
                {linkText}
              </Button>
            </Link>
          </Grid>
        </Grid>
      </Box>
    );
  }
}

export default EnterAccount;
