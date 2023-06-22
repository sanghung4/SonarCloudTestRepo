import { useEffect, useMemo } from 'react';
import {
  Box,
  Card,
  Container,
  Divider,
  Grid,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Route, Switch, useLocation } from 'react-router-dom';

import Breadcrumbs from 'common/Breadcrumbs';
import Link from 'common/Link';
import PrivacyPolicy from 'Legal/PrivacyPolicy';
import TermsOfAccess from 'Legal/TermsOfAccess';
import TermsOfSale from 'Legal/TermsOfSale';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useScrollToTop } from 'hooks/useScrollToTop';

function Legal() {
  useScrollToTop();
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const location = useLocation();

  /**
   * Memo
   */
  const documentTitle = useMemo(titleMemo, [location, t]);

  useDocumentTitle(documentTitle);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  return (
    <>
      <Switch>
        <Route path="/privacy-policy">
          <Breadcrumbs pageTitle={t('legal.privacyPolicy')} />
        </Route>
        <Route path="/terms-of-access">
          <Breadcrumbs pageTitle={t('legal.termsOfAccess')} />
        </Route>
        <Route path="/terms-of-sale">
          <Breadcrumbs pageTitle={t('legal.termsOfSale')} />
        </Route>
      </Switch>
      <Container>
        <Grid container spacing={2}>
          <Grid item xs={12} md={3}>
            <Card>
              <Box px={2} py={2} mt={2}>
                <Link to="/terms-of-access">
                  <Typography variant="body1" color="primary.main">
                    {t('legal.termsOfAccess')}
                  </Typography>
                </Link>
              </Box>
              <Box px={2}>
                <Divider />
              </Box>
              <Box px={2} py={2}>
                <Link to="/privacy-policy">
                  <Typography variant="body1" color="primary.main">
                    {t('legal.privacyPolicy')}
                  </Typography>
                </Link>
              </Box>
              <Box px={2}>
                <Divider />
              </Box>
              <Box px={2} py={2} mb={3}>
                <Link to="/terms-of-sale">
                  <Typography variant="body1" color="primary.main">
                    {t('legal.termsOfSale')}
                  </Typography>
                </Link>
              </Box>
            </Card>
          </Grid>
          <Grid item xs={12} md={9}>
            <Box mb={4}>
              <Card>
                <Box mx={2} my={2} py={2}>
                  <Switch>
                    <Route path="/privacy-policy" component={PrivacyPolicy} />
                    <Route path="/terms-of-access" component={TermsOfAccess} />
                    <Route path="/terms-of-sale" component={TermsOfSale} />
                  </Switch>
                </Box>
              </Card>
            </Box>
          </Grid>
        </Grid>
      </Container>
    </>
  );
  function titleMemo() {
    const { pathname } = location;
    switch (pathname) {
      case '/privacy-policy':
        return t('legal.privacyPolicy');
      case '/terms-of-access':
        return t('legal.termsOfAccess');
      case '/terms-of-sale':
        return t('legal.termsOfSale');
      default:
        return '';
    }
  }
}

export default Legal;
