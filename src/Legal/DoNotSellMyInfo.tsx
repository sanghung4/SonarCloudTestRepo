import React from 'react';

import {
  Box,
  Container,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import Breadcrumbs from 'common/Breadcrumbs';
import CCPAForm from './CCPAForm';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useScrollToTop } from 'hooks/useScrollToTop';

function DoNotSellMyInfo() {
  useScrollToTop();
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('legal.doNotSellMyInfo'));

  return (
    <>
      <Breadcrumbs pageTitle={t('legal.ccpaForm')} />
      <Container maxWidth="md">
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
            {t('legal.ccpaForm')}
          </Typography>
          <Box pt={3} pb={1} textAlign={isSmallScreen ? 'center' : 'left'}>
            <Typography variant="body2">{t('legal.ccpaHelpText')}</Typography>
          </Box>
          <CCPAForm />
        </Box>
      </Container>
    </>
  );
}

export default DoNotSellMyInfo;
