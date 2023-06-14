import React, {
  useEffect,
  createContext,
  useContext,
  useState,
  FC,
} from 'react';
import {
  createConfig,
  signIn,
  signOut,
  getAccessToken,
  isAuthenticated,
  EventEmitter as OktaEmitter,
  getUser,
  Okta,
  User as OktaUser,
} from '@okta/okta-react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { ACCESS_TOKEN } from 'constants/storage';
import { oidc } from 'constants/auth';
import { configVar, initialConfig } from 'apollo/local';
import client from 'apollo/client';
import { useOverlay } from 'providers/Overlay';
import firebaseUtils from 'utils/firebaseUtils';
import * as storage from 'constants/storage';
import { useAppState } from 'providers/AppState';

export enum UserGroup {
  MANAGER = 'Branch Inventory - Managers',
  EMPLOYEE = 'Branch Inventory - Employees',
}

export interface User extends OktaUser {
  groups?: UserGroup[];
}

export interface IAuthContext {
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  isManager: boolean;
  user: User | null;
  login: () => void;
  logout: () => void;
  testingLogin: (credentials: Okta.Credentials) => void;
}

const initialContext = {
  isAuthenticated: false,
  isAuthenticating: false,
  isManager: true,
  user: null,
  login: () => {},
  logout: () => {},
  testingLogin: () => {},
};

const AuthContext = createContext<IAuthContext>(initialContext);

const AuthProvider: FC = ({ children }) => {
  const { hideAlert } = useOverlay();
  const {resetProductQuantityMap} = useAppState()

  const [user, setUser] = useState<User | null>(null);
  const [authenticating, setAuthenticating] = useState(false);

  useEffect(() => {
    const signInSuccessListener = OktaEmitter.addListener(
      'signInSuccess',
      async (e) => {
        try {
          setUser(await getUser());
        } catch (error: any) {
          firebaseUtils.crashlyticsRecordError(error);
          console.error(`Okta Login Error: ${error}`);
        }
        AsyncStorage.setItem(ACCESS_TOKEN, e.access_token || '');
        setAuthenticating(false);
      }
    );

    const signOutSuccessListener = OktaEmitter.addListener(
      'signOutSuccess',
      async (e) => {
        if (e.resolve_type !== 'signed_out') {
          return;
        }

        AsyncStorage.clear();
        client.clearStore();
        configVar(initialConfig);
        setUser(null);
        setAuthenticating(false);
        hideAlert();
      }
    );

    const onErrorListener = OktaEmitter.addListener('onError', (e) => {
      console.error(`Okta Error: ${JSON.stringify(e)}`);
      setAuthenticating(false);
    });

    const onCancelledListener = OktaEmitter.addListener('onCancelled', (e) => {
      console.warn(`Okta Cancelled: ${e}`);
      setAuthenticating(false);
    });
    createConfig(oidc);
    checkAuthentication();

    return () => {
      signInSuccessListener.remove();
      signOutSuccessListener.remove();
      onErrorListener.remove();
      onCancelledListener.remove();
    };
  }, []);

  const checkAuthentication = async () => {
    setAuthenticating(true);
    const authRes = await isAuthenticated();
    if (authRes.authenticated) {
      try {
        setUser(await getUser());
      } catch (error: any) {
        firebaseUtils.crashlyticsRecordError(error);
        if (error.message.includes('401')) {
          signOut();
        }
      }
      const tokenRes = await getAccessToken();
      AsyncStorage.setItem(ACCESS_TOKEN, tokenRes.access_token || '');
    }

    setAuthenticating(false);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isManager:
          user && Array.isArray(user.groups)
            ? user.groups.includes(UserGroup.MANAGER)
            : false,
        isAuthenticated: user !== null,
        isAuthenticating: authenticating,
        login: () => {
          setAuthenticating(true);
          signIn();
        },
        logout: async () => {
          await AsyncStorage.removeItem(storage.PRODUCT_QUANTITY_MAP)
          resetProductQuantityMap();
          setAuthenticating(true);
          signOut();
        },
        testingLogin: (credentials) => {
          setAuthenticating(true);
          signIn(credentials);
        },
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;

export const useAuth = (): IAuthContext => useContext(AuthContext);
