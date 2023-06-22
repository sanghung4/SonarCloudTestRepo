import {
  Box,
  Button,
  Divider,
  Link,
  TextField,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import useHomeSignInForm from 'Home/util/useHomeSignInForm';

export default function SignInForm() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Form
   */
  const { formik } = useHomeSignInForm();

  /**
   * Output
   */
  return (
    <Box bgcolor="background.paper">
      <form
        onSubmit={formik.handleSubmit}
        noValidate
        data-testid="sign-in-form-main"
      >
        <Box px={4} py={3}>
          <Typography
            variant="h5"
            color="primary"
            display="block"
            fontWeight={700}
            mb={2}
          >
            {t('common.signIn')}
          </Typography>
          <TextField
            id="username"
            label={t('common.emailAddress')}
            placeholder={t('common.enterEmailAddress')}
            name="username"
            type="email"
            autoComplete="username"
            value={formik.values.username}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={!!(formik.touched.username && formik.errors.username)}
            helperText={
              formik.touched.username && formik.errors.username
                ? formik.errors.username
                : ' '
            }
            inputProps={{ 'data-testid': 'sign-in-form-username' }}
            fullWidth
          />
          <TextField
            id="password"
            label={t('common.password')}
            placeholder={t('common.enterPassword')}
            name="password"
            type="password"
            autoComplete="current-password"
            value={formik.values.password}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={!!(formik.touched.password && formik.errors.password)}
            helperText={
              formik.touched.password && formik.errors.password
                ? formik.errors.password
                : ' '
            }
            inputProps={{ 'data-testid': 'sign-in-form-password' }}
            fullWidth
          />
          <Box
            display="flex"
            justifyContent="flex-end"
            alignItems="center"
            my={1}
          >
            <Link
              to={`/forgot-password?email=${encodeURIComponent(
                formik.values.username
              )}`}
              component={RouterLink}
            >
              <Typography
                variant="caption"
                color="secondary02.main"
                mr={3}
                data-testid="forgot-password-button"
              >
                {t('common.forgotPassword')}
              </Typography>
            </Link>
            <Button type="submit" data-testid="sign-in-form-button">
              {t('common.signIn')}
            </Button>
          </Box>
        </Box>
      </form>
      <Box mx={3}>
        <Divider light />
      </Box>
      <Box display="flex" flexDirection="column" mx={5} my={3}>
        <Typography variant="h5" gutterBottom color="primary" fontWeight={700}>
          {t('common.newToReece')}
        </Typography>
        <Typography variant="body1" gutterBottom>
          {t('common.registeringOnline')}
        </Typography>
        <Link to="/register" component={RouterLink}>
          <Typography
            variant="body1"
            component="span"
            color="primary02.main"
            data-testid="sign-up-button"
          >
            {t('common.signUpNow')}
          </Typography>
        </Link>
      </Box>
    </Box>
  );
}
