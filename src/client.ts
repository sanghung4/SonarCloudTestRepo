import { ApolloClient, InMemoryCache, from, ApolloLink } from '@apollo/client';
import { setContext } from '@apollo/client/link/context';
import { onError } from '@apollo/client/link/error';
import { AccessToken, IDToken } from '@okta/okta-auth-js';
import { createUploadLink } from 'apollo-upload-client';

import { List } from 'generated/graphql';
import { Configuration } from 'utils/configuration';
import { selectedAccountsVar } from 'providers/SelectedAccountsProvider';

type OktaLSToken = {
  accessToken: AccessToken;
  idToken: IDToken;
};

// Util Functions
export const overwriteIfNotNull = (previous: any, incoming: any) =>
  previous || incoming;
export const applyIncomingIfValid = (_: any, incoming: any[]) => {
  // Due to the nature of Apollo GQL, it is impossible to set [] as falsey in test coverage
  // istanbul ignore next
  return incoming ? [...incoming] : null;
};
export const onlyTakeIncoming = (_: any, incoming: any) => incoming;
export const readSelectedAccountsVar = () => selectedAccountsVar();

// Cache
export const cache = new InMemoryCache({
  typePolicies: {
    Cart: {
      fields: { products: { merge: applyIncomingIfValid } }
    },
    ContractDetails: { keyFields: ['contractNumber'] },
    ContractProduct: { keyFields: ['id', 'sequenceNumber'] },
    List: {
      keyFields: ['id'],
      fields: { listLineItems: { merge: applyIncomingIfValid } }
    },
    ListLineItem: {
      keyFields: ['id', 'sortOrder'],
      fields: {
        name: { merge: overwriteIfNotNull },
        imageUrls: { merge: overwriteIfNotNull },
        techSpecs: { merge: overwriteIfNotNull },
        manufacturerName: { merge: overwriteIfNotNull },
        manufacturerNumber: { merge: overwriteIfNotNull },
        stock: { merge: overwriteIfNotNull },
        minIncrementQty: { merge: overwriteIfNotNull },
        pricePerUnit: { merge: overwriteIfNotNull },
        status: { merge: overwriteIfNotNull },
        sortOrder: { merge: onlyTakeIncoming }
      }
    },
    Query: {
      fields: {
        selectedAccounts: { read: readSelectedAccountsVar }
      }
    }
  }
});

const httpLink = createUploadLink({
  uri: Configuration.apiUrl
});

export const invalidateListCache = (selectedList?: List) => {
  //@ts-ignore
  const cacheDataHelper = cache.data.data;
  Object.keys(cacheDataHelper).forEach((key) => {
    if (key.match(/^ListLineItem/)) {
      //@ts-ignore
      delete cache.data.data[key];
    }
    // Only purge the list cache that match the selected list
    if (key.match(/^List:/) && cacheDataHelper[key].id === selectedList?.id) {
      //@ts-ignore
      delete cache.data.data[key];
    }
  });
};

export const authLink = setContext((_, { headers }) => {
  // get the authentication token from local storage if it exists
  const oktaTokenObj: OktaLSToken = JSON.parse(
    localStorage.getItem('okta-token-storage') || 'false'
  );
  // return the headers to the context so httpLink can read them
  return {
    headers: {
      ...headers,
      'x-max-api-secret': Configuration.maxApiSecret ?? '',
      authorization: oktaTokenObj?.accessToken
        ? // istanbul ignore next
          `Bearer ${oktaTokenObj.accessToken.accessToken}`
        : ''
    }
  };
});

// Ignore since there is no test case we can ever cover on this
// istanbul ignore next
const errorLink = onError(({ graphQLErrors, networkError }) => {
  if (graphQLErrors) {
    graphQLErrors.forEach(({ message: messageStr }) => {
      let message = messageStr;
      try {
        const messageObj = JSON.parse(messageStr);
        message = messageObj.error ?? JSON.stringify(messageObj);
      } catch {}

      console.error('[GraphQL error]:', message);
    });
  }

  if (networkError) console.error(`[Network error]: ${networkError}`);
});

const httpLinkContentful = createUploadLink({
  uri: Configuration.contentfulApiUrl
});

export const authLinkContentful = setContext((_, { headers }) => {
  return {
    headers: {
      ...headers,
      authorization: `Bearer ${
        Configuration.contentfulPreviewEnable
          ? Configuration.contentfulPreviewApiKey
          : Configuration.contentfulApiKey
      }`
    }
  };
});

const apiLink = from([
  errorLink,
  authLink,
  //@ts-ignore
  httpLink
]);

const contentfulLink = from([
  errorLink,
  authLinkContentful,
  //@ts-ignore
  httpLinkContentful
]);

export const apolloLink = ApolloLink.split(
  (operation) => operation.getContext().clientName === 'contentful',
  contentfulLink,
  apiLink
);

const client = new ApolloClient({ link: apolloLink, cache });

export default client;
