import { Okta } from '@okta/okta-react-native';
import Config from 'react-native-config';

export const oidc: Okta.ConfigParameters = {
  clientId: `${Config.OKTA_CLIENT_ID}`,
  redirectUri: `${Config.OKTA_URI}:/callback`,
  endSessionRedirectUri: `${Config.OKTA_URI}:/`,
  discoveryUri: `${Config.OKTA_DOMAIN}`,
  scopes: ['openid', 'profile', 'offline_access', 'groups'],
  requireHardwareBackedKeyStore: false,
  issuer: `${Config.OKTA_DOMAIN}`,
};
