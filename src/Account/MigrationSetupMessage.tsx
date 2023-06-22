import {
  Box,
  Grid,
  Typography,
  useScreenSize,
  Button,
  useSnackbar,
  CircularProgress
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';

import { useResendLegacyInviteEmailMutation } from 'generated/graphql';
import { MaxIcon, WelcomeToMaxIcon } from 'icons';
import Link from 'common/Link';
import useDocumentTitle from 'hooks/useDocumentTitle';

type LocationState = {
  email: string;
};

function MigrationSetupMessage() {
  /**
   * Custom Hooks
   */
  const location = useLocation<LocationState>();
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  useDocumentTitle(t('common.welcomeToMaX'));

  /**
   * GraphQL Queries
   */
  const [sendInviteEmail, { loading }] = useResendLegacyInviteEmailMutation({
    onCompleted: () =>
      pushAlert(t('register.emailSentNotification'), { variant: 'success' }),
    onError: () =>
      pushAlert(t('register.emailSentErrorNotification'), { variant: 'error' })
  });

  return (
    <Grid
      container
      direction="column"
      sx={{ alignItems: 'center', pb: isSmallScreen ? 6 : 0, px: 6, mt: 8 }}
    >
      <Box display="flex" flexDirection="row">
        <Typography variant="h3" color="primary" sx={{ fontWeight: 700 }}>
          {t('account.welcomeTo')}
        </Typography>
        <Box
          sx={{
            pt: 0.125,
            pl: 0.125
          }}
        >
          <MaxIcon height={25} width={75} />
        </Box>
      </Box>

      <Grid item sx={{ p: isSmallScreen ? 2 : 4, pb: isSmallScreen ? 2 : 4 }}>
        <WelcomeToMaxIcon />
      </Grid>
      <Grid
        item
        sx={{
          textAlign: 'center',
          pt: 2,
          maxWidth: isSmallScreen ? 640 : '30vw'
        }}
      >
        <Typography>
          {`${t('account.maxWelcomeLine1')} ${t('account.maxWelcomeLine2')}`}
        </Typography>
      </Grid>
      {location.state ? (
        <>
          <Grid item>
            <Box pt={6}>
              <Typography>{t('register.noEmail')}</Typography>
            </Box>
          </Grid>
          <Grid item>
            <Button
              disabled={loading}
              fullWidth
              variant="text"
              color="primaryLight"
              size="medium"
              data-testid="resend-email-button"
              onClick={() =>
                sendInviteEmail({
                  variables: { legacyUserEmail: location.state.email }
                })
              }
              sx={{ p: 0 }}
            >
              {loading ? (
                <CircularProgress size={15} />
              ) : (
                t('register.resendEmail')
              )}
            </Button>
          </Grid>
          <Grid item container justifyContent="center">
            <Box mt={8} mb={5} maxWidth={450}>
              <Typography
                color="primary"
                variant="h4"
                align="center"
                sx={{ fontWeight: 500, fontSize: 20, pb: 1 }}
              >
                {t('common.havingTrouble')}
              </Typography>
              <Typography
                align="center"
                sx={{ fontWeight: 400, lineHeight: '24px' }}
              >
                {t('common.hereToHelp')}
                <Link to="/location-search">{t('home.supportUrl1')}</Link>
                {t('home.supportSub2')}
                <Link to="/support">{t('home.supportUrl2')}</Link>
              </Typography>
            </Box>
          </Grid>
        </>
      ) : null}
    </Grid>
  );
}

export default MigrationSetupMessage;
