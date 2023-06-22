import React from 'react';

import {
  Box,
  Button,
  Card,
  Divider,
  Grid,
  Link,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import { RegistrationCompleteIcon } from 'icons';

type Props = {
  isBranchInfo?: boolean | null;
  email?: string | null;
  phone?: string | null;
  isInvite?: boolean | null;
};

function Complete(props: Props) {
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  return (
    <Grid
      container
      direction="column"
      alignItems="center"
      sx={{ pb: isSmallScreen ? 6 : 0 }}
    >
      <Grid item sx={{ p: isSmallScreen ? 2 : 4, pb: isSmallScreen ? 2 : 4 }}>
        <RegistrationCompleteIcon />
      </Grid>
      <Grid item>
        <Typography
          align="center"
          variant={isSmallScreen ? 'h4' : 'h3'}
          component="h1"
          color="primary"
          sx={{ fontWeight: 700 }}
          data-testid="registration-complete-title"
        >
          {t('register.complete')}
        </Typography>
      </Grid>
      {!props.isInvite ? (
        <>
          <Grid item sx={{ textAlign: 'center', py: 2 }}>
            <Typography variant="body1" color="textSecondary" paragraph>
              {t('register.submitted')}
            </Typography>
            <Typography variant="body1" color="textSecondary" paragraph>
              {t('register.moreInformation')}
            </Typography>
          </Grid>
          <Grid item sx={{ py: isSmallScreen ? 0 : 4, pt: 2, pb: 6 }}>
            <Card>
              <Grid
                container
                justifyContent="center"
                sx={{
                  textAlign: 'center',
                  px: 4,
                  py: 3
                }}
              >
                <Grid item xs={12}>
                  <Typography
                    color="primary"
                    component="h2"
                    variant="h4"
                    sx={{ fontWeight: 700 }}
                  >
                    {props.isBranchInfo
                      ? t('register.branch')
                      : t('register.admin')}
                  </Typography>
                </Grid>
                <Grid item xs={12} sx={{ py: 2 }}>
                  <span>{t('common.phoneNumber')}: </span>
                  {props.phone ? (
                    <Link
                      href={`tel:${props.phone}`}
                      sx={{ color: 'primary02.main' }}
                    >
                      {props.phone}
                    </Link>
                  ) : null}
                </Grid>
                {!props.isBranchInfo ? (
                  <Grid item xs={12}>
                    <span>{t('common.emailAddress')}: </span>
                    {props.email ? (
                      <Link
                        href={`mailto:${props.email}`}
                        sx={{ color: 'primary02.main' }}
                      >
                        {props.email}
                      </Link>
                    ) : null}
                  </Grid>
                ) : null}
              </Grid>
            </Card>
          </Grid>
          <Divider orientation="horizontal" flexItem sx={{ pb: '1px' }} />
          <Box pt={3}>
            <Grid item>
              <p>{t('register.meantime')}</p>
            </Grid>
          </Box>
        </>
      ) : (
        <>
          <Grid item sx={{ textAlign: 'center', py: 2 }}>
            <Typography variant="body1" color="textSecondary" paragraph>
              {t('register.inviteComplete')}
            </Typography>
          </Grid>
          <Divider orientation="horizontal" flexItem sx={{ pb: '1px' }} />
          <Box pt={3}>
            <Grid item>
              <p>{t('register.inviteSignInNow')}</p>
            </Grid>
          </Box>
        </>
      )}
      <Box width={225} py={3}>
        <Grid item>
          <Link to="/login" component={RouterLink}>
            <Button
              fullWidth
              variant="alternative"
              data-testid="startBrowsingButton"
            >
              {t('common.signIn')}
            </Button>
          </Link>
        </Grid>
      </Box>
    </Grid>
  );
}

export default Complete;
