import React, { useContext } from 'react';

import {
  Box,
  Button,
  Card,
  Container,
  Divider,
  Grid,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { Link as RouterLink } from 'react-router-dom';

import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import SignInForm from 'Home/SignInForm';
import UserPanel from 'Home/UserPanel';

import { ReactComponent as Pluses } from 'images/home/plus_signs.svg';
import {
  landerBackground,
  MaxLogoStyled,
  ScheduleAppointmentTypography
} from 'Home/util/styled';
import { useDomainInfo } from 'hooks/useDomainInfo';

/**
 * Constants
 */
export const EHG_SCHEDULE_URL = '/Appointment';

/**
 * Component
 */
function Landing() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { subdomain } = useDomainInfo();
  const isBathNKitchen = subdomain === 'expressionshomegallery';

  /**
   * Context
   */
  const { authState } = useContext(AuthContext);

  // TO-DO - We should be looking to move away from MUI later on, all of these will be placerholder for now
  /**
   * Render
   */
  return (
    <Box
      display="flex"
      minHeight={500}
      justifyContent="center"
      bgcolor="common.white"
    >
      <Box maxWidth={1920} width="100%" mb={isSmallScreen ? 0 : 10}>
        <Box sx={[!isSmallScreen && landerBackground]}>
          <Container
            disableGutters={isSmallScreen}
            maxWidth={isSmallScreen ? 'md' : 'lg'}
            sx={{
              display: 'flex',
              flexDirection: 'column'
            }}
          >
            <Box
              py={isSmallScreen ? 0 : 8}
              mb={isSmallScreen ? 2 : 0}
              sx={[isSmallScreen && landerBackground]}
            >
              <Grid
                container
                wrap="nowrap"
                justifyContent={isSmallScreen ? 'space-between' : undefined}
              >
                {!isSmallScreen && (
                  <Grid container item direction="column" xs>
                    <Box pr={8}>
                      <Card sx={{ width: 378 }}>
                        {authState?.isAuthenticated ? (
                          <UserPanel />
                        ) : (
                          <SignInForm />
                        )}
                      </Card>
                    </Box>
                  </Grid>
                )}
                <Grid
                  container
                  item
                  direction="row"
                  wrap="nowrap"
                  xs={isSmallScreen ? 7 : 5}
                  zeroMinWidth
                  alignItems="flex-end"
                  justifyContent={isSmallScreen ? 'flex-start' : 'flex-end'}
                  sx={{ minHeight: 305 }}
                >
                  <Box
                    margin={isSmallScreen ? 2 : 0}
                    maxWidth={isSmallScreen ? 360 : 500}
                    mx={isSmallScreen ? 2 : 0}
                    zIndex={1}
                  >
                    <Box
                      component="span"
                      fontSize={isSmallScreen ? 25 : 60}
                      color="common.white"
                      fontWeight={500}
                      data-testid="hero-text"
                    >
                      {`${t('common.heroText')} `}
                      <MaxLogoStyled
                        width={isSmallScreen ? 71 : 167}
                        height={isSmallScreen ? 25 : 63}
                        viewBox={'0 0 207 73'}
                      />
                    </Box>
                    <Box
                      color="common.white"
                      fontSize={isSmallScreen ? 16 : 24}
                      fontWeight={400}
                      data-testid="hero-sub"
                    >
                      {t('common.heroSub')}
                    </Box>
                  </Box>
                </Grid>
                <Grid
                  container
                  item
                  alignItems="center"
                  xs="auto"
                  sx={{ width: 'auto' }}
                >
                  <Box
                    position="relative"
                    width={isSmallScreen ? 89 : 160}
                    height="100%"
                  >
                    <Box
                      position="absolute"
                      bottom={isSmallScreen ? -38 : -136}
                    >
                      <Pluses
                        width={isSmallScreen ? 67 : 136}
                        height={isSmallScreen ? 231 : 472}
                        viewBox={'0 0 136 472'}
                      />
                    </Box>
                  </Box>
                </Grid>
              </Grid>
            </Box>
            {isSmallScreen && (
              <>
                {authState?.isAuthenticated ? <UserPanel /> : <SignInForm />}
                <Box mx={4}>
                  <Divider />
                </Box>
              </>
            )}
          </Container>
        </Box>
        {isBathNKitchen && (
          <Box bgcolor="common.white" px={4}>
            <Box py={8} textAlign="center">
              <ScheduleAppointmentTypography>
                {t('home.scheduleCaption')}
              </ScheduleAppointmentTypography>
              <Link
                to={EHG_SCHEDULE_URL}
                component={RouterLink}
                target="_blank"
              >
                <Button size={isSmallScreen ? 'medium' : 'large'}>
                  {t('home.scheduleNow')}
                </Button>
              </Link>
            </Box>
            <Divider />
          </Box>
        )}
      </Box>
    </Box>
  );
}

export default Landing;
