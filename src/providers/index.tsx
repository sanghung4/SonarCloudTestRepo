import React from 'react';
import { ApolloProvider } from '@apollo/client';
import client from 'apollo/client';
import { SafeAreaProvider } from 'react-native-safe-area-context';

import ThemeProvider from './Theme';
import AuthProvider from './Auth';
import OverlayProvider from './Overlay';
import { theme } from './Theme/constants';
import AppStateProvider from './AppState';

const Providers: React.FC = ({ children }) => (
  <ApolloProvider client={client}>
    <ThemeProvider theme={theme}>
      <OverlayProvider>
        <AuthProvider>
          <AppStateProvider>
          <SafeAreaProvider>{children}</SafeAreaProvider>
          </AppStateProvider>
        </AuthProvider>
      </OverlayProvider>
    </ThemeProvider>
  </ApolloProvider>
);

export default Providers;
