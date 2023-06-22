import React, { useContext } from 'react';

import {
  Box,
  Button,
  Container,
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { AuthContext } from 'AuthProvider';
import Link from 'common/Link';

export default function LoginAndSupport() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { authState } = useContext(AuthContext);

  return (
    <Box
      display="flex"
      justifyContent="center"
      flexDirection="column"
      pt={isSmallScreen ? 4 : 8}
      sx={{ bgcolor: 'background.paper' }}
    >
      <Container
        disableGutters={isSmallScreen}
        maxWidth={isSmallScreen ? 'md' : 'lg'}
      >
        {/* Login section */}
        {!authState?.isAuthenticated ? (
          <Box mb={6}>
            <Grid container>
              <Grid container item xs={isSmallScreen ? 12 : 5}>
                <Box mx={isSmallScreen ? 6 : 0} mb={4}>
                  <Typography
                    variant={isSmallScreen ? 'h3' : 'h1'}
                    color="primary"
                    sx={{
                      fontWeight: isSmallScreen ? 500 : 700,
                      pb: 3
                    }}
                  >
                    {t('home.signinTitle')}
                  </Typography>
                  <Typography
                    variant={isSmallScreen ? 'h5' : 'h3'}
                    color="primary"
                    sx={{ fontWeight: 400 }}
                  >
                    {t('home.signinSub')}
                  </Typography>
                </Box>
              </Grid>
              <Grid container item xs={isSmallScreen ? 12 : 7} wrap="nowrap">
                <Grid
                  item
                  container
                  xs={6}
                  alignItems={isSmallScreen ? 'flex-end' : undefined}
                >
                  <Box
                    ml={isSmallScreen ? 6 : 16}
                    mr={isSmallScreen ? 1 : 2}
                    width="100%"
                  >
                    <Box pb={1}>
                      <Typography color="textSecondary">
                        {t('home.signinNewTo')}
                      </Typography>
                    </Box>
                    <Link to="/register">
                      <Button
                        size="large"
                        data-testid="sign-up-button-bottom-of-page"
                        fullWidth
                      >
                        {t('home.signup')}
                      </Button>
                    </Link>
                  </Box>
                </Grid>
                <Grid
                  item
                  container
                  xs={6}
                  alignItems={isSmallScreen ? 'flex-end' : undefined}
                >
                  <Box
                    ml={isSmallScreen ? 1 : 2}
                    mr={isSmallScreen ? 6 : 16}
                    width="100%"
                  >
                    <Box pb={1}>
                      <Typography color="textSecondary">
                        {t('home.signinAlreadyHas')}
                      </Typography>
                    </Box>
                    <Link to="/login">
                      <Button
                        size="large"
                        fullWidth
                        data-testid="sign-in-button-bottom-of-page"
                        variant="secondary"
                      >
                        {t('home.signin')}
                      </Button>
                    </Link>
                  </Box>
                </Grid>
              </Grid>
            </Grid>
          </Box>
        ) : null}
        {/* Support section */}
        <Grid
          container
          justifyContent={isSmallScreen ? 'flex-start' : 'center'}
        >
          <Box mx={6} mt={6} pb={16} maxWidth={450}>
            <Typography
              color="primary"
              variant={isSmallScreen ? 'h4' : 'h3'}
              align={isSmallScreen ? 'left' : 'center'}
              sx={{
                fontWeight: 500,
                pb: 1
              }}
              data-testid="support-title"
            >
              {t('home.supportTitle')}
            </Typography>
            <Typography
              color="primary"
              variant="h5"
              align={isSmallScreen ? 'left' : 'center'}
              sx={{
                fontWeight: 400
              }}
              data-testid="support-subtitle-combined"
            >
              {t('home.supportSub1')}
              <Link to="/location-search" data-testid="support-subtitle-1">
                {t('home.supportUrl1')}
              </Link>
              {t('home.supportSub2')}
              <Link to="/support" data-testid="support-subtitle-2">
                {t('home.supportUrl2')}
              </Link>
            </Typography>
          </Box>
        </Grid>
      </Container>
    </Box>
  );
}
