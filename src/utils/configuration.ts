declare global {
  interface Window {
    __configuration__: any;
  }
}
window.__configuration__ = window.__configuration__ || {};

export type Environment =
  | 'development'
  | 'test'
  | 'uat'
  | 'production'
  | 'production-backup';

interface PortalConfiguration {
  apiUrl: string; // REACT_APP_API_URL=https://api.ecomm.reecedev.us/
  appUrl: string; // REACT_APP_URL=http://https://app.ecomm.reecedev.us/
  oktaUrl: string; //   REACT_APP_OKTA_URL=https://dev-432546.okta.com
  oktaClientId: string; //   REACT_APP_OKTA_CLIENT_ID=0oa13b0b5bMKXZfJU4x7
  enableTranslationTest: boolean; // REACT_APP_ENABLE_TRANSLATION_TEST=true
  segmentApiKey: string; // REACT_APP_SEGMENT_API_KEY=Pl02bgvESMF4HvCiB8If8HgqdfJCL3GE
  googleApiKey: string; // REACT_APP_GOOGLE_API_KEY=AIzaSyDEVmbEdAOo2i3dkRrGFnrA92ta7qJ08DM
  environment: Environment; // REACT_APP_ENVIRONMENT=development
  showMaintenancePage: boolean; // REACT_APP_SHOW_MAINTENANCE_PAGE
  maxApiSecret: string; // REACT_APP_MAX_API_SECRET
  contentfulApiUrl?: string; // REACT_APP_CONTENTFUL_API
  contentfulApiKey?: string; // REACT_APP_CONTENTFUL_API_KEY
  contentfulPreviewApiKey?: string; // REACT_APP_CONTENTFUL_PREVIEW_API_KEY
  contentfulPreviewEnable?: boolean; // REACT_APP_CONTENTFUL_PREVIEW_ENABLE
  contentfulAboutUsId?: string; // REACT_APP_CONTENTFUL_ABOUTUS_ID
}

export const Configuration: PortalConfiguration /* & TestConfiguration */ = {
  apiUrl: window.__configuration__.REACT_APP_API_URL!,
  appUrl: window.__configuration__.REACT_APP_URL!,
  oktaUrl: window.__configuration__.REACT_APP_OKTA_URL!,
  oktaClientId: window.__configuration__.REACT_APP_OKTA_CLIENT_ID!,
  enableTranslationTest:
    window.__configuration__.REACT_APP_ENABLE_TRANSLATION_TEST === 'true',
  segmentApiKey: window.__configuration__.REACT_APP_SEGMENT_API_KEY!,
  googleApiKey: window.__configuration__.REACT_APP_GOOGLE_API_KEY!,
  environment: window.__configuration__.REACT_APP_ENVIRONMENT as Environment,
  showMaintenancePage:
    window.__configuration__.REACT_APP_SHOW_MAINTENANCE_PAGE === 'true',
  maxApiSecret: window.__configuration__.REACT_APP_MAX_API_SECRET!,
  contentfulApiUrl: window.__configuration__.REACT_APP_CONTENTFUL_API_URL,
  contentfulApiKey: window.__configuration__.REACT_APP_CONTENTFUL_API_KEY,
  contentfulPreviewApiKey:
    window.__configuration__.REACT_APP_CONTENTFUL_PREVIEW_API_KEY,
  contentfulPreviewEnable:
    window.__configuration__.REACT_APP_CONTENTFUL_PREVIEW_ENABLE === 'true',
  contentfulAboutUsId: window.__configuration__.REACT_APP_CONTENTFUL_ABOUTUS_ID
};

// Needs to be a function for test mocks
export const getEnvironment = () => ({
  isProd: Configuration.environment === 'production',
  isSandbox: Configuration.environment === 'production-backup',
  isDev: Configuration.environment === 'development',
  isTest: Configuration.environment === 'test',
  isUat: Configuration.environment === 'uat'
});
