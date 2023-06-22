import { ReactNode, useContext } from 'react';

import {
  Box,
  Container,
  Grid,
  Hidden,
  Link,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import logo from 'images/logo.svg';
import toolboxImage from 'images/toolbox-graphic.svg';
import { Configuration } from 'utils/configuration';

type Props = {
  children: ReactNode;
};

function MaintenancePage(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { activeFeatures } = useContext(AuthContext);

  return (
    <>
      {Configuration.showMaintenancePage ||
      activeFeatures?.includes('IN_MAINTENANCE') ? (
        <Box
          bgcolor={isSmallScreen ? 'transparent' : 'common.white'}
          display="flex"
          flex="1"
          mx={isSmallScreen ? 6 : 0}
        >
          <Container
            maxWidth="md"
            sx={{
              display: 'flex',
              flex: 1
            }}
          >
            <Grid
              container
              spacing={isSmallScreen ? 0 : 6}
              direction={isSmallScreen ? 'column' : 'row'}
            >
              <Hidden smDown>
                <Grid item xs={12} md={6} sx={{ position: 'relative' }}>
                  <Box
                    component="img"
                    src={toolboxImage}
                    alt="toolbox"
                    sx={(theme) => ({
                      [theme.breakpoints.up('sm')]: {
                        position: 'absolute',
                        top: '50%',
                        left: '50%',
                        transform: 'translate(-50%, -50%)'
                      }
                    })}
                  />
                </Grid>
              </Hidden>
              <Grid
                container
                item
                xs={12}
                md={5}
                direction="column"
                justifyContent="center"
                alignItems="center"
              >
                <Hidden mdUp>
                  <Box
                    component="img"
                    src={logo}
                    alt="reece-logo"
                    sx={{ mb: 5, maxWidth: '100px', height: 'auto' }}
                  />
                  <Box
                    component="img"
                    src={toolboxImage}
                    alt="toolbox"
                    sx={{ mb: 5, maxWidth: '200px', height: 'auto' }}
                  />
                </Hidden>
                <Box>
                  <Hidden smDown>
                    <Box>
                      <Box textAlign="center">
                        <Box
                          component="img"
                          src={logo}
                          alt="reece-logo"
                          sx={{
                            maxWidth: '100px',
                            height: 'auto',
                            pb: '15px'
                          }}
                        />
                      </Box>
                    </Box>
                  </Hidden>
                  <Typography
                    variant={isSmallScreen ? 'h4' : 'h3'}
                    fontWeight={600}
                    color="common.black"
                  >
                    {t('common.downForMaintenance')}
                  </Typography>
                </Box>
                <Typography
                  variant={isSmallScreen ? 'body1' : 'body1'}
                  align="center"
                  fontWeight={500}
                  sx={{ py: isSmallScreen ? 2 : 4 }}
                >
                  {t('common.maintenancePageError')}
                </Typography>
                <Typography
                  variant={isSmallScreen ? 'body1' : 'h5'}
                  align="center"
                  fontWeight={400}
                  sx={{ py: isSmallScreen ? 2 : 4 }}
                >
                  {t('common.needHelpMessage')}
                </Typography>
                <Box>
                  <Link href={`mailto:${t('maintenancePage.email')}`}>
                    <Typography
                      variant="body1"
                      display="inline"
                      color="primary02.main"
                    >
                      {t('maintenancePage.email')}
                    </Typography>
                  </Link>
                </Box>
              </Grid>
            </Grid>
          </Container>
        </Box>
      ) : (
        props.children
      )}
    </>
  );
}

export default MaintenancePage;
