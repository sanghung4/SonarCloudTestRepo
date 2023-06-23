import {
  createContext,
  Dispatch,
  useCallback,
  useContext,
  useEffect,
  useState
} from 'react';

import {
  AuthState,
  OktaAuth,
  SigninWithCredentialsOptions
} from '@okta/okta-auth-js';
import { useOktaAuth } from '@okta/okta-react';
import { WrapperProps } from '@reece/global-types';
import { noop } from 'lodash-es';
import useLocalStorage from 'use-local-storage';
import { Maybe } from 'yup';
import { useLocation, useNavigate } from 'react-router-dom';

/**
 * Types
 */
export type AuthContextType = {
  sessionId?: Maybe<string>;
  setSessionId: Dispatch<Maybe<string>>;
  authState?: Maybe<AuthState>;
  oktaAuth?: Maybe<OktaAuth>;
  login?: (values: SigninWithCredentialsOptions) => Promise<void>;
};

/**
 * Default values
 */
export const defaultAuthContext: AuthContextType = { setSessionId: noop };

/**
 * Context
 */
export const AuthContext = createContext(defaultAuthContext);
export const useAuthContext = () => useContext(AuthContext);

/**
 * Provider
 */
function AuthProvider({ children }: WrapperProps) {
  /**
   * Custom hooks
   */
  const [localStorageSessionId, setLocalStorageSessionId] = useLocalStorage<
    Maybe<string>
  >('sessionId', null);
  const { pathname } = useLocation();
  const navigate = useNavigate();
  const { authState, oktaAuth } = useOktaAuth();

  /**
   * States
   */
  const [sessionId, setSessionIdState] = useState<Maybe<string>>(
    localStorageSessionId
  );

  /**
   * Callbacks
   */
  const setSessionId = (dispatch?: Maybe<string>) => {
    setSessionIdState(dispatch);
    setLocalStorageSessionId(dispatch);
  };
  const login = useCallback(loginCb, [oktaAuth]);
  async function loginCb(values: SigninWithCredentialsOptions) {
    const { sessionToken } = await oktaAuth.signInWithCredentials(values);
    oktaAuth.signInWithRedirect({ sessionToken });
  }

  /**
   * Effect
   */
  useEffect(() => {
    if (
      pathname !== '/login' &&
      !sessionId &&
      authState &&
      !authState.isAuthenticated
    ) {
      navigate('/login');
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pathname, sessionId, authState?.isAuthenticated]);

  /**
   * Render
   */
  return (
    <AuthContext.Provider value={{ authState, sessionId, setSessionId, login }}>
      {children}
    </AuthContext.Provider>
  );
}

export default AuthProvider;
