import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';

import { resources } from 'locales';

i18next.use(initReactI18next).init({
  resources,
  debug: process.env.NODE_ENV === 'development',
  // TODO: For testing purposes only
  lng: process.env.REACT_APP_I18N_LNG || 'en',
  fallbackLng: 'en',
  // TODO: For testing purposes only
  supportedLngs: ['en', 'test']
});

export default i18next;
