import { UserClaims } from "@okta/okta-auth-js";

type AdditionalUserDetails = {
  groups?: [
    | "WMS Admin - Mincron"
    | "WMS Admin - Pricing"
    | "WMS Admin - Pricing Read Only"
    | "WMS Admin - Metrics"
    | "WMS Admin - Count"
    | "WMS Admin - Pricing Administrators"
    | string
  ];
};

export type UserDetails = UserClaims<AdditionalUserDetails>;
export type UserBranchID = (string | null)[] | null | undefined;

export interface AuthContextParams {
  userInfo: UserDetails | undefined;
  userBranchId: UserBranchID;
}

export interface AuthProviderProps {
  children: React.ReactNode;
}