// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom';
import 'test-utils/mockMediaQuery';

import { Configuration, Environment } from 'utils/configuration';

Configuration.apiUrl = process.env.REACT_APP_API_URL!;
Configuration.oktaUrl = process.env.REACT_APP_OKTA_URL!;
Configuration.oktaClientId = process.env.REACT_APP_OKTA_CLIENT_ID!;
Configuration.enableTranslationTest =
  process.env.REACT_APP_ENABLE_TRANSLATION_TEST === 'true';
Configuration.segmentApiKey = process.env.REACT_APP_SEGMENT_API_KEY!;
Configuration.googleApiKey = process.env.REACT_APP_GOOGLE_API_KEY!;
Configuration.environment = process.env.REACT_APP_ENVIRONMENT as Environment;
Configuration.showMaintenancePage =
  process.env.REACT_APP_SHOW_MAINTENANCE_PAGE === 'true';
Configuration.contentfulApiUrl = process.env.REACT_APP_CONTENTFUL_API!;
Configuration.contentfulApiKey = process.env.REACT_APP_CONTENTFUL_API_KEY!;
Configuration.contentfulPreviewApiKey =
  process.env.REACT_APP_CONTENTFUL_PREVIEW_API_KEY!;
Configuration.contentfulPreviewEnable =
  process.env.REACT_APP_CONTENTFUL_PREVIEW_ENABLE === 'true';
Configuration.contentfulAboutUsId = process.env.contentfulAboutUsId!;
