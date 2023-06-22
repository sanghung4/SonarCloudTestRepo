import 'react-app-polyfill/ie11';
import smoothscroll from 'smoothscroll-polyfill';
import { ResizeObserver } from '@juggle/resize-observer';

import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';

import { ApolloProvider } from '@apollo/client';
import { ThemeProvider } from '@dialexa/reece-component-library';

import App from 'App';
import client from 'client';
import ErrorBoundary from 'common/ErrorBoundary';
import './i18n';
import './global.css';
import { load } from 'utils/analytics';
import 'dotenv/config';

smoothscroll.polyfill();

if (!window.ResizeObserver) {
  window.ResizeObserver = ResizeObserver;
}

load();

ReactDOM.render(
  <ApolloProvider client={client}>
    <ThemeProvider>
      <Router>
        <ErrorBoundary>
          <Suspense fallback={null}>
            <App />
          </Suspense>
        </ErrorBoundary>
      </Router>
    </ThemeProvider>
  </ApolloProvider>,
  document.getElementById('root')
);
