import {
  Box,
  Grid,
  Hidden,
  Link,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import logo from 'images/logo.svg';
import { Toolbox } from 'icons';

function MaintenancePage() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  return (
    <Grid
      container
      bgcolor="common.white"
      display="flex"
      flex="1"
      padding={12}
      justifyContent="center"
    >
      <Grid
        item
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          mr: { lg: 3 }
        }}
        direction={isSmallScreen ? 'column' : 'row'}
      >
        <Hidden mdUp>
          <Box
            component="img"
            src={logo}
            alt="reece-logo"
            sx={{
              maxWidth: '100px',
              height: 'auto',
              pb: '15px'
            }}
            data-testId="reece-log"
          />
        </Hidden>

        <Toolbox style={{ maxWidth: '284px' }} />
      </Grid>

      <Grid
        item
        direction="column"
        sx={{
          display: 'flex',
          textAlign: 'center',
          justifyContent: 'center'
        }}
        lg={3}
        md={2}
      >
        <Hidden mdDown>
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
              data-testId="reece-log"
            />
          </Box>
        </Hidden>
        <Typography
          variant={isSmallScreen ? 'h4' : 'h3'}
          fontWeight={600}
          color="common.black"
          sx={{
            textAlign: 'center',
            justifyContent: 'center'
          }}
          data-testId="maintenance-down"
        >
          {t('common.invoicesDown')}
        </Typography>

        <Typography
          variant={isSmallScreen ? 'body1' : 'h5'}
          align="center"
          fontWeight={400}
          fontSize="1rem"
          lineHeight="1.5rem"
          sx={{ marginTop: '1.5rem' }}
          data-testId="maintenance-help"
        >
          {t('common.needHelpMessage')}
        </Typography>
        <Box>
          <Link
            href={`mailto:${t('maintenancePage.email')}`}
            data-testId="maintenance-email"
          >
            <Typography
              variant="body1"
              display="inline"
              color="primary02.main"
              fontWeight={400}
              fontSize="1rem"
              lineHeight="1.5rem"
            >
              {t('maintenancePage.email')}
            </Typography>
          </Link>
        </Box>
      </Grid>
    </Grid>
  );
}

export default MaintenancePage;
