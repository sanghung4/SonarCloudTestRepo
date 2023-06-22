import React from 'react';

import {
  Box,
  Container,
  Divider,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import ContactFormComponent from 'Support/ContactForm';
import SupportInfo from 'Support/SupportInfo';
import useDocumentTitle from 'hooks/useDocumentTitle';

function Support() {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('common.support'));

  /**
   * Context
   */

  return (
    <Container maxWidth="lg">
      <Box
        display="flex"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        pt={isSmallScreen ? 3 : 7}
        pb={isSmallScreen ? 5 : 7}
      >
        <Typography
          color="primary"
          component="h1"
          variant={isSmallScreen ? 'h4' : 'h3'}
        >
          {t('support.header')}
        </Typography>
        <Box pt={3} pb={1} textAlign={isSmallScreen ? 'center' : undefined}>
          <Typography variant="body2">{t('support.helpText')}</Typography>
        </Box>
        <Box
          display="flex"
          flexDirection={isSmallScreen ? 'column' : 'row'}
          pt={3}
          width="100%"
          justifyContent={'center'}
        >
          <Box width={isSmallScreen ? '100%' : '35%'}>
            <SupportInfo />
          </Box>
          {isSmallScreen && (
            <Box>
              <Divider />
            </Box>
          )}
          <Box width={isSmallScreen ? '100%' : '65%'}>
            <ContactFormComponent />
          </Box>
        </Box>
      </Box>
    </Container>
  );
}

export default Support;
