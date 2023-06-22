import React, { useEffect, useState } from 'react';

import { Grid, Link, Box } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import { SignOutIcon } from 'icons';
import { PricingInfoTypography, StyledAlert } from './util/Styled';

function SearchVisitorAlert() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * State
   */
  const [fixedAlert, setFixedAlert] = useState(false);

  useEffect(() => {
    window.addEventListener('scroll', handleScroll, { passive: true });
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  const handleScroll = () => {
    const position = window.pageYOffset;
    const headerElement = document.getElementsByTagName('header')[0];
    const headerHeight = headerElement.getBoundingClientRect().height;
    setFixedAlert(position >= headerHeight);
  };

  return (
    <Grid
      container
      justifyContent="center"
      sx={[
        fixedAlert && {
          position: 'sticky',
          top: 0,
          zIndex: 1
        }
      ]}
    >
      <Grid item sm={12} md={7} lg={5} display="flex" justifyContent="center">
        <StyledAlert
          icon={<SignOutIcon width={30} height={30} />}
          severity="info"
        >
          <Box display="flex" gap={0.5}>
            <Link to="/login" component={RouterLink}>
              <PricingInfoTypography
                fontWeight={500}
                sx={{ textDecoration: 'underline' }}
              >
                {t('search.signIn')}
              </PricingInfoTypography>
            </Link>
            <PricingInfoTypography fontWeight={400}>
              {t('search.pricingInfo')}
            </PricingInfoTypography>
          </Box>
        </StyledAlert>
      </Grid>
    </Grid>
  );
}

export default SearchVisitorAlert;
