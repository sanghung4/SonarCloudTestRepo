import { createContext, useContext, useEffect, useState } from "react";

import { useOktaAuth } from "@okta/okta-react";

import { AuthContextParams, AuthProviderProps, UserDetails } from "./types";
import { useHistory } from "react-router-dom";
import { toRelativeUrl } from "@okta/okta-auth-js";
import { useGetUserBranchLazyQuery, useGetUserBranchQuery } from "../graphql";

const AuthContext = createContext<AuthContextParams>({
  userInfo: undefined,
  userBranchId: null,
});

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const { authState, oktaAuth } = useOktaAuth();
  const history = useHistory();

  // ----- STATE ----- //
  // User Information
  const [userInfo, setUserInfo] = useState<UserDetails>();
  const [userBranchId, setUserBranchId] = useState<(string | null)[] | null | undefined>();

  //Get UserBranch
  const [userBranch, {data, loading}] = useGetUserBranchLazyQuery();

  const getUserBranchId =() => {
    if (userInfo?.email) {
      const variables = { input:  userInfo?.email?.toLowerCase()
      } 
      userBranch({ variables })
        .then(({data}) => {
          if (data) {
            setUserBranchId(data.userBranch.branch);
          } else {
            throw new Error();
          }
        })
        .catch(() => {
         setUserBranchId(null)
        });
    }
  }

  // ----- EFFECTS ----- //
  useEffect(() => {
    if (!authState || !authState.isAuthenticated) {
      // When user isn't authenticated, forget any user info
      setUserInfo(undefined);
    } else {
      const accessToken = oktaAuth.getAccessToken();
      localStorage.setItem("token", accessToken ? accessToken : "");
      oktaAuth
        .getUser()
        .then((info) => {
          setUserInfo(info);
        })
        .catch((err) => {
          console.error(err);
        });
    }
  }, [authState, oktaAuth]); // Update if authState changes
  
  useEffect(() => {
    // Redirect at first login start
    const isReadOnlyUser = userInfo?.groups?.length === 1 && userInfo?.groups?.includes('WMS Admin - Pricing Read Only');
    if (userInfo && window.location.pathname === "/") {
      history.replace(toRelativeUrl(isReadOnlyUser ? "/pricing" : "/metrics", window.location.origin));
    }

    // Get user Branches Info
    getUserBranchId()

  }, [userInfo])
  

  // ----- METHODS ----- //

  return (
    <AuthContext.Provider
      value={{
        userInfo,
        userBranchId,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuthContext = () => useContext(AuthContext);
