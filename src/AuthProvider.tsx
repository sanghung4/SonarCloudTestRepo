import React, {
  ReactNode,
  createContext,
  useMemo,
  useEffect,
  useState,
  useCallback
} from 'react';

import {
  AuthState,
  OktaAuth,
  SigninWithCredentialsOptions,
  UserClaims
} from '@okta/okta-auth-js';
import { useOktaAuth } from '@okta/okta-react';
import { useHistory, useLocation } from 'react-router-dom';

import ApolloClient from 'client';
import { identify } from 'utils/analytics';
import {
  ApprovedUser,
  Feature,
  Maybe,
  useFeaturesQuery,
  useSetFeatureEnabledMutation,
  useUserQuery
} from 'generated/graphql';
import { selectedAccountsVar } from 'providers/SelectedAccountsProvider';

export type Profile = {
  permissions: Array<string>;
  userId: string;
  isEmployee: boolean;
  isVerified: boolean;
};

export type UserContextType = {
  activeFeatures?: string[];
  authState: AuthState | null;
  ecommUser?: Maybe<ApprovedUser>;
  features?: Feature[];
  featuresLoading?: boolean;
  firstName?: string;
  handleLogout?: (redirectTo?: string) => void;
  isLoggingOut?: boolean;
  lastName?: string;
  login?: (values: SigninWithCredentialsOptions) => Promise<void>;
  oktaAuth?: OktaAuth;
  profile?: Profile;
  setFeature?: (featureId: string, isEnabled: boolean) => void;
  user?: UserClaims;
};

type Props = {
  children: ReactNode;
};

export const AuthContext = createContext<UserContextType>({
  authState: null,
  isLoggingOut: false
});

function AuthProvider(props: Props) {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const location = useLocation();
  const { oktaAuth, authState } = useOktaAuth();

  /**
   * State
   */
  const [user, setUser] = useState<UserClaims>();
  const [profile, setProfile] = useState<Profile>({
    permissions: [] as Array<string>,
    userId: '',
    isEmployee: false,
    isVerified: false
  });
  const [features, setFeatures] = useState<any[]>([]);
  const [isLoggingOut, setIsLoggingOut] = useState(false);

  /**
   * Data
   */
  const { data: featuresQueryData, loading: featuresLoading } =
    useFeaturesQuery();
  const { data: userData } = useUserQuery({
    skip: !profile?.userId,
    variables: {
      userId: profile?.userId
    }
  });

  /**
   * Memo
   */
  const firstName = useMemo(firstNameMemo, [user]);
  const lastName = useMemo(lastNameMemo, [user]);
  const featureData: any = useMemo(featuresMemo, [featuresQueryData?.features]);
  const activeFeatures = useMemo(activeFeaturesMemo, [featuresQueryData]);

  /**
   * Effects
   */
  useEffect(fetchUser, [authState?.isAuthenticated, isLoggingOut, oktaAuth]);
  useEffect(fetchProfile, [
    authState?.accessToken,
    authState?.isAuthenticated,
    isLoggingOut,
    oktaAuth
  ]);
  useEffect(fetchFeatures, [
    featureData,
    authState?.isAuthenticated,
    featuresQueryData?.features
  ]);
  useEffect(() => {
    if (location.pathname === '/' && isLoggingOut) setIsLoggingOut(false);
  }, [isLoggingOut, location.pathname]);
  useEffect(checkVerifiedUser, [history, location, profile]);

  const [setFeatureEnabledMutation] = useSetFeatureEnabledMutation();
  /**
   * Callbacks
   */
  const login = useCallback(loginCb, [oktaAuth]);

  async function loginCb(values: SigninWithCredentialsOptions) {
    const transaction = await oktaAuth.signInWithCredentials(values);
    identify(transaction.user?.profile?.login);
    oktaAuth.signInWithRedirect({
      sessionToken: transaction.sessionToken
    });
  }

  return (
    <AuthContext.Provider
      value={{
        activeFeatures,
        authState,
        ecommUser: userData?.user,
        features,
        featuresLoading,
        firstName,
        handleLogout,
        isLoggingOut,
        lastName,
        login,
        oktaAuth,
        profile,
        setFeature,
        user
      }}
    >
      {props.children}
    </AuthContext.Provider>
  );

  function firstNameMemo() {
    return user?.name?.split(' ')[0] ?? '';
  }
  function lastNameMemo() {
    return user?.name?.split(' ')[1] ?? '';
  }

  function fetchUser() {
    if (authState?.isAuthenticated && !isLoggingOut) {
      const getUser = async () => {
        try {
          const oktaUserData = await oktaAuth.getUser();
          setUser(oktaUserData);
        } catch {}
      };

      getUser();
    }
  }

  function fetchProfile() {
    if (authState?.isAuthenticated && !isLoggingOut) {
      const getProfile = () => {
        const decoded = JSON.parse(
          atob(authState?.accessToken?.accessToken.split('.')[1] ?? '')
        );
        setProfile({
          permissions: decoded.ecommPermissions,
          userId: decoded.ecommUserId,
          isEmployee: decoded.isEmployee,
          isVerified: decoded.isVerified
        });

        if (
          (localStorage.getItem('currentUserId') || '') !== decoded.ecommUserId
        ) {
          localStorage.removeItem('defaultAccounts');
        }

        localStorage.setItem('currentUserId', decoded.ecommUserId);
      };

      getProfile();
    }
  }

  function fetchFeatures() {
    setFeatures(featureData);
  }

  function checkVerifiedUser() {
    if (
      profile.isEmployee &&
      !profile.isVerified &&
      location.pathname !== '/'
    ) {
      history.replace('/');
    }
  }

  function setFeature(featureId: string, isEnabled: boolean) {
    setFeatureEnabledMutation({
      variables: {
        featureId,
        setFeatureEnabledInput: {
          isEnabled
        }
      },
      refetchQueries: ['features']
    });
  }

  async function handleLogout(redirectTo: string = '/') {
    if (authState?.isAuthenticated && !isLoggingOut) {
      setIsLoggingOut(true);
      await oktaAuth.signOut({
        postLogoutRedirectUri: `${window.location.origin}${redirectTo}`
      });

      selectedAccountsVar({});
      localStorage.setItem('selectedAccounts', JSON.stringify({}));
      sessionStorage.clear();
      await ApolloClient.clearStore();
    }
  }

  function featuresMemo() {
    const features = featuresQueryData?.features?.map((feature: any) => {
      return feature;
    });
    return features;
  }

  function activeFeaturesMemo() {
    return (featuresQueryData?.features ?? [])
      .filter((f) => f.isEnabled)
      .map((f) => f.name);
  }
}

export default AuthProvider;
