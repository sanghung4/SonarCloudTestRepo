import { useEffect } from 'react';

import { Box, SnackbarProvider } from '@dialexa/reece-component-library';
import { useHistory, useLocation } from 'react-router-dom';
import { OktaAuth } from '@okta/okta-auth-js';
import { Security } from '@okta/okta-react';

import 'global.scss';
import AuthProvider from 'AuthProvider';
import CategoriesProvider from 'Categories/CategoriesProvider';
import CartProvider from 'providers/CartProvider';
import BranchProvider from 'providers/BranchProvider';
import BranchSidebar from 'Branches/BranchSidebar';
import Footer from 'common/Footer';
import Header from 'common/Header';
import CompanyListHeader from 'common/Header/CompanyList';
import HoldAlert from 'common/HoldAlert';
import HeaderProvider from 'common/Header/HeaderProvider';
import ListsProvider from 'providers/ListsProvider';
import MaintenancePage from 'Maintenance';
import Routes from 'Routes';

import { selectedAccountsVar } from 'providers/SelectedAccountsProvider';
import { page } from 'utils/analytics';
import { Configuration } from 'utils/configuration';

// TODO: Remove after testing i18n
import I18N from 'i18nTestComponent';
import SelectedAccountsProvider from 'providers/SelectedAccountsProvider';

function usePageViews() {
  const location = useLocation();
  const selectedAccounts = selectedAccountsVar();

  useEffect(() => {
    page({
      account: selectedAccounts.billTo?.erpAccountId || '',
      job: selectedAccounts.shipTo?.erpAccountId || ''
    });
  }, [location, selectedAccounts]);
}

function App() {
  /**
   * Custom Hooks
   */
  const history = useHistory();

  /**
   * Okta
   */
  const oktaAuth = new OktaAuth({
    issuer: `${Configuration.oktaUrl}/oauth2/default`,
    clientId: Configuration.oktaClientId,
    redirectUri: `${window.location.origin}/login/callback`,
    pkce: true
  });

  /**
   * Callbacks
   */
  const onAuthRequired = () => {
    history.push('/login');
  };

  const restoreOriginalUri = async (
    _oktaAuth: OktaAuth,
    originalUri: string
  ) => {
    let isVerifiedUser = false;
    const accessToken = _oktaAuth.getAccessToken();

    if (accessToken !== undefined) {
      const { isEmployee, isVerified } = JSON.parse(
        atob(accessToken.split('.')[1] ?? '')
      );
      isVerifiedUser = !isEmployee || (isEmployee && isVerified);
    }

    history.replace(isVerifiedUser ? '/select-accounts' : '/', {
      fromLogin: true,
      originalUri
    });
  };

  /**
   * Track client-side history in Segment analytics
   */
  usePageViews();

  return window.location.pathname === '/credit_callback' ? (
    <Routes />
  ) : (
    <Security
      oktaAuth={oktaAuth}
      restoreOriginalUri={restoreOriginalUri}
      onAuthRequired={onAuthRequired}
    >
      <AuthProvider>
        <SnackbarProvider>
          <SelectedAccountsProvider>
            <CategoriesProvider>
              <CartProvider>
                <BranchProvider>
                  <HeaderProvider>
                    {/* <Box component="header" sx={{ bgcolor: 'darkOrange.main' }}>
                  <Typography
                    variant="h1"
                    sx={{
                      fontSize: '1.5rem',
                      textAlign: 'center',
                      color: 'common.white'
                    }}
                  >
                    We are experiencing a service outage with maX. Our team is
                    working to resolve the issue. Sorry for the inconvenience.
                  </Typography>
                </Box> */}
                    <MaintenancePage>
                      {/* <Box
                  component="header"
                  sx={{ bgcolor: 'darkOrange.main' }}
                >
                  <Typography
                    variant="h1"
                    sx={{
                      fontSize: '1.5rem',
                      textAlign: 'center',
                      color: 'common.white'
                    }}
                  >
                    Attention: our website provider Amazon Web Services is
                    experiencing a global outage impacting our maX sites. Our
                    old website experience is operable. You can access that by
                    clicking the banner below: 
                  </Typography>
                </Box> */}
                      <CompanyListHeader />
                      <BranchSidebar />
                      {/* TODO: Remove after testing i18n */}
                      {Configuration.enableTranslationTest && <I18N />}
                      <Box
                        id="main"
                        display="flex"
                        flexDirection="column"
                        flex="1 0 auto"
                      >
                        <Header />
                        <Box
                          position="relative"
                          display="flex"
                          flexDirection="column"
                          flex="1"
                        >
                          <HoldAlert />
                          <ListsProvider>
                            <Box
                              flex="1"
                              display="flex"
                              flexDirection="column"
                              alignItems="stretch"
                            >
                              <Box
                                display="flex"
                                flexDirection="column"
                                minHeight="100%"
                                flex="1"
                                id="content"
                                overflow="hidden"
                              >
                                <Routes />
                              </Box>
                            </Box>
                          </ListsProvider>
                        </Box>
                        <Footer />
                      </Box>
                    </MaintenancePage>
                  </HeaderProvider>
                </BranchProvider>
              </CartProvider>
            </CategoriesProvider>
          </SelectedAccountsProvider>
        </SnackbarProvider>
      </AuthProvider>
    </Security>
  );
}

export default App;
