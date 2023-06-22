import React, { useContext, useState, useCallback, useEffect } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Grid,
  Link,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { VerifyEmailIcon } from 'icons';
import {
  useResendVerificationEmailMutation,
  useVerifyUserMutation
} from 'generated/graphql';
import { AuthContext } from 'AuthProvider';
import { RegistrationCompleteIcon } from 'icons';
import { useQueryParams } from 'hooks/useSearchParam';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { useLocation } from 'react-router-dom';
import useDocumentTitle from 'hooks/useDocumentTitle';

type VerifyEmailParams = {
  token: string;
};

type LocationState = {
  userId: string;
};

function VerifyEmail() {
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('register.verifyYourEmail'));
  const { profile } = useContext(AuthContext);
  const [verifiedSuccess, setVerifiedSuccess] = useState(false);
  const { pushAlert } = useSnackbar();
  const { isWaterworks } = useDomainInfo();

  const location = useLocation<LocationState>();

  const [queryParams] = useQueryParams<VerifyEmailParams>();

  const [verifiedUserMutation, { loading: verifyUserLoading }] =
    useVerifyUserMutation({
      onCompleted: () => {
        setVerifiedSuccess(true);
      }
    });

  const [
    resendVerificationEmailMutation,
    { loading: resendVerificationLoading }
  ] = useResendVerificationEmailMutation({
    onCompleted: () => {
      pushAlert(t('register.emailSentNotification'), { variant: 'success' });
    },
    onError: () => {
      pushAlert(t('register.emailSentErrorNotification'), { variant: 'error' });
    }
  });

  const onResendEmail = useCallback(onResendEmailCb, [
    resendVerificationEmailMutation,
    location,
    profile?.userId,
    isWaterworks
  ]);

  useEffect(checkForVaildVerificationToken, [
    verifiedUserMutation,
    queryParams.token,
    profile?.userId
  ]);

  return (
    <Box data-testid="verifyemail-component">
      {verifyUserLoading ? (
        <Grid
          container
          direction="column"
          alignItems="center"
          sx={{ pb: isSmallScreen ? 6 : 0, mt: 10 }}
        >
          <Grid
            item
            sx={{
              p: isSmallScreen ? 2 : 4,
              pb: isSmallScreen ? 2 : 4
            }}
          >
            <CircularProgress size={75} color="primary02.main" />
          </Grid>
        </Grid>
      ) : verifiedSuccess ? (
        <Grid
          container
          direction="column"
          alignItems="center"
          sx={{ pb: isSmallScreen ? 6 : 0, mt: 5 }}
        >
          <Grid item>
            <Typography
              align="center"
              variant={isSmallScreen ? 'h4' : 'h3'}
              component="h1"
              color="primary"
              sx={{ fontWeight: 700 }}
            >
              {t('register.registrationComplete')}
            </Typography>
          </Grid>
          <Grid
            item
            sx={{
              p: isSmallScreen ? 2 : 4,
              pb: isSmallScreen ? 2 : 4
            }}
          >
            <RegistrationCompleteIcon />
          </Grid>
          <Grid item sx={{ textAlign: 'center', pt: 2 }}>
            <Typography variant="body1" color="textSecondary" paragraph>
              {t('register.employeeLevelAccess')}
            </Typography>
          </Grid>
          <Link href="/login">
            <Button fullWidth>{t('common.signIn')}</Button>
          </Link>
        </Grid>
      ) : (
        <Grid
          container
          direction="column"
          alignItems="center"
          sx={{ pb: isSmallScreen ? 6 : 0, mt: 5 }}
        >
          <Grid item>
            <Typography
              align="center"
              variant={isSmallScreen ? 'h4' : 'h3'}
              component="h1"
              color="primary"
              sx={{ fontWeight: 700 }}
            >
              {t('register.verifyYourEmail')}
            </Typography>
          </Grid>
          <Grid
            item
            sx={{
              p: isSmallScreen ? 2 : 4
            }}
          >
            <VerifyEmailIcon />
          </Grid>
          <Grid item sx={{ textAlign: 'center', pt: 2 }}>
            <Typography variant="body1" color="textSecondary" paragraph>
              {t('register.emailSent')}
            </Typography>
          </Grid>
          <Box pt={3}>
            <Grid item>
              <p>{t('register.noEmail')}</p>
            </Grid>
          </Box>
          <Grid item>
            <Button
              data-testid="resend-email-verify-button"
              fullWidth
              variant="text"
              color="primaryLight"
              size="small"
              disabled={resendVerificationLoading}
              onClick={onResendEmail}
            >
              {t('register.resendEmail')}
            </Button>
          </Grid>
        </Grid>
      )}
    </Box>
  );

  function checkForVaildVerificationToken() {
    if (queryParams.token) {
      verifiedUserMutation({
        variables: {
          verificationToken: queryParams.token
        }
      });
    }
  }

  function onResendEmailCb() {
    resendVerificationEmailMutation({
      variables: {
        userId: profile?.userId ? profile?.userId : location.state.userId,
        isWaterworksSubdomain: isWaterworks
      }
    });
  }
}

export default VerifyEmail;
