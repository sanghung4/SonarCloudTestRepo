import { OktaAuth } from '@okta/okta-auth-js';
import { Security } from '@okta/okta-react';
import { useNavigate } from 'react-router-dom';

import Sidebar from 'common/Sidebar';
import AuthProvider from 'providers/AuthProvider';
import Routes from 'Routes';
import { oktaAuthOptions } from 'util/configurations';

/**
 * Init
 */
const oktaAuth = new OktaAuth(oktaAuthOptions);

/**
 * Component
 */
function App() {
  /**
   * Custom Hooks
   */
  const navigate = useNavigate();

  /**
   * Callbacks
   */
  const onAuthRequired = () => navigate('/login');
  const restoreOriginalUri = async (_oktaAuth: OktaAuth) => {
    const accessToken = _oktaAuth.getAccessToken();

    if (accessToken === undefined) {
      navigate('/login', { replace: true });
      return;
    }

    try {
      const { isEmployee, isVerified } = JSON.parse(
        window.atob(accessToken.split('.')[1] ?? '')
      );
      const isVerifiedUser = !isEmployee || (isEmployee && isVerified);
      navigate(isVerifiedUser ? '/' : '/login', { replace: true });
    } catch (e) {
      console.error(e);
    }
  };
  /**
   * Render
   */
  return (
    <Security
      oktaAuth={oktaAuth}
      onAuthRequired={onAuthRequired}
      restoreOriginalUri={restoreOriginalUri}
    >
      <AuthProvider>
        <Sidebar>
          <Routes />
        </Sidebar>
      </AuthProvider>
    </Security>
  );
}
export default App;
