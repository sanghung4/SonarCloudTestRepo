import { OktaAuthOptions } from '@okta/okta-auth-js';
declare global {
  interface Window {
    __configuration__: any;
  }
}

export enum Environments {
  DEV = 'development',
  TEST = 'test',
  PROD = 'production'
}

type PunchoutUIConfiguration = {
  apiUrl: string;
  maxContentWidth?: number;
  maxBodyWidth?: number;
  oktaIssuer: string;
  oktaCallbackPath: string;
  oktaClientId: string;
  oktaScopes?: string[];
  environment: Environments;
};

const { __configuration__: CF } = window;
export const configuration: PunchoutUIConfiguration = {
  apiUrl: CF?.REACT_APP_API_URL ?? '',
  maxContentWidth: parseInt(CF?.REACT_APP_AXIOS_MAX_CONTENT_LENGTH) || undefined,
  maxBodyWidth: parseInt(CF?.REACT_APP_AXIOS_MAX_BODY_LENGTH) || undefined,
  oktaIssuer: CF?.REACT_APP_OKTA_ISSUER ?? '',
  oktaCallbackPath: CF?.REACT_APP_OKTA_CALLBACK_PATH ?? '',
  oktaClientId: CF?.REACT_APP_OKTA_CLIENT_ID ?? '',
  oktaScopes: CF?.REACT_APP_OKTA_SCOPES?.split(/\s+/),
  environment: CF?.REACT_APP_ENV
};

export const oktaAuthOptions: OktaAuthOptions = {
  clientId: configuration.oktaClientId || 'clientId',
  issuer: configuration.oktaIssuer || 'http://localhost/oauth2/default',
  redirectUri: `${window.location.origin}${
    configuration.oktaCallbackPath || '/'
  }`,
  pkce: true,
  scopes: configuration.oktaScopes
};
