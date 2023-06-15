/* eslint-disable @typescript-eslint/no-non-null-assertion */
declare global {
  interface Window {
    __configuration__: Record<string, string>;
  }
}
window.__configuration__ = window.__configuration__ || {};

interface PortalConfiguration {
  apiUrl: string; // REACT_APP_API_URL=https://api.ecomm.reecedev.us/
  oktaUrl: string; //   REACT_APP_OKTA_URL=https://dev-432546.okta.com
  oktaClientId: string; //   REACT_APP_OKTA_CLIENT_ID=0oa13b0b5bMKXZfJU4x7
  oktaScopes: string;
  countPollingInterval: string; //  REACT_APP_COUNT_POLLING_INTERVAL=1500
}

export const Configuration: PortalConfiguration /* & TestConfiguration */ = {
  apiUrl: window.__configuration__.REACT_APP_API_URL!,
  oktaUrl: window.__configuration__.REACT_APP_OKTA_URL!,
  oktaClientId: window.__configuration__.REACT_APP_OKTA_CLIENT_ID!,
  oktaScopes: window.__configuration__.REACT_APP_OKTA_SCOPES,
  countPollingInterval: window.__configuration__.REACT_APP_COUNT_POLLING_INTERVAL,
};

export const oktaAuthOptions = {
  clientId: Configuration.oktaClientId || "{clientId}",
  issuer:
    Configuration.oktaUrl || "https://{yourOktaDomain}.com/oauth2/default",
  redirectUri: `${window.location.origin}/`,
  scopes: ["openid", "profile", "email", "groups"],
  countPollingInterval: "1500",
};
